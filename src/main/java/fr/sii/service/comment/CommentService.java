package fr.sii.service.comment;

import com.google.appengine.api.datastore.KeyFactory;
import fr.sii.domain.comment.Comment;
import fr.sii.domain.user.User;
import fr.sii.repository.comment.CommentRepository;
import fr.sii.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tmaugin on 29/04/2015.
 */
@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserService userService;

    public List<Comment> matchUsers(List<Comment> rs)
    {
        List<Comment> nrs = new ArrayList<>();
        for (Comment r : rs)
        {
            User u = userService.findOne(r.getUserId());
            r.setUser(u);
            nrs.add(r);
        }
        return nrs;
    }
    public Comment matchUser(List<Comment> rs)
    {
        List<Comment> nrs = new ArrayList<>();
        if(rs.size() > 0)
        {
            Comment r = rs.get(0);
            User u = userService.findOne(r.getUserId());
            r.setUser(u);
            return r;
        }
        return null;
    }

    public Comment matchUser(Comment r)
    {
        if(r != null)
        {
            User u = userService.findOne(r.getUserId());
            r.setUser(u);
            return r;
        }
        return null;
    }

    public List<Comment> findAll()
    {
        return matchUsers(commentRepository.findAll());
    }

    public void deleteAll()
    {
        commentRepository.deleteAll();
    }

    public Comment save(Comment r)
    {
        if(r.getAdded() == null)
        {
            r.setAdded(new Date());
        }
        Comment s = commentRepository.save(r);
        s.setEntityId(KeyFactory.stringToKey(s.getId()).getId());
        Comment s2 = commentRepository.save(s);
        return matchUser(s2);
    }

    public Comment put(Long id,Comment r)
    {
        Comment pr = findOne(id);
        if(pr != null)
        {
            if(r.getAdded() == null)
            {
                r.setAdded(new Date());
            }
            commentRepository._delete(id);
            r.setEntityId(id);
            return matchUser(commentRepository.save(r));
        }
        return null;
    }

    public void delete(Long id)
    {
        commentRepository._delete(id);
    }

    public Comment findOne(Long id)
    {
        List<Comment> rs = commentRepository.findByEntityId(id);
        return matchUser(rs);
    }

    public List<Comment> findByUserId(Long id)
    {
        return matchUsers(commentRepository.findByUserId(id));
    }

    public List<Comment> findByRowId(Long rowId)
    {
        return matchUsers(commentRepository.findByRowIdOrderByAddedAsc(rowId));
    }
}