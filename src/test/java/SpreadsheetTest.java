import com.google.gdata.util.ServiceException;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import com.jayway.restassured.module.mockmvc.response.MockMvcResponse;
import com.jayway.restassured.response.Header;
import com.nimbusds.jose.JOSEException;
import fr.sii.domain.spreadsheet.Row;
import fr.sii.domain.spreadsheet.RowDraft;
import fr.sii.domain.spreadsheet.RowSession;
import fr.sii.domain.token.Token;
import fr.sii.service.auth.AuthUtils;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


/**
 * Created by tmaugin on 08/04/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:META-INF/spring/applicationContext.xml","classpath:META-INF/spring/dispatcherServletTest.xml"})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SpreadsheetTest {

    @Autowired
    private fr.sii.service.spreadsheet.SpreadsheetService spreadsheetService;

    @Autowired
    WebApplicationContext webApplicationContext;

    String secretToken = "secretTestsecretTestsecretTestsecretTestsecretTestsecretTestsecretTestsecretTest";

    @Before
    public void setUp() {
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
    public void test1_addRowPass() {
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
        row.setTrack("web");
        row.setTravel(false);
        row.setDraft(false);
        row.setUserId(1L);

        Row returnedRow = new RowSession();

        boolean error = false;
        try {
            returnedRow = spreadsheetService.addRow(row);
        } catch (IOException e) {
            e.printStackTrace();
            error = true;
        } catch (ServiceException e) {
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
        assertEquals(false,returnedRow.getTravel());
        assertNotEquals(null, returnedRow.getAdded());
        assertEquals(false, returnedRow.getDraft());
        assertEquals((Long) 1L, returnedRow.getUserId());
    }

    @Test
    public void test2_addRowDraftPass() {
        Row row = new RowDraft();
        row.setDraft(true);
        row.setUserId(1L);

        Row returnedRow = new RowSession();

        boolean error = false;
        try {
            returnedRow = spreadsheetService.addRow(row);
        } catch (IOException e) {
            e.printStackTrace();
            error = true;
        } catch (ServiceException e) {
            e.printStackTrace();
            error = true;
        }
        assertEquals(false,error);

        assertEquals(true,returnedRow.getDraft());
        assertEquals((Long)1L,returnedRow.getUserId());
        assertNotEquals(null, returnedRow.getAdded());
    }

    @Test
    public void test3_addRowPassRest() {

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
    public void test4_addRowNotVerifiedRest() {

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
    public void test5_addRowNotAuthenticatedRest() {
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
    public void test6_addRowDraftPassRest() {
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
    public void test7_addRowDraftNotVerifiedRest() {
        given().contentType("application/json")
                .header(getHeaderNotVerified())
                .body("{}")
                .when()
                .post("/api/restricted/draft")
                .then()
                .statusCode(403);
    }

    @Test
    public void test8_addRowDraftNotAuthenticatedRest() {
        given().contentType("application/json")
                .body("{}")
                .when()
                .post("/api/restricted/draft")
                .then()
                .statusCode(403);
    }

    @Test
    public void test9_addRowFailFinancialRest() {
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
        test36_deleteRows();
        test1_addRowPass();
        test1_addRowPass();
        given()
                .header(getHeader())
                .contentType("application/json")
                .when()
                .get("/api/restricted/session")
                .then()
                .statusCode(200)
                .body("size()", equalTo(2));
    }

    @Test
    public void test13_getRowsOtherAccount() {
        test36_deleteRows();
        test1_addRowPass();
        test1_addRowPass();
        given()
                .header(getHeaderOtherAccount())
                .contentType("application/json")
                .when()
                .get("/api/restricted/session")
                .then()
                .statusCode(200)
                .body("size()", equalTo(0));
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

        test1_addRowPass();
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

        test1_addRowPass();
        given()
                .contentType("application/json")
                .header(getHeaderOtherAccount())
                .when()
                .get("/api/restricted/session/" + added.toString())
                .then()
                .statusCode(200)
                .body(isEmptyString());
    }

    @Test
    public void test18_getRowNotVerified() {
        given()
                .header(getHeaderNotVerified())
                .contentType("application/json")
                .when()
                .get("/api/restricted/session/1")
                .then()
                .statusCode(403);
    }

    @Test
    public void test19_getRowNotAuthenticated() {
        given()
                .contentType("application/json")
                .when()
                .get("/api/restricted/session/1")
                .then()
                .statusCode(403);
    }

    @Test
    public void test20_getRowsDraft() {
        test36_deleteRows();
        test2_addRowDraftPass();
        test1_addRowPass();
        test2_addRowDraftPass();
        given()
                .header(getHeader())
                .contentType("application/json")
                .when()
                .get("/api/restricted/draft")
                .then()
                .statusCode(200)
                .body("size()", equalTo(2));
    }

    @Test
    public void test21_getRowsDraftOtherAccount() {
        test36_deleteRows();
        test2_addRowDraftPass();
        test1_addRowPass();
        test2_addRowDraftPass();
        given()
                .header(getHeaderOtherAccount())
                .contentType("application/json")
                .when()
                .get("/api/restricted/draft")
                .then()
                .statusCode(200)
                .body("size()", equalTo(0));
    }

    @Test
    public void test22_getRowsDraftNotVerified() {
        given()
                .header(getHeaderNotVerified())
                .contentType("application/json")
                .when()
                .get("/api/restricted/draft")
                .then()
                .statusCode(403);
    }


    @Test
    public void test23_getRowsDraftNotAuthenticated() {
        given()
                .contentType("application/json")
                .when()
                .get("/api/restricted/draft")
                .then()
                .statusCode(403);
    }

    @Test
    public void test24_getRowDraft() {
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

        test2_addRowDraftPass();
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
    public void test25_getRowDraftOtherAccount() {
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

        test2_addRowDraftPass();
        given()
                .header(getHeaderOtherAccount())
                .contentType("application/json")
                .when()
                .get("/api/restricted/draft/" + added.toString())
                .then()
                .statusCode(200)
                .body(isEmptyString());
    }

    @Test
    public void test26_getRowDraftNotVerified() {
        given()
                .header(getHeaderNotVerified())
                .contentType("application/json")
                .when()
                .get("/api/restricted/draft/1")
                .then()
                .statusCode(403);
    }

    @Test
    public void test27_getRowDraftNotAuthenticated() {
        given()
                .contentType("application/json")
                .when()
                .get("/api/restricted/draft/1")
                .then()
                .statusCode(403);
    }

    @Test
    public void test28_putRow() {
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
    public void test29_putRowOtherAccount() {
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
                .body(isEmptyString());

        given()
                .header(getHeader())
                .contentType("application/json")
                .when()
                .get("/api/restricted/draft/" + added.toString())
                .then()
                .statusCode(200)
                .body("email", Matchers.is("email@email.fr"));
    }

    @Test
    public void test30_putRowNotVerified() {
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
    public void test31_putRowNotAuthenticated() {
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
    public void test32_deleteDraft() {
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
                .statusCode(200)
                .body(isEmptyString());
    }

    @Test
    public void test33_deleteDraftOtherAccount() {
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
                .statusCode(200);

        given()
                .header(getHeader())
                .contentType("application/json")
                .when()
                .get("/api/restricted/draft/" + added.toString())
                .then()
                .statusCode(200)
                .body("userId",Matchers.is(1));
    }

    @Test
    public void test34_deleteDraftNotVerified() {
        given()
                .contentType("application/json")
                .header(getHeaderNotVerified())
                .delete("/api/restricted/draft/1")
                .then()
                .statusCode(403);
    }

    @Test
    public void test35_deleteDraftNotAuthorized() {
        given()
                .contentType("application/json")
                .delete("/api/restricted/draft/1")
                .then()
                .statusCode(403);
    }

    @Test
    public void test36_deleteRows() {
        given()
                .contentType("application/json")
                .when()
                .delete("/api/admin/session");
    }
}