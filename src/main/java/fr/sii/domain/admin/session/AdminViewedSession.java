package fr.sii.domain.admin.session;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.datanucleus.api.jpa.annotations.Extension;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by tmaugin on 15/07/2015.
 * SII
 */
@Entity
@Component
public class AdminViewedSession{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String id;
    @JsonProperty("id")
    private Long entityId;

    @NotNull
    private Long rowId;

    @NotNull
    private Long userId;

    @NotNull
    private Date lastSeen;


    public AdminViewedSession(Long rowId, Long userId, Date lastSeen) {
        this.rowId = rowId;
        this.userId = userId;
        this.setLastSeen(lastSeen);
        this.entityId = new Date().getTime();
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonIgnore
    public String getId() {
        return id;
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Date lastSeen) {
        this.lastSeen = lastSeen;
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

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
}
