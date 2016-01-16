package fr.sii.repository;

import fr.sii.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface RateRepo extends JpaRepository<Rate, Integer> {

    List<Rate> findByTalkUserId(int userId);

    List<Rate> findByTalkId(int talkId);

    Rate findByTalkIdAndAdminUserId(int talkId, int adminId);

    @Query("SELECT r FROM Rate r JOIN FETCH r.adminUser")
    List<Rate> findAllFetchAdmin();
}
