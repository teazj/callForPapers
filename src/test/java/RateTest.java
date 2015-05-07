/**
 * Created by tmaugin on 06/05/2015.
 */
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import fr.sii.domain.rate.Rate;
import fr.sii.service.rate.RateService;
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


import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:META-INF/spring/applicationContext.xml","classpath:META-INF/spring/dispatcherServletTest.xml"})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RateTest {

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
    RateService rateService;

    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Test
    public void test1_insert()
    {
        Rate r = new Rate();
        r.setUserId(1L);
        r.setRate(4);
        r.setRowId(1L);
        Rate savedRate = rateService.save(r);
        assertNotNull(savedRate);
        assertNotNull(savedRate.getEntityId());
    }
}