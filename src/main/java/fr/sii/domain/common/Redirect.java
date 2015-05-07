package fr.sii.domain.common;

import javax.validation.constraints.NotNull;

/**
 * Created by tmaugin on 07/05/2015.
 */
public class Redirect {
    @NotNull
    private String redirect;

    public Redirect(String redirect) {
        this.redirect = redirect;
    }

    public Redirect() {
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }
}
