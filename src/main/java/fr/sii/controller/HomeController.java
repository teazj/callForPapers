package fr.sii.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

import fr.sii.dto.Speaker;
import fr.sii.dto.TalkAdmin;
import fr.sii.entity.Talk;
import fr.sii.service.TalkAdminService;
import fr.sii.service.TalkUserService;

/**
 * Created by tmaugin on 15/04/2015.
 */

@Controller
public class HomeController {

    @Autowired
    TalkUserService talkService;

    /**
     * Redirect to index.html
     * @return
     */
    @RequestMapping("/")
    public String index() {
        return "forward:/index.html";
    }

    /**
     * Get all sessions
     */
    @RequestMapping(value="/speakers", method= RequestMethod.GET)
    @ResponseBody
    public List<Speaker> getAllSpeakers() {
        return talkService.findAllSpeaker(Talk.State.CONFIRMED, Talk.State.ACCEPTED);
    }
}
