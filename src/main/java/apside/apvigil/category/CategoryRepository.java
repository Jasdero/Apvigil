package apside.apvigil.category;

import java.util.List;

import org.springframework.data.repository.CrudRepository;


public interface CategoryRepository extends CrudRepository<Category, Long> {

	public List<Category> findAll();
	public Category findByName(String name);
	public List<Category> findByActivated(boolean activated);
	public Long countByActivated(boolean activated);
	
}
