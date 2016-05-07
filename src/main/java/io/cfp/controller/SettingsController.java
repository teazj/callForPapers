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

import io.cfp.config.global.ServiceProviderSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.cfp.entity.Talk;
import io.cfp.entity.TalkFormat;
import java.util.List;
import io.cfp.domain.exception.NotVerifiedException;

import io.cfp.service.TalkUserService;
import io.cfp.dto.TrackDto;

@RestController
@RequestMapping(value="/api/settings", produces = "application/json; charset=utf-8")
public class SettingsController {

    @Autowired
    private ServiceProviderSettings serviceProviderSettings;

    @Autowired
    private TalkUserService talkService;

    @RequestMapping(value="/serviceproviders", method= RequestMethod.GET)
    public ServiceProviderSettings getServiceProviderSettings() {
        return serviceProviderSettings;
    }

    /**
     * Obtain list of talk formats
     */
    @RequestMapping(value = "/talk/formats")
    public List<TalkFormat> getTalkFormat() {
        return talkService.getTalkFormat();
    }

    /**
     * Get all session for the current user
     */
    @RequestMapping(value = "/talk/tracks", method = RequestMethod.GET)
    public List<TrackDto> getTrack() throws NotVerifiedException {
        return talkService.getTracks();
    }
}
