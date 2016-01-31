package fr.sii.controller.admin.rate;

import fr.sii.domain.exception.NotFoundException;
import fr.sii.dto.RateAdmin;
import fr.sii.service.RateAdminService;
import fr.sii.service.admin.user.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value="api/admin/rates", produces = "application/json; charset=utf-8")
public class AdminRateController {

    @Autowired
    private AdminUserService adminUserServiceCustom;

    @Autowired
    private RateAdminService rateService;

    /**
     * Get all ratings
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<RateAdmin> getRates() {
        return rateService.getAll();
    }

    /**
     * Delete all ratings
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public void deleteRates() {
        rateService.deleteAll();
    }

    /**
     * Add a new rating
     */
    @RequestMapping(method=RequestMethod.POST)
    public RateAdmin postRate(@Valid @RequestBody RateAdmin rate) throws NotFoundException {
        return rateService.add(rate, adminUserServiceCustom.getCurrentUser(), rate.getTalkId());
    }

    /**
     * Edit a rating
     */
    @RequestMapping(value= "/{rateId}", method=RequestMethod.PUT)
    public RateAdmin putRate(@PathVariable int rateId, @Valid @RequestBody RateAdmin rate) {
        rate.setId(rateId);
        return rateService.edit(rate);
    }

    /**
     * Get a specific rating
     */
    @RequestMapping(value= "/{rateId}", method= RequestMethod.GET)
    public RateAdmin getRate(@PathVariable int rateId) {
        return rateService.get(rateId);
    }

    /**
     * Get all rating for a given user
     * @param userId
     * @return
     */
    @RequestMapping(value= "/user/{userId}", method= RequestMethod.GET)
    public List<RateAdmin> getRateByUserId(@PathVariable int userId) {
        return rateService.findForUser(userId);
    }

    /**
     * Get all ratings for a given session
     */
    @RequestMapping(value= "/session/{talkId}", method= RequestMethod.GET)
    public List<RateAdmin> getRatesByTalkId(@PathVariable int talkId) {
        return rateService.findForTalk(talkId);
    }

    /**
     * Get rating for current user and a session
     */
    @RequestMapping(value= "/session/{talkId}/user/me", method = RequestMethod.GET)
    public RateAdmin getRateByRowIdAndUserId(@PathVariable int talkId) throws NotFoundException {
        int adminId = adminUserServiceCustom.getCurrentUser().getId();
        return rateService.findForTalkAndAdmin(talkId, adminId);
    }

    /**
     * Delete specific rating
     */
    @RequestMapping(value= "/{rateId}", method= RequestMethod.DELETE)
    public void deleteRate(@PathVariable int rateId) {
        rateService.delete(rateId);
    }
}
