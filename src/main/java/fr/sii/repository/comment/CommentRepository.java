package fr.sii.repository.comment;

import com.google.appengine.api.datastore.Key;
import fr.sii.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
/**
 * Created by tmaugin on 29/04/2015.
 */
public interface CommentRepository extends JpaRepository<Comment, Key> {
    @Modifying
    @Query("delete from Comment u where u.entityId = ?1")
    void _delete(Long id);
    List<Comment> findByEntityId(Long id);
    List<Comment> findByUserId(Long id);
    List<Comment> findByRowIdOrderByAddedAsc(Long id);
    List<Comment> findByUserIdAndRowId(Long userId, Long rowId);
}
