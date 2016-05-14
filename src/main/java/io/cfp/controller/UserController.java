/*
 * Copyright (c) 2016 BreizhCamp
 * [http://breizhcamp.org]
 *
 * This file is part of CFP.io.
 *
 * CFP.io is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package io.cfp.controller;

import io.cfp.dto.user.UserProfil;
import io.cfp.entity.Role;
import io.cfp.entity.User;
import io.cfp.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping(value = { "/v0/users", "/api/users" }, produces = APPLICATION_JSON_UTF8_VALUE)
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Get current user profil
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/me", method = RequestMethod.GET)
    @Secured(Role.AUTHENTICATED)
    public Map<String, Object> getUserProfil(@AuthenticationPrincipal User user) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("email", user.getEmail());
        map.put("lastname", user.getLastname());
        map.put("firstname", user.getFirstname());
        map.put("language", user.getLanguage());
        map.put("phone", user.getPhone());
        map.put("company", user.getCompany());
        map.put("bio", user.getBio());
        map.put("social", user.getSocial());
        map.put("twitter", user.getTwitter());
        map.put("googleplus", user.getGoogleplus());
        map.put("github", user.getGithub());
        map.put("imageProfilURL", user.getImageProfilURL());
        map.put("gender", user.getGender());
        map.put("tshirtSize", user.getTshirtSize());

        return map;
    }

    /**
     * Edit current user profil
     *
     * @param user
     * @param profil
     * @return
     */
    @RequestMapping(value = "/me", method = RequestMethod.PUT)
    @Secured(Role.AUTHENTICATED)
    public UserProfil putUserProfil(@AuthenticationPrincipal User user, @RequestBody UserProfil profil) {
        profil.setEmail(user.getEmail());
        userService.update(user.getId(), profil);
        return profil;

    }
}
