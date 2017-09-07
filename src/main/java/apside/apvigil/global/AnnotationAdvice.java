package apside.apvigil.global;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import apside.apvigil.category.CategoryService;
import apside.apvigil.security.authentication.User;
import apside.apvigil.security.authentication.UserService;

@ControllerAdvice
public class AnnotationAdvice {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CategoryService categoryService;
	

	@ModelAttribute("currentUser")
	public User getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		return user;
	}
	
	@ModelAttribute("moderationActions")
	public Long countCategoriestoModerate() {
		return categoryService.countByActivated(false);
	}
	
}
