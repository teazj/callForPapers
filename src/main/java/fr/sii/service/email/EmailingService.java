package fr.sii.service.email;

/**
 * Created by tmaugin on 02/04/2015.
 */

import fr.sii.config.email.EmailingSettings;
import fr.sii.domain.email.Email;
import fr.sii.repository.email.EmailingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class EmailingService {

    @Autowired
    private EmailingSettings emailingSettings;

    @Autowired
    private EmailingRepository emailingRepository;

    @PostConstruct
    public void login(){
        emailingRepository.login(emailingSettings);
    }

    public void send(Email e) throws Exception {
        emailingRepository.send(e);
    }
}