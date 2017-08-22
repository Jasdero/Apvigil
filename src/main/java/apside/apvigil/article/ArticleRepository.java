package apside.apvigil.article;

import java.util.List;

import org.springframework.data.repository.CrudRepository;



public interface ArticleRepository extends CrudRepository<Article, Long>{

	public List<Article> findByUserId(Long id);
	public List<Article> findByCategoryId(Long id);
	public List<Article> findAll();
}
