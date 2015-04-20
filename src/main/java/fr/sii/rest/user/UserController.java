package fr.sii.rest.user;

import fr.sii.persistance.user.User;
import fr.sii.persistance.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;


/**
 * Created by tmaugin on 20/04/2015.
 */
@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value="/user", method= RequestMethod.GET)
    @ResponseBody
    public List<User> getUser() {
        return userRepository.list();
    }

    @RequestMapping(value="/user", method= RequestMethod.POST)
    @ResponseBody
    public User getUser(@RequestBody @Valid User u) {
        return userRepository.save(u);
    }
}