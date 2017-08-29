package apside.apvigil.profile;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import apside.apvigil.article.ArticleService;
import apside.apvigil.category.Category;
import apside.apvigil.category.CategoryService;
import apside.apvigil.security.authentication.User;
import apside.apvigil.security.authentication.UserRepository;
import apside.apvigil.security.authentication.UserServiceImpl;

@Controller
public class ProfileController {

	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	UserRepository userRepository;
	
	
	@ModelAttribute("currentUser")
	public User getUser() {
		User user = getCurrentUser();
		return user;
	}
	
	
	@ModelAttribute("categories")
	public List<Category> populateCategories() {
		return categoryService.findAllCategories();
	}
	
	@GetMapping("/profile/{userId}")
	public String showProfile(@PathVariable("userId") long id, Model model) {
		User user = getCurrentUser();
		if (id != user.getId()) {
			return "redirect:/articles";
		}
		List<Category> categories = categoryService.findAllCategories();
		List<Category> userCategories = user.getCategories();
		List<Category> availableCategories = new ArrayList<>();
		for(Category category : categories) {
			if(!userCategories.contains(category)) {
				availableCategories.add(category);
			}
		}
		model.addAttribute("availableCategories", availableCategories);
		model.addAttribute("articles", articleService.getAllArticlesByUser(id));
		return "profile/profile";
	}
	
	@GetMapping("/profile/dashboard")
	public String showDashboard(Model model) {
		User user = getCurrentUser();
		List<Category> favoriteCategories = user.getCategories();
		user.setNotifications(0);
		user.setLastVisit();
		userRepository.save(user);
		model.addAttribute("favoriteCategories", favoriteCategories);
		return "profile/dashboard";
	}
	
	@PostMapping("/profile/{userId}/favorites/{categoryId}")
	public String addFavoriteCategory(@PathVariable("userId") long id, @PathVariable("categoryId") long categoryId ,Model model) {
		User user = getCurrentUser();
		if (id != user.getId()) {
			return "redirect:/articles";
		}
		Category category = categoryService.findOne(categoryId);
		userService.saveFavoriteCategory(category, user);
		return "redirect:/profile/" + id;
	}
	
	@PostMapping("/profile/{userId}/unfavorites/{categoryId}")
	public String removeFavoriteCategory(@PathVariable("userId") long id, @PathVariable("categoryId") long categoryId ,Model model) {
		User user = getCurrentUser();
		if (id != user.getId()) {
			return "redirect:/articles";
		}
		Category category = categoryService.findOne(categoryId);
		userService.removeFavoriteCategory(category, user);
		return "redirect:/profile/" + id;
	}
	
	private User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		return user;
	}
}
