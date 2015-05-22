import com.google.gdata.util.ServiceException;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import com.jayway.restassured.module.mockmvc.response.MockMvcResponse;
import com.jayway.restassured.response.Header;
import com.nimbusds.jose.JOSEException;
import fr.sii.domain.spreadsheet.Row;
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
public class SpreadsheetAdminTest {

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
        row.setType("conference");
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
        assertEquals("conference",returnedRow.getType());
        assertEquals("web",returnedRow.getTrack());
        assertEquals(false,returnedRow.getTravel());
        assertNotEquals(null, returnedRow.getAdded());
        assertEquals(false, returnedRow.getDraft());
        assertEquals((Long) 1L, returnedRow.getUserId());
    }

    @Test
    public void test2_getRows() {
        deleteRows();
        test1_addRowPass();
        test1_addRowPass();
        given()
                .contentType("application/json")
                .when()
                .get("/api/admin/session")
                .then()
                .statusCode(200)
                .body("size()", equalTo(2));
    }

    @Test
    public void test3_getRow() {
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
                .body("difficulty", Matchers.is(3)).extract().
                        response();
        Long added = response.path("added");

        test1_addRowPass();
        given()
                .contentType("application/json")
                .when()
                .get("/api/admin/session/" + added.toString())
                .then()
                .statusCode(200)
                .body("name", Matchers.is("Maugin1"));
    }

    @Test
    public void test4_getRowNotFound() {
        given()
                .contentType("application/json")
                .when()
                .get("/api/admin/session/1")
                .then()
                .statusCode(404);
    }

    public void deleteRows()
    {
        try {
            spreadsheetService.deleteRows();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}