package fr.sii.repository;

import java.util.List;

import fr.sii.entity.Talk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import fr.sii.entity.User;

public interface UserRepo extends JpaRepository<User, Integer> {

    List<User> findByEmail(String email);

    List<User> findByGoogleId(String providerId);

    List<User> findByGithubId(String providerId);

    List<User> findByVerifyToken(String verifyToken);

}
