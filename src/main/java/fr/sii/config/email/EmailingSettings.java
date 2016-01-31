package fr.sii.config.email;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by tmaugin on 02/04/2015.
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Email settings
 */
@Component
public class EmailingSettings {

  public enum EmailType {

    SELECTIONNED("selectionned.html"), PENDING("pending.html"), NOT_SELECTIONNED("notSelectionned.html"), CONFIRMED("confirmed.html"), VERIFY("verify.html"), NEW_COMMENT_TO_ADMIN(
        "newMessageAdmin.html"), NEW_COMMENT_TO_SPEAKER("newMessage.html"), TEST("test.html");

    public String getTemplate() {
      return template;
    }

    private final String template;

    EmailType(final String template) {
      this.template = template;
    }
  }

  private final static Map<EmailType, String> SUBJECTS_FR = new HashMap<>();

  private final static Map<EmailType, String> SUBJECTS_EN = new HashMap<>();

  static {
    SUBJECTS_FR.put(EmailType.SELECTIONNED, "Confirmation de votre session");
    SUBJECTS_FR.put(EmailType.PENDING, "Confirmation de votre session");
    SUBJECTS_FR.put(EmailType.NOT_SELECTIONNED, "Confirmation de votre session");
    SUBJECTS_FR.put(EmailType.CONFIRMED, "Confirmation de votre session");
    SUBJECTS_FR.put(EmailType.NEW_COMMENT_TO_SPEAKER, "Nouveau commentaire sur le talk %s");
    SUBJECTS_FR.put(EmailType.NEW_COMMENT_TO_ADMIN, "Speaker %s a posté un commentaire pour le talk %s");
    SUBJECTS_FR.put(EmailType.VERIFY, "Confirmation de votre adresse e-mail");
  }

  static {
    SUBJECTS_EN.put(EmailType.SELECTIONNED, "Confirmation of your session");
    SUBJECTS_EN.put(EmailType.PENDING, "Confirmation of your session");
    SUBJECTS_EN.put(EmailType.NOT_SELECTIONNED, "Confirmation of your session");
    SUBJECTS_EN.put(EmailType.CONFIRMED, "Confirmation of your session");
    SUBJECTS_EN.put(EmailType.NEW_COMMENT_TO_SPEAKER, "New comment about talk %s");
    SUBJECTS_EN.put(EmailType.NEW_COMMENT_TO_ADMIN, "Speaker %s posted a new comment on talk %s");
    SUBJECTS_EN.put(EmailType.VERIFY, "Confirm your email address");
  }

  @Value("${cfp.email.emailsender}")
  private String emailSender;

  @Value("${cfp.email.send}")
  private boolean send;

  public String getSubject(final EmailType emailType, final Locale locale, final String... strings) {
    final Map<EmailType, String> subjects = selectDictionary(locale);
    return String.format(subjects.get(emailType), (Object[]) strings);
  }

  public String getTemplatePath(final EmailType emailType, final Locale locale) {
    return "mails/" + locale.getLanguage() + "/" + emailType.getTemplate();
  }

  public String getEmailSender() {
    return emailSender;
  }

  public boolean isSend() {
    return send;
  }

  private Map<EmailType, String> selectDictionary(final Locale locale) {
    Map<EmailType, String> subjects;

    switch (locale.getLanguage()) {
    case "fr":
      subjects = EmailingSettings.SUBJECTS_FR;
      break;
    case "en":
      subjects = EmailingSettings.SUBJECTS_EN;
      break;
    default:
      subjects = EmailingSettings.SUBJECTS_FR;
      break;
    }
    return subjects;
  }
}
