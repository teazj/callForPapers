package fr.sii.config.email;

import fr.sii.config.email.EmailingSettings.EmailType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.Locale;

@RunWith(BlockJUnit4ClassRunner.class)
public class EmailingSettingsTest {
    private final static String TALK = "Talk12345";

    private final static String SPEAKER = "Speaker12345";

    EmailingSettings emailingSettings = new EmailingSettings();

    @Test
    public void whenPostingNewCommentToSpeakerSubjectIsValid() {
        // Given
        final EmailType emailType = EmailType.NEW_COMMENT_TO_SPEAKER;

        // When
        final String subject = emailingSettings.getSubject(emailType, Locale.FRENCH, TALK);

        // Then
        Assert.isTrue(subject.contains(TALK));
    }

    @Test
    public void whenPostingNewCommentToAdminsSubjectIsValid() {
        // Given
        final EmailType emailType = EmailType.NEW_COMMENT_TO_ADMIN;

        // When
        final String subject = emailingSettings.getSubject(emailType, Locale.FRENCH, SPEAKER, TALK);

        // Then
        Assert.isTrue(subject.contains(TALK));
        Assert.isTrue(subject.contains(SPEAKER));
    }
}
