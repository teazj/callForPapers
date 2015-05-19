import com.google.gdata.util.ServiceException;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import com.jayway.restassured.module.mockmvc.response.MockMvcResponse;
import fr.sii.domain.spreadsheet.Row;
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
public class SpreadsheetTest {

    @Autowired
    private fr.sii.service.spreadsheet.SpreadsheetService spreadsheetService;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Before public void
    setUp() {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Test
    public void test1_addRowPass() {
        Row row = new Row();
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

        Row returnedRow = new Row();

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
    }

    @Test
    public void test2_addRowPassRest() {
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
                .statusCode(200)
                .body("name", Matchers.is("Maugin"))
                .body("difficulty", Matchers.is(3));
    }

    @Test
    public void test3_addRowFailFinancialRest() {
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
                        "}")
                .when()
                .post("/api/restricted/session")
                .then()
                .statusCode(400);
    }

    @Test
    public void test4_addRowFailFinancial2Rest() {
        given().contentType("application/json")
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
    public void test5_addRowFailDifficultyRest() {
        given()
                .contentType("application/json")
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
    public void test6_getRows() {
        test8_deleteRows();
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
    public void test7_getRow() {
        MockMvcResponse response = given().contentType("application/json")
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
    public void test8_deleteRows() {
        test1_addRowPass();
        given()
                .contentType("application/json")
                .when()
                .delete("/api/admin/session")
                .then()
                .statusCode(200)
                .body("size()", equalTo(0));
    }
}