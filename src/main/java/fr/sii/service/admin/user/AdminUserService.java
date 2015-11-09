package fr.sii.service.admin.user;

import com.google.appengine.api.users.UserServiceFactory;
import fr.sii.domain.exception.NotFoundException;
import fr.sii.entity.AdminUser;
import fr.sii.repository.AdminUserRepo;

import java.util.List;

/**
 * Created by tmaugin on 24/04/2015.
 */
public class AdminUserService {

    private AdminUserRepo adminUserRepo;

    public void setAdminUserRepo(AdminUserRepo adminUserRepo) {
        this.adminUserRepo = adminUserRepo;
    }

    public List<AdminUser> findAll()
    {
        return adminUserRepo.findAll();
    }

    public AdminUser getCurrentUser() throws NotFoundException {
        com.google.appengine.api.users.UserService userService = UserServiceFactory.getUserService();
        com.google.appengine.api.users.User user = userService.getCurrentUser();
        if(user == null)
        {
            throw new NotFoundException("User not found");
        }
        String id = user.getUserId();
        String idParsed = id.substring(0, id.length() - 2);
        Long userId = Long.parseLong(idParsed);
        AdminUser adminUserCustom = null;
        try {
            adminUserCustom = findOne(userId);
        } catch (NotFoundException e) {
            // User not found
            adminUserCustom = save(user);
        }
        return adminUserCustom;
    }

    public void deleteAll()
    {
        adminUserRepo.deleteAll();
    }

    public AdminUser save(AdminUser adminUser)
    {
        return adminUserRepo.save(adminUser);
    }

    public AdminUser save(com.google.appengine.api.users.User user)
    {
        String id = user.getUserId();
        String idParsed = id.substring(0, id.length() - 2);
        int userId = Integer.parseInt(idParsed);

        AdminUser adminUser = new AdminUser();
        adminUser.setId(userId);
        adminUser.setName(user.getNickname());
        adminUser.setEmail(user.getEmail());

        return adminUserRepo.save(adminUser);
    }

    public AdminUser put(Long id,AdminUser adminUser) throws NotFoundException {
        delete(id);
        adminUser.setEntityId(id);
        return adminUserRepo.save(adminUser);
    }

    public void delete(Long id) throws NotFoundException {
        findOne(id);
        adminUserRepo._delete(id);
    }

    public AdminUser findOne(Long id) throws NotFoundException {
        List<AdminUser> r = adminUserRepo.findByEntityId(id);
        if(!r.isEmpty())
            return r.get(0);
        else
            throw new NotFoundException("User not found");
    }

}