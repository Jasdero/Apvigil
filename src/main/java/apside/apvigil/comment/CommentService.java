package apside.apvigil.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apside.apvigil.article.Article;


@Service
public class CommentService {

	
	@Autowired
	private CommentRepository commentRepository;
	
	
	public void saveComment(Comment comment, Article article, String username) {
		comment.setAuthor(username);
		comment.setArticle(article);
		article.addComment(comment);
		commentRepository.save(comment);
	}
}
