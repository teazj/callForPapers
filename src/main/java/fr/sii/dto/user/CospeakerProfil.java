package fr.sii.dto.user;

import java.util.Set;
import fr.sii.dto.TalkUser;
import com.fasterxml.jackson.annotation.JsonIgnore;


public class CospeakerProfil {
    private int id;
    private String email;

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
}
