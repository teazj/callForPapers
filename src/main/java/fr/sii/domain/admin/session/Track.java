package fr.sii.domain.admin.session;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tmaugin on 27/07/2015.
 * SII
 */
public class Track {

    private final List<String> validValues = Arrays.asList("cloud", "mobile", "discovery", "web");

    @NotNull
    private String track;

    @JsonIgnore
    @AssertTrue(message = "Track field must be one of \"cloud\", \"mobile\", \"discovery\" or \"web\".")
    public boolean getValidValues(){
        return validValues.contains(this.getTrack());
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }
}
