package fr.sii.repository.admin.user;

import com.google.appengine.api.datastore.Key;
import fr.sii.domain.admin.user.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminUserRespository extends JpaRepository<AdminUser, Key> {
    List<AdminUser> findByname(String name);
    List<AdminUser> findByemail(String email);

    @Modifying
    @Query("delete from AdminRate u where u.entityId = ?1")
    void _delete(Long id);
    List<AdminUser> findByEntityId(Long id);
}
