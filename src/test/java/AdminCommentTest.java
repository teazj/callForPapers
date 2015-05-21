/**
 * Created by tmaugin on 06/05/2015.
*/

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import fr.sii.domain.admin.comment.AdminComment;
import fr.sii.service.admin.comment.AdminCommentService;
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
@ContextConfiguration(locations={"classpath:META-INF/spring/applicationContext.xml","classpath:META-INF/spring/dispatcherServletTest.xml"})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AdminCommentTest {

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
    AdminCommentService adminCommentService;

    @After
    public void tearDown() {
        authHelper.tearDown();
    }

    @Test
    public void test1_insert()
    {
        AdminComment c = new AdminComment();
        c.setUserId(1L);
        c.setComment("Lorem ipsum");
        c.setRowId(1L);
        AdminComment savedAdminComment = adminCommentService.save(c);

        assertNotNull(savedAdminComment);
        assertEquals("Lorem ipsum", savedAdminComment.getComment());
        assertEquals((Long) 1L,savedAdminComment.getUserId());
        assertEquals((Long) 1L, savedAdminComment.getRowId());
        assertNotNull(savedAdminComment.getEntityId());
    }

    @Test
    public void test2_postRest()
    {
        given()
                .contentType("application/json")
                .body("{\n" +
                        "\"comment\" : \"Lorem ipsum\",\n" +
                        "\"rowId\" : \"1\"\n" +
                        "}")
                .when()
                .post("/api/admin/comment")
                .then()
                .statusCode(200)
                .body("comment", Matchers.is("Lorem ipsum"))
                .body("rowId", Matchers.is(1))
                .body("userId", Matchers.is(68276854));
    }
}