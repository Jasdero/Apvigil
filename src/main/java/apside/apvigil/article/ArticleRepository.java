package apside.apvigil.article;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;



public interface ArticleRepository extends CrudRepository<Article, Long>{

	public List<Article> findByUserId(Long id);
	public Page<Article> findByCategoryName(String name, Pageable pageable);
	public List<Article> findByTitleContainingOrDescriptionContaining(String title, String description);
	public Article findByUrl(String url);
	public Page<Article> findAll(Pageable pageable);
	public List<Article> findAll();
	public long count();
	public long countByCategoryId(Long id);
	public int countByCreatedOnAfterAndCategoryId(Date lastVisit, long id);

}
