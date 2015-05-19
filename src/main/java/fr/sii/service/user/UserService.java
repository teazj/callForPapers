package fr.sii.service.user;

import fr.sii.domain.user.User;
import fr.sii.repository.user.UserRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tmaugin on 15/05/2015.
 */
@Service
public class UserService {

        @Autowired
        private UserRespository userRespository;

        public User save(User u)
        {
            User s = userRespository.save(u);
            return s;
        }

        public User put(Long id,User u)
        {
            User uCopy = (User) u.clone(); // Avoid different persistence manager
            User pu = findById(id);
            if(pu != null)
            {
                userRespository._delete(id);
                uCopy.setEntityId(pu.getEntityId());
                return userRespository.save(uCopy);
            }
            return null;
        }

        public void delete(Long id)
        {
            userRespository._delete(id);
        }

        public List<User> findAll()
        {
            return userRespository.findAll();
        }

        public User findById(Long id)
        {
            List<User> r = userRespository.findByEntityId(id);
            if(r.size() > 0)

                return r.get(0);
            else
                return null;
        }

        public User findByemail(String email)
        {
            List<User> r = userRespository.findByEmail(email);
            if(r.size() > 0)

                return r.get(0);
            else
                return null;
        }

        public User findByVerifyToken(String verifyToken)
        {
            List<User> r = userRespository.findByVerifyToken(verifyToken);
            if(r.size() > 0)

                return r.get(0);
            else
                return null;
        }

        public User findByProvider(User.Provider provider, String providerId)
        {
            switch (provider) {
                case GOOGLE:
                    List<User> r = userRespository.findByGoogle(providerId);
                    if(r.size() > 0)
                        return r.get(0);
                    else
                        return null;
                case GITHUB:
                    List<User> r2 = userRespository.findByGithub(providerId);
                    if(r2.size() > 0)
                        return r2.get(0);
                    else
                        return null;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }