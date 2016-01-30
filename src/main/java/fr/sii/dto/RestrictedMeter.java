package fr.sii.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Created by Thomas on 24/08/2015.
 */
public class RestrictedMeter implements Serializable {
    private Counts meter;

    public RestrictedMeter() {
        this.meter = new Counts();
    }

    public Counts getMeter() {
        return meter;
    }

    @JsonIgnore
    public Integer getTalks() {
        return this.meter.talks;
    }

    public void setTalks(Integer talks) {
        this.meter.talks = talks;
    }

    private class Counts implements Serializable {
        private Integer talks;

        public Counts() {
        }

        public Integer getTalks() {
            return talks;
        }
    }
}
