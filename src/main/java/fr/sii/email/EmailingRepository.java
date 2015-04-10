package fr.sii.email;

import javax.mail.MessagingException;

/**
 * Created by tmaugin on 02/04/2015.
 */
public interface EmailingRepository {
    void send(EmailModel e) throws Exception;
    void login(EmailingSettings e);
}