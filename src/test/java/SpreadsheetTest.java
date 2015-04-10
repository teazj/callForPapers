import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import com.jayway.restassured.RestAssured;
import fr.sii.Application;
import fr.sii.persistance.spreadsheet.RowModel;
import fr.sii.persistance.spreadsheet.SpreadsheetRepository;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import java.io.IOException;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


/**
 * Created by tmaugin on 08/04/2015.
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SpreadsheetTest {

    @Autowired
    SpreadsheetRepository spreadsheetRepository;

    @Value("${local.server.port}")
    int port;

    @Autowired
    private Environment env;

    @Before
    public void setUp() {
        try {
            spreadsheetRepository.login(env.getProperty("google.login"),env.getProperty("google.password"));
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
        RestAssured.port = port;
    }

    @Test
    public void test1_addRowPass() {
        RowModel row = new RowModel();
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
        row.setTrack("Web");
        row.setTravel(false);

        String spreadsheetName = env.getProperty("google.spreadsheetName");
        RowModel returnedRow = new RowModel();

        boolean error = false;
        try {
            returnedRow = spreadsheetRepository.addRow(spreadsheetName,"test",row);
        } catch (IOException e) {
            error = true;
        } catch (ServiceException e) {
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
        assertEquals("Web",returnedRow.getTrack());
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
                        "\"track\" : \"Web\",\n" +
                        "\"coSpeaker\" : \"moi, toi\",\n" +
                        "\"financial\" : \"true\",\n" +
                        "\"travel\" : \"true\",\n" +
                        "\"travelFrom\" : \"Angers\",\n" +
                        "\"hotel\" : \"true\",\n" +
                        "\"hotelDate\" : \"13/11/1992\"\n" +
                        "}")
                .when()
                .post("devfest/session")
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
                        "\"track\" : \"Web\",\n" +
                        "\"coSpeaker\" : \"moi, toi\",\n" +
                        "\"financial\" : \"true\",\n" +
                        "\"travel\" : \"true\",\n" +
                        "\"travelFrom\" : \"Angers\",\n" +
                        "\"hotel\" : \"true\",\n" +
                        "}")
                .when()
                .post("devfest/session")
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
                        "\"track\" : \"Web\",\n" +
                        "\"coSpeaker\" : \"moi, toi\",\n" +
                        "\"financial\" : \"true\",\n" +
                        "\"travel\" : \"false\",\n" +
                        "\"travelFrom\" : \"Angers\",\n" +
                        "\"hotel\" : \"false\",\n" +
                        "\"hotelDate\" : \"13/11/1992\"\n" +
                        "}")
                .when()
                .post("devfest/session")
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
                    "\"track\" : \"Web\",\n" +
                    "\"coSpeaker\" : \"moi, toi\",\n" +
                    "\"financial\" : \"true\",\n" +
                    "\"travel\" : \"true\",\n" +
                    "\"travelFrom\" : \"Angers\",\n" +
                    "\"hotel\" : \"true\",\n" +
                    "}")
            .when()
            .post("devfest/session")
            .then()
            .statusCode(400);
    }

    @Test
    public void test6_getRows() {
        test7_deleteRows();
        test1_addRowPass();
        test1_addRowPass();
        given()
                .contentType("application/json")
                .when()
                .get("devfest/session")
                .then()
                .statusCode(200)
                .body("size()", equalTo(2));
    }

    @Test
    public void test7_deleteRows() {
        test1_addRowPass();
        given()
                .contentType("application/json")
                .when()
                .delete("devfest/session")
                .then()
                .statusCode(200)
                .body("size()", equalTo(0));
    }
}