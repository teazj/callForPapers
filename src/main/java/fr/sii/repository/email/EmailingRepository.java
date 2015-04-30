package fr.sii.repository.email;

import fr.sii.config.email.EmailingSettings;
import fr.sii.domain.email.Email;

/**
 * Created by tmaugin on 02/04/2015.
 */
public interface EmailingRepository {
    void send(Email e) throws Exception;
    void login(EmailingSettings e);
}