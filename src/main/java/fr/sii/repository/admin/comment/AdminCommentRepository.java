package fr.sii.repository.admin.comment;

import com.google.appengine.api.datastore.Key;
import fr.sii.domain.admin.comment.AdminComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
/**
 * Created by tmaugin on 29/04/2015.
 */
public interface AdminCommentRepository extends JpaRepository<AdminComment, Key> {
    @Modifying
    @Query("delete from AdminComment u where u.entityId = ?1")
    void _delete(Long id);
    List<AdminComment> findByEntityId(Long id);
    List<AdminComment> findByUserId(Long id);
    List<AdminComment> findByRowIdOrderByAddedAsc(Long id);
    List<AdminComment> findByUserIdAndRowId(Long userId, Long rowId);
}
