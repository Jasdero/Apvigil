package apside.apvigil.article;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	
	public List<Article> findAll() {
		return articleRepository.findAll();
	}
	
	public long count() {
		return articleRepository.count();
	}
	
	public long countByCategoryId(Long id) {
		return articleRepository.countByCategoryId(id);
	}
	
	
	public Page<Article> findAllWithPagination(Pageable pageable) {
		return articleRepository.findAll(pageable);
	}
	
	
	
	public List<Article> getAllArticlesByUser(Long id) {
		return articleRepository.findByUserId(id);
	}
	
	public Article findArticle(Long id) {
		return articleRepository.findOne(id);
	}
	
	public Page<Article> getAllArticlesByCategoryName(String name, Pageable pageable) {
		return articleRepository.findByCategoryName(name, pageable);
	}
	
	public Article findByUrl(String url) {
		return articleRepository.findByUrl(url);
	}

}
