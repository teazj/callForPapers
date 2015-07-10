package fr.sii.domain.spreadsheet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

/**
 * Created by tmaugin on 19/05/2015.
 */
public class RowDraft implements Row {
    private String email;
    private String name;
    private String firstname;
    private String phone;
    private String company;
    private String bio;
    private String social;
    private String twitter;
    private String googlePlus;
    private String github;
    private String sessionName;
    private String description;
    private String references;
    @Min(value = 1,message = "Difficulty must be higher than 0")
    @Max(value = 3,message = "Difficulty must be lower than 4")
    private Integer difficulty;
    private String type;
    private String track;
    private String coSpeaker;
    private Boolean financial;
    private Boolean travel = false;
    private String travelFrom;
    private Boolean hotel = false;
    private String hotelDate;
    @JsonIgnore
    private Date added;
    @JsonIgnore
    private boolean draft;
    @JsonIgnore
    private Long userId;

    public RowDraft(String email, String name, String firstname, String phone, String company, String bio, String social,String twitter, String googlePlus, String github, String sessionName, String description, String references, Integer difficulty, String track, String coSpeaker, Boolean financial, Boolean travel, String travelFrom, Boolean hotel, String hotelDate, Date added) {
        this.email = email;
        this.name = name;
        this.firstname = firstname;
        this.phone = phone;
        this.company = company;
        this.bio = bio;
        this.social = social;
        this.twitter = twitter;
        this.googlePlus = googlePlus;
        this.github = github;
        this.sessionName = sessionName;
        this.description = description;
        this.references = references;
        this.difficulty = difficulty;
        this.track = track;
        this.coSpeaker = coSpeaker;
        this.financial = financial;
        this.travel = travel;
        this.travelFrom = travelFrom;
        this.hotel = hotel;
        this.hotelDate = hotelDate;
        this.added = added;
        this.draft = true;
    }

    public RowDraft()
    {
        this.draft = true;
    }

    @JsonIgnore
    @AssertTrue(message = "You must either require financial help and fulfill the form or not require financial help.")
    public boolean getfinancialRequested(){

        boolean financial = this.financial == null ? false : this.financial;
        boolean hotel = this.hotel == null ? false : this.hotel;
        String hotelDate = this.hotelDate == null ? "" : this.hotelDate;
        boolean travel = this.travel == null ? false : this.travel;
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

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getGooglePlus() {
        return googlePlus;
    }

    public void setGooglePlus(String googlePlus) {
        this.googlePlus = googlePlus;
    }

    public void setGoogleplus(String googlePlus) {
        this.googlePlus = googlePlus;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    @JsonProperty
    public Long getAdded() {
        return (added != null) ? added.getTime() : null;
    }

    @JsonIgnore
    public void setAdded(Date added) {
        this.added = added;
    }

    @JsonIgnore
    public void setAdded(String added) {
        Date date = null;
        try
        {
            date = new Date(Long.parseLong(added));
        }catch(NumberFormatException e)
        {
            e.printStackTrace();
        }
        this.added = date;
    }

    @JsonProperty
    public Boolean getDraft(){ return this.draft; }

    @JsonIgnore
    public void setDraft(Boolean draft){ this.draft = draft; }

    @JsonIgnore
    public void setDraft(String draft){ this.draft = draft.toLowerCase().equals("true"); }

    @JsonProperty
    public Long getUserId(){ return this.userId; }

    @JsonIgnore
    public void setUserid(String userId){ this.userId = Long.parseLong(userId); }

    @JsonIgnore
    public void setUserId(Long userId){ this.userId = userId; }

    @Override
    public String toString() {
        return "RowDraft{" +
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
                ", added=" + added +
                ", draft=" + draft +
                ", userId=" + userId +
                '}';
    }
}
