package fr.sii.service;

import fr.sii.dto.TalkAdmin;
import fr.sii.dto.user.UserProfil;
import fr.sii.entity.AdminUser;
import fr.sii.entity.Rate;
import fr.sii.entity.Talk;
import fr.sii.repository.RateRepo;
import fr.sii.repository.TalkRepo;
import fr.sii.service.admin.user.AdminUserService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    public TalkAdmin edit(TalkAdmin talkAdmin) {
        Talk talk = talkRepo.findOne(talkAdmin.getId());
        mapper.map(talkAdmin, talk);
        return mapper.map(talk, TalkAdmin.class);
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
