package fr.sii.service.user;

import fr.sii.entity.User;
import fr.sii.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
	private UserRepo userRepo;

	public User save(User user) {
		return userRepo.save(user);
	}

	public void delete(Integer id) {
		userRepo.delete(id);
	}

	public List<User> findAll() {
		return userRepo.findAll();
	}

	public User findById(Integer id) {
		return userRepo.findOne(id);
	}

	public User findByemail(String email) {
		List<User> users = userRepo.findByEmail(email);
		if (!users.isEmpty())

			return users.get(0);
		else
			return null;
	}

	public User findByVerifyToken(String verifyToken) {
		List<User> users = userRepo.findByVerifyToken(verifyToken);
		if (!users.isEmpty())

			return users.get(0);
		else
			return null;
	}

	public User findByProvider(User.Provider provider, String providerId) {
		switch (provider) {
			case GOOGLE:
				List<User> users = userRepo.findByGoogleId(providerId);
				if (!users.isEmpty())
					return users.get(0);
				else
					return null;
			case GITHUB:
				List<User> users2 = userRepo.findByGithubId(providerId);
				if (!users2.isEmpty())
					return users2.get(0);
				else
					return null;
			default:
				throw new IllegalArgumentException();
		}
	}
}