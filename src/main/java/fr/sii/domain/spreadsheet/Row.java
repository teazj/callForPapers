package fr.sii.domain.spreadsheet;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by tmaugin on 03/04/2015.
 */
public interface Row {

    public static final List<String> fields = Arrays.asList(
            "email","name","firstname","phone","company","bio","social","sessionName","description","references","difficulty","type","track","coSpeaker","financial","travel","travelFrom","hotel","hotelDate","added","draft","userId","twitter", "googlePlus","github"
    );

    // Needed for automatic spreadsheet creation
    String email = null;
    String name = null;
    String firstname = null;
    String phone = null;
    String company = null;
    String bio = null;
    String social = null;
    String twitter = null;
    String googlePlus = null;
    String github = null;
    String sessionName = null;
    String description = null;
    String references = null;
    Integer difficulty = null;
    String type = null;
    String track = null;
    String coSpeaker = null;
    Boolean financial = null;
    Boolean travel = null;
    String travelFrom = null;
    Boolean hotel = null;
    String hotelDate = null;
    Date added = null;
    boolean draft = false;
    Long userId = null;

   String getEmail();

   void setEmail(String email);

   String getName();

   void setName(String name);

   String getFirstname();

   void setFirstname(String firstname);

   String getPhone();

   void setPhone(String phone);

   String getCompany();

   void setCompany(String company);

   String getBio();

   void setBio(String bio);

   String getSocial();

   void setSocial(String social);

   String getTwitter();

   void setTwitter(String social);

   String getGooglePlus();

   void setGooglePlus(String social);

   void setGoogleplus(String social);

   String getGithub();

   void setGithub(String social);

   String getSessionName();

   void setSessionName(String sessionName);

   void setSessionname(String sessionName);

   String getDescription();

   void setDescription(String description);

   String getReferences();

   void setReferences(String references);

   Integer getDifficulty();

    void setDifficulty(Integer difficulty);

    void setDifficulty(String difficulty);

    String getTrack();

    void setTrack(String track);

    String getType();

    void setType(String track);

    String getCoSpeaker();

    void setCoSpeaker(String coSpeaker);

    void setCospeaker(String coSpeaker);

    Boolean getFinancial();

    void setFinancial(Boolean financial);

    void setFinancial(String financial);

    Boolean getTravel();

    void setTravel(Boolean travel);

    void setTravel(String travel);

    String getTravelFrom();

    void setTravelFrom(String travelFrom);

    void setTravelfrom(String travelFrom);

    Boolean getHotel();

    void setHotel(Boolean hotel);

    void setHotel(String hotel);

    String getHotelDate();

    void setHotelDate(String hotelDate);

    void setHoteldate(String hotelDate);

    Long getAdded();

    void setAdded(Date added);

    void setAdded(String added);

    Boolean getDraft();

    void setDraft(Boolean draft);

    void setDraft(String draft);

    Long getUserId();

    void setUserid(String userId);

    void setUserId(Long userId);
}
