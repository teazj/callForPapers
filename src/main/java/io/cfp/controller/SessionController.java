/*
 * Copyright (c) 2016 BreizhCamp
 * [http://breizhcamp.org]
 *
 * This file is part of CFP.io.
 *
 * CFP.io is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package io.cfp.controller;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.cfp.domain.exception.CospeakerNotFoundException;
import io.cfp.domain.exception.NotVerifiedException;
import io.cfp.dto.TalkUser;
import io.cfp.entity.Role;
import io.cfp.entity.Talk;
import io.cfp.entity.User;
import io.cfp.service.TalkUserService;
import io.cfp.service.email.EmailingService;

@RestController
@RequestMapping(value = { "/v0", "/api" }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SessionController  {

    @Autowired
    private EmailingService emailingService;

    @Autowired
    private TalkUserService talkService;



    /**
     * Add a session
     */
    @RequestMapping(value="/proposals", method=RequestMethod.POST)
    @Secured(Role.AUTHENTICATED)
    public TalkUser submitTalk(@AuthenticationPrincipal User user, @Valid @RequestBody TalkUser talkUser) throws Exception, CospeakerNotFoundException  {
        TalkUser savedTalk = talkService.submitTalk(user.getId(), talkUser);

        if (user != null && savedTalk != null) {
            emailingService.sendConfirmed(user, savedTalk, user.getLocale());
        }

        return savedTalk;
    }

    /**
     * Get all session for the current user
     */
    @RequestMapping(value = "/proposals", method = RequestMethod.GET)
    @Secured(Role.AUTHENTICATED)
    public List<TalkUser> listTalks(@AuthenticationPrincipal User user) throws NotVerifiedException {
        return talkService.findAll(user.getId(), Talk.State.CONFIRMED, Talk.State.ACCEPTED, Talk.State.REFUSED);
    }

    /**
     * Get a session
     */
    @RequestMapping(value = "/proposals/{talkId}", method = RequestMethod.GET)
    @Secured(Role.AUTHENTICATED)
    public TalkUser getGoogleSpreadsheet(@AuthenticationPrincipal User user, @PathVariable Integer talkId) throws NotVerifiedException {
        return talkService.getOne(user.getId(), talkId);
    }

    /**
     * Change a draft to a session
     */
    @RequestMapping(value= "/proposals/{talkId}", method=RequestMethod.PUT)
    @Secured(Role.AUTHENTICATED)
    public TalkUser submitDraftToTalk(@AuthenticationPrincipal User user, @Valid @RequestBody TalkUser talkUser, @PathVariable Integer talkId) throws Exception, CospeakerNotFoundException {
        talkUser.setId(talkId);
        TalkUser savedTalk = talkService.submitDraftToTalk(user.getId(), talkUser);

        if (user != null && savedTalk != null) {
            emailingService.sendConfirmed(user, savedTalk, user.getLocale());
        }

        return savedTalk;
    }

    /**
     * Add a new draft
     */
    @RequestMapping(value="/drafts", method=RequestMethod.POST)
    @Secured(Role.AUTHENTICATED)
    public TalkUser addDraft(@AuthenticationPrincipal User user, @Valid @RequestBody TalkUser talkUser) throws NotVerifiedException, CospeakerNotFoundException {
        return talkService.addDraft(user.getId(), talkUser);
    }

    /**
     * Get all drafts for current user
     */
    @RequestMapping(value = "/drafts", method = RequestMethod.GET)
    @Secured(Role.AUTHENTICATED)
    public List<TalkUser> listDrafts(@AuthenticationPrincipal User user) throws NotVerifiedException {
        return talkService.findAll(user.getId(), Talk.State.DRAFT);
    }

    /**
     * Get a draft
     */
    @RequestMapping(value = "/drafts/{talkId}", method = RequestMethod.GET)
    @Secured(Role.AUTHENTICATED)
    public TalkUser getDraft(@AuthenticationPrincipal User user, @PathVariable Integer talkId) throws NotVerifiedException {
        return talkService.getOne(user.getId(), talkId);
    }

    /**
     * Delete a draft
     */
    @RequestMapping(value = "/drafts/{talkId}", method = RequestMethod.DELETE)
    @Secured(Role.AUTHENTICATED)
    public TalkUser deleteDraft(@AuthenticationPrincipal User user, @PathVariable Integer talkId) throws NotVerifiedException {
        return talkService.deleteDraft(user.getId(), talkId);
    }

    /**
     * Edit a draft
     */
    @RequestMapping(value= "/drafts/{talkId}", method=RequestMethod.PUT)
    @Secured(Role.AUTHENTICATED)
    public TalkUser editDraft(@AuthenticationPrincipal User user, @Valid @RequestBody TalkUser talkUser, @PathVariable Integer talkId) throws NotVerifiedException, CospeakerNotFoundException {
        talkUser.setId(talkId);
        return talkService.editDraft(user.getId(), talkUser);
    }

    /**
     * Get all co-draft for the current user
     */
    @RequestMapping(value="/codrafts", method= RequestMethod.GET)
    @Secured(Role.AUTHENTICATED)
    public List<TalkUser> getCoDrafts(@AuthenticationPrincipal User user) throws NotVerifiedException {
        return talkService.findAllCospeakerTalks(user.getId(), Talk.State.DRAFT);
    }

    /**
     * Get a co-draft for the current user
     */
    @RequestMapping(value="/codrafts/{talkId}", method= RequestMethod.GET)
    @Secured(Role.AUTHENTICATED)
    public TalkUser getCoDraft(@AuthenticationPrincipal User user, @PathVariable Integer talkId) throws NotVerifiedException {
        TalkUser talk = talkService.getOneCospeakerTalk(user.getId(), talkId);
        return talk;
    }

    /**
     * Get all co-session for the current user
     */
    @RequestMapping(value="/cosessions", method= RequestMethod.GET)
    @Secured(Role.AUTHENTICATED)
    public List<TalkUser> getCoSessions(@AuthenticationPrincipal User user) throws NotVerifiedException {
        return talkService.findAllCospeakerTalks(user.getId(), Talk.State.CONFIRMED, Talk.State.ACCEPTED, Talk.State.REFUSED);
    }

    /**
     * Get a co-session for the current user
     */
    @RequestMapping(value = "/cosessions/{talkId}", method = RequestMethod.GET)
    @Secured(Role.AUTHENTICATED)
    public TalkUser getCoSession(@AuthenticationPrincipal User user, @PathVariable Integer talkId) throws NotVerifiedException {
        TalkUser talk = talkService.getOneCospeakerTalk(user.getId(), talkId);

        return talk;
    }
}
