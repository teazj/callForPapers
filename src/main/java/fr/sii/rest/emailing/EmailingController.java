package fr.sii.rest.emailing;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.sii.email.EmailModel;
import fr.sii.email.EmailingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
    public String postEmailing(@Valid @RequestBody EmailModel email) throws Exception {
        emailingService.send(email);
        return "ok";
    }
}
