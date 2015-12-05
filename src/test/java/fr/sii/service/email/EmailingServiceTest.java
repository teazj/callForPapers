package fr.sii.service.email;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;

import fr.sii.Application;
import fr.sii.dto.CommentUser;
import fr.sii.dto.TalkAdmin;
import fr.sii.dto.TalkUser;
import fr.sii.entity.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class EmailingServiceTest {

    @Autowired
    private EmailingService emailingService;

    private GreenMail testSmtp;

    @Autowired
    private JavaMailSenderImpl mailSender;

    private User user;

    private TalkAdmin talkAdmin;

    private TalkUser talkUser;

    private CommentUser comment;

    @Before
    public void setup() {
        user = new User();
        user.setId(1);
        user.setEmail("john.doe@gmail.com");
        user.setFirstname("john");
        user.setVerifyToken("verifyToken");

        talkUser = new TalkUser();
        talkUser.setId(1);
        talkUser.setName("My amazing user talk 1");

        talkAdmin = new TalkAdmin();
        talkAdmin.setId(2);
        talkAdmin.setName("My amazing user talk 2");

        comment = new CommentUser();
        comment.setId(1);
        comment.setComment("Bla blabla");

        MockitoAnnotations.initMocks(this);

        testSmtp = new GreenMail(ServerSetupTest.SMTP);
        testSmtp.start();

        // Don't forget to set the test port!
        mailSender.setPort(3025);
        mailSender.setHost("localhost");
    }

    @Test
    public void sendEmailValidation() {
        emailingService.sendEmailValidation(user);
    }

    @Test
    public void sendNewMessage() {
        emailingService.sendNewMessage(user, talkAdmin, comment);
    }

    @Test
    public void sendNewMessageAdmin() {
        emailingService.sendNewMessageAdmin(user, talkUser);

    }

    @Test
    public void sendSessionConfirmation() {
        emailingService.sendSessionConfirmation(user, talkUser);

    }

    @After
    public void cleanup() {
        testSmtp.stop();
    }
}