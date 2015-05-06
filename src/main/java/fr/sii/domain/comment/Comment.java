package fr.sii.domain.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.sii.domain.user.User;
import org.datanucleus.api.jpa.annotations.Extension;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by tmaugin on 29/04/2015.
 */

@Entity
@Component
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String id;
    @JsonProperty("id")
    private Long entityId;

    @NotNull
    private String comment;

    private Date added;

    @NotNull
    private Long rowId;

    //@NotNull
    private Long userId;

    @Transient
    private User user;

    @JsonIgnore
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getAdded() {
        return (added != null) ? added.getTime() : null;
    }

    public void setAdded(Date added) {
        this.added = added;
    }

    public Long getRowId() {
        return rowId;
    }

    public void setRowId(Long rowId) {
        this.rowId = rowId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
