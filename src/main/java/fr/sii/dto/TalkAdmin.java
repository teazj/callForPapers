package fr.sii.dto;

/**
 * Talk DTO for admin view
 */
public class TalkAdmin extends TalkUser {

    private int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
