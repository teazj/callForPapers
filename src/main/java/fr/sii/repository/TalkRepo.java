package fr.sii.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fr.sii.entity.Talk;

public interface TalkRepo extends JpaRepository<Talk, Integer> {
    List<Talk> findByUserIdAndStateIn(int userId, Collection<Talk.State> states);

    List<Talk> findByStateIn(Collection<Talk.State> states);

    int countByUserId(int userId);

    Talk findByIdAndUserId(int talkId, int userId);

    @Query("SELECT t FROM Talk t JOIN FETCH t.cospeakers c WHERE c.id = :userId")
    List<Talk> findByCospeakers(@Param("userId") int userId);
}
