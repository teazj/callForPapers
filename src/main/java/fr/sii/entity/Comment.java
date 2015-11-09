package fr.sii.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Comment on a talk
 */
@Entity
@Table(name = "comments")
public class Comment {

    private int id;
    private String comment;
    private Date added;
    private boolean internal;

    private Talk talk;
    private AdminUser adminUser;

    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    @NotNull
    public String getComment() {
        return comment;
    }

    @NotNull
    public Date getAdded() {
        return added;
    }

    public boolean isInternal() {
        return internal;
    }

    @ManyToOne
    public Talk getTalk() {
        return talk;
    }

    @ManyToOne
    public AdminUser getAdminUser() {
        return adminUser;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setAdded(Date added) {
        this.added = added;
    }

    public void setInternal(boolean internal) {
        this.internal = internal;
    }

    public void setTalk(Talk talk) {
        this.talk = talk;
    }

    public void setAdminUser(AdminUser adminUser) {
        this.adminUser = adminUser;
    }
}
