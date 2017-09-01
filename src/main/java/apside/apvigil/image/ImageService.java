package apside.apvigil.image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import apside.apvigil.security.authentication.User;

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

	public void saveImage(MultipartFile file, User user) throws IOException{
		if (!file.isEmpty()) {
			String recordedImageName = user.getEmail()+file.getOriginalFilename();
			Files.copy(file.getInputStream(), Paths.get(UPLOAD_ROOT, recordedImageName));
			repository.save(new Image(recordedImageName));
		}
	}
	
	public void deleteImage(String name) throws IOException {
		Files.delete(Paths.get(UPLOAD_ROOT, name));
	}
	
	
}
