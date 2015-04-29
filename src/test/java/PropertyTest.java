import fr.sii.config.email.EmailingSettings;
import fr.sii.config.spreadsheet.SpreadsheetSettings;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;

/**
 * Created by tmaugin on 08/04/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:META-INF/spring/applicationContext.xml","classpath:META-INF/spring/dispatcherServletTest.xml"})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PropertyTest {

    @Autowired
    private SpreadsheetSettings spreadsheetSettings;

    @Autowired
    private EmailingSettings emailingSettings;

    @Test
    public void test1_getApplicationPropertyGoogleApp() {
        assertEquals("CallForPaper", spreadsheetSettings.getSpreadsheetName());
        assertEquals("test",spreadsheetSettings.getWorksheetName());
    }

    @Test
         public void test2_getApplicationPropertyGoogleLogin() {
        assertEquals("maugin.thomas",spreadsheetSettings.getLogin());
    }

    @Test
    public void test3_getApplicationPropertyEmail() {
        assertEquals("smtp.gmail.com",emailingSettings.getSmtphost());
        assertEquals("587",emailingSettings.getSmtpport());
        assertEquals("maugin.thomas@gmail.com",emailingSettings.getUsername());
    }
}
