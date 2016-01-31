package fr.sii.domain.admin.meter;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Created by tmaugin on 16/07/2015.
 * SII
 */
public class AdminMeter implements Serializable {

    private Counts meter;

    public Counts getMeter() {
        return meter;
    }

    public AdminMeter() {
        this.meter = new Counts();
    }

    @JsonIgnore
    public Integer getSpeakers() {
        return this.meter.speakers;
    }

    public void setSpeakers(Integer speakers) {
        this.meter.speakers = speakers;
    }

    @JsonIgnore
    public Integer getDrafts() {
        return this.meter.drafts;
    }

    public void setDrafts(Integer drafts) {
        this.meter.drafts = drafts;
    }

    @JsonIgnore
    public Integer getTalks() {
        return this.meter.talks;
    }

    public void setTalks(Integer talks) {
        this.meter.talks = talks;
    }

    private class Counts  implements Serializable{
        private Integer speakers;
        private Integer drafts;
        private Integer talks;

        public Integer getSpeakers() {
            return speakers;
        }

        public Integer getDrafts() {
            return drafts;
        }

        public Integer getTalks() {
            return talks;
        }

        public Counts() {
        }
    }
}
