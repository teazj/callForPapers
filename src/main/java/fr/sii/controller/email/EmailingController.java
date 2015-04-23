package fr.sii.controller.email;

import fr.sii.domain.email.Email;
import fr.sii.service.email.EmailingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * Created by tmaugin on 09/04/2015.
 */

@Controller
@RequestMapping(produces = "application/json; charset=utf-8")
public class EmailingController {

    @Autowired
    private EmailingService emailingService;

    @RequestMapping(value="/email", method= RequestMethod.POST)
    @ResponseBody
    public String postEmailing(@Valid @RequestBody Email email) throws Exception {
        emailingService.send(email);
        return "ok";
    }
}
