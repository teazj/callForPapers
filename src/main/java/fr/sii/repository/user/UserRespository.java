package fr.sii.repository.user;

import com.google.appengine.api.datastore.Key;
import fr.sii.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by tmaugin on 15/05/2015.
 */
public interface UserRespository extends JpaRepository<User, Key> {
    public List<User> findByEntityId(Long id);

    public List<User> findByEmail(String email);

    public List<User> findByGoogle(String providerId);

    public List<User> findByGithub(String providerId);

    public List<User> findByVerifyToken(String verifyToken);

    @Modifying
    @Query("delete from User u where u.entityId = ?1")
    void _delete(Long id);
}
