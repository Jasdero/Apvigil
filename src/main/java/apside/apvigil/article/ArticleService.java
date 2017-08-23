package apside.apvigil.article;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apside.apvigil.rating.Rating;
import apside.apvigil.security.authentication.User;


@Service
public class ArticleService {

	@Autowired
	private ArticleRepository articleRepository;
	

	public void addArticle(Article article, User user, Rating rating) {
		article.setRating(rating);
		rating.setArticle(article);
		user.addArticle(article);
		articleRepository.save(article);
		
	}
	
	public List<Article> getAllArticlesSorted() {
		return articleRepository.getAllSortedByDate();
	}
	
	
	
	public List<Article> getAllArticlesByUser(Long id) {
		return articleRepository.findByUserId(id);
	}
	
	public Article findArticle(Long id) {
		return articleRepository.findOne(id);
	}
	
	public List<Article> getAllArticlesByCategory(Long id) {
		return articleRepository.findByCategoryId(id);
	}

}
