package fr.sii.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by tmaugin on 15/04/2015.
 */

@Controller
public class HomeController {
    @RequestMapping("/")
    public String index() {
        return "forward:/index.html";
    }
}
