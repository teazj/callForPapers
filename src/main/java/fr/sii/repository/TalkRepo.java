package fr.sii.repository;

import fr.sii.entity.Talk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TalkRepo extends JpaRepository<Talk, Integer> {
    List<Talk> findByUserIdAndStateIn(int userId, List<Talk.State> states);

    Talk findByIdAndUserId(int talkId, int userId);
}
