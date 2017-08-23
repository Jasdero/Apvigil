package apside.apvigil.article;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;



public interface ArticleRepository extends CrudRepository<Article, Long>{

	public List<Article> findByUserId(Long id);
	public List<Article> findByCategoryId(Long id);
	public List<Article> findAll();
	
	@Query("select a from Article a order by a.createdOn DESC")
	public List<Article> getAllSortedByDate();
}
