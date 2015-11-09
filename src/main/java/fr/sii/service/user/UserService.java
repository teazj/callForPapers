package fr.sii.service.user;

import fr.sii.entity.User;
import fr.sii.repository.UserRepo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class UserService {

	private UserRepo userRepo;

	public void setUserRepo(UserRepo userRepo) {
		this.userRepo = userRepo;
	}

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
				List<User> users = userRepo.findByGoogle(providerId);
				if (!users.isEmpty())
					return users.get(0);
				else
					return null;
			case GITHUB:
				List<User> users2 = userRepo.findByGithub(providerId);
				if (!users2.isEmpty())
					return users2.get(0);
				else
					return null;
			default:
				throw new IllegalArgumentException();
		}
	}
}