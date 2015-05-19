package fr.sii.service.admin.rate;

import fr.sii.domain.admin.rate.AdminRate;
import fr.sii.domain.admin.user.AdminUser;
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
            AdminUser u = adminUserService.findOne(r.getUserId());
            r.setUser(u);
            nrs.add(r);
        }
        return nrs;
    }
    public AdminRate matchUser(List<AdminRate> rs)
    {
        List<AdminRate> nrs = new ArrayList<>();
        if(rs.size() > 0)
        {
            AdminRate r = rs.get(0);
            AdminUser u = adminUserService.findOne(r.getUserId());
            r.setUser(u);
            return r;
        }
        return null;
    }

    public AdminRate matchUser(AdminRate r)
    {
        if(r != null)
        {
            AdminUser u = adminUserService.findOne(r.getUserId());
            r.setUser(u);
            return r;
        }
        return null;
    }

    public List<AdminRate> findAll()
    {
        return matchUsers(rateRepository.findAll());
    }

    public void deleteAll()
    {
        rateRepository.deleteAll();
    }

    public AdminRate save(AdminRate r)
    {
        AdminRate s = rateRepository.save(r);
        return matchUser(s);
    }

    public AdminRate put(Long id,AdminRate r)
    {
        AdminRate pr = findOne(id);
        if(pr != null)
        {
            rateRepository._delete(id);
            r.setEntityId(id);
            return matchUser(rateRepository.save(r));
        }
        return null;
    }

    public void delete(Long id)
    {
        rateRepository._delete(id);
    }

    public AdminRate findOne(Long id)
    {
        List<AdminRate> rs = rateRepository.findByEntityId(id);
        return matchUser(rs);
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
        return matchUser(rateRepository.findByRowIdAndUserId(rowId, userId));
    }
}