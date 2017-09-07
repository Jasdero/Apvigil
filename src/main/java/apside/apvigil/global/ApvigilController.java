package apside.apvigil.global;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import apside.apvigil.security.authentication.User;
import apside.apvigil.security.authentication.UserService;

public abstract class ApvigilController {
	
	
	@Autowired
	private UserService userService;
	
	protected User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		return user;
	}
	
	protected int[] getNextAndPreviousPages(int pageNumber, long totalPages) {

		int previous = pageNumber - 1;
		int next = pageNumber + 1;
		if (pageNumber == 0) {
			previous = pageNumber;
		}
		
		if (pageNumber == totalPages) {
			next = pageNumber;
		}
		
		int[] pages = {previous, next};
		return pages;
	}
	

}
