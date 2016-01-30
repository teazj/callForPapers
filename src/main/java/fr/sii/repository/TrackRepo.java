package fr.sii.repository;

import fr.sii.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by lhuet on 24/11/15.
 */
public interface TrackRepo extends JpaRepository<Track, Integer> {

}
