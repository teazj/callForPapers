package fr.sii.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.StringUtils;
import org.datanucleus.api.jpa.annotations.Extension;
import org.hibernate.validator.constraints.Email;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by tmaugin on 15/05/2015.
 */
@Entity
@Component
public class User implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String id;
    @JsonProperty("id")
    private Long entityId;

    public User()
    {
        this.entityId = new Date().getTime();
        this.verified = false;
        this.profile = "{}";
    }

    public Object clone() {
        Object o = null;
        try {
            o = super.clone();
        } catch(CloneNotSupportedException cnse) {
            cnse.printStackTrace(System.err);
        }
        return o;
    }

    @Email
    private String email;

    private String password;

    private String profile;

    private String google;

    private String github;

    private boolean verified;

    private String verifyToken;

    public enum Provider {
        GOOGLE("google"), GITHUB("github");

        String name;

        Provider(final String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public String capitalize() {
            return StringUtils.capitalize(this.name);
        }
    }

    @JsonIgnore
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEmail() {
        return email;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }


    public String getGoogle() {
        return google;
    }

    public String getGithub() {
        return github;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setProviderId(final Provider provider, final String value) {
        switch (provider) {
            case GOOGLE:
                this.google = value;
                break;
            case GITHUB:
                this.github = value;
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getVerifyToken() {
        return verifyToken;
    }

    public void setVerifyToken(String verifyToken) {
        this.verifyToken = verifyToken;
    }

    @JsonIgnore
    public int getSignInMethodCount() throws IllegalArgumentException, IllegalAccessException,
            NoSuchFieldException, SecurityException {
        int count = 0;

        if (this.getPassword() != null) {
            count++;
        }
        for (final Provider p : Provider.values()) {
            if (this.getClass().getDeclaredField(p.name).get(this) != null) {
                count++;
            }
        }
        return count;
    }
}
