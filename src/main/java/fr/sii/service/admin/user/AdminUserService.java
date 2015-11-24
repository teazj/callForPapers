package fr.sii.service.admin.user;

import com.google.appengine.api.users.UserServiceFactory;
import fr.sii.domain.exception.NotFoundException;
import fr.sii.entity.AdminUser;
import fr.sii.entity.User;
import fr.sii.repository.AdminUserRepo;
import fr.sii.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AdminUserService {

    @Autowired
    private AdminUserRepo adminUserRepo;

    @Autowired
    private UserRepo userRepo;

    /** Current loggued admin, scoped request object */
    @Autowired
    private AdminUser currentAdmin;

    public List<AdminUser> findAll()
    {
        return adminUserRepo.findAll();
    }

    /**
     * Retrieve an admin if the user e-mail match an admin e-mail
     * @param userId Id of the connected user
     * @return AdminUser if existing, null otherwise
     */
    public AdminUser findFromUserId(int userId) {
        User user = userRepo.findOne(userId);
        if (user == null) return null;
        return adminUserRepo.findByEmail(user.getEmail());
    }

    /**
     * Set connected admin for the current request
     * @param admin Admin to set
     */
    public void setCurrentAdmin(AdminUser admin) {
        //warning, do not replace the autowired object because it's a proxy around an object created by spring in the request
        currentAdmin.setId(admin.getId());
        currentAdmin.setEmail(admin.getEmail());
        currentAdmin.setName(admin.getName());
    }

    public AdminUser getCurrentUser() throws NotFoundException {
        com.google.appengine.api.users.UserService userService = UserServiceFactory.getUserService();
        com.google.appengine.api.users.User user = userService.getCurrentUser();
        if(user == null) throw new NotFoundException("User not found");

        //TODO unicité à verifier
        AdminUser admin = adminUserRepo.findByEmail(user.getEmail());
        if (admin == null) {
            admin = save(user);
        }

        return admin;
    }

    public AdminUser save(com.google.appengine.api.users.User user) {

        AdminUser adminUser = new AdminUser();
        adminUser.setName(user.getNickname());
        adminUser.setEmail(user.getEmail());

        AdminUser saved = adminUserRepo.save(adminUser);
        adminUserRepo.flush();
        return saved;
    }
}
