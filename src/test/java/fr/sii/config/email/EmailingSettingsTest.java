package fr.sii.config.email;

import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.springframework.util.Assert;

import fr.sii.config.email.EmailingSettings.EmailType;

@RunWith(BlockJUnit4ClassRunner.class)
public class EmailingSettingsTest {
  private final static String TALK = "My talk";

  private final static String SPEAKER = "Speaker";

  EmailingSettings emailingSettings = new EmailingSettings();

  @Test
  public void getNewMessageSubject() {
    // Given
    final EmailType emailType = EmailType.NEW_MESSAGE;

    // When
    final String subject = emailingSettings.getSubject(emailType, Locale.FRENCH, TALK, SPEAKER);

    // Then
    Assert.isTrue(subject.contains(TALK));
    Assert.isTrue(subject.contains(SPEAKER));
  }

  @Test
  public void getNewMessageAdminSubject() {
    // Given
    final EmailType emailType = EmailType.NEW_MESSAGE_ADMIN;

    // When
    final String subject = emailingSettings.getSubject(emailType, Locale.FRENCH, TALK);

    // Then
    Assert.isTrue(subject.contains(TALK));
  }
}
