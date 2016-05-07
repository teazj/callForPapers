/*
 * The MIT License
 *
 *  Copyright (c) 2016, CloudBees, Inc.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 *
 */

package io.cfp.controller;

import io.cfp.dto.TalkAdmin;
import io.cfp.entity.Talk;
import io.cfp.service.TalkAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import io.cfp.domain.exception.CospeakerNotFoundException;


import java.util.List;

@Controller
@RequestMapping(value="api/admin", produces = "application/json; charset=utf-8")
public class AdminSessionController {

    @Autowired
    private TalkAdminService talkService;

    /**
     * Get all sessions
     */
    @RequestMapping(value="/sessions", method= RequestMethod.GET)
    @ResponseBody
    public List<TalkAdmin> getAllSessions() {
        return talkService.findAll(Talk.State.CONFIRMED, Talk.State.ACCEPTED, Talk.State.REFUSED);
    }

    /**
     * Get all drafts
     */
    @RequestMapping(value="/drafts", method= RequestMethod.GET)
    @ResponseBody
    public List<TalkAdmin> getAllDrafts() {
        return talkService.findAll(Talk.State.DRAFT);
    }

    /**
     * Get a specific session
     */
    @RequestMapping(value= "/sessions/{talkId}", method= RequestMethod.GET)
    @ResponseBody
    public TalkAdmin getTalk(@PathVariable int talkId) {
        return talkService.getOne(talkId);
    }

    /**
     * Edit a specific session
     */
    @RequestMapping(value= "/sessions/{talkId}", method= RequestMethod.PUT)
    @ResponseBody
    public TalkAdmin editTalk(@PathVariable int talkId, @RequestBody TalkAdmin talkAdmin) throws CospeakerNotFoundException{
        talkAdmin.setId(talkId);
        return talkService.edit(talkAdmin);
    }

    /**
     * Delete a session
     */
    @RequestMapping(value="/sessions/{talkId}", method= RequestMethod.DELETE)
    @ResponseBody
    public TalkAdmin deleteGoogleSpreadsheet(@PathVariable int talkId) {
        return talkService.delete(talkId);
    }
}
