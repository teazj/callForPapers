package fr.sii.controller.admin.comment;

import fr.sii.domain.admin.comment.AdminComment;
import fr.sii.domain.exception.NotFoundException;
import fr.sii.service.admin.comment.AdminCommentService;
import fr.sii.service.admin.user.AdminUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by tmaugin on 29/04/2015.
 */
@Controller
@RequestMapping(value="api/admin/comment", produces = "application/json; charset=utf-8")
public class AdminCommentController {

    AdminCommentService adminCommentService;

    AdminUserService adminUserServiceCustom;

    public void setAdminCommentService(AdminCommentService adminCommentService) {
        this.adminCommentService = adminCommentService;
    }

    public void setAdminUserServiceCustom(AdminUserService adminUserServiceCustom) {
        this.adminUserServiceCustom = adminUserServiceCustom;
    }

    @RequestMapping(method= RequestMethod.GET)
    @ResponseBody
    public List<AdminComment> getComments() {
        return adminCommentService.findAll();
    }

    @RequestMapping(method= RequestMethod.DELETE)
    @ResponseBody
    public void deleteComments() {
        adminCommentService.deleteAll();
    }

    @RequestMapping(method=RequestMethod.POST)
    @ResponseBody public AdminComment postComment(@Valid @RequestBody AdminComment adminComment) {
        adminComment.setUserId(adminUserServiceCustom.getCurrentUser().getEntityId());
        return adminCommentService.save(adminComment);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    @ResponseBody public AdminComment putComment(@PathVariable Long id, @Valid @RequestBody AdminComment adminComment) throws NotFoundException {
        adminComment.setUserId(adminUserServiceCustom.getCurrentUser().getEntityId());
        return adminCommentService.put(id, adminComment);
    }

    @RequestMapping(value="/{id}", method= RequestMethod.GET)
    @ResponseBody
    public AdminComment getComment(@PathVariable Long id) throws NotFoundException {
        return adminCommentService.findOne(id);
    }

    @RequestMapping(value="/user/{id}", method= RequestMethod.GET)
    @ResponseBody
    public List<AdminComment> getCommentsByUserId(@PathVariable Long id) {
        return adminCommentService.findByUserId(id);
    }

    @RequestMapping(value="/row/{rowId}", method= RequestMethod.GET)
    @ResponseBody
    public List<AdminComment> getCommentsByRowId(@PathVariable Long rowId ) {
        return adminCommentService.findByRowId(rowId);
    }

    @RequestMapping(value="/{id}", method= RequestMethod.DELETE)
    @ResponseBody
    public void deleteComment(@PathVariable Long id) throws NotFoundException {
        adminCommentService.delete(id);
    }
}