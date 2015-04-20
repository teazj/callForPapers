package fr.sii.persistance.configuration;

import fr.sii.persistance.user.User;
import fr.sii.persistance.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Profile("default")
public class DatabaseLoader {

    @Autowired
    Environment env;

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    private void initDatabase() {
        if(Boolean.valueOf(env.getProperty("database.load")))
        {
            User user = new User("Thomas","Maugin");
            User user2 = new User("Hoho","Maugin");
            userRepository.save(user);
            userRepository.save(user2);
        }
    }
}