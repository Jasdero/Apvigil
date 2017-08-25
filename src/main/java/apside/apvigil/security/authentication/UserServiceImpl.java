package apside.apvigil.security.authentication;


import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import apside.apvigil.article.ArticleService;
import apside.apvigil.category.Category;
import apside.apvigil.category.CategoryRepository;


@Service
public class UserServiceImpl implements UserService{

	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	@Override
	public List<User> findAll(){
		return userRepository.findAll();
	}
	
	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public User findUser(Long id) {
		return userRepository.findOne(id);
	}

	@Override
	public void saveUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setActive(1);
		Role userRole = roleRepository.findByRole("ROLE_USER");
		user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		userRepository.save(user);
		
	}
	
	public void saveFavoriteCategory(Category category, User user) {
		Set<Category> categories = user.getCategories();
		categories.add(category);
		user.setCategories(categories);
		categoryRepository.save(category);
	}
	
	public void removeFavoriteCategory(Category category, User user) {
		Set<Category> categories = user.getCategories();
		categories.remove(category);
		user.setCategories(categories);
		categoryRepository.save(category);
	}
	
	public void setNumberOfNotications(User user) {
		Set<Category> categories = user.getCategories();
		int result = 0;
		Date lastVisit = user.getLastVisit();
		for (Category category : categories) {
			long id = category.getId();
			result += articleService.countByCreatedOnAfterAndCategoryId(lastVisit, id);
		}
		user.setNotifications(result);
		userRepository.save(user);
	}
}
