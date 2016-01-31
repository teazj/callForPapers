package fr.sii.dto;

/**
 * Created by tmaugin on 05/05/2015.
 */
public class AdminUserInfo {
    private boolean connected;
    private boolean admin;
    private boolean config;
    private String uri;
    private String email;

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isConfig() {
        return config;
    }

    public void setConfig(boolean config) {
        this.config = config;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AdminUserInfo(String uri, boolean connected, boolean admin, boolean config, String email) {
        this.uri = uri;
        this.connected = connected;
        this.admin = admin;
        this.config = config;
        this.email= email;
    }
}
