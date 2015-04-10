package fr.sii.email;

/**
 * Created by tmaugin on 02/04/2015.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class EmailingService {

    @Autowired
    private EmailingSettings emailingSettings;

    @Autowired
    private EmailingRepository emailingRepository;

    @Autowired
    public void login(){
        emailingRepository.login(emailingSettings);
    }

    public void send(EmailModel e) throws Exception {
        emailingRepository.send(e);
    }
}