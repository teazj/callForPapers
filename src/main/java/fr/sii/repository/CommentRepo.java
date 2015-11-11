package fr.sii.repository;

import fr.sii.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepo extends JpaRepository<Comment, Integer> {

    /**
     * Retrieve a comment for the user which are visible to user
     * @param commentId Id of the comment to retrieve
     * @param talkId Id of the talk to retrieve
     * @param userId Id of the connected user
     * @return Comment or null if not found
     */
    Comment findByIdAndTalkIdAndTalkUserIdAndInternalIsFalse(int commentId, int talkId, int userId);

    /**
     * Retrieve all comments for the user which are visible to user
     * @param talkId Id of the talk to retrieve all comment
     * @param userId Id of the connected user
     * @return All visible comments for the user
     */
    List<Comment> findByTalkIdAndTalkUserIdAndInternalIsFalse(int talkId, int userId);

    /**
     * Retrieve all comments which are visible to the user
     * @param talkId Id of the talk to retrieve all comment
     * @return All visible comments for the user
     */
    List<Comment> findByTalkIdAndInternalIsFalse(int talkId);
}
