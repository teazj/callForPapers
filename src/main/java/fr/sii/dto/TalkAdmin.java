package fr.sii.dto;

/**
 * Talk DTO for admin view
 */
public class TalkAdmin extends TalkUser {

    private int userId;

    private boolean reviewed;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isReviewed() {
        return reviewed;
    }

    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }
}
