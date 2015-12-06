package fr.sii.service.email;

import fr.sii.config.email.EmailingSettings;
import fr.sii.config.global.GlobalSettings;
import fr.sii.service.admin.user.AdminUserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;

import fr.sii.Application;
import fr.sii.dto.TalkAdmin;
import fr.sii.dto.TalkUser;
import fr.sii.entity.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@Ignore
public class EmailingServiceTest {

    public static final String JOHN_DOE_EMAIL = "john.doe@gmail.com";

    @Spy
    private EmailingService emailingService;

    private GreenMail testSmtp;

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Autowired
    private GlobalSettings globalSettings;

    @Autowired
    private EmailingSettings emailingSettings;

    @Autowired
    private AdminUserService adminUserServiceCustom;

    private User user;

    private TalkAdmin talkAdmin;

    private TalkUser talkUser;

    @Before
    public void setup() {
        user = new User();
        user.setId(1);
        user.setEmail(JOHN_DOE_EMAIL);
        user.setFirstname("john");
        user.setVerifyToken("verifyToken");

        talkUser = new TalkUser();
        talkUser.setId(1);
        talkUser.setName("My amazing user talk 1");

        talkAdmin = new TalkAdmin();
        talkAdmin.setId(2);
        talkAdmin.setName("My amazing user talk 2");

        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setField(emailingService, "globalSettings", globalSettings);
        ReflectionTestUtils.setField(emailingService, "emailingSettings", emailingSettings);
        ReflectionTestUtils.setField(emailingService, "adminUserServiceCustom", adminUserServiceCustom);
        ReflectionTestUtils.setField(emailingService, "mailSender", mailSender);

        testSmtp = new GreenMail(ServerSetupTest.SMTP);
        testSmtp.start();

        // Don't forget to set the test port!
        mailSender.setPort(3025);
        mailSender.setHost("localhost");
    }


    @Test
    public void sendSessionConfirmation() {
        // Given
        String templatePath = emailingSettings.getTemplatePath(EmailingSettings.EmailType.CONFIRMED, Locale.FRENCH);
        String subject = emailingSettings.getSubject(EmailingSettings.EmailType.CONFIRMED, Locale.FRENCH);

        // When
        emailingService.sendConfirmed(user, talkUser, Locale.FRENCH);

        // Then
        verify(emailingService).processContent(eq(templatePath), anyMap());
        verify(emailingService).sendEmail(eq(user.getEmail()), eq(subject), anyString(), isNull(List.class));
    }

    @Test
    public void sendNewMessage() {
        // Given
        String templatePath = emailingSettings.getTemplatePath(EmailingSettings.EmailType.NEW_MESSAGE, Locale.FRENCH);
        String subject = emailingSettings.getSubject(EmailingSettings.EmailType.NEW_MESSAGE, Locale.FRENCH, talkUser.getName(), talkUser.getName());

        // When
        verify(emailingService).processContent(eq(templatePath), anyMap());
        emailingService.sendNewMessage(user, talkAdmin, Locale.FRENCH);

        // Then
        verify(emailingService).sendEmail(eq(JOHN_DOE_EMAIL), eq(subject), anyString(), notNull(List.class));
    }

    @Test
    public void sendNewMessageAdmin() {
        // Given
        String templatePath = emailingSettings.getTemplatePath(EmailingSettings.EmailType.NEW_MESSAGE_ADMIN, Locale.FRENCH);
        String subject = emailingSettings.getSubject(EmailingSettings.EmailType.NEW_MESSAGE_ADMIN, Locale.FRENCH, talkUser.getName());

        // When
        emailingService.sendNewMessageAdmin(user, talkUser, Locale.FRENCH);

        // Then
        verify(emailingService).processContent(eq(templatePath), anyMap());
        verify(emailingService).sendEmail(eq(emailingSettings.getEmailSender()), eq(subject), anyString(), notNull(List.class));
    }

    @Test
    public void sendNotSelectionned() {
        // Given
        String templatePath = emailingSettings.getTemplatePath(EmailingSettings.EmailType.NOT_SELECTIONNED, Locale.FRENCH);
        String subject = emailingSettings.getSubject(EmailingSettings.EmailType.NOT_SELECTIONNED, Locale.FRENCH);

        // When
        emailingService.sendNotSelectionned(user, talkUser, Locale.FRENCH);

        // Then
        verify(emailingService).processContent(eq(templatePath), anyMap());
        verify(emailingService).sendEmail(eq(emailingSettings.getEmailSender()), eq(subject), anyString(), notNull(List.class));
    }

    @Test
    public void sendPending() {
        // Given
        String templatePath = emailingSettings.getTemplatePath(EmailingSettings.EmailType.PENDING, Locale.FRENCH);
        String subject = emailingSettings.getSubject(EmailingSettings.EmailType.PENDING, Locale.FRENCH);

        // When
        emailingService.sendPending(user, talkUser, Locale.FRENCH);

        // Then
        verify(emailingService).processContent(eq(templatePath), anyMap());
        verify(emailingService).sendEmail(eq(emailingSettings.getEmailSender()), eq(subject), anyString(), notNull(List.class));
    }

    @Test
    public void sendSelectionned() {
        // Given
        String templatePath = emailingSettings.getTemplatePath(EmailingSettings.EmailType.SELECTIONNED, Locale.FRENCH);
        String subject = emailingSettings.getSubject(EmailingSettings.EmailType.SELECTIONNED, Locale.FRENCH);

        // When
        emailingService.sendSelectionned(user, talkUser, Locale.FRENCH);

        // Then
        verify(emailingService).processContent(eq(templatePath), anyMap());
        verify(emailingService).sendEmail(eq(emailingSettings.getEmailSender()), eq(subject), anyString(), notNull(List.class));
    }

    @Test
    public void processContentTest() {
        // Given
        String templatePath = emailingSettings.getTemplatePath(EmailingSettings.EmailType.TEST, Locale.FRENCH);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("var1", "test1");
        map.put("var2", "test2");

        // When
        String content = emailingService.processContent(templatePath, map);

        // Then
        assertEquals(false, content.contains("$"));
    }

    @Test
    public void sendEmailValidation() {
        // Given
        String templatePath = emailingSettings.getTemplatePath(EmailingSettings.EmailType.VERIFY, Locale.FRENCH);
        String subject = emailingSettings.getSubject(EmailingSettings.EmailType.VERIFY, Locale.FRENCH);

        // When
        verify(emailingService).processContent(eq(templatePath), anyMap());
        emailingService.sendEmailValidation(user, Locale.FRENCH);

        // Then
        verify(emailingService).sendEmail(eq(JOHN_DOE_EMAIL), eq(subject), anyString(), isNull(List.class));
    }

    @Test
    public void processContentConfirmed() {
        // Given
        String templatePath = emailingSettings.getTemplatePath(EmailingSettings.EmailType.CONFIRMED, Locale.FRENCH);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", "Thomas");
        map.put("talk", "Google App Engine pour les nuls");
        map.put("hostname", "http://yourappid.appspot.com/");
        map.put("id", "123");

        // When
        String content = emailingService.processContent(templatePath, map);

        // Then
        assertEquals(false, content.contains("$"));
    }

    @Test
    public void processContentNotSelectionned() {
        // Given
        String templatePath = emailingSettings.getTemplatePath(EmailingSettings.EmailType.NOT_SELECTIONNED, Locale.FRENCH);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", "Thomas");
        map.put("talk", "Google App Engine pour les nuls");
        map.put("community", "GDG Nantes");
        map.put("event", "DevFest 2015");
        map.put("hostname", "http://yourappid.appspot.com/");

        // When
        String content = emailingService.processContent(templatePath, map);

        // Then
        assertEquals(false, content.contains("$"));
    }

    @Test
    public void processContentPending() {
        // Given
        String templatePath = emailingSettings.getTemplatePath(EmailingSettings.EmailType.PENDING, Locale.FRENCH);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", "Thomas");
        map.put("talk", "Google App Engine pour les nuls");
        map.put("event", "DevFest 2015");
        map.put("date", "13/11/1992");
        map.put("hostname", "http://yourappid.appspot.com/");

        // When
        String content = emailingService.processContent(templatePath, map);

        // Then
        assertEquals(false, content.contains("$"));
    }

    @Test
    public void processContentSelectionned() {
        // Given
        String templatePath = emailingSettings.getTemplatePath(EmailingSettings.EmailType.SELECTIONNED, Locale.FRENCH);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", "Thomas");
        map.put("talk", "Google App Engine pour les nuls");
        map.put("event", "DevFest 2015");
        map.put("date", "13/11/1992");
        map.put("releaseDate", "12/11/1992");
        map.put("hostname", "http://yourappid.appspot.com/");

        // When
        String content = emailingService.processContent(templatePath, map);

        // Then
        assertEquals(false, content.contains("$"));
    }

    @Test
    public void processContentVerify() {
        // Given
        String templatePath = emailingSettings.getTemplatePath(EmailingSettings.EmailType.VERIFY, Locale.FRENCH);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("link", "http://google.fr");
        map.put("hostname", "http://yourappid.appspot.com/");

        // When
        String content = emailingService.processContent(templatePath, map);

        // Then
        assertEquals(false, content.contains("$"));
    }

    @Test
    public void processContentNewMessage() {
        // Given
        String templatePath = emailingSettings.getTemplatePath(EmailingSettings.EmailType.NEW_MESSAGE, Locale.FRENCH);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", "Thomas");
        map.put("talk", "Google App Engine pour les nuls");
        map.put("hostname", "http://yourappid.appspot.com/");
        map.put("id", "123");

        // When
        String content = emailingService.processContent(templatePath, map);

        // Then
        assertEquals(false, content.contains("$"));
    }

    @Test
    public void processContentNewMessageAdmin() {
        // Given
        String templatePath = emailingSettings.getTemplatePath(EmailingSettings.EmailType.NEW_MESSAGE_ADMIN, Locale.FRENCH);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", "Thomas");
        map.put("talk", "Google App Engine pour les nuls");
        map.put("hostname", "http://yourappid.appspot.com/");
        map.put("id", "123");

        // When
        String content = emailingService.processContent(templatePath, map);

        // Then
        assertEquals(false, content.contains("$"));
    }

    @Test
    public void processContentSessionConfirmation() {
        // Given
        String templatePath = emailingSettings.getTemplatePath(EmailingSettings.EmailType.CONFIRMED, Locale.FRENCH);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", "Thomas");
        map.put("talk", "Google App Engine pour les nuls");
        map.put("hostname", "http://yourappid.appspot.com/");
        map.put("id", "123");

        // When
        String content = emailingService.processContent(templatePath, map);

        // Then
        assertEquals(false, content.contains("$"));
    }

    @After
    public void cleanup() {
        testSmtp.stop();
    }
}
