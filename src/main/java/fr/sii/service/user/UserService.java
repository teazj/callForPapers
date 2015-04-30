package fr.sii.service.user;

import com.google.appengine.api.datastore.KeyFactory;
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

    public void deleteAll()
    {
        userRespository.deleteAll();
    }

    public User save(User u)
    {
        User s = userRespository.save(u);
        s.setEntityId(KeyFactory.stringToKey(s.getId()).getId());
        User s2 = userRespository.save(s);
        return s2;
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