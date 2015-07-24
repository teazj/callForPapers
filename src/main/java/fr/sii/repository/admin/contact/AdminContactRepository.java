package fr.sii.repository.admin.contact;

import com.google.appengine.api.datastore.Key;
import fr.sii.domain.admin.contact.AdminContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
/**
 * Created by tmaugin on 24/07/2015.
 */
public interface AdminContactRepository extends JpaRepository<AdminContact, Key> {
    @Modifying
    @Query("delete from AdminContact u where u.entityId = ?1")
    void _delete(Long id);
    List<AdminContact> findByEntityId(Long id);
    List<AdminContact> findByRowIdOrderByAddedDesc(Long id);
}
