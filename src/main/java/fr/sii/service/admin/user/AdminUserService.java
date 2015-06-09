package fr.sii.service.admin.user;

import com.google.appengine.api.users.UserServiceFactory;
import fr.sii.domain.admin.user.AdminUser;
import fr.sii.domain.exception.NotFoundException;
import fr.sii.repository.admin.user.AdminUserRespository;

import java.util.List;

/**
 * Created by tmaugin on 24/04/2015.
 */
public class AdminUserService {

    private AdminUserRespository adminUserRespository;

    public void setAdminUserRespository(AdminUserRespository adminUserRespository) {
        this.adminUserRespository = adminUserRespository;
    }

    public List<AdminUser> findAll()
    {
        return adminUserRespository.findAll();
    }

    public AdminUser getCurrentUser()
    {
        com.google.appengine.api.users.UserService userService = UserServiceFactory.getUserService();
        com.google.appengine.api.users.User user = userService.getCurrentUser();
        if(user == null)
        {
            return null;
        }
        String id = user.getUserId();
        String idParsed = id.substring(0, id.length() - 2);
        Long userId = Long.parseLong(idParsed);
        AdminUser adminUserCustom = null;
        try {
            adminUserCustom = findOne(userId);
        } catch (NotFoundException e) {
            // User not found
        }

        if(adminUserCustom == null)
        {
            adminUserCustom = save(user);
        }
        return adminUserCustom;
    }

    public void deleteAll()
    {
        adminUserRespository.deleteAll();
    }

    public AdminUser save(AdminUser u)
    {
        return adminUserRespository.save(u);
    }

    public AdminUser save(com.google.appengine.api.users.User u)
    {
        String id = u.getUserId();
        String idParsed = id.substring(0, id.length() - 2);
        Long userId = Long.parseLong(idParsed);

        AdminUser adminUser = new AdminUser(userId);
        adminUser.setName(u.getNickname());
        adminUser.setEmail(u.getEmail());

        return adminUserRespository.save(adminUser);
    }

    public AdminUser put(Long id,AdminUser u) throws NotFoundException {
        delete(id);
        u.setEntityId(id);
        return adminUserRespository.save(u);
    }

    public void delete(Long id) throws NotFoundException {
        findOne(id);
        adminUserRespository._delete(id);
    }

    public AdminUser findOne(Long id) throws NotFoundException {
        List<AdminUser> r = adminUserRespository.findByEntityId(id);
        if(r.size() > 0)
            return r.get(0);
        else
            throw new NotFoundException("User not found");
    }

    public List<AdminUser> findByemail(String email)
    {
        return adminUserRespository.findByemail(email);
    }
}