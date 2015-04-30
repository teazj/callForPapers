package fr.sii.repository.rate;

import com.google.appengine.api.datastore.Key;
import fr.sii.domain.rate.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface RateRespository extends JpaRepository<Rate, Key> {
    @Modifying
    @Query("delete from Rate u where u.entityId = ?1")
    void _delete(Long id);
    List<Rate> findByEntityId(Long id);
    List<Rate> findByUserId(Long id);
    List<Rate> findByRowIdOrderByRateDesc(Long id);
    List<Rate> findByRowIdAndUserId(Long rowId, Long userId);
}
