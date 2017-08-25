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
	
	
	
}
