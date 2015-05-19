package fr.sii.repository.admin.rate;

import com.google.appengine.api.datastore.Key;
import fr.sii.domain.admin.rate.AdminRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminRateRespository extends JpaRepository<AdminRate, Key> {
    @Modifying
    @Query("delete from AdminRate u where u.entityId = ?1")
    void _delete(Long id);
    List<AdminRate> findByEntityId(Long id);
    List<AdminRate> findByUserId(Long id);
    List<AdminRate> findByRowIdOrderByRateDesc(Long id);
    List<AdminRate> findByRowIdAndUserId(Long rowId, Long userId);
}
