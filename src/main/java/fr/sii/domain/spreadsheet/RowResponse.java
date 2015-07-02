package fr.sii.domain.spreadsheet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.sii.domain.admin.comment.AdminComment;
import fr.sii.domain.admin.rate.AdminRate;

import java.util.Date;
import java.util.List;

/**
 * Created by tmaugin on 29/04/2015.
 */
public class RowResponse extends RowSession {
    private List<AdminRate> adminRates;
    private List<AdminComment> adminComments;

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
                new Date(row.getAdded())
        );
        this.adminRates = adminRates;
        this.adminComments = adminComments;
    }

    public Double getMean()
    {
        Double mean = 0D;
        if(adminRates != null)
        {
            if(adminRates.size() > 0 )
            {
                for(AdminRate adminRate : adminRates)
                {
                    mean+= adminRate.getRate();
                }
                return mean/ adminRates.size();
            }
            else
            {
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

    @JsonIgnore
    public List<AdminRate> getAdminRates() {
        return adminRates;
    }

    public void setAdminRates(List<AdminRate> adminRates) {
        this.adminRates = adminRates;
    }
}
