package fr.sii.service.admin.stats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.sii.domain.admin.meter.AdminMeter;
import fr.sii.repository.TalkRepo;
import fr.sii.repository.UserRepo;

/**
 * Created by tmaugin on 16/07/2015.
 * SII
 */
@Service
@Transactional
public class AdminStatsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TalkRepo talkRepo;

    public AdminMeter getAdminMeter() {
        AdminMeter meter = new AdminMeter();
        meter.setSpeakers((int) userRepo.count());
        meter.setTalks((int) talkRepo.count());
        return meter;
    }
}
