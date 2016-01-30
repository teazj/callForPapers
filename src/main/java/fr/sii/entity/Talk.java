package fr.sii.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

/**
 * Talk
 */
@Entity
@Table(name = "talks")
public class Talk {
    private int id;
    private State state;
    private String name;
    private Track track;
    private String description;
    private String references;
    private Integer difficulty;
    private Date added;
    private TalkFormat talkFormat;
    private User user;
    //schedule data
    private Date date;
    private String heure;
    private Set<User> cospeakers;

    @Transient
    public int getDuree() {
        return talkFormat.getDureeMinutes();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NotNull
    @Enumerated(EnumType.STRING)
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @NotNull(message = "Session name field is required")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne
    @JoinColumn(name = "track")
    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    @Type(type = "text")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Type(type = "text")
    @Column(name = "refs")
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

    @ManyToOne
    @JoinColumn(name = "talkformat")
    public TalkFormat getTalkFormat() {
        return talkFormat;
    }

    public void setTalkFormat(TalkFormat talkFormat) {
        this.talkFormat = talkFormat;
    }

    @ManyToOne
    @JoinColumn(name = "user")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "schedule_date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "schedule_heure")
    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "cospeakers", joinColumns = @JoinColumn(name = "talk_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    public Set<User> getCospeakers() {
        return cospeakers;
    }

    public void setCospeakers(Set<User> cospeakers) {
        this.cospeakers = cospeakers;
    }

    public enum State {DRAFT, CONFIRMED, ACCEPTED, REFUSED}
}
