package fr.sii.repository.user;

import java.util.List;
import fr.sii.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface UserRespository extends JpaRepository<User, Long> {
    List<User> findByname(String name);
    List<User> findByemail(String email);

    @Modifying
    @Query("delete from User u where u.id = ?1")
    void _delete(Long id);
}
