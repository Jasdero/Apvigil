package apside.apvigil.security.authentication;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface UserRepository extends CrudRepository<User, Long>{

	User findByEmail(String email);
}
