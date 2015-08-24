package fr.sii.service.restricted.stats;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.memcache.stdimpl.GCacheFactory;
import com.google.gdata.util.ServiceException;
import fr.sii.domain.restricted.meter.RestrictedMeter;
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
 * Created by tmaugin on 24/08/2015.
 * SII
 */
public class RestrictedStatsService {

    private final String METER_KEY = "RestrictedMeter";

    private SpreadsheetService googleService;

    public void setGoogleService(SpreadsheetService googleService) {
        this.googleService = googleService;
    }

    public RestrictedMeter getCommonMeter() throws ServiceException, EntityNotFoundException, IOException {
        Cache cache;
        RestrictedMeter restrictedMeter = null;
        try {
            CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
            Map properties = new HashMap<>();
            properties.put(GCacheFactory.EXPIRATION_DELTA, TimeUnit.MINUTES.toSeconds(5));
            cache = cacheFactory.createCache(properties);
            restrictedMeter = (RestrictedMeter) cache.get(METER_KEY);
            if(restrictedMeter == null) {
                restrictedMeter = processMeter();
                cache.put(METER_KEY, restrictedMeter);
            }
        } catch (CacheException e) {
            e.printStackTrace();
            return processMeter();
        }
        return restrictedMeter;
    }

    private RestrictedMeter processMeter() throws ServiceException, EntityNotFoundException, IOException {
        RestrictedMeter meter = new RestrictedMeter();
        List<RowResponse> rowResponses = googleService.getRowsSession();
        meter.setTalks(rowResponses.size());
        return meter;
    }
}

