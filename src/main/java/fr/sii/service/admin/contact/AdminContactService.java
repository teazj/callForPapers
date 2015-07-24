package fr.sii.service.admin.contact;

import fr.sii.domain.admin.contact.AdminContact;
import fr.sii.domain.admin.user.AdminUser;
import fr.sii.domain.exception.NotFoundException;
import fr.sii.domain.user.User;
import fr.sii.repository.admin.contact.AdminContactRepository;
import fr.sii.service.admin.user.AdminUserService;
import fr.sii.service.user.UserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tmaugin on 24/07/2015.
 */
public class AdminContactService {

    private AdminContactRepository adminContactRepository;

    private AdminUserService adminUserService;

    private UserService userService;

    public void setAdminContactRepository(AdminContactRepository adminContactRepository) {
        this.adminContactRepository = adminContactRepository;
    }

    public void setAdminUserService(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public List<AdminContact> matchUsers(List<AdminContact> adminComments)
    {
        List<AdminContact> nrs = new ArrayList<>();
        for (AdminContact adminComment : adminComments)
        {
            if(adminComment.isAdmin()) {
                try {
                    AdminUser u = adminUserService.findOne(adminComment.getUserId());
                    adminComment.setUser(u);
                    nrs.add(adminComment);
                } catch (NotFoundException e) {
                    //e.printStackTrace();
                }
            } else {
                User u = userService.findById(adminComment.getUserId());
                if(u != null) {
                    AdminUser jsonUser = new AdminUser(u.getEntityId());
                    jsonUser.setEmail(u.getEmail());
                    jsonUser.setName(u.getEmail());
                    adminComment.setUser(jsonUser);
                    nrs.add(adminComment);
                }
            }
        }
        return nrs;
    }
    public AdminContact matchUser(List<AdminContact> adminComments) throws NotFoundException {
        if(!adminComments.isEmpty())
        {
            AdminContact adminComment = adminComments.get(0);
            if(adminComment.isAdmin()) {
                try {
                    AdminUser u = adminUserService.findOne(adminComment.getUserId());
                    adminComment.setUser(u);
                } catch (NotFoundException e) {
                    //e.printStackTrace();
                }
                return adminComment;
            } else {
                User u = userService.findById(adminComment.getUserId());
                if(u != null) {
                    AdminUser jsonUser = new AdminUser(u.getEntityId());
                    jsonUser.setEmail(u.getEmail());
                    jsonUser.setName(u.getEmail());
                    adminComment.setUser(jsonUser);
                    return adminComment;
                }
            }
        }
        throw new NotFoundException("Contact not found");
    }

    public AdminContact matchUser(AdminContact adminComment){
        if(adminComment.isAdmin()) {
            try {
                AdminUser u = adminUserService.findOne(adminComment.getUserId());
                adminComment.setUser(u);
            } catch (NotFoundException e) {
                //e.printStackTrace();
            }
        } else {
            User u = userService.findById(adminComment.getUserId());
            if(u != null) {
                AdminUser jsonUser = new AdminUser(u.getEntityId());
                jsonUser.setEmail(u.getEmail());
                jsonUser.setName(u.getEmail());
                adminComment.setUser(jsonUser);
            }
        }
        return adminComment;
    }

    public AdminContact save(AdminContact adminComment) {
        if(adminComment.getAdded() == null)
        {
            adminComment.setAdded(new Date());
        }
        return matchUser(adminContactRepository.save(adminComment));
    }

    public AdminContact put(Long id,AdminContact adminComment) throws NotFoundException {
        if(adminComment.getAdded() == null)
        {
            adminComment.setAdded(new Date());
        }
        delete(id);
        adminComment.setEntityId(id);
        return matchUser(adminContactRepository.save(adminComment));
    }

    public void delete(Long id) throws NotFoundException {
        findOne(id);
        adminContactRepository._delete(id);
    }

    public AdminContact findOne(Long id) throws NotFoundException {
        return matchUser(adminContactRepository.findByEntityId(id));
    }

    public List<AdminContact> findByRowId(Long rowId)
    {
        return matchUsers(adminContactRepository.findByRowIdOrderByAddedDesc(rowId));
    }
}