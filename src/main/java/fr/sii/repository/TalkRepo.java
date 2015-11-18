package fr.sii.repository;

import fr.sii.entity.Talk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface TalkRepo extends JpaRepository<Talk, Integer> {
    List<Talk> findByUserIdAndStateIn(int userId, Collection<Talk.State> states);

    List<Talk> findByStateIn(Collection<Talk.State> states);

    int countByUserId(int userId);

    Talk findByIdAndUserId(int talkId, int userId);
}
