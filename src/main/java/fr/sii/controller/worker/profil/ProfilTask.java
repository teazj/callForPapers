package fr.sii.controller.worker.profil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.gdata.util.ServiceException;
import fr.sii.domain.user.UserProfil;
import fr.sii.service.spreadsheet.SpreadsheetService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by tmaugin on 30/07/2015.
 * SII
 */

@Controller
@RequestMapping(value="/worker/profil", produces = "application/json; charset=utf-8")
public class ProfilTask {

    private SpreadsheetService googleService;

    public void setGoogleService(SpreadsheetService googleService) {
        this.googleService = googleService;
    }

    /**
     * Update all sessions in spreadsheat with new profil
     * @param request
     * @param response
     * @throws ServiceException
     */
    @RequestMapping(method= RequestMethod.POST)
    @ResponseBody
    public void updateProfilSessions(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        String userId = request.getParameter("userId");
        String profil = request.getParameter("profil");

        ObjectMapper m = new ObjectMapper();
        try {
            UserProfil userProfil = m.readValue(profil, UserProfil.class);
            googleService.updateProfilSessions(userProfil, Long.parseLong(userId));
        } catch (IOException | EntityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
