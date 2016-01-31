package fr.sii.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.sii.entity.TalkFormat;
import fr.sii.entity.Track;

/**
 * Created by lhuet on 24/11/15.
 */
public interface TrackRepo extends JpaRepository<Track, Integer> {

}
