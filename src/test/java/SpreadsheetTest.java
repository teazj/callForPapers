import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.gdata.util.ServiceException;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import com.jayway.restassured.module.mockmvc.response.MockMvcResponse;
import com.jayway.restassured.response.Header;
import com.nimbusds.jose.JOSEException;
import fr.sii.config.google.GoogleSettings;
import fr.sii.config.spreadsheet.SpreadsheetSettings;
import fr.sii.domain.spreadsheet.Row;
import fr.sii.domain.spreadsheet.RowDraft;
import fr.sii.domain.spreadsheet.RowSession;
import fr.sii.domain.token.Token;
import fr.sii.service.auth.AuthUtils;
import fr.sii.service.spreadsheet.SpreadsheetService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;

import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


/**
 * Created by tmaugin on 08/04/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:META-INF/spring/applicationContextTest.xml","classpath:META-INF/spring/dispatcherServletTest.xml"})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SpreadsheetTest {

    @Autowired
    private SpreadsheetService spreadsheetService;

    @Autowired
    private SpreadsheetSettings spreadsheetSettings;

    @Autowired
    private GoogleSettings googleSettings;

    private static boolean init = false;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private String secretToken = "secretTestsecretTestsecretTestsecretTestsecretTestsecretTestsecretTestsecretTest";

    private void getOAuth()
    {
        if(init)
            return;
        init = true;
        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        httpTransport,
                        jsonFactory,
                        googleSettings.getClientId(),
                        googleSettings.getClientSecret(),
                        Arrays.asList("https://www.googleapis.com/auth/drive", "https://spreadsheets.google.com/feeds"))
                        .setAccessType("offline")
                        .setApprovalPrompt("force")
                        .build();
        String url = flow.newAuthorizationUrl().setRedirectUri("urn:ietf:wg:oauth:2.0:oob").build();

        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);

        HtmlPage page = null;
        try {
            page = webClient.getPage(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HtmlSubmitInput signInButton = page.getElementByName("signIn");
        HtmlTextInput userNameField = page.getElementByName("Email");
        HtmlPasswordInput passwordField = page.getElementByName("Passwd");
        userNameField.setValueAttribute(spreadsheetSettings.getLogin());
        passwordField.setValueAttribute(spreadsheetSettings.getPassword());
        HtmlPage allowAccessPage = null;
        try {
            allowAccessPage = signInButton.click();
        } catch (IOException e) {
        }
        HtmlButton allowAccessButton = (HtmlButton)allowAccessPage.getElementById("submit_approve_access");
        allowAccessButton.removeAttribute("disabled");
        HtmlPage tokenPage = null;
        try {
            tokenPage = allowAccessButton.click();
        } catch (IOException e) {
            e.printStackTrace();
        }
        HtmlTextInput tokenElement = (HtmlTextInput)tokenPage.getElementById("code");
        String code = tokenElement.getText();
        webClient.close();

        String accessTokenUrl = "https://accounts.google.com/o/oauth2/token";

        try {
            TokenResponse tokenResponse = new AuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    new com.google.api.client.json.jackson.JacksonFactory(),
                    new GenericUrl(accessTokenUrl), code)
                    .setRedirectUri("urn:ietf:wg:oauth:2.0:oob")
                    .setCode(code)
                    .setGrantType("authorization_code")
                    .setClientAuthentication(
                            new BasicAuthentication(googleSettings.getClientId(),googleSettings.getClientSecret()))
                    .execute();

            if(tokenResponse.getRefreshToken() != null && !tokenResponse.getRefreshToken().equals(""))
            {
                spreadsheetService.login(tokenResponse.getRefreshToken());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Before
    public void setUp() {
        getOAuth();
        AuthUtils.TOKEN_SECRET = secretToken;
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    public Header getHeader()
    {
        Token token = null;
        try {
            token = AuthUtils.createToken("testHost","1",true);
            token.getToken();
        } catch (JOSEException e) {
            e.printStackTrace();
        }
        return new Header("Authorization", "Bearer " + token.getToken());
    }

    public Header getHeaderOtherAccount()
    {
        Token token = null;
        try {
            token = AuthUtils.createToken("testHost","2",true);
            token.getToken();
        } catch (JOSEException e) {
            e.printStackTrace();
        }
        return new Header("Authorization", "Bearer " + token.getToken());
    }

    public Header getHeaderNotVerified()
    {
        Token token = null;
        try {
            token = AuthUtils.createToken("testHost","1",false);
            token.getToken();
        } catch (JOSEException e) {
            e.printStackTrace();
        }
        return new Header("Authorization", "Bearer " + token.getToken());
    }

    @Test
    public void test01_addRowPass() {
        Row row = new RowSession();
        row.setBio("Bio");
        row.setCompany("SII");
        row.setCoSpeaker("moi, toi");
        row.setDescription("description");
        row.setDifficulty(3);
        row.setEmail("email@email.fr");
        row.setFinancial(true);
        row.setFirstname("Thomas");
        row.setHotel(true);
        row.setHotelDate("13/11/1992");
        row.setName("Maugin");
        row.setPhone("33683653379");
        row.setReferences("refs");
        row.setSessionName("session name");
        row.setSocial("www.thomas-maugin.fr, https://github.com/Thom-x");
        row.setType("conference");
        row.setTrack("web");
        row.setTravel(false);
        row.setDraft(false);
        row.setUserId(1L);

        Row returnedRow = new RowSession();

        boolean error = false;
        try {
            returnedRow = spreadsheetService.addRow(row);
        } catch (IOException | ServiceException | EntityNotFoundException e) {
            e.printStackTrace();
            error = true;
        }
        assertEquals(false,error);

        assertEquals("Bio",returnedRow.getBio());
        assertEquals("SII",returnedRow.getCompany());
        assertEquals("moi, toi",returnedRow.getCoSpeaker());
        assertEquals("description",returnedRow.getDescription());
        assertEquals(Integer.valueOf(3),Integer.valueOf(returnedRow.getDifficulty()));
        assertEquals("email@email.fr",returnedRow.getEmail());
        assertEquals(true,returnedRow.getFinancial());
        assertEquals("Thomas",returnedRow.getFirstname());
        assertEquals(true,returnedRow.getHotel());
        assertEquals("13/11/1992",returnedRow.getHotelDate());
        assertEquals("Maugin",returnedRow.getName());
        assertEquals("33683653379",returnedRow.getPhone());
        assertEquals("refs",returnedRow.getReferences());
        assertEquals("session name",returnedRow.getSessionName());
        assertEquals("www.thomas-maugin.fr, https://github.com/Thom-x",returnedRow.getSocial());
        assertEquals("web",returnedRow.getTrack());
        assertEquals("conference",returnedRow.getType());
        assertEquals(false,returnedRow.getTravel());
        assertNotEquals(null, returnedRow.getAdded());
        assertEquals(false, returnedRow.getDraft());
        assertEquals((Long) 1L, returnedRow.getUserId());
    }

    @Test
    public void test02_addRowDraftPass() {
        Row row = new RowDraft();
        row.setDraft(true);
        row.setUserId(1L);

        Row returnedRow = new RowSession();

        boolean error = false;
        try {
            returnedRow = spreadsheetService.addRow(row);
        } catch (IOException | ServiceException | EntityNotFoundException e) {
            e.printStackTrace();
            error = true;
        }
        assertEquals(false,error);

        assertEquals(true,returnedRow.getDraft());
        assertEquals((Long)1L,returnedRow.getUserId());
        assertNotEquals(null, returnedRow.getAdded());
    }

    @Test
    public void test03_addRowPassRest() {

        given().contentType("application/json")
                .header(getHeader())
                .body("{\n" +
                        "\"email\" : \"email@email.fr\",\n" +
                        "\"name\" : \"Maugin\",\n" +
                        "\"firstname\" : \"Thomas\",\n" +
                        "\"phone\" : \"33683653379\",\n" +
                        "\"company\" : \"SII\",\n" +
                        "\"bio\" : \"Bio\",\n" +
                        "\"social\" : \"www.thomas-maugin.fr, https://github.com/Thom-x\",\n" +
                        "\"sessionName\" : \"session name\",\n" +
                        "\"description\" : \"description\",\n" +
                        "\"references\" : \"refs\",\n" +
                        "\"difficulty\" : \"3\",\n" +
                        "\"type\" : \"conference\",\n" +
                        "\"track\" : \"web\",\n" +
                        "\"coSpeaker\" : \"moi, toi\",\n" +
                        "\"financial\" : \"true\",\n" +
                        "\"travel\" : \"true\",\n" +
                        "\"travelFrom\" : \"Angers\",\n" +
                        "\"hotel\" : \"true\",\n" +
                        "\"hotelDate\" : \"13/11/1992\"\n" +
                        "}")
                .when()
                .post("/api/restricted/session")
                .then()
                .statusCode(200)
                .body("name", Matchers.is("Maugin"))
                .body("draft", Matchers.is(false))
                .body("userId", Matchers.is(1))
                .body("difficulty", Matchers.is(3));
    }

    @Test
    public void test04_addRowNotVerifiedRest() {

        given().contentType("application/json")
                .header(getHeaderNotVerified())
                .body("{\n" +
                        "\"email\" : \"email@email.fr\",\n" +
                        "\"name\" : \"Maugin\",\n" +
                        "\"firstname\" : \"Thomas\",\n" +
                        "\"phone\" : \"33683653379\",\n" +
                        "\"company\" : \"SII\",\n" +
                        "\"bio\" : \"Bio\",\n" +
                        "\"social\" : \"www.thomas-maugin.fr, https://github.com/Thom-x\",\n" +
                        "\"sessionName\" : \"session name\",\n" +
                        "\"description\" : \"description\",\n" +
                        "\"references\" : \"refs\",\n" +
                        "\"difficulty\" : \"3\",\n" +
                        "\"type\" : \"conference\",\n" +
                        "\"track\" : \"web\",\n" +
                        "\"coSpeaker\" : \"moi, toi\",\n" +
                        "\"financial\" : \"true\",\n" +
                        "\"travel\" : \"true\",\n" +
                        "\"travelFrom\" : \"Angers\",\n" +
                        "\"hotel\" : \"true\",\n" +
                        "\"hotelDate\" : \"13/11/1992\"\n" +
                        "}")
                .when()
                .post("/api/restricted/session")
                .then()
                .statusCode(403);
    }

    @Test
    public void test05_addRowNotAuthenticatedRest() {
        given().contentType("application/json")
                .body("{\n" +
                        "\"email\" : \"email@email.fr\",\n" +
                        "\"name\" : \"Maugin\",\n" +
                        "\"firstname\" : \"Thomas\",\n" +
                        "\"phone\" : \"33683653379\",\n" +
                        "\"company\" : \"SII\",\n" +
                        "\"bio\" : \"Bio\",\n" +
                        "\"social\" : \"www.thomas-maugin.fr, https://github.com/Thom-x\",\n" +
                        "\"sessionName\" : \"session name\",\n" +
                        "\"description\" : \"description\",\n" +
                        "\"references\" : \"refs\",\n" +
                        "\"difficulty\" : \"3\",\n" +
                        "\"type\" : \"conference\",\n" +
                        "\"track\" : \"web\",\n" +
                        "\"coSpeaker\" : \"moi, toi\",\n" +
                        "\"financial\" : \"true\",\n" +
                        "\"travel\" : \"true\",\n" +
                        "\"travelFrom\" : \"Angers\",\n" +
                        "\"hotel\" : \"true\",\n" +
                        "\"hotelDate\" : \"13/11/1992\"\n" +
                        "}")
                .when()
                .post("/api/restricted/session")
                .then()
                .statusCode(403);
    }


    @Test
    public void test06_addRowDraftPassRest() {
        given().contentType("application/json")
                .header(getHeader())
                .body("{}")
                .when()
                .post("/api/restricted/draft")
                .then()
                .statusCode(200)
                .body("draft", Matchers.is(true))
                .body("userId", Matchers.is(1));
    }

    @Test
    public void test07_addRowDraftNotVerifiedRest() {
        given().contentType("application/json")
                .header(getHeaderNotVerified())
                .body("{}")
                .when()
                .post("/api/restricted/draft")
                .then()
                .statusCode(403);
    }

    @Test
    public void test08_addRowDraftNotAuthenticatedRest() {
        given().contentType("application/json")
                .body("{}")
                .when()
                .post("/api/restricted/draft")
                .then()
                .statusCode(403);
    }

    @Test
    public void test09_addRowFailFinancialRest() {
        given().contentType("application/json")
                .header(getHeader())
                .body("{\n" +
                        "\"email\" : \"email@email.fr\",\n" +
                        "\"name\" : \"Maugin\",\n" +
                        "\"firstname\" : \"Thomas\",\n" +
                        "\"phone\" : \"33683653379\",\n" +
                        "\"company\" : \"SII\",\n" +
                        "\"bio\" : \"Bio\",\n" +
                        "\"social\" : \"www.thomas-maugin.fr, https://github.com/Thom-x\",\n" +
                        "\"sessionName\" : \"session name\",\n" +
                        "\"description\" : \"description\",\n" +
                        "\"references\" : \"refs\",\n" +
                        "\"difficulty\" : \"3\",\n" +
                        "\"type\" : \"conference\",\n" +
                        "\"track\" : \"web\",\n" +
                        "\"coSpeaker\" : \"moi, toi\",\n" +
                        "\"financial\" : \"true\",\n" +
                        "\"travel\" : \"true\",\n" +
                        "\"travelFrom\" : \"Angers\",\n" +
                        "\"hotel\" : \"true\",\n" +
                        "}")
                .when()
                .post("/api/restricted/session")
                .then()
                .statusCode(400);
    }

    @Test
    public void test10_addRowFailFinancial2Rest() {
        given().contentType("application/json")
                .header(getHeader())
                .body("{\n" +
                        "\"email\" : \"email@email.fr\",\n" +
                        "\"name\" : \"Maugin\",\n" +
                        "\"firstname\" : \"Thomas\",\n" +
                        "\"phone\" : \"33683653379\",\n" +
                        "\"company\" : \"SII\",\n" +
                        "\"bio\" : \"Bio\",\n" +
                        "\"www.thomas-maugin.fr, https://github.com/Thom-x\" : \"\",\n" +
                        "\"sessionName\" : \"session name\",\n" +
                        "\"description\" : \"description\",\n" +
                        "\"references\" : \"refs\",\n" +
                        "\"difficulty\" : \"3\",\n" +
                        "\"type\" : \"conference\",\n" +
                        "\"track\" : \"web\",\n" +
                        "\"coSpeaker\" : \"moi, toi\",\n" +
                        "\"financial\" : \"true\",\n" +
                        "\"travel\" : \"false\",\n" +
                        "\"travelFrom\" : \"Angers\",\n" +
                        "\"hotel\" : \"false\",\n" +
                        "\"hotelDate\" : \"13/11/1992\"\n" +
                        "}")
                .when()
                .post("/api/restricted/session")
                .then()
                .statusCode(400);
    }

    @Test
    public void test11_addRowFailDifficultyRest() {
        given()
                .contentType("application/json")
                .header(getHeader())
                .body("{\n" +
                        "\"email\" : \"email@email.fr\",\n" +
                        "\"name\" : \"Maugin\",\n" +
                        "\"firstname\" : \"Thomas\",\n" +
                        "\"phone\" : \"33683653379\",\n" +
                        "\"company\" : \"SII\",\n" +
                        "\"bio\" : \"Bio\",\n" +
                        "\"social\" : \"www.thomas-maugin.fr, https://github.com/Thom-x\",\n" +
                        "\"sessionName\" : \"session name\",\n" +
                        "\"description\" : \"description\",\n" +
                        "\"references\" : \"refs\",\n" +
                        "\"difficulty\" : \"5\",\n" +
                        "\"type\" : \"conference\",\n" +
                        "\"track\" : \"web\",\n" +
                        "\"coSpeaker\" : \"moi, toi\",\n" +
                        "\"financial\" : \"true\",\n" +
                        "\"travel\" : \"true\",\n" +
                        "\"travelFrom\" : \"Angers\",\n" +
                        "\"hotel\" : \"true\",\n" +
                        "}")
                .when()
                .post("/api/restricted/session")
                .then()
                .statusCode(400);
    }

    @Test
    public void test12_getRows() {
        try {
            deleteRows();
            test01_addRowPass();
            test01_addRowPass();
            given()
                    .header(getHeader())
                    .contentType("application/json")
                    .when()
                    .get("/api/restricted/session")
                    .then()
                    .statusCode(200)
                    .body("size()", equalTo(2));
        } catch (ServiceException | EntityNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test13_getRowsOtherAccount() {
        try {
            deleteRows();
            test01_addRowPass();
            test01_addRowPass();
            given()
                    .header(getHeaderOtherAccount())
                    .contentType("application/json")
                    .when()
                    .get("/api/restricted/session")
                    .then()
                    .statusCode(200)
                    .body("size()", equalTo(0));
        } catch (ServiceException | EntityNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test14_getRowsNotVerified() {
        given()
                .header(getHeaderNotVerified())
                .contentType("application/json")
                .when()
                .get("/api/restricted/session")
                .then()
                .statusCode(403);
    }

    @Test
    public void test15_getRowsNotAuthenticated() {
        given()
                .contentType("application/json")
                .when()
                .get("/api/restricted/session")
                .then()
                .statusCode(403);
    }

    @Test
    public void test16_getRow() {
        MockMvcResponse response = given().contentType("application/json")
                .header(getHeader())
                .body("{\n" +
                        "\"email\" : \"email@email.fr\",\n" +
                        "\"name\" : \"Maugin1\",\n" +
                        "\"firstname\" : \"Thomas\",\n" +
                        "\"phone\" : \"33683653379\",\n" +
                        "\"company\" : \"SII\",\n" +
                        "\"bio\" : \"Bio\",\n" +
                        "\"social\" : \"www.thomas-maugin.fr, https://github.com/Thom-x\",\n" +
                        "\"sessionName\" : \"session name\",\n" +
                        "\"description\" : \"description\",\n" +
                        "\"references\" : \"refs\",\n" +
                        "\"difficulty\" : \"3\",\n" +
                        "\"type\" : \"conference\",\n" +
                        "\"track\" : \"web\",\n" +
                        "\"coSpeaker\" : \"moi, toi\",\n" +
                        "\"financial\" : \"true\",\n" +
                        "\"travel\" : \"true\",\n" +
                        "\"travelFrom\" : \"Angers\",\n" +
                        "\"hotel\" : \"true\",\n" +
                        "\"hotelDate\" : \"13/11/1992\"\n" +
                        "}")
                .when()
                .post("/api/restricted/session")
                .then()
                .statusCode(200)
                .body("name", Matchers.is("Maugin1"))
                .body("userId", Matchers.is(1))
                .body("difficulty", Matchers.is(3)).extract().
                response();
        Long added = response.path("added");

        test01_addRowPass();
        given()
                .header(getHeader())
                .contentType("application/json")
                .when()
                .get("/api/restricted/session/" + added.toString())
                .then()
                .statusCode(200)
                .body("userId", Matchers.is(1))
                .body("name", Matchers.is("Maugin1"));
    }

    @Test
    public void test17_getRowOtherAccount() {
        MockMvcResponse response = given().contentType("application/json")
                .header(getHeader())
                .body("{\n" +
                        "\"email\" : \"email@email.fr\",\n" +
                        "\"name\" : \"Maugin1\",\n" +
                        "\"firstname\" : \"Thomas\",\n" +
                        "\"phone\" : \"33683653379\",\n" +
                        "\"company\" : \"SII\",\n" +
                        "\"bio\" : \"Bio\",\n" +
                        "\"social\" : \"www.thomas-maugin.fr, https://github.com/Thom-x\",\n" +
                        "\"sessionName\" : \"session name\",\n" +
                        "\"description\" : \"description\",\n" +
                        "\"references\" : \"refs\",\n" +
                        "\"difficulty\" : \"3\",\n" +
                        "\"type\" : \"conference\",\n" +
                        "\"track\" : \"web\",\n" +
                        "\"coSpeaker\" : \"moi, toi\",\n" +
                        "\"financial\" : \"true\",\n" +
                        "\"travel\" : \"true\",\n" +
                        "\"travelFrom\" : \"Angers\",\n" +
                        "\"hotel\" : \"true\",\n" +
                        "\"hotelDate\" : \"13/11/1992\"\n" +
                        "}")
                .when()
                .post("/api/restricted/session")
                .then()
                .statusCode(200)
                .body("name", Matchers.is("Maugin1"))
                .body("userId", Matchers.is(1))
                .body("difficulty", Matchers.is(3)).extract().
                response();
        Long added = response.path("added");

        test01_addRowPass();
        given()
                .contentType("application/json")
                .header(getHeaderOtherAccount())
                .when()
                .get("/api/restricted/session/" + added.toString())
                .then()
                .statusCode(403);
    }

    @Test
    public void test18_getRowNotFound() {
                given()
                .contentType("application/json")
                .header(getHeaderOtherAccount())
                .when()
                .get("/api/restricted/session/1")
                .then()
                .statusCode(404);
    }

    @Test
    public void test19_getRowNotVerified() {
        given()
                .header(getHeaderNotVerified())
                .contentType("application/json")
                .when()
                .get("/api/restricted/session/1")
                .then()
                .statusCode(403);
    }

    @Test
    public void test20_getRowNotAuthenticated() {
        given()
                .contentType("application/json")
                .when()
                .get("/api/restricted/session/1")
                .then()
                .statusCode(403);
    }

    @Test
    public void test21_getRowsDraft() {
        try {
            deleteRows();
            test02_addRowDraftPass();
            test01_addRowPass();
            test02_addRowDraftPass();
            given()
                    .header(getHeader())
                    .contentType("application/json")
                    .when()
                    .get("/api/restricted/draft")
                    .then()
                    .statusCode(200)
                    .body("size()", equalTo(2));
        } catch (ServiceException | EntityNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test22_getRowsDraftOtherAccount() {
        try {
            deleteRows();
            test02_addRowDraftPass();
            test01_addRowPass();
            test02_addRowDraftPass();
            given()
                    .header(getHeaderOtherAccount())
                    .contentType("application/json")
                    .when()
                    .get("/api/restricted/draft")
                    .then()
                    .statusCode(200)
                    .body("size()", equalTo(0));
        } catch (ServiceException | EntityNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test23_getRowsDraftNotVerified() {
        given()
                .header(getHeaderNotVerified())
                .contentType("application/json")
                .when()
                .get("/api/restricted/draft")
                .then()
                .statusCode(403);
    }


    @Test
    public void test24_getRowsDraftNotAuthenticated() {
        given()
                .contentType("application/json")
                .when()
                .get("/api/restricted/draft")
                .then()
                .statusCode(403);
    }

    @Test
    public void test25_getRowDraft() {
        MockMvcResponse response = given().contentType("application/json")
                .header(getHeader())
                .body("{\n" +
                        "\"email\" : \"email@email.fr\",\n" +
                        "\"name\" : \"Maugin1\",\n" +
                        "\"firstname\" : \"Thomas\",\n" +
                        "\"phone\" : \"33683653379\",\n" +
                        "\"company\" : \"SII\",\n" +
                        "\"bio\" : \"Bio\",\n" +
                        "\"social\" : \"www.thomas-maugin.fr, https://github.com/Thom-x\",\n" +
                        "\"sessionName\" : \"session name\",\n" +
                        "\"description\" : \"description\",\n" +
                        "\"references\" : \"refs\",\n" +
                        "\"difficulty\" : \"3\",\n" +
                        "\"type\" : \"conference\",\n" +
                        "\"track\" : \"web\",\n" +
                        "\"coSpeaker\" : \"moi, toi\",\n" +
                        "\"financial\" : \"true\",\n" +
                        "\"travel\" : \"true\",\n" +
                        "\"travelFrom\" : \"Angers\",\n" +
                        "\"hotel\" : \"true\",\n" +
                        "\"hotelDate\" : \"13/11/1992\"\n" +
                        "}")
                .when()
                .post("/api/restricted/draft")
                .then()
                .statusCode(200)
                .body("name", Matchers.is("Maugin1"))
                .body("userId", Matchers.is(1))
                .body("difficulty", Matchers.is(3)).extract().
                        response();
        Long added = response.path("added");

        test02_addRowDraftPass();
        given()
                .header(getHeader())
                .contentType("application/json")
                .when()
                .get("/api/restricted/draft/" + added.toString())
                .then()
                .statusCode(200)
                .body("userId", Matchers.is(1))
                .body("name", Matchers.is("Maugin1"));
    }

    @Test
    public void test26_getRowDraftOtherAccount() {
        MockMvcResponse response = given().contentType("application/json")
                .header(getHeader())
                .body("{\n" +
                        "\"email\" : \"email@email.fr\",\n" +
                        "\"name\" : \"Maugin1\",\n" +
                        "\"firstname\" : \"Thomas\",\n" +
                        "\"phone\" : \"33683653379\",\n" +
                        "\"company\" : \"SII\",\n" +
                        "\"bio\" : \"Bio\",\n" +
                        "\"social\" : \"www.thomas-maugin.fr, https://github.com/Thom-x\",\n" +
                        "\"sessionName\" : \"session name\",\n" +
                        "\"description\" : \"description\",\n" +
                        "\"references\" : \"refs\",\n" +
                        "\"difficulty\" : \"3\",\n" +
                        "\"type\" : \"conference\",\n" +
                        "\"track\" : \"web\",\n" +
                        "\"coSpeaker\" : \"moi, toi\",\n" +
                        "\"financial\" : \"true\",\n" +
                        "\"travel\" : \"true\",\n" +
                        "\"travelFrom\" : \"Angers\",\n" +
                        "\"hotel\" : \"true\",\n" +
                        "\"hotelDate\" : \"13/11/1992\"\n" +
                        "}")
                .when()
                .post("/api/restricted/draft")
                .then()
                .statusCode(200)
                .body("name", Matchers.is("Maugin1"))
                .body("userId", Matchers.is(1))
                .body("difficulty", Matchers.is(3)).extract().
                        response();
        Long added = response.path("added");

        test02_addRowDraftPass();
        given()
                .header(getHeaderOtherAccount())
                .contentType("application/json")
                .when()
                .get("/api/restricted/draft/" + added.toString())
                .then()
                .statusCode(403);
    }

    @Test
    public void test27_getRowDraftNotFound() {
        given()
                .header(getHeaderOtherAccount())
                .contentType("application/json")
                .when()
                .get("/api/restricted/draft/1")
                .then()
                .statusCode(404);
    }

    @Test
    public void test28_getRowDraftNotVerified() {
        given()
                .header(getHeaderNotVerified())
                .contentType("application/json")
                .when()
                .get("/api/restricted/draft/1")
                .then()
                .statusCode(403);
    }

    @Test
    public void test29_getRowDraftNotAuthenticated() {
        given()
                .contentType("application/json")
                .when()
                .get("/api/restricted/draft/1")
                .then()
                .statusCode(403);
    }

    @Test
    public void test30_putRow() {
        MockMvcResponse response = given().contentType("application/json")
                .header(getHeader())
                .body("{\n" +
                        "\"email\" : \"email@email.fr\",\n" +
                        "\"name\" : \"Maugin1\",\n" +
                        "\"firstname\" : \"Thomas\",\n" +
                        "\"phone\" : \"33683653379\",\n" +
                        "\"company\" : \"SII\",\n" +
                        "\"bio\" : \"Bio\",\n" +
                        "\"social\" : \"www.thomas-maugin.fr, https://github.com/Thom-x\",\n" +
                        "\"sessionName\" : \"session name\",\n" +
                        "\"description\" : \"description\",\n" +
                        "\"references\" : \"refs\",\n" +
                        "\"difficulty\" : \"3\",\n" +
                        "\"type\" : \"conference\",\n" +
                        "\"track\" : \"web\",\n" +
                        "\"coSpeaker\" : \"moi, toi\",\n" +
                        "\"financial\" : \"true\",\n" +
                        "\"travel\" : \"true\",\n" +
                        "\"travelFrom\" : \"Angers\",\n" +
                        "\"hotel\" : \"true\",\n" +
                        "\"hotelDate\" : \"13/11/1992\"\n" +
                        "}")
                .when()
                .post("/api/restricted/draft")
                .then()
                .statusCode(200)
                .body("name", Matchers.is("Maugin1"))
                .body("userId", Matchers.is(1))
                .body("difficulty", Matchers.is(3)).extract().
                        response();
        Long added = response.path("added");
        given()
                .contentType("application/json")
                .header(getHeader())
                .body("{\n" +
                        "\"email\" : \"email@email.fr\",\n" +
                        "\"name\" : \"Maugin2\",\n" +
                        "\"firstname\" : \"Thomas\",\n" +
                        "\"phone\" : \"33683653379\",\n" +
                        "\"company\" : \"SII\",\n" +
                        "\"bio\" : \"Bio\",\n" +
                        "\"social\" : \"www.thomas-maugin.fr, https://github.com/Thom-x\",\n" +
                        "\"sessionName\" : \"session name\",\n" +
                        "\"description\" : \"description\",\n" +
                        "\"references\" : \"refs\",\n" +
                        "\"difficulty\" : \"3\",\n" +
                        "\"type\" : \"conference\",\n" +
                        "\"track\" : \"web\",\n" +
                        "\"coSpeaker\" : \"moi, toi\",\n" +
                        "\"financial\" : \"true\",\n" +
                        "\"travel\" : \"true\",\n" +
                        "\"travelFrom\" : \"Angers\",\n" +
                        "\"hotel\" : \"true\",\n" +
                        "\"hotelDate\" : \"13/11/1992\"\n" +
                        "}")
                .when()
                .put("/api/restricted/session/" + added.toString())
                .then()
                .statusCode(200)
                .body("name", Matchers.is("Maugin2"))
                .body("userId", Matchers.is(1))
                .body("draft", Matchers.is(false));
    }

    @Test
    public void test31_putRowOtherAccount() {
        MockMvcResponse response = given().contentType("application/json")
                .header(getHeader())
                .body("{\n" +
                        "\"email\" : \"email@email.fr\",\n" +
                        "\"name\" : \"Maugin1\",\n" +
                        "\"firstname\" : \"Thomas\",\n" +
                        "\"phone\" : \"33683653379\",\n" +
                        "\"company\" : \"SII\",\n" +
                        "\"bio\" : \"Bio\",\n" +
                        "\"social\" : \"www.thomas-maugin.fr, https://github.com/Thom-x\",\n" +
                        "\"sessionName\" : \"session name\",\n" +
                        "\"description\" : \"description\",\n" +
                        "\"references\" : \"refs\",\n" +
                        "\"difficulty\" : \"3\",\n" +
                        "\"type\" : \"conference\",\n" +
                        "\"track\" : \"web\",\n" +
                        "\"coSpeaker\" : \"moi, toi\",\n" +
                        "\"financial\" : \"true\",\n" +
                        "\"travel\" : \"true\",\n" +
                        "\"travelFrom\" : \"Angers\",\n" +
                        "\"hotel\" : \"true\",\n" +
                        "\"hotelDate\" : \"13/11/1992\"\n" +
                        "}")
                .when()
                .post("/api/restricted/draft")
                .then()
                .statusCode(200)
                .body("name", Matchers.is("Maugin1"))
                .body("userId", Matchers.is(1))
                .body("difficulty", Matchers.is(3)).extract().
                        response();
        Long added = response.path("added");
        given()
                .contentType("application/json")
                .header(getHeaderOtherAccount())
                .body("{\n" +
                        "\"email\" : \"emailOtherAccount@email.fr\",\n" +
                        "\"name\" : \"Maugin2\",\n" +
                        "\"firstname\" : \"Thomas\",\n" +
                        "\"phone\" : \"33683653379\",\n" +
                        "\"company\" : \"SII\",\n" +
                        "\"bio\" : \"Bio\",\n" +
                        "\"social\" : \"www.thomas-maugin.fr, https://github.com/Thom-x\",\n" +
                        "\"sessionName\" : \"session name\",\n" +
                        "\"description\" : \"description\",\n" +
                        "\"references\" : \"refs\",\n" +
                        "\"difficulty\" : \"3\",\n" +
                        "\"type\" : \"conference\",\n" +
                        "\"track\" : \"web\",\n" +
                        "\"coSpeaker\" : \"moi, toi\",\n" +
                        "\"financial\" : \"true\",\n" +
                        "\"travel\" : \"true\",\n" +
                        "\"travelFrom\" : \"Angers\",\n" +
                        "\"hotel\" : \"true\",\n" +
                        "\"hotelDate\" : \"13/11/1992\"\n" +
                        "}")
                .when()
                .put("/api/restricted/session/" + added.toString())
                .then()
                .statusCode(403);
    }

    @Test
    public void test32_putRowNotFound() {
        given()
                .contentType("application/json")
                .header(getHeaderOtherAccount())
                .body("{\n" +
                        "\"email\" : \"emailOtherAccount@email.fr\",\n" +
                        "\"name\" : \"Maugin2\",\n" +
                        "\"firstname\" : \"Thomas\",\n" +
                        "\"phone\" : \"33683653379\",\n" +
                        "\"company\" : \"SII\",\n" +
                        "\"bio\" : \"Bio\",\n" +
                        "\"social\" : \"www.thomas-maugin.fr, https://github.com/Thom-x\",\n" +
                        "\"sessionName\" : \"session name\",\n" +
                        "\"description\" : \"description\",\n" +
                        "\"references\" : \"refs\",\n" +
                        "\"difficulty\" : \"3\",\n" +
                        "\"type\" : \"conference\",\n" +
                        "\"track\" : \"web\",\n" +
                        "\"coSpeaker\" : \"moi, toi\",\n" +
                        "\"financial\" : \"true\",\n" +
                        "\"travel\" : \"true\",\n" +
                        "\"travelFrom\" : \"Angers\",\n" +
                        "\"hotel\" : \"true\",\n" +
                        "\"hotelDate\" : \"13/11/1992\"\n" +
                        "}")
                .when()
                .put("/api/restricted/session/1")
                .then()
                .statusCode(404);
    }

    @Test
    public void test33_putRowNotVerified() {
        given()
                .contentType("application/json")
                .header(getHeaderNotVerified())
                .body("{\n" +
                        "\"email\" : \"email@email.fr\",\n" +
                        "\"name\" : \"Maugin2\",\n" +
                        "\"firstname\" : \"Thomas\",\n" +
                        "\"phone\" : \"33683653379\",\n" +
                        "\"company\" : \"SII\",\n" +
                        "\"bio\" : \"Bio\",\n" +
                        "\"social\" : \"www.thomas-maugin.fr, https://github.com/Thom-x\",\n" +
                        "\"sessionName\" : \"session name\",\n" +
                        "\"description\" : \"description\",\n" +
                        "\"references\" : \"refs\",\n" +
                        "\"difficulty\" : \"3\",\n" +
                        "\"type\" : \"conference\",\n" +
                        "\"track\" : \"web\",\n" +
                        "\"coSpeaker\" : \"moi, toi\",\n" +
                        "\"financial\" : \"true\",\n" +
                        "\"travel\" : \"true\",\n" +
                        "\"travelFrom\" : \"Angers\",\n" +
                        "\"hotel\" : \"true\",\n" +
                        "\"hotelDate\" : \"13/11/1992\"\n" +
                        "}")
                .when()
                .put("/api/restricted/session/1")
                .then()
                .statusCode(403);
    }

    @Test
    public void test34_putRowNotAuthenticated() {
        given()
                .contentType("application/json")
                .body("{\n" +
                        "\"email\" : \"email@email.fr\",\n" +
                        "\"name\" : \"Maugin2\",\n" +
                        "\"firstname\" : \"Thomas\",\n" +
                        "\"phone\" : \"33683653379\",\n" +
                        "\"company\" : \"SII\",\n" +
                        "\"bio\" : \"Bio\",\n" +
                        "\"social\" : \"www.thomas-maugin.fr, https://github.com/Thom-x\",\n" +
                        "\"sessionName\" : \"session name\",\n" +
                        "\"description\" : \"description\",\n" +
                        "\"references\" : \"refs\",\n" +
                        "\"difficulty\" : \"3\",\n" +
                        "\"type\" : \"conference\",\n" +
                        "\"track\" : \"web\",\n" +
                        "\"coSpeaker\" : \"moi, toi\",\n" +
                        "\"financial\" : \"true\",\n" +
                        "\"travel\" : \"true\",\n" +
                        "\"travelFrom\" : \"Angers\",\n" +
                        "\"hotel\" : \"true\",\n" +
                        "\"hotelDate\" : \"13/11/1992\"\n" +
                        "}")
                .when()
                .put("/api/restricted/session/1")
                .then()
                .statusCode(403);
    }

    @Test
    public void test35_deleteDraft() {
        MockMvcResponse response = given().contentType("application/json")
                .header(getHeader())
                .body("{\n" +
                        "\"email\" : \"email@email.fr\",\n" +
                        "\"name\" : \"Maugin1\",\n" +
                        "\"firstname\" : \"Thomas\",\n" +
                        "\"phone\" : \"33683653379\",\n" +
                        "\"company\" : \"SII\",\n" +
                        "\"bio\" : \"Bio\",\n" +
                        "\"social\" : \"www.thomas-maugin.fr, https://github.com/Thom-x\",\n" +
                        "\"sessionName\" : \"session name\",\n" +
                        "\"description\" : \"description\",\n" +
                        "\"references\" : \"refs\",\n" +
                        "\"difficulty\" : \"3\",\n" +
                        "\"type\" : \"conference\",\n" +
                        "\"track\" : \"web\",\n" +
                        "\"coSpeaker\" : \"moi, toi\",\n" +
                        "\"financial\" : \"true\",\n" +
                        "\"travel\" : \"true\",\n" +
                        "\"travelFrom\" : \"Angers\",\n" +
                        "\"hotel\" : \"true\",\n" +
                        "\"hotelDate\" : \"13/11/1992\"\n" +
                        "}")
                .when()
                .post("/api/restricted/draft")
                .then()
                .statusCode(200)
                .body("name", Matchers.is("Maugin1"))
                .body("userId", Matchers.is(1))
                .body("difficulty", Matchers.is(3)).extract().
                        response();
        Long added = response.path("added");
        given()
                .contentType("application/json")
                .header(getHeader())
                .delete("/api/restricted/draft/" + added.toString())
                .then()
                .statusCode(200);

        given()
                .header(getHeader())
                .contentType("application/json")
                .when()
                .get("/api/restricted/draft/" + added.toString())
                .then()
                .statusCode(404);
    }

    @Test
    public void test36_deleteDraftNotFound() {
        given()
                .contentType("application/json")
                .header(getHeader())
                .delete("/api/restricted/draft/1")
                .then()
                .statusCode(404);
    }

    @Test
    public void test37_deleteDraftOtherAccount() {
        MockMvcResponse response = given().contentType("application/json")
                .header(getHeader())
                .body("{\n" +
                        "\"email\" : \"email@email.fr\",\n" +
                        "\"name\" : \"Maugin1\",\n" +
                        "\"firstname\" : \"Thomas\",\n" +
                        "\"phone\" : \"33683653379\",\n" +
                        "\"company\" : \"SII\",\n" +
                        "\"bio\" : \"Bio\",\n" +
                        "\"social\" : \"www.thomas-maugin.fr, https://github.com/Thom-x\",\n" +
                        "\"sessionName\" : \"session name\",\n" +
                        "\"description\" : \"description\",\n" +
                        "\"references\" : \"refs\",\n" +
                        "\"difficulty\" : \"3\",\n" +
                        "\"type\" : \"conference\",\n" +
                        "\"track\" : \"web\",\n" +
                        "\"coSpeaker\" : \"moi, toi\",\n" +
                        "\"financial\" : \"true\",\n" +
                        "\"travel\" : \"true\",\n" +
                        "\"travelFrom\" : \"Angers\",\n" +
                        "\"hotel\" : \"true\",\n" +
                        "\"hotelDate\" : \"13/11/1992\"\n" +
                        "}")
                .when()
                .post("/api/restricted/draft")
                .then()
                .statusCode(200)
                .body("name", Matchers.is("Maugin1"))
                .body("userId", Matchers.is(1))
                .body("difficulty", Matchers.is(3)).extract().
                        response();
        Long added = response.path("added");
        given()
                .contentType("application/json")
                .header(getHeaderOtherAccount())
                .delete("/api/restricted/draft/" + added.toString())
                .then()
                .statusCode(403);

        given()
                .header(getHeader())
                .contentType("application/json")
                .when()
                .get("/api/restricted/draft/" + added.toString())
                .then()
                .statusCode(200);
    }

    @Test
    public void test38_deleteDraftNotVerified() {
        given()
                .contentType("application/json")
                .header(getHeaderNotVerified())
                .delete("/api/restricted/draft/1")
                .then()
                .statusCode(403);
    }

    @Test
    public void test39_deleteDraftNotAuthorized() {
        given()
                .contentType("application/json")
                .delete("/api/restricted/draft/1")
                .then()
                .statusCode(403);
    }

    public void deleteRows() throws ServiceException, EntityNotFoundException, IOException {
        spreadsheetService.deleteRows();
    }
}