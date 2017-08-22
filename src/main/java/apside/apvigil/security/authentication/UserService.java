package apside.apvigil.security.authentication;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface UserService {
	
	public User findUserByEmail(String email);
	public void saveUser(User user);
	public List<User> findAll();
}
