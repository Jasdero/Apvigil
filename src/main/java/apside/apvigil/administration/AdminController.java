package apside.apvigil.administration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import apside.apvigil.security.authentication.User;
import apside.apvigil.security.authentication.UserServiceImpl;

@Controller
public class AdminController {

	@Autowired
	UserServiceImpl userService;
	
	
	@ModelAttribute("currentUser")
	public User getUser() {
		User user = getCurrentUser();
		return user;
	}
	
	@GetMapping("users")
	public String listUsers(Model model) {
		model.addAttribute("users", userService.findAll());
		return "users/listUsers";
	}
	
	private User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		return user;
	}
}
