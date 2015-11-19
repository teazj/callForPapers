package fr.sii.service.admin.stats;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.memcache.stdimpl.GCacheFactory;
import com.google.gdata.util.ServiceException;
import fr.sii.domain.admin.meter.AdminMeter;
import fr.sii.domain.exception.NotFoundException;
import fr.sii.repository.TalkRepo;
import fr.sii.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by tmaugin on 16/07/2015.
 * SII
 */
@Service
@Transactional
public class AdminStatsService {

    private final String METER_KEY = "Meter";

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TalkRepo talkRepo;

    public AdminMeter getAdminMeter() throws ServiceException, EntityNotFoundException, IOException, NotFoundException {
        Cache cache;
        AdminMeter adminMeter = null;
        try {
            CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
            Map properties = new HashMap<>();
            properties.put(GCacheFactory.EXPIRATION_DELTA, TimeUnit.MINUTES.toSeconds(5));
            cache = cacheFactory.createCache(properties);
            adminMeter = (AdminMeter) cache.get(METER_KEY);
            if(adminMeter == null) {
                adminMeter = processMeter();
                cache.put(METER_KEY, adminMeter);
            }
        } catch (CacheException e) {
            e.printStackTrace();
            return processMeter();
        }
        return adminMeter;
    }

    private AdminMeter processMeter() throws ServiceException, EntityNotFoundException, IOException, NotFoundException {
        AdminMeter meter = new AdminMeter();
        meter.setSpeakers((int) userRepo.count());
        meter.setTalks((int) talkRepo.count());
        return meter;
    }
}
