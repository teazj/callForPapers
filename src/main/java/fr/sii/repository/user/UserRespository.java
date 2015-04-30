package fr.sii.repository.user;

import java.util.List;
import com.google.appengine.api.datastore.Key;
import fr.sii.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRespository extends JpaRepository<User, Key> {
    List<User> findByname(String name);
    List<User> findByemail(String email);

    @Modifying
    @Query("delete from Rate u where u.entityId = ?1")
    void _delete(Long id);
    List<User> findByEntityId(Long id);
}
