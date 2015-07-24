package fr.sii.domain.token;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by tmaugin on 15/05/2015.
 */
public class Token {
    private String token;

    public Token(@JsonProperty("token") String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}