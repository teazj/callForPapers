package fr.sii.domain.spreadsheet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.sii.domain.rate.Rate;

import java.util.Date;
import java.util.List;

/**
 * Created by tmaugin on 29/04/2015.
 */
public class RowResponse extends Row {
    private List<Rate> rates;

    public RowResponse(Row row, List<Rate> rates)
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
                row.getTrack(),
                row.getCoSpeaker(),
                row.getFinancial(),
                row.getTravel(),
                row.getTravelFrom(),
                row.getHotel(),
                row.getHotelDate(),
                new Date(row.getAdded())
        );
        this.rates = rates;
    }

    public Double getMean()
    {
        Double mean = 0D;
        if(rates != null)
        {
            if(rates.size() > 0 )
            {
                for(Rate rate : rates)
                {
                    mean+= rate.getRate();
                }
                return mean/rates.size();
            }
            else
            {
                return 0D;
            }
        }
        return null;
    }

    @JsonIgnore
    public List<Rate> getRates() {
        return rates;
    }

    public void setRates(List<Rate> rates) {
        this.rates = rates;
    }
}
