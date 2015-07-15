package fr.sii.service.admin.session;

import fr.sii.domain.admin.session.AdminViewedSession;
import fr.sii.domain.exception.NotFoundException;
import fr.sii.repository.admin.session.AdminViewedSessionRepository;

import java.util.List;

/**
 * Created by tmaugin on 15/07/2015.
 * SII
 */
public class AdminViewedSessionService {

    private AdminViewedSessionRepository adminViewedSessionRepository;

    public void setAdminViewedSessionRepository(AdminViewedSessionRepository adminViewedSessionRepository) {
        this.adminViewedSessionRepository = adminViewedSessionRepository;
    }

    public List<AdminViewedSession> findAll()
    {
        return adminViewedSessionRepository.findAll();
    }

    public void deleteAll()
    {
        adminViewedSessionRepository.deleteAll();
    }

    public AdminViewedSession toOne(List<AdminViewedSession> list) throws NotFoundException {
        if(list != null && list.size() > 0) {
            return list.get(0);
        }
        throw new NotFoundException("");
    }

    public AdminViewedSession save(AdminViewedSession r) {
        return adminViewedSessionRepository.save(r);
    }

    public AdminViewedSession put(Long rowId, Long userId, AdminViewedSession r) {
        try {
            AdminViewedSession current = findByRowIdAndUserId(rowId, userId);
            r.setEntityId(current.getEntityId());
            delete(current.getEntityId());
        } catch (NotFoundException e) {
        }
        return adminViewedSessionRepository.save(r);
    }

    public void delete(Long id) throws NotFoundException {
        findOne(id);
        adminViewedSessionRepository._delete(id);
    }

    public AdminViewedSession findOne(Long id) throws NotFoundException {
        List<AdminViewedSession> sessions = adminViewedSessionRepository.findByEntityId(id);
        if(sessions.size() == 0) {
            throw new NotFoundException("");
        }
        return sessions.get(0);
    }

    public List<AdminViewedSession> findByUserId(Long id)
    {
        return adminViewedSessionRepository.findByUserId(id);
    }

    public AdminViewedSession findByRowIdAndUserId(Long rowId, Long userId) throws NotFoundException {
        return toOne(adminViewedSessionRepository.findByRowIdAndUserId(rowId, userId));
    }
}
