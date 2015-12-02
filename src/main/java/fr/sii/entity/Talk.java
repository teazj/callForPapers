package fr.sii.entity;

import org.hibernate.annotations.Type;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
/**
 * Talk
 */
@Entity
@Table(name = "talks")
public class Talk {
    public enum State { DRAFT, CONFIRMED, ACCEPTED, REFUSED }

    private int id;
    private State state;
    private String name;
    private String type;
    private Track track;
    private String description;
    private String references;
    private Integer difficulty;
    private Date added;
    private TalkFormat talkformat;
    private User user;
    //schedule data
    private Date date;
    private String heure;

    private Set<User> cospeakers = new HashSet<User>();


    //cospeakers
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name="cospeakers", 
                joinColumns={@JoinColumn(name="talk_id")}, 
                inverseJoinColumns={@JoinColumn(name="user_id")})
    public Set<User> getCospeakers() {
        return cospeakers;
    }

    public void setCospeakers(Set<User> cospeakers) {
        this.cospeakers = cospeakers;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    @NotNull
    @Enumerated(EnumType.STRING)
    public State getState() {
        return state;
    }

    @NotNull(message = "Session name field is required")
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @ManyToOne
    @JoinColumn(name = "track")
    public Track getTrack() {
        return track;
    }

    @Type(type="text")
    public String getDescription() {
        return description;
    }

    @Type(type="text")
    @Column(name = "refs")
    public String getReferences() {
        return references;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public Date getAdded() {
        return added;
    }

    @ManyToOne
    @JoinColumn(name = "user")
    public User getUser() {
        return user;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReferences(String references) {
        this.references = references;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public void setAdded(Date added) {
        this.added = added;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "talkformat")
    public TalkFormat getTalkFormat() {
        return talkformat;
    }

    public void setTalkFormat(TalkFormat talkformat) {
        this.talkformat = talkformat;
    }

    @Column(name="schedule_heure")
    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    @Column(name="schedule_date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Transient
    public int getDuree() {
        return talkformat.getDureeMinutes();
    }


    

}
