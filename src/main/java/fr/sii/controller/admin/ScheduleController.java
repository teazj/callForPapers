package fr.sii.controller.admin;

import fr.sii.dto.TalkUser;
import fr.sii.dto.user.Schedule;
import fr.sii.dto.user.UserProfil;
import fr.sii.entity.Talk;
import fr.sii.service.TalkUserService;
import fr.sii.service.email.EmailingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * Created by Nicolas on 30/01/2016.
 */

@RestController
@RequestMapping(value = "api/admin", produces = "application/json; charset=utf-8")
public class ScheduleController {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduleController.class);

    private final TalkUserService talkUserService;

    private final EmailingService emailingService;

    @Autowired
    public ScheduleController(TalkUserService talkUserService, EmailingService emailingService) {
        super();
        this.talkUserService = talkUserService;
        this.emailingService = emailingService;
    }

    /**
     * Get all All talks.
     *
     * @return Confirmed talks in "LikeBox" format.
     */
    @RequestMapping(value = "/scheduledtalks/confirmed", method = RequestMethod.GET)
    public List<Schedule> getConfirmedScheduleList() {
        LOG.info("GET /scheduledtalks/confirmed");

        List<TalkUser> talkUserList = talkUserService.findAll(Talk.State.CONFIRMED);

        return getSchedules(talkUserList);
    }

    /**
     * Get all ACCEPTED talks'speakers .
     *
     * @return Speakers Set
     */
    @RequestMapping(value = "/scheduledtalks/accepted/speakers", method = RequestMethod.GET)
    public Set<UserProfil> getSpeakerList() {
        LOG.info("GET /scheduledtalks/accepted/speakers");

        List<TalkUser> talkUserList = talkUserService.findAll(Talk.State.ACCEPTED);

        return talkUserList.stream().map(t -> t.getSpeaker()).collect(toSet());
    }

    /**
     * Get all ACCEPTED talks.
     *
     * @return Accepted talks in "LikeBox" format.
     */
    @RequestMapping(value = "/scheduledtalks/accepted", method = RequestMethod.GET)
    public List<Schedule> getScheduleList() {
        LOG.info("GET /scheduledtalks/accepted");

        List<TalkUser> talkUserList = talkUserService.findAll(Talk.State.ACCEPTED);

        return getSchedules(talkUserList);
    }

    private List<Schedule> getSchedules(List<TalkUser> talkUserList) {
        return talkUserList.stream().map(t -> {
            Schedule schedule = new Schedule(t.getId(), t.getName(), t.getDescription());

            // speakers
            String spreakers = t.getSpeaker().getFirstname() + " " + t.getSpeaker().getLastname();
            if (t.getCospeakers() != null) {
                spreakers += ", " + t.getCospeakers().stream().map(c -> c.getFirstname() + " " + c.getLastname()).collect(Collectors.joining(", "));
            }
            schedule.setSpeakers(spreakers);

            // event_type
            schedule.setEventType(t.getTrackLabel());

            return schedule;
        }).collect(toList());
    }

    /**
     * Update talks with schedule data.
     *
     * @param scheduleList Accepted talks in "LikeBox" format.
     */
    @RequestMapping(value = "/scheduledtalks", method = RequestMethod.PUT)
    public Map<String, List<TalkUser>> putScheduleList(@RequestBody List<Schedule> scheduleList, @RequestParam(defaultValue = "false", required = false) boolean sendMail) {
        LOG.info("PUT /scheduledtalks");
        List<TalkUser> refused = talkUserService.findAll(Talk.State.ACCEPTED, Talk.State.CONFIRMED, Talk.State.REFUSED);
        List<TalkUser> accepted = new ArrayList<>();

        scheduleList.forEach(s -> {
            LocalDateTime dateEventStart = LocalDateTime.parse(s.getEventStart(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            // update database
            TalkUser talkUser = talkUserService.updateConfirmedTalk(s.getId(), dateEventStart);
            // add to confirmed list
            accepted.add(talkUser);
            // update refused list
            refused.remove(talkUser);
        });

        if (sendMail) {
            accepted.forEach(t -> emailingService.sendSelectionned(t, Locale.FRENCH));
            refused.forEach(t -> emailingService.sendNotSelectionned(t, Locale.FRENCH));
        }
        Map<String, List<TalkUser>> result = new HashMap<>();
        result.put("accepted", accepted);
        result.put("refused", refused);

        return result;
    }

    /**
     * Notify by mails scheduling result.
     */
    @RequestMapping(value = "/scheduledtalks/notification", method = RequestMethod.POST)
    public Map<String, List<TalkUser>> notifyScheduling() {
        LOG.info("POST /scheduledtalks/notification");
        List<TalkUser> refused = talkUserService.findAll(Talk.State.REFUSED);
        List<TalkUser> accepted = talkUserService.findAll(Talk.State.ACCEPTED);

        accepted.forEach(t -> emailingService.sendSelectionned(t, Locale.FRENCH));
        refused.forEach(t -> emailingService.sendNotSelectionned(t, Locale.FRENCH));

        Map<String, List<TalkUser>> result = new HashMap<>();
        result.put("confirmed", accepted);
        result.put("refused", refused);

        return result;
    }

}
