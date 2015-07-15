package fr.sii.repository.admin.session;

import com.google.appengine.api.datastore.Key;
import fr.sii.domain.admin.session.AdminViewedSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by tmaugin on 15/07/2015.
 * SII
 */
public interface AdminViewedSessionRepository extends JpaRepository<AdminViewedSession, Key> {
    @Modifying
    @Query("delete from AdminViewedSession u where u.entityId = ?1")
    void _delete(Long id);
    List<AdminViewedSession> findByEntityId(Long id);
    List<AdminViewedSession> findByRowIdAndUserId(Long rowId, Long userId);
    List<AdminViewedSession> findByUserId(Long userId);
}
