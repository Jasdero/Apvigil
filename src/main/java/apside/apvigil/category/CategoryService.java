package apside.apvigil.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CategoryService {

	@Autowired
	CategoryRepository categoryRepository;
	
	
	public void saveCategory(Category category) {
		categoryRepository.save(category);
	}
	
	public List<Category> findAllCategories() {
		return categoryRepository.findAll();
	}
	
	public Category findOne(Long id) {
		return categoryRepository.findOne(id);
	}
	
	public Category findOneByName(String name) {
		return categoryRepository.findByName(name);
	}
	
	public int countAssociatedArticles(String name) {
		Category category = categoryRepository.findByName(name);
		return category.getArticles().size();
	}
	
	public void deleteCategory(Long id) {
		categoryRepository.delete(id);
	}
	
	public List<Category> findByActivated(boolean activated) {
		return categoryRepository.findByActivated(activated);
	}
	
	public Long countByActivated(boolean activated) {
		return categoryRepository.countByActivated(activated);
	}
	
	
	
}
