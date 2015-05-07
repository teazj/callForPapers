package fr.sii.service.user;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserServiceFactory;
import fr.sii.domain.user.User;
import fr.sii.repository.user.UserRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tmaugin on 24/04/2015.
 */
@Service
public class UserService {

    @Autowired
    private UserRespository userRespository;

    public List<User> findAll()
    {
        return userRespository.findAll();
    }

    public User getCurrentUser()
    {
        com.google.appengine.api.users.UserService userService = UserServiceFactory.getUserService();
        com.google.appengine.api.users.User user = userService.getCurrentUser();
        if(user == null)
        {
            return null;
        }
        String id = user.getUserId();
        String idParsed = id.substring(0, id.length() - 2);
        Long userId = Long.parseLong(idParsed);
        User userCustom = findOne(userId);

        if(userCustom == null)
        {
            userCustom = save(user);
        }
        return userCustom;
    }

    public void deleteAll()
    {
        userRespository.deleteAll();
    }

    public User save(User u)
    {
        User s = userRespository.save(u);
        return s;
    }

    public User save(com.google.appengine.api.users.User u)
    {
        String id = u.getUserId();
        String idParsed = id.substring(0, id.length() - 2);
        Long userId = Long.parseLong(idParsed);

        User user = new User(userId);
        user.setName(u.getNickname());
        user.setEmail(u.getEmail());

        User s = userRespository.save(user);
        return s;
    }

    public User put(Long id,User u)
    {
        User pu = findOne(id);
        if(pu != null)
        {
            u.setEntityId(pu.getEntityId());
            User s = userRespository.save(u);
            return s;
        }
        return null;
    }

    public void delete(Long id)
    {
        userRespository._delete(id);
    }

    public User findOne(Long id)
    {
        List<User> r = userRespository.findByEntityId(id);
        if(r.size() > 0)

            return r.get(0);
        else
            return null;
    }

    public List<User> findByemail(String email)
    {
        return userRespository.findByemail(email);
    }
}