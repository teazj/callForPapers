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

import io.cfp.domain.exception.ForbiddenException;
import io.cfp.domain.exception.NotFoundException;
import io.cfp.dto.CommentUser;
import io.cfp.entity.AdminUser;
import io.cfp.service.CommentAdminService;
import io.cfp.service.admin.user.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value="api/admin/sessions/{talkId}/comments", produces = "application/json; charset=utf-8")
public class AdminCommentController {

    @Autowired
    private AdminUserService adminUserServiceCustom;

    @Autowired
    private CommentAdminService commentService;

    /**
     * Get all comment message for a given session
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<CommentUser> getAll(@PathVariable int talkId) {
        return commentService.findAll(talkId, true);
    }

    /**
     * Add new comment message to a session
     */
    @RequestMapping(method=RequestMethod.POST)
    public CommentUser postComment(@Valid @RequestBody CommentUser comment, @PathVariable int talkId) throws NotFoundException, IOException {
        AdminUser admin = adminUserServiceCustom.getCurrentUser();
        return commentService.addComment(admin, talkId, comment, true);
    }

    /**
     * Edit comment message
     */
    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    public CommentUser putComment(@PathVariable int id, @Valid @RequestBody CommentUser comment) throws NotFoundException, ForbiddenException {
        comment.setId(id);
        return commentService.editComment(comment);
    }

    /**
     * Delete comment message
     */
    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public void deleteComment(@PathVariable int id) throws NotFoundException, ForbiddenException {
        commentService.delete(id);
    }

}
