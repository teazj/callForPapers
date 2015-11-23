package fr.sii.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.sii.dto.TalkUser;
import fr.sii.entity.Talk;
import fr.sii.entity.User;
import fr.sii.repository.TalkRepo;
import fr.sii.repository.UserRepo;
import ma.glasnost.orika.MapperFacade;

/**
 * Service for managing talks by the user
 */
@Service
@Transactional
public class TalkUserService {

    @Autowired
    private TalkRepo talkRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MapperFacade mapper;

    /**
     * Retrieve all talks for a User
     * @param userId Id of the user
     * @param states List of states the talk must be
     * @return List of talks
     */
    public List<TalkUser> findAll(int userId, Talk.State... states) {
        List<Talk> talks = talkRepo.findByUserIdAndStateIn(userId, Arrays.asList(states));
        return mapper.mapAsList(talks, TalkUser.class);
    }

    /**
     * Count number of talks the users has submitted (drafts included)
     * @param userId Id of the user
     * @return Number of talks
     */
    public int count(int userId) {
        return talkRepo.countByUserId(userId);
    }

    /**
     * Retrieve a talk which belong to the user
     * @param userId Id of the user to retrieve
     * @param talkId Id of the talk to retrieve
     * @return Talk or null if not found
     */
    public TalkUser getOne(int userId, int talkId) {
        Talk talk = talkRepo.findByIdAndUserId(talkId, userId);
        return mapper.map(talk, TalkUser.class);
    }

    /**
     * Add a submitted talk
     * @param user user who submitted the talk
     * @param talkUser Talk to add
     * @return Talk added
     */
    public TalkUser submitTalk(User user, TalkUser talkUser) {
        return newTalk(user, talkUser, Talk.State.CONFIRMED);
    }

    /**
     * Convert a draft into a submitted talk
     * @param userId Id of user who submitted the talk
     * @param talkUser Talk to submit
     * @return Talk submitted or null if not exists
     */
    public TalkUser submitDraftToTalk(int userId, TalkUser talkUser) {
        return editTalk(userId, talkUser, Talk.State.CONFIRMED);
    }

    /**
     * Add a new draft talk
     * @param userId User who submitted the talk
     * @param talkUser Talk to save
     * @return talk saved
     */
    public TalkUser addDraft(int userId, TalkUser talkUser) {
        return newTalk(userRepo.getOne(userId), talkUser, Talk.State.DRAFT);
    }

    /**
     * Save an update draft
     * @param userId User who submitted the talk
     * @param talkUser Draft to update
     * @return Draft updated or null if doesn't exists
     */
    public TalkUser editDraft(int userId, TalkUser talkUser) {
        return editTalk(userId, talkUser, Talk.State.DRAFT);
    }

    /**
     * Delete a draft talk
     * @param userId Id of the user who submit the talk
     * @param talkId Id of the draft to delete
     * @return Talk deleted or null if inexistant
     */
    public TalkUser deleteDraft(int userId, int talkId) {
        Talk talk = talkRepo.findByIdAndUserId(talkId, userId);

        if (talk.getState() != Talk.State.DRAFT) return null;
        TalkUser deleted = mapper.map(talk, TalkUser.class);
        talkRepo.delete(talk);
        return deleted;
    }

    /**
     * Add a new talk into the database
     * @param user User who submit the talk
     * @param talkUser Talk to add
     * @param state New state of the talk
     * @return Talk added
     */
    private TalkUser newTalk(User user, TalkUser talkUser, Talk.State state) {
        Talk talk = mapper.map(talkUser, Talk.class);

        talk.setAdded(new Date());
        talk.setUser(user);
        talk.setState(state);

        Talk save = talkRepo.save(talk);
        talkRepo.flush();
        return mapper.map(save, TalkUser.class);
    }

    /**
     * Update an existing draft talk
     * @param userId Id of the user who submit the talk
     * @param talkUser Talk to update
     * @param newState New state of the talk
     * @return Talk updated or null if not existing
     */
    private TalkUser editTalk(int userId, TalkUser talkUser, Talk.State newState) {
        Talk talk = talkRepo.findByIdAndUserId(talkUser.getId(), userId);
        if (talk == null) return null;
        if (talk.getState() != Talk.State.DRAFT) return null;
        talkUser.setState(newState);

        mapper.map(talkUser, talk);
        talkRepo.flush();

        return mapper.map(talk, TalkUser.class);
    }
}
