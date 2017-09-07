package apside.apvigil.category;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import apside.apvigil.global.ApvigilController;
import apside.apvigil.security.authentication.Role;
import apside.apvigil.security.authentication.RoleRepository;
import apside.apvigil.security.authentication.User;

@Controller
public class CategoryController extends ApvigilController{
	
	private final String CREATE_CATEGORY_FORM = "categories/categoryForm";
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	CategoryService categoryService;
	
	@GetMapping("/categories")
	public String showAll(Model model) {
		model.addAttribute("categories", categoryService.findAllCategories());
		return "categories/listCategories";
	}
	
	@GetMapping("/categories/new")
	public String showForm(Model model) {
		Category category = new Category();
		model.addAttribute(category);
		return CREATE_CATEGORY_FORM;
	}
	
	@PostMapping("/categories/new")
	public String processForm(@Valid Category category, BindingResult result, RedirectAttributes redirectAttributes) {
		User user = getCurrentUser();
		Category categoryExists = categoryService.findOneByName(category.getName());
		Role roleAdmin = roleRepository.findByRole("ROLE_ADMIN");
		Role superAdmin = roleRepository.findByRole("ROLE_SUPERADMIN");
		Role roleUser = roleRepository.findByRole("ROLE_USER");
		if (categoryExists != null) {
			result.rejectValue("name", "category.error", "This category already exists");;
		}
		
		if(result.hasErrors()) {
			return CREATE_CATEGORY_FORM;
		}
		
		if (user.getRoles().contains(roleAdmin) || user.getRoles().contains(superAdmin)) {
			category.setActivated(true);
		}
		
		if(user.getRoles().contains(roleUser)) {
			redirectAttributes.addFlashAttribute("successMessage", "Your demand has been taken into account and will be processed soon");
		}
		
		categoryService.saveCategory(category);
		return "redirect:/categories";
	}
	
	@PreAuthorize("!hasRole('ROLE_USER')")
	@GetMapping("/categories/moderate")
	public String moderateCategories(Model model) {
		model.addAttribute("categories", categoryService.findByActivated(false));
		return "categories/moderateCategories";
	}
	
	@PreAuthorize("!hasRole('ROLE_USER')")
	@PostMapping("/categories/{categoryId}/moderate")
	public String activateCategory(@PathVariable("categoryId") long id) {
		Category category = categoryService.findOne(id);
		category.setActivated(true);
		categoryService.saveCategory(category);
		return "redirect:/categories/moderate";
	}
	
	@PreAuthorize("!hasRole('ROLE_USER')")
	@PostMapping("/categories/{categoryId}/delete")
	public String deleteCategory(@PathVariable("categoryId") long id) {
		categoryService.deleteCategory(id);
		return "redirect:/categories/moderate";
	}
	

}
