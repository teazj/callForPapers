package fr.sii.entity;

import javax.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Max(5)
    @Min(0)
    @NotNull
    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    @NotNull
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "talk")
    public Talk getTalk() {
        return talk;
    }

    public void setTalk(Talk talk) {
        this.talk = talk;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin")
    public AdminUser getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(AdminUser adminUser) {
        this.adminUser = adminUser;
    }
}
