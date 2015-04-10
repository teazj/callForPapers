package fr.sii.persistance.configuration;

import fr.sii.persistance.user.User;
import fr.sii.persistance.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by tmaugin on 07/04/2015.
 */
@Service
@Profile("default")
public class DatabaseLoader {

    private final UserRepository userRepository;

    @Autowired
    public DatabaseLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @PostConstruct
    private void initDatabase() {
        User user = new User("Thomas","Maugin");
        User user2 = new User("Hoho","Maugin");
        userRepository.save(user);
        userRepository.save(user2);
    }
}