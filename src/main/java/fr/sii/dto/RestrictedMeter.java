package fr.sii.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Created by Thomas on 24/08/2015.
 */
public class RestrictedMeter implements Serializable  {
    private Counts meter;

    public Counts getMeter() {
        return meter;
    }

    public RestrictedMeter() {
        this.meter = new Counts();
    }

    @JsonIgnore
    public Integer getTalks() {
        return this.meter.talks;
    }

    public void setTalks(Integer talks) {
        this.meter.talks = talks;
    }

    private class Counts  implements Serializable {
        private Integer talks;

        public Integer getTalks() {
            return talks;
        }

        public Counts() {
        }
    }
}
