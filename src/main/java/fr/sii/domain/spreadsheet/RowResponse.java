package fr.sii.domain.spreadsheet;

import fr.sii.domain.admin.comment.AdminComment;
import fr.sii.domain.admin.rate.AdminRate;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by tmaugin on 29/04/2015.
 */
public class RowResponse extends RowSession implements Serializable {
    private List<AdminRate> adminRates;
    private List<AdminComment> adminComments;
    private Date lastSeen;
    private Long userId;

    public RowResponse(Row row, List<AdminRate> adminRates, List<AdminComment> adminComments, Long userId, Date lastSeen)
    {
        super(
                row.getEmail(),
                row.getName(),
                row.getFirstname(),
                row.getPhone(),
                row.getCompany(),
                row.getBio(),
                row.getSocial(),
                row.getTwitter(),
                row.getGooglePlus(),
                row.getGithub(),
                row.getSessionName(),
                row.getDescription(),
                row.getReferences(),
                row.getDifficulty(),
                row.getType(),
                row.getTrack(),
                row.getCoSpeaker(),
                row.getFinancial(),
                row.getTravel(),
                row.getTravelFrom(),
                row.getHotel(),
                row.getHotelDate(),
                new Date(row.getAdded()),
                row.getDraft(),
                row.getUserId()
        );
        this.adminRates = adminRates;
        this.adminComments = adminComments;
        this.userId = userId;
        this.lastSeen = lastSeen;
    }

    public RowResponse(Row row, List<AdminRate> adminRates, List<AdminComment> adminComments)
    {
        super(
                row.getEmail(),
                row.getName(),
                row.getFirstname(),
                row.getPhone(),
                row.getCompany(),
                row.getBio(),
                row.getSocial(),
                row.getTwitter(),
                row.getGooglePlus(),
                row.getGithub(),
                row.getSessionName(),
                row.getDescription(),
                row.getReferences(),
                row.getDifficulty(),
                row.getType(),
                row.getTrack(),
                row.getCoSpeaker(),
                row.getFinancial(),
                row.getTravel(),
                row.getTravelFrom(),
                row.getHotel(),
                row.getHotelDate(),
                new Date(row.getAdded()),
                row.getDraft(),
                row.getUserId()
        );
        this.adminRates = adminRates;
        this.adminComments = adminComments;
        this.userId = null;
        this.lastSeen = null;
    }

    public Boolean getReviewed() {
        if(userId == null) return false;
        for(AdminRate rate : adminRates) {
            if(rate.getUserId().toString().equals((userId.toString()))) {
                return true;
            }
        }
        return false;
    }

    public Integer getNotViewedCount() {
        Integer count = this.adminComments.size();
        if(lastSeen == null) return count;
        for(AdminComment adminComment : this.adminComments) {
            if(adminComment.getAdded() < lastSeen.getTime()) {
                --count;
            }
        }
        return count;
    }

    public Double getMean()
    {
        Double mean = 0D;
        if(adminRates != null)
        {
            int size = 0;
            for(AdminRate adminRate : adminRates)
            {
                if(adminRate.getRate() != 0) {
                    size++;
                    mean+= adminRate.getRate();
                }
            }
            if(size != 0) {
                return mean/size;
            } else {
                return 0D;
            }
        }
        return null;
    }

    public long getLastModification()
    {
        long lastModified = 0;
        for(AdminRate adminRate : adminRates)
        {
            if(lastModified == 0 || lastModified < adminRate.getAdded()) {
                lastModified = adminRate.getAdded();
            }
        }
        for(AdminComment adminComment : adminComments) {
            if (lastModified == 0 || lastModified < adminComment.getAdded()) {
                lastModified = adminComment.getAdded();
            }
        }
        return lastModified;
    }
}
