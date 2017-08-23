package apside.apvigil.article;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import apside.apvigil.category.Category;
import apside.apvigil.category.CategoryService;
import apside.apvigil.comment.Comment;
import apside.apvigil.comment.CommentService;
import apside.apvigil.rating.Rating;
import apside.apvigil.rating.RatingService;
import apside.apvigil.security.authentication.User;
import apside.apvigil.security.authentication.UserServiceImpl;

@Controller
public class ArticleController {

	private static final String CREATE_ARTICLE_FORM = "articles/articleForm";
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private RatingService ratingService;
	
	@Autowired 
	private CommentService commentService;
	
	@Autowired
	private CategoryService categoryService;
	
	@ModelAttribute("currentUser")
	public User getUser() {
		User user = getCurrentUser();
		return user;
	}
	
	@ModelAttribute("categories")
	public List<Category> populateCategories(){
		return categoryService.findAllCategories();
	}
	
	
	@GetMapping("/articles")
	public ModelAndView listArticles() {
		ModelAndView mav = new ModelAndView("articles/listArticles");
		mav.addObject("articles", articleService.getAllArticlesSorted());
		return mav;
	}
	
	@GetMapping("/articles/categories/{categoryId}")
	public String getArticlesByCategory(Model model, @PathVariable("categoryId") long id) {
		model.addAttribute("articles", articleService.getAllArticlesByCategory(id));
		return "articles/listArticles";
	}
	
	@GetMapping("/articles/new")
	public String showForm(Model model) {
		Article article = new Article();
		model.addAttribute(article);
		return CREATE_ARTICLE_FORM;
	}
	
	@PostMapping("/articles/new")
	public String processForm(@Valid Article article, BindingResult result) {
		if(result.hasErrors()) {
			return CREATE_ARTICLE_FORM;
		}
		User user = getCurrentUser();
		Rating rating = new Rating();
		articleService.addArticle(article, user, rating);
		return "redirect:/articles";
	}
	
	@GetMapping("/articles/{articleId}")
	public String showArticle(@PathVariable("articleId") long id, Model model ) {
		Article article = articleService.findArticle(id);
		Rating rating = article.getRating();
		User user = getCurrentUser();
		Set<Rating> ratings = user.getRatings();
		if(!ratings.contains(rating)) {
			model.addAttribute("vote", true);
		}
		model.addAttribute(article);
		return "articles/articleDetail";
	}
	
	@PostMapping("/articles/{articleId}/{ratingId}")
	public String increaseRating(@PathVariable("ratingId")long id, @PathVariable("articleId") long articleId) {
		Rating rating = ratingService.getRating(id);
		User user = getCurrentUser();
		rating.increaseValue();
		ratingService.saveRating(rating, user);
		return "redirect:/articles/" + articleId;
	}
	
	@PostMapping("/articles/{articleId}/comment")
	public String addComment(@PathVariable("articleId") long articleId, @Valid Comment comment) {
		String username = getCurrentUser().getUserName();
		Article article = articleService.findArticle(articleId);
		commentService.saveComment(comment, article, username);
		return "redirect:/articles/" + articleId;
	}
	
	@GetMapping("/articles/user/{userId}")
	public String getArticlesForUser(@PathVariable("userId") long id, Model model) {
		model.addAttribute("articles", articleService.getAllArticlesByUser(id));
		return "articles/listArticles";
	}
	
	private User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		return user;
	}
}
