package fr.sii.service.user;

import fr.sii.domain.user.User;
import fr.sii.repository.user.UserRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        return s;
    }

    public User put(Long id,User u)
    {
        if(findOne(id) != null)
        {
            u.setId(id);
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
        return userRespository.findOne(id);
    }

    public List<User> findByemail(String email)
    {
        return userRespository.findByemail(email);
    }
}