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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.cfp.domain.exception.ForbiddenException;
import io.cfp.domain.exception.NotFoundException;
import io.cfp.dto.CommentUser;
import io.cfp.dto.TalkAdmin;
import io.cfp.entity.AdminUser;
import io.cfp.entity.User;
import io.cfp.service.CommentAdminService;
import io.cfp.service.TalkAdminService;
import io.cfp.service.admin.user.AdminUserService;
import io.cfp.service.email.EmailingService;
import io.cfp.service.user.UserService;

/**
 * Manages comments sent by administrators to speakers about a talk
 */
@RestController
@RequestMapping(value = "api/admin/sessions/{talkId}/contacts", produces = "application/json; charset=utf-8")
public class AdminContactController {

    @Autowired
    private AdminUserService adminUserServiceCustom;

    @Autowired
    private EmailingService emailingService;

    @Autowired
    private CommentAdminService commentService;

    @Autowired
    private TalkAdminService talkService;

    @Autowired
    private UserService userService;

    /**
     * Get all contact message for a given session
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<CommentUser> getAll(@PathVariable int talkId) {
        return commentService.findAll(talkId, false);
    }

    /**
     * Add new contact message to a session
     */
    @RequestMapping(method = RequestMethod.POST)
    public CommentUser postContact(@Valid @RequestBody CommentUser comment, @PathVariable int talkId, HttpServletRequest httpServletRequest) throws NotFoundException, IOException {

        AdminUser admin = adminUserServiceCustom.getCurrentUser();
        TalkAdmin talk = talkService.getOne(talkId);
        User user = userService.findById(talk.getUserId());

        CommentUser saved = commentService.addComment(admin, talkId, comment, false);

        // Send new message email
        if (user != null) {
            Locale userPreferredLocale = httpServletRequest.getLocale();
            emailingService.sendNewCommentToSpeaker(user, talk, userPreferredLocale);
        }

        return saved;
    }

    /**
     * Edit contact message
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public CommentUser putContact(@PathVariable int id, @Valid @RequestBody CommentUser comment) throws NotFoundException, ForbiddenException {
        comment.setId(id);
        return commentService.editComment(comment);
    }
}
