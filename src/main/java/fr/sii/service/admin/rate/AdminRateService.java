package fr.sii.service.admin.rate;

import fr.sii.domain.admin.rate.AdminRate;
import fr.sii.domain.admin.user.AdminUser;
import fr.sii.domain.exception.NotFoundException;
import fr.sii.repository.admin.rate.AdminRateRespository;
import fr.sii.service.admin.user.AdminUserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tmaugin on 27/04/2015.
 */
public class AdminRateService {

    private AdminRateRespository adminRateRespository;

    private AdminUserService adminUserService;

    public void setAdminRateRespository(AdminRateRespository adminRateRespository) {
        this.adminRateRespository = adminRateRespository;
    }

    public void setAdminUserService(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    public List<AdminRate> matchUsers(List<AdminRate> adminRates)
    {
        List<AdminRate> newAdminRates = new ArrayList<>();
        for (AdminRate adminRate : adminRates)
        {
            try {
                AdminUser adminUser = adminUserService.findOne(adminRate.getUserId());
                adminRate.setUser(adminUser);
                newAdminRates.add(adminRate);
            } catch (NotFoundException e) {
                //e.printStackTrace();
            }
        }
        return newAdminRates;
    }
    public AdminRate matchUser(List<AdminRate> adminRates) throws NotFoundException {
        if(!adminRates.isEmpty())
        {
            AdminRate adminRate = adminRates.get(0);
            try {
                AdminUser adminUser = adminUserService.findOne(adminRate.getUserId());
                adminRate.setUser(adminUser);
            } catch (NotFoundException e) {
                //e.printStackTrace();
            }
            return adminRate;
        }
        throw new NotFoundException("Rate not found");
    }

    public AdminRate matchUser(AdminRate adminRate) {
        try {
            AdminUser adminUser = adminUserService.findOne(adminRate.getUserId());
            adminRate.setUser(adminUser);
        } catch (NotFoundException e) {
            //e.printStackTrace();
        }
        return adminRate;
    }

    public List<AdminRate> findAll()
    {
        return matchUsers(adminRateRespository.findAll());
    }

    public void deleteAll()
    {
        adminRateRespository.deleteAll();
    }

    public AdminRate save(AdminRate adminRate) {
        if(adminRate.getAdded() == null)
        {
            adminRate.setAdded(new Date());
        }
        return matchUser(adminRateRespository.save(adminRate));
    }

    public AdminRate put(Long id,AdminRate adminRate) throws NotFoundException {
        delete(id);
        adminRate.setEntityId(id);
        if(adminRate.getAdded() == null)
        {
            adminRate.setAdded(new Date());
        }
        return matchUser(adminRateRespository.save(adminRate));
    }

    public void delete(Long id) throws NotFoundException {
        findOne(id);
        adminRateRespository._delete(id);
    }

    public AdminRate findOne(Long id) throws NotFoundException {
        return matchUser(adminRateRespository.findByEntityId(id));
    }

    public List<AdminRate> findByUserId(Long id)
    {
        return matchUsers(adminRateRespository.findByUserId(id));
    }
    public List<AdminRate> findByRowId(Long id)
    {
        return matchUsers(adminRateRespository.findByRowIdOrderByRateDesc(id));
    }
    public AdminRate findByRowIdAndUserId(Long rowId, Long userId)
    {
        try {
            return matchUser(adminRateRespository.findByRowIdAndUserId(rowId, userId));
        } catch (NotFoundException e) {
            return null;
        }
    }
}