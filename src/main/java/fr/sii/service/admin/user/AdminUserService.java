package fr.sii.service.admin.user;

import com.google.appengine.api.users.UserServiceFactory;
import fr.sii.domain.admin.user.AdminUser;
import fr.sii.repository.admin.user.AdminUserRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tmaugin on 24/04/2015.
 */
@Service
public class AdminUserService {

    @Autowired
    private AdminUserRespository adminUserRespository;

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
        AdminUser adminUserCustom = findOne(userId);

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
        AdminUser s = adminUserRespository.save(u);
        return s;
    }

    public AdminUser save(com.google.appengine.api.users.User u)
    {
        String id = u.getUserId();
        String idParsed = id.substring(0, id.length() - 2);
        Long userId = Long.parseLong(idParsed);

        AdminUser adminUser = new AdminUser(userId);
        adminUser.setName(u.getNickname());
        adminUser.setEmail(u.getEmail());

        AdminUser s = adminUserRespository.save(adminUser);
        return s;
    }

    public AdminUser put(Long id,AdminUser u)
    {
        AdminUser pu = findOne(id);
        if(pu != null)
        {
            adminUserRespository._delete(id);
            u.setEntityId(pu.getEntityId());
            return adminUserRespository.save(u);
        }
        return null;
    }

    public void delete(Long id)
    {
        adminUserRespository._delete(id);
    }

    public AdminUser findOne(Long id)
    {
        List<AdminUser> r = adminUserRespository.findByEntityId(id);
        if(r.size() > 0)

            return r.get(0);
        else
            return null;
    }

    public List<AdminUser> findByemail(String email)
    {
        return adminUserRespository.findByemail(email);
    }
}