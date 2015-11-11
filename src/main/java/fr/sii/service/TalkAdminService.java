package fr.sii.service;

import fr.sii.dto.TalkAdmin;
import fr.sii.entity.Talk;
import fr.sii.repository.TalkRepo;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * Retrieve a talk
     * @param talkId Id of the talk to retrieve
     * @return Talk or null if not found
     */
    public TalkAdmin getOne(int talkId) {
        Talk talk = talkRepo.findOne(talkId);
        return mapper.map(talk, TalkAdmin.class);
    }
}
