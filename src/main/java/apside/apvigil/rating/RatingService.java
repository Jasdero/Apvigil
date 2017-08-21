package apside.apvigil.rating;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apside.apvigil.security.authentication.User;

@Service
public class RatingService {

	
	@Autowired
	RatingRepository ratingRepository;
	
	public Rating getRating(Long id) {
		return ratingRepository.findOne(id);
	}
	
	public void saveRating(Rating rating, User user) {
		Set<Rating> ratings = user.getRatings();
		ratings.add(rating);
		user.setRatings(ratings);
		ratingRepository.save(rating);
	}
}
