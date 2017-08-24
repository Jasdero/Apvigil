package apside.apvigil.article;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;



public interface ArticleRepository extends CrudRepository<Article, Long>{

	public List<Article> findByUserId(Long id);
	public List<Article> findByCategoryId(Long id);
	public Article findByUrl(String url);
	public Page<Article> findAll(Pageable pageable);
	public List<Article> findAll();
	public long count();

}
