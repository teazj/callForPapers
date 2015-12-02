package fr.sii.repository;

import fr.sii.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Integer> {

    List<User> findByEmail(String email);
    User findById(int userId);

    List<User> findByGoogleId(String providerId);

    List<User> findByGithubId(String providerId);

    List<User> findByVerifyToken(String verifyToken);

}
