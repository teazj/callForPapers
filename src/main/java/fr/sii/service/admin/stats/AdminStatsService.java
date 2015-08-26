package fr.sii.service.admin.stats;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.memcache.stdimpl.GCacheFactory;
import com.google.gdata.util.ServiceException;
import fr.sii.domain.admin.meter.AdminMeter;
import fr.sii.domain.exception.NotFoundException;
import fr.sii.domain.spreadsheet.RowResponse;
import fr.sii.service.spreadsheet.SpreadsheetService;

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
public class AdminStatsService {

    private final String METER_KEY = "Meter";

    private SpreadsheetService googleService;

    public void setGoogleService(SpreadsheetService googleService) {
        this.googleService = googleService;
    }

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
        meter.setDrafts(googleService.getRowsDraft().size());
        List<RowResponse> rowResponses = googleService.getRowsSessionAdmin();
        Set<String> unique = new HashSet<String>();
        for (RowResponse rowResponse : rowResponses) {
            unique.add(rowResponse.getEmail());
        }
        meter.setSpeakers(unique.size());
        meter.setTalks(rowResponses.size());
        return meter;
    }
}

