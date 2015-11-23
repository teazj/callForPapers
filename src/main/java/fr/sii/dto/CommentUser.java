package fr.sii.dto;

import java.util.Date;

import fr.sii.dto.user.UserProfil;

/**
 * Comment DTO sent to the speaker
 */
public class CommentUser {

    private int id;
    private String comment;
    private String adminName;
    private UserProfil user;
    private Date added;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public Date getAdded() {
        return added;
    }

    public void setAdded(Date added) {
        this.added = added;
    }

    public UserProfil getUser() {
        return user;
    }

    public void setUser(UserProfil user) {
        this.user = user;
    }
}
