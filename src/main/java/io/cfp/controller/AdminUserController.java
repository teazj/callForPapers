/*
 * The MIT License
 *
 *  Copyright (c) 2016, CloudBees, Inc.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 *
 */

package io.cfp.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jwt.JWTClaimsSet;

import io.cfp.dto.AdminUserInfo;
import io.cfp.entity.AdminUser;
import io.cfp.service.admin.user.AdminUserService;
import io.cfp.service.auth.AuthUtils;

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

        AdminUser adminUser = adminUserService.findFromEmail(claimsSet.getSubject());

        if (adminUser == null) {
            return new AdminUserInfo("./", false, false, false, "");
        }

        return new AdminUserInfo("./logout", true, true, true, adminUser.getEmail());
    }
}
