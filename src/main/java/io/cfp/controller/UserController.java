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

import io.cfp.domain.exception.NotFoundException;
import io.cfp.domain.exception.NotVerifiedException;
import io.cfp.dto.user.UserProfil;
import io.cfp.entity.User;
import io.cfp.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/restricted", produces = "application/json; charset=utf-8")
public class UserController extends RestrictedController {

    @Autowired
    private UserService userService;

    /**
     * Get current user profil
     *
     * @param req
     * @return
     * @throws NotVerifiedException
     * @throws NotFoundException
     * @throws IOException
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public Map<String, Object> getUserProfil(HttpServletRequest req) throws NotVerifiedException, NotFoundException, IOException {
        User u = retrieveUser(req);

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("email", u.getEmail());
        map.put("lastname", u.getLastname());
        map.put("firstname", u.getFirstname());
        map.put("phone", u.getPhone());
        map.put("company", u.getCompany());
        map.put("bio", u.getBio());
        map.put("social", u.getSocial());
        map.put("twitter", u.getTwitter());
        map.put("googleplus", u.getGoogleplus());
        map.put("github", u.getGithub());
        map.put("imageProfilURL", u.getImageProfilURL());

        return map;
    }

    /**
     * Edit current user profil
     *
     * @param req
     * @param profil
     * @return
     * @throws NotVerifiedException
     * @throws NotFoundException
     * @throws IOException
     */
    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    public UserProfil putUserProfil(HttpServletRequest req, @RequestBody UserProfil profil) throws NotVerifiedException, NotFoundException, IOException {
        User u = retrieveUser(req);
        profil.setEmail(u.getEmail());
        userService.update(u.getId(), profil);

        return profil;

    }
}
