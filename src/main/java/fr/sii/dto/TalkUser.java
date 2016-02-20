package fr.sii.dto;

import java.util.Date;
import java.util.Set;

import fr.sii.dto.user.UserProfil;
import fr.sii.dto.user.CospeakerProfil;
import fr.sii.entity.Talk;

/**
 * Talk DTO for user view
 */
public class TalkUser {

    private int id;
    private Talk.State state;
    private String name;
    private int format;
    private Integer trackId;
    private String trackLabel;
    private String description;
    private String references;
    private Integer difficulty;
    private Date added;
    private UserProfil speaker;
    private Set<CospeakerProfil> cospeakers;


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

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public Integer getTrackId() {
        return trackId;
    }

    public void setTrackId(Integer trackId) {
        this.trackId = trackId;
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

    public String getTrackLabel() {
        return trackLabel;
    }

    public void setTrackLabel(String trackLabel) {
        this.trackLabel = trackLabel;
    }

    public void setCospeaker(Set<CospeakerProfil> cospeakers) {
        this.cospeakers = cospeakers;
    }

    public Set<CospeakerProfil> getCospeakers() {
        return cospeakers;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TalkUser talkUser = (TalkUser) o;

        return id == talkUser.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
