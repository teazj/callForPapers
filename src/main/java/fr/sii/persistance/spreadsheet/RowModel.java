package fr.sii.persistance.spreadsheet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by tmaugin on 03/04/2015.
 */
public class RowModel {

    @NotNull(message = "Email field is required")
    private String email;
    @NotNull(message = "Name field is required")
    private String name;
    @NotNull(message = "Firstname field is required")
    private String firstname;
    private String phone;
    private String company;
    @NotNull(message = "Bio field is required")
    private String bio;
    private String social;
    @NotNull(message = "Session name field is required")
    private String sessionName;
    @NotNull(message = "Description field is required")
    private String description;
    private String references;
    @NotNull(message = "Difficulty field is required")
    @Min(value = 1,message = "Difficulty must be higher than 0")
    @Max(value = 3,message = "Difficulty must be lower than 4")
    private Integer difficulty;
    @NotNull(message = "Track field is required")
    private String track;
    private String coSpeaker;
    @NotNull(message = "Financial field is required")
    private Boolean financial;
    private Boolean travel = false;
    private String travelFrom;
    private Boolean hotel = false;
    private String hotelDate;
    private Date added;


    @JsonIgnore
    @AssertTrue(message = "You must either require financial help and fulfill the form or not require financial help.")
    public boolean getfinancialRequested(){
        boolean financial = this.getFinancial().equals(null) ? false : this.financial;
        boolean hotel = this.getHotel().equals(null) ? false : this.hotel;
        String hotelDate = this.hotelDate == null ? "" : this.hotelDate;
        boolean travel = this.getTravel().equals(null) ? false : this.travel;
        String travelFrom = this.travelFrom == null ? "" : this.travelFrom;

        if(!financial){
            return true;
        }
        else
        {
            // One or more field need to be fulfill
            if(!hotel && !travel)
            {
                return false;
            }
            else
            {
                return (hotel && !hotelDate.equals("")) || (travel && !travelFrom.equals(""));
            }
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getSocial() {
        return social;
    }

    public void setSocial(String social) {
        this.social = social;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public void setSessionname(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    @JsonProperty
    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    @JsonIgnore
    public void setDifficulty(String difficulty) {
        this.difficulty = Integer.valueOf(difficulty);
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getCoSpeaker() {
        return coSpeaker;
    }

    public void setCoSpeaker(String coSpeaker) {
        this.coSpeaker = coSpeaker;
    }

    public void setCospeaker(String coSpeaker) {
        this.coSpeaker = coSpeaker;
    }

    public Boolean getFinancial() {
        return financial;
    }

    @JsonProperty
    public void setFinancial(Boolean financial) {
        this.financial = financial;
    }

    @JsonIgnore
    public void setFinancial(String financial) {
        this.financial = financial.toLowerCase().equals("true");
    }

    public Boolean getTravel() {
        return travel;
    }

    @JsonProperty
    public void setTravel(Boolean travel) { this.travel = travel; }

    @JsonIgnore
    public void setTravel(String travel) {
        this.travel = travel.toLowerCase().equals("true");
    }

    public String getTravelFrom() {
        return travelFrom;
    }

    public void setTravelFrom(String travelFrom) {
        this.travelFrom = travelFrom;
    }

    public void setTravelfrom(String travelFrom) {
        this.travelFrom = travelFrom;
    }

    public Boolean getHotel() {
        return hotel;
    }

    @JsonProperty
    public void setHotel(Boolean hotel) {
        this.hotel = hotel;
    }

    @JsonIgnore
    public void setHotel(String hotel) {
        this.hotel = hotel.toLowerCase().equals("true");
    }

    public String getHotelDate() {
        return hotelDate;
    }

    public void setHotelDate(String hotelDate) {
        this.hotelDate = hotelDate;
    }

    public void setHoteldate(String hotelDate) {
        this.hotelDate = hotelDate;
    }

    public Date getAdded() {
        return added;
    }

    @JsonProperty
    public void setAdded(Date added) {
        this.added = added;
    }

    @JsonIgnore
    public void setAdded(String added) {
        DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
        Date date = null;
        try {
            date = df.parse(added);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.added = date;
    }

    @Override
    public String toString() {
        return "RowModel{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", firstname='" + firstname + '\'' +
                ", phone='" + phone + '\'' +
                ", company='" + company + '\'' +
                ", bio='" + bio + '\'' +
                ", social='" + social + '\'' +
                ", sessionName='" + sessionName + '\'' +
                ", description='" + description + '\'' +
                ", references='" + references + '\'' +
                ", difficulty=" + difficulty +
                ", track='" + track + '\'' +
                ", coSpeaker='" + coSpeaker + '\'' +
                ", financial=" + financial +
                ", travel=" + travel +
                ", travelFrom='" + travelFrom + '\'' +
                ", hotel=" + hotel +
                ", hotelDate='" + hotelDate + '\'' +
                '}';
    }
}
