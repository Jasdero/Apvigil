package apside.apvigil.security.authentication;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import apside.apvigil.rating.Rating;
import apside.apvigil.article.Article;
import apside.apvigil.category.Category;



@Entity
public class User {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;
	
	@Column(name = "email")
	@Email(message = "*Please provide a valid email")
	private String email;
	
	@Length(min = 5, message ="*Your password must have at least 5 characters")
	@NotEmpty(message = "*Please provide your password")
	private String password;
	
	@NotEmpty(message = "*Please provide your user name")
	private String userName;

	private int active;
	
	@Column(name = "last_visit")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastVisit;
	
	private int notifications = 0;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;
	
	@ManyToMany(mappedBy = "users", cascade = { CascadeType.PERSIST, CascadeType.MERGE})
	private Set<Rating> ratings;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "user_category", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
	private List<Category> categories;
	
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private Set<Article> articles;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Set<Article> getArticles() {
		return articles;
	}
	
	public void addArticle(Article article) {
		articles.add(article);
		article.setUser(this);
	}

	public void setArticles(Set<Article> articles) {
		this.articles = articles;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}
	
	public Date getLastVisit() {
		return this.lastVisit;
	}
	
	public void setLastVisit() {
		this.lastVisit = new Date();
	}
	
	public int getNotifications() {
		return this.notifications;
	}
	
	public void setNotifications(int notifications) {
		this.notifications = notifications;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public Set<Rating> getRatings() {
		return ratings;
	}

	public void setRatings(Set<Rating> ratings) {
		this.ratings = ratings;
	}
	
	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	
}
