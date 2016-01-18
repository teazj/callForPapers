package fr.sii.service;

import fr.sii.dto.TalkAdmin;
import fr.sii.dto.user.UserProfil;
import fr.sii.entity.AdminUser;
import fr.sii.entity.Rate;
import fr.sii.entity.Talk;
import fr.sii.repository.RateRepo;
import fr.sii.repository.TalkRepo;
import fr.sii.repository.TrackRepo;
import fr.sii.repository.UserRepo;
import fr.sii.entity.User;

import fr.sii.entity.TalkFormat;
import fr.sii.dto.user.CospeakerProfil;
import fr.sii.domain.exception.CospeakerNotFoundException;

import fr.sii.repository.TalkFormatRepo;
import fr.sii.service.admin.user.AdminUserService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;



import static java.util.stream.Collectors.*;

/**
 * Service for managing talks by the admins
 */
@Service
@Transactional
public class TalkAdminService {

    @Autowired
    private TalkRepo talkRepo;

    @Autowired
    private RateRepo rateRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TalkFormatRepo talkFormatRepo;

    @Autowired
    private TrackRepo trackRepo;

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private MapperFacade mapper;

    /**
     * Retrieve all talks
     * @param states List of states the talk must be
     * @return List of talks
     */
    public List<TalkAdmin> findAll(Talk.State... states) {
        List<Talk> talks = talkRepo.findByStatesFetch(Arrays.asList(states));
        List<Rate> rates = rateRepo.findAllFetchAdmin();

        AdminUser admin = adminUserService.getCurrentUser();
        Map<Integer, List<Rate>> reviewed = rates.stream()
            .filter(r -> admin.getId() == r.getAdminUser().getId())
            .collect(groupingBy(r -> r.getTalk().getId()));

        Map<Integer, Double> averages = rates.stream()
            .collect(groupingBy(r -> r.getTalk().getId(), averagingInt(Rate::getRate)));

        Map<Integer, List<String>> voters = rates.stream()
            .collect(groupingBy(r -> r.getTalk().getId(), mapping(r -> r.getAdminUser().getEmail(), toList())));

        List<TalkAdmin> talkList = mapper.mapAsList(talks, TalkAdmin.class);
        for (TalkAdmin talk : talkList) {
            int talkId = talk.getId();

            talk.setReviewed(reviewed.get(talkId) != null);
            talk.setMean(averages.get(talkId));
            talk.setVoteUsersEmail(voters.get(talkId));
        }
        return talkList;
    }

    /**
     * Retrieve a talk
     * @param talkId Id of the talk to retrieve
     * @return Talk or null if not found
     */
    public TalkAdmin getOne(int talkId) {
        Talk talk = talkRepo.findOne(talkId);
        TalkAdmin talkAdmin = mapper.map(talk, TalkAdmin.class);
        UserProfil user = mapper.map(talk.getUser(),UserProfil.class);
        user.setImageProfilURL(talk.getUser().getImageProfilURL());
        talkAdmin.setSpeaker(user);
        talkAdmin.setUserId(user.getId());
        return talkAdmin;
    }

    /**
     * Edit a talk
     * @param talkAdmin Talk to edit
     * @return Edited talk
     */
    public TalkAdmin edit(TalkAdmin talkAdmin)  throws CospeakerNotFoundException {
      Talk talk = talkRepo.findOne(talkAdmin.getId());
      if (talk == null) return null;
      talk.setTrack(trackRepo.findOne(talkAdmin.getTrackId()));
      talk.setTalkFormat(talkFormatRepo.findOne(talkAdmin.getFormat()));
      setCoSpeakerId(talkAdmin.getCospeakers());
      mapper.map(talkAdmin, talk);
      talkRepo.flush();
      return mapper.map(talk, TalkAdmin.class);
    }

    /**
     * For each cospeaker, check if the user is in the CFP database and set the id on the user object
     * @param cospeakers CoSpeakers to check and set id
     * @throws CospeakerNotFoundException If a cospeaker is not found
     */
    private void setCoSpeakerId(Set<CospeakerProfil> cospeakers) throws CospeakerNotFoundException {
        if (cospeakers == null || cospeakers.isEmpty()) return;

        for (CospeakerProfil cospeaker : cospeakers) {
            List<User> existingUser = userRepo.findByEmail(cospeaker.getEmail());
            if (existingUser.isEmpty()) {
                throw new CospeakerNotFoundException("error cospeaker not found", new CospeakerProfil(cospeaker.getEmail()));
            }

            cospeaker.setId(existingUser.get(0).getId());
        }
    }

    /**
     * Delete a talk
     * @param talkId Id of the talk to delete
     * @return Deleted talk
     */
    public TalkAdmin delete(int talkId) {
        Talk talk = talkRepo.findOne(talkId);
        TalkAdmin deleted = mapper.map(talk, TalkAdmin.class);
        talkRepo.delete(talk);
        return deleted;
    }
}
