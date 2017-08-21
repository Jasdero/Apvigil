package apside.apvigil.article;

import java.util.ArrayList;
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
	
	public List<Article> getAllArticles() {
		List<Article> articles = new ArrayList<>();
		articleRepository.findAll().forEach(articles::add);
		return articles;
	}
	
	public List<Article> getAllArticlesByUser(Long id) {
		List<Article> articles = new ArrayList<>();
		articleRepository.findByUserId(id).forEach(articles::add);
		return articles;
	}
	
	public Article findArticle(Long id) {
		return articleRepository.findOne(id);
	}

}
