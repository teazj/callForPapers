package fr.sii.service.user;

import fr.sii.domain.user.User;
import fr.sii.repository.user.UserRespository;

import java.util.List;

/**
 * Created by tmaugin on 15/05/2015.
 */
public class UserService {

    private UserRespository userRespository;

    public void setUserRespository(UserRespository userRespository) {
        this.userRespository = userRespository;
    }

    public User save(User user)
        {
            User s = userRespository.save(user);
            return s;
        }

        public User put(Long id,User user)
        {
            User userCopy = (User) user.clone(); // Avoid different persistence manager
            User persistedUser = findById(id);
            if(persistedUser != null)
            {
                userRespository._delete(id);
                userCopy.setEntityId(persistedUser.getEntityId());
                return userRespository.save(userCopy);
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
            List<User> users = userRespository.findByEntityId(id);
            if(!users.isEmpty())

                return users.get(0);
            else
                return null;
        }

        public User findByemail(String email)
        {
            List<User> users = userRespository.findByEmail(email);
            if(!users.isEmpty())

                return users.get(0);
            else
                return null;
        }

        public User findByVerifyToken(String verifyToken)
        {
            List<User> users = userRespository.findByVerifyToken(verifyToken);
            if(!users.isEmpty())

                return users.get(0);
            else
                return null;
        }

        public User findByProvider(User.Provider provider, String providerId)
        {
            switch (provider) {
                case GOOGLE:
                    List<User> users = userRespository.findByGoogle(providerId);
                    if(!users.isEmpty())
                        return users.get(0);
                    else
                        return null;
                case GITHUB:
                    List<User> users2 = userRespository.findByGithub(providerId);
                    if(!users2.isEmpty())
                        return users2.get(0);
                    else
                        return null;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }