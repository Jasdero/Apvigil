package apside.apvigil.article;


import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import apside.apvigil.comment.Comment;
import apside.apvigil.rating.Rating;
import apside.apvigil.security.authentication.User;


@Entity
public class Article {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Length(min = 5)
	private String title;
	
	@NotNull
	@Column(columnDefinition="TEXT")
	private String url;
	
	@Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn = new Date();
	
	@ManyToOne
	private User user;
	
	@OneToOne(mappedBy="article", cascade = CascadeType.ALL)
	private Rating rating;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="article")
	private Set<Comment> comments;


	public Rating getRating() {
		return rating;
	}


	public void setRating(Rating rating) {
		this.rating = rating;
	}


	public Article() {
		
	}
	
	
	public Article(Long id, String title, String url, User user) {
		this.id = id;
		this.title =title;
		this.url = url;
		this.setUser(new User());
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public Date getCreatedOn() {
		return createdOn;
	}
	
	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}
	
	public void addComment(Comment comment) {
		comment.setArticle(this);
		comments.add(comment);
	}
}
