package fr.sii.domain.admin.rate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.sii.domain.admin.user.AdminUser;
import org.datanucleus.api.jpa.annotations.Extension;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by tmaugin on 27/04/2015.
 */
@Entity
@Component
public class AdminRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String id;
    @JsonProperty("id")
    private Long entityId;

    @Max(5)
    @Min(1)
    @NotNull
    private Integer rate;

    private Date added;

    @NotNull
    private Long rowId;

    @Transient
    private AdminUser adminUser;

    private Long userId;

    public AdminRate() {
        this.entityId = new Date().getTime();
    }

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

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public AdminUser getUser() {
        return adminUser;
    }

    public void setUser(AdminUser adminUser) {
        this.adminUser = adminUser;
    }

    public Long getAdded() {
        return (added != null) ? added.getTime() : null;
    }

    public void setAdded(Date added) {
        this.added = added;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRowId() {
        return rowId;
    }

    public void setRowId(Long rowId) {
        this.rowId = rowId;
    }
}
