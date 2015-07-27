package fr.sii.controller.admin.comment;

import fr.sii.domain.admin.comment.AdminComment;
import fr.sii.domain.exception.ForbiddenException;
import fr.sii.domain.exception.NotFoundException;
import fr.sii.service.admin.comment.AdminCommentService;
import fr.sii.service.admin.user.AdminUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * Created by tmaugin on 29/04/2015.
 */
@Controller
@RequestMapping(value="api/admin/comments", produces = "application/json; charset=utf-8")
public class AdminCommentController {

    private AdminCommentService adminCommentService;

    private AdminUserService adminUserServiceCustom;

    public void setAdminCommentService(AdminCommentService adminCommentService) {
        this.adminCommentService = adminCommentService;
    }

    public void setAdminUserServiceCustom(AdminUserService adminUserServiceCustom) {
        this.adminUserServiceCustom = adminUserServiceCustom;
    }

    /**
     * Get all comments
     * @return
     */
    @RequestMapping(method= RequestMethod.GET)
    @ResponseBody
    public List<AdminComment> getComments() {
        return adminCommentService.findAll();
    }

    /**
     * Delete a comment
     * @param id
     * @return
     * @throws NotFoundException
     * @throws ForbiddenException
     */
    @RequestMapping(value="/{id}", method= RequestMethod.DELETE)
    @ResponseBody
    public AdminComment deleteComment(@PathVariable Long id) throws NotFoundException, ForbiddenException {
        AdminComment currentComment = adminCommentService.findOne(id);
        if(!currentComment.isDeleted() && currentComment.getUserId().toString().equals(adminUserServiceCustom.getCurrentUser().getEntityId().toString())) {
            currentComment.setComment("");
            currentComment.setDeleted(true);
            return adminCommentService.put(id, (AdminComment) currentComment.clone());
        } else {
            throw new ForbiddenException("This is not your comment, you can't delete it");
        }
    }

    /**
     * Add a comment
     * @param adminComment
     * @return
     */
    @RequestMapping(method=RequestMethod.POST)
    @ResponseBody public AdminComment postComment(@Valid @RequestBody AdminComment adminComment) {
        adminComment.setUserId(adminUserServiceCustom.getCurrentUser().getEntityId());
        return adminCommentService.save(adminComment);
    }

    /**
     * Edit a comment
     * @param id
     * @param adminComment
     * @return
     * @throws NotFoundException
     * @throws ForbiddenException
     */
    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    @ResponseBody public AdminComment putComment(@PathVariable Long id, @Valid @RequestBody AdminComment adminComment) throws NotFoundException, ForbiddenException {
        AdminComment currentComment = adminCommentService.findOne(id);
        if(!currentComment.isDeleted() && currentComment.getUserId().toString().equals(adminUserServiceCustom.getCurrentUser().getEntityId().toString())) {
            adminComment.setUserId(adminUserServiceCustom.getCurrentUser().getEntityId());
            adminComment.setAdded(new Date(currentComment.getAdded()));
            return adminCommentService.put(id, adminComment);
        } else {
            throw new ForbiddenException("This is not your comment, you can't edit it");
        }
    }

    /**
     * Get a specific comment
     * @param id
     * @return
     * @throws NotFoundException
     */
    @RequestMapping(value="/{id}", method= RequestMethod.GET)
    @ResponseBody
    public AdminComment getComment(@PathVariable Long id) throws NotFoundException {
        return adminCommentService.findOne(id);
    }

    /**
     * Get all comments for a specific user
     * @param id
     * @return
     */
    @RequestMapping(value="/user/{id}", method= RequestMethod.GET)
    @ResponseBody
    public List<AdminComment> getCommentsByUserId(@PathVariable Long id) {
        return adminCommentService.findByUserId(id);
    }

    /**
     * Get all comment for a specific session
     * @param rowId
     * @return
     */
    @RequestMapping(value="/row/{rowId}", method= RequestMethod.GET)
    @ResponseBody
    public List<AdminComment> getCommentsByRowId(@PathVariable Long rowId ) {
        return adminCommentService.findByRowId(rowId);
    }
}