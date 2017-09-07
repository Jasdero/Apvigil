package apside.apvigil.administration;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import apside.apvigil.security.authentication.Role;
import apside.apvigil.security.authentication.RoleRepository;
import apside.apvigil.security.authentication.User;
import apside.apvigil.security.authentication.UserRepository;
import apside.apvigil.security.authentication.UserServiceImpl;

@Controller
public class AdminController {

	@Autowired
	UserServiceImpl userServiceImpl;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	
	
	@GetMapping("users")
	public String listUsers(Model model) {
		model.addAttribute("users", userServiceImpl.findAll());
		return "users/listUsers";
	}
	
	@PostMapping("/users/{userId}/promote")
	public String promoteUser(@PathVariable("userId") long id) {
		User user = userServiceImpl.findUser(id);
		Role userRole = roleRepository.findByRole("ROLE_ADMIN");
		user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		userRepository.save(user);
		return "redirect:/users";
	}
	
	@PostMapping("/users/{userId}/demote")
	public String demoteUser(@PathVariable("userId") long id) {
		User user = userServiceImpl.findUser(id);
		Role userRole = roleRepository.findByRole("ROLE_USER");
		user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		userRepository.save(user);
		return "redirect:/users";
	}
	
}
