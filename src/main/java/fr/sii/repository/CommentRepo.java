package fr.sii.repository;

import fr.sii.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepo extends JpaRepository<Comment, Integer> {

    Comment findByIdAndTalkIdAndTalkUserId(int commentId, int talkId, int userId);

    List<Comment> findByTalkIdAndTalkUserId(int talkId, int userId);

}
