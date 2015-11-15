package fr.sii.controller.admin.comment;

import fr.sii.domain.exception.ForbiddenException;
import fr.sii.domain.exception.NotFoundException;
import fr.sii.dto.CommentUser;
import fr.sii.entity.AdminUser;
import fr.sii.service.CommentAdminService;
import fr.sii.service.admin.user.AdminUserService;
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
        return commentService.addComment(admin, talkId, comment, false);
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
    public CommentUser deleteComment(@PathVariable int id, @Valid @RequestBody CommentUser comment) throws NotFoundException, ForbiddenException {
        comment.setId(id);
        return commentService.delete(comment);
    }

}