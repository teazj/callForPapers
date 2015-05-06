package fr.sii.controller.comment;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import fr.sii.domain.comment.Comment;
import fr.sii.service.comment.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by tmaugin on 29/04/2015.
 */
@Controller
@RequestMapping(value="/comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    fr.sii.service.user.UserService userServiceCustom;

    @RequestMapping(method= RequestMethod.GET)
    @ResponseBody
    public List<Comment> getComments() {
        return commentService.findAll();
    }

    @RequestMapping(method= RequestMethod.DELETE)
    @ResponseBody
    public void deleteComments() {
        commentService.deleteAll();
    }

    @RequestMapping(method=RequestMethod.POST)
    @ResponseBody public Comment postComment(@Valid @RequestBody Comment comment) throws Exception {
        comment.setUserId(userServiceCustom.getCurrentUser().getEntityId());
        return commentService.save(comment);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    @ResponseBody public Comment putComment(@PathVariable Long id, @Valid @RequestBody Comment comment){
        comment.setUserId(userServiceCustom.getCurrentUser().getEntityId());
        return commentService.put(id, comment);
    }

    @RequestMapping(value="/{id}", method= RequestMethod.GET)
    @ResponseBody
    public Comment getComment(@PathVariable Long id) {
        return commentService.findOne(id);
    }

    @RequestMapping(value="/user/{id}", method= RequestMethod.GET)
    @ResponseBody
    public List<Comment> getCommentsByUserId(@PathVariable Long id) {
        return commentService.findByUserId(id);
    }

    @RequestMapping(value="/row/{rowId}", method= RequestMethod.GET)
    @ResponseBody
    public List<Comment> getCommentsByRowId(@PathVariable Long rowId ) {
        return commentService.findByRowId(rowId);
    }

    @RequestMapping(value="/{id}", method= RequestMethod.DELETE)
    @ResponseBody
    public void deleteComment(@PathVariable Long id) {
        commentService.delete(id);
    }
}