package apside.apvigil.image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

	private final ImageRepository repository;
	private final ResourceLoader resourceLoader;

	private static String UPLOAD_ROOT = "src/main/resources/static/images";

	@Autowired
	public ImageService(ImageRepository repository, ResourceLoader resourceLoader) {
		this.repository = repository;
		this.resourceLoader = resourceLoader;
	}

	public Resource findOneImage(String filename) {
		return resourceLoader.getResource("file:" + UPLOAD_ROOT + "/" + filename);
	}
	
	public Image findByName(String name) {
		return repository.findByName(name);
	}

	public void saveImage(MultipartFile file) throws IOException{
		if (!file.isEmpty()) {
			Files.copy(file.getInputStream(), Paths.get(UPLOAD_ROOT, file.getOriginalFilename()));
			repository.save(new Image(file.getOriginalFilename()));
		}
	}
	
}
