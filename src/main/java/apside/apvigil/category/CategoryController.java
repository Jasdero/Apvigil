package apside.apvigil.category;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import apside.apvigil.security.authentication.User;
import apside.apvigil.security.authentication.UserServiceImpl;

@Controller
public class CategoryController {
	
	private final String CREATE_CATEGORY_FORM = "categories/categoryForm";
	
	@ModelAttribute("user")
	public User getUser() {
		User user = getCurrentUser();
		return user;
	}
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	CategoryService categoryService;
	
	@GetMapping("/categories")
	public String showAll(Model model) {
		List<Category> categories = categoryService.getAllCategories();
		model.addAttribute("categories", categories);
		return "categories/listCategories";
	}
	
	
	@GetMapping("/categories/new")
	public String showForm(Model model) {
		Category category = new Category();
		model.addAttribute(category);
		return CREATE_CATEGORY_FORM;
	}
	
	@PostMapping("/categories/new")
	public String processForm(@Valid Category category, BindingResult result) {
		if(result.hasErrors()) {
			return CREATE_CATEGORY_FORM;
		}
		categoryService.saveCategory(category);
		return "redirect:/categories";
	}
	
	private User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		return user;
	}

}
