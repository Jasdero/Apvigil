package apside.apvigil.article;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
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
	
	
	@GetMapping("/articles/list/{pageNumber}")
	public ModelAndView listArticles(@PathVariable("pageNumber") int pageNumber) {
		User user = getCurrentUser();
		userService.setNumberOfNotications(user);
		ModelAndView mav = new ModelAndView("articles/listArticles");
		Pageable pageable = new PageRequest(pageNumber, 5, Direction.DESC, "createdOn");
		long totalPages = articleService.count()/5;
		int[] previousNext = getNextAndPreviousPages(pageNumber, totalPages);
		mav.addObject("articles", articleService.findAllWithPagination(pageable));
		mav.addObject("pages", totalPages+1);
		mav.addObject("previous", previousNext[0]);
		mav.addObject("next", previousNext[1]);
		return mav;
	}
	
	@GetMapping("/articles/{categoryName}/{pageNumber}")
	public String getArticlesByCategory(Model model, @PathVariable("categoryName") String name, @PathVariable("pageNumber") int pageNumber) {
		Pageable pageable = new PageRequest(pageNumber, 5, Direction.DESC, "createdOn");
		Page<Article> articles = articleService.getAllArticlesByCategoryName(name, pageable);
		long totalPages = categoryService.countAssociatedArticles(name)/5;
		int[] previousNext = getNextAndPreviousPages(pageNumber, totalPages);
		model.addAttribute("articles", articles);
		model.addAttribute("pages", totalPages+1);
		model.addAttribute("previous", previousNext[0]);
		model.addAttribute("next", previousNext[1]);
		model.addAttribute("category", categoryService.findOneByName(name));
		return "articles/listArticlesByCategory";
	}
	
	@GetMapping("/articles/new")
	public String showForm(Model model) {
		Article article = new Article();
		model.addAttribute(article);
		return CREATE_ARTICLE_FORM;
	}
	
	@PostMapping("/articles/new")
	public String processForm(@Valid Article article, BindingResult result) {
		Article articleExists = articleService.findByUrl(article.getUrl());
		if (articleExists != null) {
			result.rejectValue("url", "error.article", "an article with the same url already exists");
		}
		if(result.hasErrors()) {
			return CREATE_ARTICLE_FORM;
		}
		User user = getCurrentUser();
		Rating rating = new Rating();
		articleService.addArticle(article, user, rating);
		return "redirect:/articles/list/0";
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
	
	
	private User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		return user;
	}
	
	private int[] getNextAndPreviousPages(int pageNumber, long totalPages) {

		int previous = pageNumber - 1;
		int next = pageNumber + 1;
		if (pageNumber == 0) {
			previous = pageNumber;
		}
		
		if (pageNumber == totalPages) {
			next = pageNumber;
		}
		
		int[] pages = {previous, next};
		return pages;
	}
}
