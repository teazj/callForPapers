package fr.sii.controller.common.user;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jwt.JWTClaimsSet;

import fr.sii.dto.AdminUserInfo;
import fr.sii.entity.AdminUser;
import fr.sii.service.admin.user.AdminUserService;
import fr.sii.service.auth.AuthUtils;

@RestController
@RequestMapping(value="/api/adminUser")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;


    /**
     * Obtain current admin user information
     */
    @RequestMapping(value="/currentUser", method= RequestMethod.GET)
    public AdminUserInfo getCurrentUser(HttpServletRequest req) {
        JWTClaimsSet claimsSet = AuthUtils.getTokenBody(req);

        if (claimsSet == null) {
            return new AdminUserInfo("./", false, false, false, "");
        }

        AdminUser adminUser = adminUserService.findFromUserId(Integer.parseInt(claimsSet.getSubject()));

        if (adminUser == null) {
            return new AdminUserInfo("./", false, false, false, "");
        }

        return new AdminUserInfo("./logout", true, true, true, adminUser.getEmail());
    }
}
