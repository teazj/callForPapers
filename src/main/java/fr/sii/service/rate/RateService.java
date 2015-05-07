package fr.sii.service.rate;

import com.google.appengine.api.datastore.KeyFactory;
import fr.sii.domain.rate.Rate;
import fr.sii.domain.user.User;
import fr.sii.repository.rate.RateRespository;
import fr.sii.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tmaugin on 27/04/2015.
 */
@Service
public class RateService {

    @Autowired
    private RateRespository rateRepository;

    @Autowired
    private UserService userService;

    public List<Rate> matchUsers(List<Rate> rs)
    {
        List<Rate> nrs = new ArrayList<>();
        for (Rate r : rs)
        {
            User u = userService.findOne(r.getUserId());
            r.setUser(u);
            nrs.add(r);
        }
        return nrs;
    }
    public Rate matchUser(List<Rate> rs)
    {
        List<Rate> nrs = new ArrayList<>();
        if(rs.size() > 0)
        {
            Rate r = rs.get(0);
            User u = userService.findOne(r.getUserId());
            r.setUser(u);
            return r;
        }
        return null;
    }

    public Rate matchUser(Rate r)
    {
        if(r != null)
        {
            User u = userService.findOne(r.getUserId());
            r.setUser(u);
            return r;
        }
        return null;
    }

    public List<Rate> findAll()
    {
        return matchUsers(rateRepository.findAll());
    }

    public void deleteAll()
    {
        rateRepository.deleteAll();
    }

    public Rate save(Rate r)
    {
        Rate s = rateRepository.save(r);
        return matchUser(s);
    }

    public Rate put(Long id,Rate r)
    {
        Rate pr = findOne(id);
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

    public Rate findOne(Long id)
    {
        List<Rate> rs = rateRepository.findByEntityId(id);
        return matchUser(rs);
    }

    public List<Rate> findByUserId(Long id)
    {
        return matchUsers(rateRepository.findByUserId(id));
    }
    public List<Rate> findByRowId(Long id)
    {
        return matchUsers(rateRepository.findByRowIdOrderByRateDesc(id));
    }
    public Rate findByRowIdAndUserId(Long rowId, Long userId)
    {
        return matchUser(rateRepository.findByRowIdAndUserId(rowId, userId));
    }
}