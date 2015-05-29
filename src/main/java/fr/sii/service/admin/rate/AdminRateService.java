package fr.sii.service.admin.rate;

import fr.sii.domain.admin.rate.AdminRate;
import fr.sii.domain.admin.user.AdminUser;
import fr.sii.domain.exception.NotFoundException;
import fr.sii.repository.admin.rate.AdminRateRespository;
import fr.sii.service.admin.user.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tmaugin on 27/04/2015.
 */
@Service
public class AdminRateService {

    @Autowired
    private AdminRateRespository rateRepository;

    @Autowired
    private AdminUserService adminUserService;

    public List<AdminRate> matchUsers(List<AdminRate> rs)
    {
        List<AdminRate> nrs = new ArrayList<>();
        for (AdminRate r : rs)
        {
            try {
                AdminUser u = adminUserService.findOne(r.getUserId());
                r.setUser(u);
                nrs.add(r);
            } catch (NotFoundException e) {
                //e.printStackTrace();
            }
        }
        return nrs;
    }
    public AdminRate matchUser(List<AdminRate> rs) throws NotFoundException {
        if(rs.size() > 0)
        {
            AdminRate r = rs.get(0);
            try {
                AdminUser u = adminUserService.findOne(r.getUserId());
                r.setUser(u);
            } catch (NotFoundException e) {
                //e.printStackTrace();
            }
            return r;
        }
        throw new NotFoundException("Rate not found");
    }

    public AdminRate matchUser(AdminRate r) {
        try {
            AdminUser u = adminUserService.findOne(r.getUserId());
            r.setUser(u);
        } catch (NotFoundException e) {
            //e.printStackTrace();
        }
        return r;
    }

    public List<AdminRate> findAll()
    {
        return matchUsers(rateRepository.findAll());
    }

    public void deleteAll()
    {
        rateRepository.deleteAll();
    }

    public AdminRate save(AdminRate r) {
        return matchUser(rateRepository.save(r));
    }

    public AdminRate put(Long id,AdminRate r) throws NotFoundException {
        delete(id);
        r.setEntityId(id);
        return matchUser(rateRepository.save(r));
    }

    public void delete(Long id) throws NotFoundException {
        findOne(id);
        rateRepository._delete(id);
    }

    public AdminRate findOne(Long id) throws NotFoundException {
        return matchUser(rateRepository.findByEntityId(id));
    }

    public List<AdminRate> findByUserId(Long id)
    {
        return matchUsers(rateRepository.findByUserId(id));
    }
    public List<AdminRate> findByRowId(Long id)
    {
        return matchUsers(rateRepository.findByRowIdOrderByRateDesc(id));
    }
    public AdminRate findByRowIdAndUserId(Long rowId, Long userId)
    {
        try {
            return matchUser(rateRepository.findByRowIdAndUserId(rowId, userId));
        } catch (NotFoundException e) {
            return null;
        }
    }
}