package fr.sii.persistance.user;

/**
 * Created by tmaugin on 07/04/2015.
 */
import java.util.ArrayList;
import java.util.List;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import fr.sii.persistance.configuration.ObjectifyFactoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class UserRepository{
    //@Autowired
//    private ObjectifyFactoryFactoryBean objectifyFactoryFactoryBean;
//    private ObjectifyFactory objectifyFactory;

    //@Autowired
    public void init() throws Exception {
//        objectifyFactory =  objectifyFactoryFactoryBean.getObject();
    }

    public List<User> list() {
//        Objectify ofy = objectifyFactory.begin();
//        List<User> persons = ofy.load().type(User.class).list();
        return new ArrayList<>();// persons;
    }

    public User save(User u) {
//        Objectify ofy = objectifyFactory.begin();
//        ofy.save().entity(u).now();
        return u;
    }
}