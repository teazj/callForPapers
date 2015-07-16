package fr.sii.service.admin.comment;

import fr.sii.domain.admin.comment.AdminComment;
import fr.sii.domain.admin.user.AdminUser;
import fr.sii.domain.exception.NotFoundException;
import fr.sii.repository.admin.comment.AdminCommentRepository;
import fr.sii.service.admin.user.AdminUserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tmaugin on 29/04/2015.
 */
public class AdminCommentService {

    private AdminCommentRepository adminCommentRepository;

    private AdminUserService adminUserService;

    public void setAdminCommentRepository(AdminCommentRepository adminCommentRepository) {
        this.adminCommentRepository = adminCommentRepository;
    }

    public void setAdminUserService(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    public List<AdminComment> matchUsers(List<AdminComment> adminComments)
    {
        List<AdminComment> nrs = new ArrayList<>();
        for (AdminComment adminComment : adminComments)
        {
            try {
                AdminUser u = adminUserService.findOne(adminComment.getUserId());
                adminComment.setUser(u);
                nrs.add(adminComment);
            } catch (NotFoundException e) {
                //e.printStackTrace();
            }
        }
        return nrs;
    }
    public AdminComment matchUser(List<AdminComment> adminComments) throws NotFoundException {
        if(!adminComments.isEmpty())
        {
            AdminComment adminComment = adminComments.get(0);
            try {
                AdminUser u = adminUserService.findOne(adminComment.getUserId());
                adminComment.setUser(u);
            } catch (NotFoundException e) {
                //e.printStackTrace();
            }
            return adminComment;
        }
        throw new NotFoundException("Comment not found");
    }

    public AdminComment matchUser(AdminComment adminComment){
        try {
            AdminUser u = adminUserService.findOne(adminComment.getUserId());
            adminComment.setUser(u);
        } catch (NotFoundException e) {
            //e.printStackTrace();
        }
        return adminComment;
    }

    public List<AdminComment> findAll()
    {
        return matchUsers(adminCommentRepository.findAll());
    }

    public void deleteAll()
    {
        adminCommentRepository.deleteAll();
    }

    public AdminComment save(AdminComment adminComment) {
        if(adminComment.getAdded() == null)
        {
            adminComment.setAdded(new Date());
        }
        return matchUser(adminCommentRepository.save(adminComment));
    }

    public AdminComment put(Long id,AdminComment adminComment) throws NotFoundException {
        if(adminComment.getAdded() == null)
        {
            adminComment.setAdded(new Date());
        }
        delete(id);
        adminComment.setEntityId(id);
        return matchUser(adminCommentRepository.save(adminComment));
    }

    public void delete(Long id) throws NotFoundException {
        findOne(id);
        adminCommentRepository._delete(id);
    }

    public AdminComment findOne(Long id) throws NotFoundException {
        return matchUser(adminCommentRepository.findByEntityId(id));
    }

    public List<AdminComment> findByUserId(Long id)
    {
        return matchUsers(adminCommentRepository.findByUserId(id));
    }

    public List<AdminComment> findByRowId(Long rowId)
    {
        return matchUsers(adminCommentRepository.findByRowIdOrderByAddedDesc(rowId));
    }
}