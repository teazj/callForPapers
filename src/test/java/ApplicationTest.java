import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.AfterClass;
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

import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.core.IsEqual.equalTo;


/**
 * Created by tmaugin on 08/04/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:META-INF/spring/applicationContext*.xml","classpath:META-INF/spring/dispatcherServlet*.xml"})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApplicationTest {

    private static final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @BeforeClass
    public static void beforeClass() {
        helper.setUp();
    }

    @AfterClass
    public static void afterClass() {
        helper.tearDown();
    }

    @Autowired
    WebApplicationContext webApplicationContext;

    @Before public void
    setUp() {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Test
    public void test1_getApplicetion() {
        given()
                .contentType("application/json")
                .when()
                .get("/api/application")
                .then()
                .statusCode(200)
                .body("size()", equalTo(5));
    }
}