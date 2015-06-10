package fr.sii.domain.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by tmaugin on 18/05/2015.
 */
public class SignupUser {

    @NotNull(message = "Email field is required")
    String email;
    @Size(min = 6)
    @NotNull(message = "Password field is required")
    String password;
    @NotNull(message = "Captcha field is required")
    String captcha;

    public SignupUser(String email, String password, String captcha) {
        this.email = email;
        this.password = password;
        this.captcha = captcha;
    }

    public SignupUser() {
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

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
