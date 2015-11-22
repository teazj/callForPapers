package fr.sii.dto;

import fr.sii.dto.user.UserProfil;
import fr.sii.entity.Talk;

import java.util.Date;

/**
 * Talk DTO for user view
 */
public class TalkUser {

    private int id;
    private Talk.State state;
    private String name;
    private String type;
    private String track;
    private String description;
    private String references;
    private Integer difficulty;
    private Date added;
    private UserProfil speaker;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Talk.State getState() {
        return state;
    }

    public void setState(Talk.State state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public Date getAdded() {
        return added;
    }

    public void setAdded(Date added) {
        this.added = added;
    }

    public UserProfil getSpeaker() {
        return speaker;
    }

    public void setSpeaker(UserProfil speaker) {
        this.speaker = speaker;
    }

}
