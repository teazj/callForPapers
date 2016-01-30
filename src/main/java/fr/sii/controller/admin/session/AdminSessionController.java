package fr.sii.controller.admin.session;

import fr.sii.domain.exception.CospeakerNotFoundException;
import fr.sii.dto.TalkAdmin;
import fr.sii.entity.Talk;
import fr.sii.service.TalkAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "api/admin", produces = "application/json; charset=utf-8")
public class AdminSessionController {

    @Autowired
    private TalkAdminService talkService;

    /**
     * Get all sessions
     */
    @RequestMapping(value = "/sessions", method = RequestMethod.GET)
    @ResponseBody
    public List<TalkAdmin> getAllSessions() {
        return talkService.findAll(Talk.State.CONFIRMED, Talk.State.ACCEPTED, Talk.State.REFUSED);
    }

    /**
     * Get all drafts
     */
    @RequestMapping(value = "/drafts", method = RequestMethod.GET)
    @ResponseBody
    public List<TalkAdmin> getAllDrafts() {
        return talkService.findAll(Talk.State.DRAFT);
    }

    /**
     * Get a specific session
     */
    @RequestMapping(value = "/sessions/{talkId}", method = RequestMethod.GET)
    @ResponseBody
    public TalkAdmin getTalk(@PathVariable int talkId) {
        return talkService.getOne(talkId);
    }

    /**
     * Edit a specific session
     */
    @RequestMapping(value = "/sessions/{talkId}", method = RequestMethod.PUT)
    @ResponseBody
    public TalkAdmin editTalk(@PathVariable int talkId, @RequestBody TalkAdmin talkAdmin) throws CospeakerNotFoundException {
        talkAdmin.setId(talkId);
        return talkService.edit(talkAdmin);
    }

    /**
     * Delete a session
     */
    @RequestMapping(value = "/sessions/{talkId}", method = RequestMethod.DELETE)
    @ResponseBody
    public TalkAdmin deleteTalk(@PathVariable int talkId) {
        return talkService.delete(talkId);
    }
}
