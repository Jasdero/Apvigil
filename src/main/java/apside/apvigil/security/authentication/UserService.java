package apside.apvigil.security.authentication;




public interface UserService {
	
	public User findUserByEmail(String email);
	public void saveUser(User user);
}
