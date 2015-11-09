package fr.sii.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Rate given by an admin to a talk
 */
@Entity
@Table(name = "rates")
public class Rate {

    private int id;
    private int rate;
    private Date added;
    private boolean love;
    private boolean hate;

    private Talk talk;
    private AdminUser adminUser;

    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    @Max(5)
    @Min(0)
    @NotNull
    public int getRate() {
        return rate;
    }

    @NotNull
    public Date getAdded() {
        return added;
    }

    public boolean isLove() {
        return love;
    }

    public boolean isHate() {
        return hate;
    }

    public Talk getTalk() {
        return talk;
    }

    public AdminUser getAdminUser() {
        return adminUser;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public void setAdded(Date added) {
        this.added = added;
    }

    public void setLove(boolean love) {
        this.love = love;
    }

    public void setHate(boolean hate) {
        this.hate = hate;
    }

    public void setTalk(Talk talk) {
        this.talk = talk;
    }

    public void setAdminUser(AdminUser adminUser) {
        this.adminUser = adminUser;
    }
}
