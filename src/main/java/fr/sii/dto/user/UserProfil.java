package fr.sii.dto.user;

/**
 * Created by tmaugin on 05/06/2015.
 * SII
 */
public class UserProfil {
    private String name;
    private String firstname;
    private String company;
    private String phone;
    private String bio;
    private String social;
    private String twitter;
    private String googlePlus;
    private String github;
    private String imageProfilKey;
    private String socialProfilImageUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getSocial() {
        return social;
    }

    public void setSocial(String social) {
        this.social = social;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getGooglePlus() {
        return googlePlus;
    }

    public void setGooglePlus(String googlePlus) {
        this.googlePlus = googlePlus;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getImageProfilKey() {
        return imageProfilKey;
    }

    public void setImageProfilKey(String imageProfileKey) {
        this.imageProfilKey = imageProfileKey;
    }

    public String getSocialProfilImageUrl() {
        return socialProfilImageUrl;
    }

    public void setSocialProfilImageUrl(String socialProfilImageUrl) {
        this.socialProfilImageUrl = socialProfilImageUrl;
    }
}
