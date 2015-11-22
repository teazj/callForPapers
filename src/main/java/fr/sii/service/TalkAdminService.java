package fr.sii.service;

import fr.sii.dto.TalkAdmin;
import fr.sii.dto.TalkUser;
import fr.sii.dto.user.UserProfil;
import fr.sii.entity.Talk;
import fr.sii.repository.TalkRepo;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * Service for managing talks by the admins
 */
@Service
@Transactional
public class TalkAdminService {

    @Autowired
    private TalkRepo talkRepo;

    @Autowired
    private MapperFacade mapper;

    /**
     * Retrieve all talks
     * @param states List of states the talk must be
     * @return List of talks
     */
    public List<TalkAdmin> findAll(Talk.State... states) {
        List<Talk> talks = talkRepo.findByStateIn(Arrays.asList(states));
        return mapper.mapAsList(talks, TalkAdmin.class);
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
        user.setSocialProfilImageUrl(talk.getUser().getImageSocialUrl());
        talkAdmin.setSpeaker(user);
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
