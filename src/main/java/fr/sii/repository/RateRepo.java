package fr.sii.repository;

import fr.sii.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RateRepo extends JpaRepository<Rate, Integer> {

    List<Rate> findByTalkUserId(int userId);

    List<Rate> findByTalkId(int talkId);

    List<Rate> findByTalkIdAndAdminUserId(int talkId, int adminId);
}
