package fr.sii.service.email;

/**
 * Created by tmaugin on 02/04/2015.
 */

import fr.sii.config.email.EmailingSettings;
import fr.sii.domain.email.Email;
import fr.sii.repository.email.EmailingRepository;

public class EmailingService {

    private EmailingSettings emailingSettings;

    private EmailingRepository emailingRepository;

    public void setEmailingSettings(EmailingSettings emailingSettings) {
        this.emailingSettings = emailingSettings;
    }

    public void setEmailingRepository(EmailingRepository emailingRepository) {
        this.emailingRepository = emailingRepository;
    }

    public void login(){
        emailingRepository.login(emailingSettings);
    }

    public void send(Email e) throws Exception {
        emailingRepository.send(e);
    }
}