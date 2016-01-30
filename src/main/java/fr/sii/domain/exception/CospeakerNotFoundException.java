package fr.sii.domain.exception;


import fr.sii.dto.user.CospeakerProfil;

public class CospeakerNotFoundException extends NotFoundException {
    private CospeakerProfil cospeaker;

    public CospeakerNotFoundException() {
        super();
    }

    public CospeakerNotFoundException(CospeakerProfil cospeaker) {
        super();
        this.cospeaker = cospeaker;
    }

    public CospeakerNotFoundException(String message, CospeakerProfil cospeaker) {
        super(message);
        this.cospeaker = cospeaker;
    }

    public CospeakerProfil getCospeaker() {
        return cospeaker;
    }

    public void setCospeaker(CospeakerProfil cospeaker) {
        this.cospeaker = cospeaker;
    }

}
