package apside.apvigil.image;

import org.springframework.data.repository.CrudRepository;

public interface ImageRepository extends CrudRepository<Image, Long>{

	public Image findByName(String name);
}
