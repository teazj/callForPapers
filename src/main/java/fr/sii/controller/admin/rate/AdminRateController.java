package fr.sii.controller.admin.rate;

import fr.sii.domain.admin.rate.AdminRate;
import fr.sii.domain.exception.NotFoundException;
import fr.sii.service.admin.rate.AdminRateService;
import fr.sii.service.admin.user.AdminUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by tmaugin on 24/04/2015.
 */
@Controller
@RequestMapping(value="api/admin/rate", produces = "application/json; charset=utf-8")
public class AdminRateController {

    AdminRateService adminRateService;

    AdminUserService adminUserServiceCustom;

    public void setAdminRateService(AdminRateService adminRateService) {
        this.adminRateService = adminRateService;
    }

    public void setAdminUserServiceCustom(AdminUserService adminUserServiceCustom) {
        this.adminUserServiceCustom = adminUserServiceCustom;
    }

    @RequestMapping(method= RequestMethod.GET)
    @ResponseBody
    public List<AdminRate> getRates() {
        return adminRateService.findAll();
    }

    @RequestMapping(method= RequestMethod.DELETE)
    @ResponseBody
    public void deleteRates() {
        adminRateService.deleteAll();
    }

    @RequestMapping(method=RequestMethod.POST)
    @ResponseBody public AdminRate postRate(@Valid @RequestBody AdminRate adminRate) {
        adminRate.setUserId(adminUserServiceCustom.getCurrentUser().getEntityId());
        return adminRateService.save(adminRate);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    @ResponseBody public AdminRate putRate(@PathVariable Long id, @Valid @RequestBody AdminRate adminRate) throws NotFoundException {
        adminRate.setUserId(adminUserServiceCustom.getCurrentUser().getEntityId());
        return adminRateService.put(id, adminRate);
    }

    @RequestMapping(value="/{id}", method= RequestMethod.GET)
    @ResponseBody
    public AdminRate getRate(@PathVariable Long id) throws NotFoundException {
        return adminRateService.findOne(id);
    }

    @RequestMapping(value="/user/{id}", method= RequestMethod.GET)
    @ResponseBody
    public List<AdminRate> getRateByUserId(@PathVariable Long id) {
        return adminRateService.findByUserId(id);
    }

    @RequestMapping(value="/row/{id}", method= RequestMethod.GET)
    @ResponseBody
    public List<AdminRate> getRatesByRowId(@PathVariable Long id) {
        return adminRateService.findByRowId(id);
    }

    @RequestMapping(value="/row/{rowId}/user/me", method= RequestMethod.GET)
    @ResponseBody
    public AdminRate getRateByRowIdAndUserId(@PathVariable Long rowId) {
        Long userId = adminUserServiceCustom.getCurrentUser().getEntityId();
        return adminRateService.findByRowIdAndUserId(rowId, userId);
    }

    @RequestMapping(value="/{id}", method= RequestMethod.DELETE)
    @ResponseBody
    public void deleteRate(@PathVariable Long id) throws NotFoundException {
        adminRateService.delete(id);
    }
}
