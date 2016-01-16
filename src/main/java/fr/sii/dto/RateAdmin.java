package fr.sii.dto;

import fr.sii.dto.user.AdminUserDTO;

import java.util.Date;

/**
 * Rate DTO for admin view
 */
public class RateAdmin {

    private int id;
    private int rate;
    private Date added;
    private boolean love;
    private boolean hate;

    private int talkId;
    private AdminUserDTO user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public Date getAdded() {
        return added;
    }

    public void setAdded(Date added) {
        this.added = added;
    }

    public boolean isLove() {
        return love;
    }

    public void setLove(boolean love) {
        this.love = love;
    }

    public boolean isHate() {
        return hate;
    }

    public void setHate(boolean hate) {
        this.hate = hate;
    }

    public int getTalkId() {
        return talkId;
    }

    public void setTalkId(int talkId) {
        this.talkId = talkId;
    }

    public AdminUserDTO getUser() {
        return user;
    }

    public void setUser(AdminUserDTO user) {
        this.user = user;
    }
}
