package fr.sii.dto.user;

import java.util.Set;
import fr.sii.dto.TalkUser;
import com.fasterxml.jackson.annotation.JsonIgnore;


public class CospeakerProfil {
    private int id;
    private String email;
    private String lastname;
    private String firstname;

    public CospeakerProfil() {
    }

    public CospeakerProfil(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
