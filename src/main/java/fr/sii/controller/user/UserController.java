package fr.sii.controller.user;

import fr.sii.domain.user.User;
import fr.sii.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by tmaugin on 24/04/2015.
 */
@Controller
@RequestMapping(value="/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(method= RequestMethod.GET)
    @ResponseBody
    public List<User> getUsers() {
        return userService.findAll();
    }

    @RequestMapping(method= RequestMethod.DELETE)
    @ResponseBody
    public void deleteUsers() {
        userService.deleteAll();
    }

    @RequestMapping(method=RequestMethod.POST)
    @ResponseBody public User postUser(@Valid @RequestBody User user){
        return userService.save(user);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    @ResponseBody public User putUser(@PathVariable Long id, @Valid @RequestBody User user){
        return userService.put(id, user);
    }

    @RequestMapping(value="/{id}", method= RequestMethod.GET)
    @ResponseBody
    public User getUser(@PathVariable Long id) {
        return userService.findOne(id);
    }

    @RequestMapping(value="/{id}", method= RequestMethod.DELETE)
    @ResponseBody
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }
}
