/**
 * Created by tmaugin on 06/05/2015.
*/

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import fr.sii.domain.admin.comment.AdminComment;
import fr.sii.service.admin.comment.AdminCommentService;
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

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:META-INF/spring/applicationContext.xml","classpath:META-INF/spring/dispatcherServletTest.xml"})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AdminCommentTest {

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig().setDefaultHighRepJobPolicyUnappliedJobPercentage(100));
    @Autowired
    WebApplicationContext webApplicationContext;

    @Before public void
    setUp() {
        helper.setUp();
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Autowired
    AdminCommentService adminCommentService;

    @After
    public void tearDown() {
        helper.tearDown();
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
        assertNotNull(savedAdminComment.getEntityId());
    }
}