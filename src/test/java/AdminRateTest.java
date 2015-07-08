/**
 * Created by tmaugin on 06/05/2015.
 */

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import fr.sii.domain.admin.rate.AdminRate;
import fr.sii.service.admin.rate.AdminRateService;
import org.hamcrest.Matchers;
import org.junit.After;
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

import java.util.HashMap;

import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:META-INF/spring/applicationContextTest.xml","classpath:META-INF/spring/dispatcherServletTest.xml"})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AdminRateTest {

    private LocalServiceTestHelper authHelper;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Before public void
    setUp() {
        HashMap<String, Object> envAttr = new HashMap<String, Object>();
        envAttr.put("com.google.appengine.api.users.UserService.user_id_key", "6827685435");

        //Setup the emulated Appengine environment
        authHelper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig().setDefaultHighRepJobPolicyUnappliedJobPercentage(100), new LocalUserServiceTestConfig())
                .setEnvIsAdmin(true).setEnvIsLoggedIn(true).setEnvEmail("maugin.thomas@gmail.com").setEnvAuthDomain("google.com").setEnvAttributes(envAttr);

        authHelper.setUp();

        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Autowired
    AdminRateService adminRateService;

    @After
    public void tearDown() {
        authHelper.tearDown();
    }

    @Test
    public void test1_insert()
    {
        AdminRate r = new AdminRate();
        r.setUserId(1L);
        r.setRate(4);
        r.setRowId(1L);
        AdminRate savedAdminRate = adminRateService.save(r);

        assertNotNull(savedAdminRate);
        assertEquals(4L, Long.parseLong(savedAdminRate.getRate().toString()));
        assertEquals((Long) 1L,savedAdminRate.getUserId());
        assertEquals((Long) 1L, savedAdminRate.getRowId());
        assertNotNull(savedAdminRate.getEntityId());
    }

    @Test
    public void test2_postRest()
    {
        given()
                .contentType("application/json")
                .body("{\n" +
                        "\"rate\" : \"3\",\n" +
                        "\"rowId\" : \"1\"\n" +
                        "}")
                .when()
                .post("/api/admin/rates")
                .then()
                .statusCode(200)
                .body("rate", Matchers.is(3))
                .body("rowId", Matchers.is(1))
                .body("userId", Matchers.is(68276854));
    }
}