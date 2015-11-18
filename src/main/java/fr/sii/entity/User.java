package fr.sii.entity;

import org.hibernate.validator.constraints.Email;

import javax.persistence.*;

/**
 * Speaker account
 */
@Entity
@Table(name = "users")
public class User {
	public enum Provider { GOOGLE, GITHUB }

	private int id;
	private String email;

	/** github oauth id if user connect with his account */
	private String githubId;
	/** google oauth id if user connect with his account */
	private String googleId;

	/** password if user have a local account */
	private String password;
	/** for local account, true if user validate his e-mail address */
	private boolean verified;
	/** token to verify local e-mail address, empty when e-mail verified */
	private String verifyToken;


	/* ****  USER PROFILE  **** */
	private String lastname;
	private String firstname;
	private String company;
	private String phone;
	private String bio;
	private String twitter;
	private String googleplus;
	private String github;
	/** other url (blog, linkedin...) */
	private String social;

	/** local stored image for user */
	private byte[] image;
	/** link to remote user image (gravatar, google plus...) */
	private String imageSocialUrl;

	/**
	 * Count sign in provider the user has
	 * @return Number of sign in provider
	 */
	@Transient
	public int getSignInMethodCount() {
		int count = 0;
		if (password != null) count++;
		if (githubId != null) count++;
		if (googleId != null) count++;
		return count;
	}

	@Transient
	public void setProviderId(Provider provider, String providerId) {
		switch (provider) {
			case GOOGLE: this.setGoogleId(providerId); break;
			case GITHUB: this.setGithubId(providerId); break;
			default: throw new IllegalArgumentException();
		}
	}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	@Email
	public String getEmail() {
		return email;
	}

	@Column(name = "github_id")
	public String getGithubId() {
		return githubId;
	}

    @Column(name = "google_id")
	public String getGoogleId() {
		return googleId;
	}

	public String getPassword() {
		return password;
	}

	public boolean isVerified() {
		return verified;
	}

    @Column(name = "verify_token")
	public String getVerifyToken() {
		return verifyToken;
	}

	public String getLastname() {
		return lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getCompany() {
		return company;
	}

	public String getPhone() {
		return phone;
	}

	public String getBio() {
		return bio;
	}

	public String getTwitter() {
		return twitter;
	}

	public String getGoogleplus() {
		return googleplus;
	}

	public String getGithub() {
		return github;
	}

	public String getSocial() {
		return social;
	}

	public byte[] getImage() {
		return image;
	}

    @Column(name = "image_social_url")
	public String getImageSocialUrl() {
		return imageSocialUrl;
	}


	public void setId(int id) {
		this.id = id;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setGithubId(String githubId) {
		this.githubId = githubId;
	}

	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public void setVerifyToken(String verifyToken) {
		this.verifyToken = verifyToken;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public void setGoogleplus(String googleplus) {
		this.googleplus = googleplus;
	}

	public void setGithub(String github) {
		this.github = github;
	}

	public void setSocial(String social) {
		this.social = social;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public void setImageSocialUrl(String imageSocialUrl) {
		this.imageSocialUrl = imageSocialUrl;
	}
}
