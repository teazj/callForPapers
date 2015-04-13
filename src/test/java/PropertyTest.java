import fr.sii.Application;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;

/**
 * Created by tmaugin on 08/04/2015.
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")   // Start the server on a random port
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PropertyTest {

    @Autowired
    private Environment env;


    @Test
    public void test1_getApplicationPropertyGoogleApp() {
        assertEquals("CallForPaper",env.getProperty("google.spreadsheetName"));
        assertEquals("891245656445-kmikis13jdvj0sagiah01vtljd8rhro5.apps.googleusercontent.com",env.getProperty("google.consumerKey"));
        assertEquals("VURYm_x9EQfIRhU_ykse-f8o",env.getProperty("google.consumerSecret"));
        assertEquals("test",env.getProperty("google.worksheetName"));
    }

    @Test
         public void test2_getApplicationPropertyGoogleLogin() {
        assertEquals("maugin.thomas",env.getProperty("google.login"));
        assertEquals("maugin.thomas@gmail.com",env.getProperty("email.username"));
    }

    @Test
    public void test2_getApplicationPropertyEmail() {
        assertEquals("smtp.gmail.com",env.getProperty("email.smtphost"));
        assertEquals("587",env.getProperty("email.smtpport"));
        assertEquals("maugin.thomas@gmail.com",env.getProperty("email.username"));
    }
}
