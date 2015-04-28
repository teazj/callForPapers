package fr.sii.service.rate;

import com.google.appengine.api.datastore.KeyFactory;
import fr.sii.domain.rate.Rate;
import fr.sii.repository.rate.RateRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by tmaugin on 27/04/2015.
 */
@Service
public class RateService {

    @Autowired
    private RateRespository rateRepository;

    public List<Rate> findAll()
    {
        return rateRepository.findAll();
    }

    public void deleteAll()
    {
        rateRepository.deleteAll();
    }

    public Rate save(Rate r)
    {
        Rate s = rateRepository.save(r);
        s.setEntityId(KeyFactory.stringToKey(s.getId()).getId());
        Rate s2 = rateRepository.save(s);
        return s2;
    }

    public Rate put(Long id,Rate r)
    {
        Rate pr = findOne(id);
        if(pr != null)
        {
            rateRepository._delete(id);
            r.setEntityId(id);
            return rateRepository.save(r);
        }
        return null;
    }

    public void delete(Long id)
    {
        rateRepository._delete(id);
    }

    public Rate findOne(Long id)
    {
        List<Rate> r = rateRepository.findByEntityId(id);
        if(r.size() > 0)
            return r.get(0);
        else
            return null;
    }

    public List<Rate> findByUserId(Long id)
    {
        return rateRepository.findByUserId(id);
    }
}