package fr.sii.entity;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;

/**
 * Speaker account
 */
@Entity
@Table(name = "users")
public class User {
    private int id;
    private String email;
    /**
     * github oauth id if user connect with his account
     */
    private String githubId;
    /**
     * google oauth id if user connect with his account
     */
    private String googleId;
    /**
     * password if user have a local account
     */
    private String password;
    /**
     * for local account, true if user validate his e-mail address
     */
    private boolean verified;
    /**
     * token to verify local e-mail address, empty when e-mail verified
     */
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
    /**
     * other url (blog, linkedin...)
     */
    private String social;
    /**
     * local stored image for user
     */
    private String imageProfilURL;

    /**
     * Count sign in provider the user has
     *
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
            case GOOGLE:
                this.setGoogleId(providerId);
                break;
            case GITHUB:
                this.setGithubId(providerId);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "github_id")
    public String getGithubId() {
        return githubId;
    }

    public void setGithubId(String githubId) {
        this.githubId = githubId;
    }

    @Column(name = "google_id")
    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    @Column(name = "verify_token")
    public String getVerifyToken() {
        return verifyToken;
    }

    public void setVerifyToken(String verifyToken) {
        this.verifyToken = verifyToken;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Type(type = "text")
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getGoogleplus() {
        return googleplus;
    }

    public void setGoogleplus(String googleplus) {
        this.googleplus = googleplus;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getSocial() {
        return social;
    }

    public void setSocial(String social) {
        this.social = social;
    }

    @Column(name = "image_profil_url")
    public String getImageProfilURL() {
        return imageProfilURL;
    }

    public void setImageProfilURL(String imageProfilURL) {
        this.imageProfilURL = imageProfilURL;
    }

    public void mergeData(User userToMerge) {
        if (this.getFirstname() == null) {
            this.setFirstname(userToMerge.getFirstname());
        }

        if (this.getLastname() == null) {
            this.setLastname(userToMerge.getLastname());
        }

        if (this.getEmail() == null) {
            this.setEmail(userToMerge.getEmail());
        }

        if (this.getGithub() == null) {
            this.setGithub(userToMerge.getGithub());
        }

        if (this.getGoogleplus() == null) {
            this.setGoogleplus(userToMerge.getGoogleplus());
        }

        if (this.getBio() == null) {
            this.setBio(userToMerge.getBio());
        }

        if (this.getCompany() == null) {
            this.setCompany(userToMerge.getCompany());
        }

        if (this.getImageProfilURL() == null) {
            if (userToMerge.getImageProfilURL() != null && !userToMerge.getImageProfilURL().equals("")) {
                this.setImageProfilURL(userToMerge.getImageProfilURL());
            }
        }
    }

    public enum Provider {GOOGLE, GITHUB}
}
