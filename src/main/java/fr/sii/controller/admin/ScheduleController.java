package fr.sii.controller.admin;

import fr.sii.dto.TalkUser;
import fr.sii.dto.user.Schedule;
import fr.sii.entity.Talk;
import fr.sii.entity.TalkFormat;
import fr.sii.service.TalkUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Created by Nicolas on 30/01/2016.
 */

@RestController
@RequestMapping(value = "api/admin", produces = "application/json; charset=utf-8")
public class ScheduleController {


    private final TalkUserService talkUserService;

    @Autowired
    public ScheduleController(TalkUserService talkUserService) {
        super();
        this.talkUserService = talkUserService;
    }

    @RequestMapping(value = "/scheduledtalks", method = RequestMethod.GET)
    public List<Schedule> getScheduleList() {
        List<TalkFormat> talkFormatList = talkUserService.getTalkFormat();
        Map<Integer, TalkFormat> talkFormatMap = talkFormatList.stream().collect(Collectors.toMap(TalkFormat::getId, Function.identity()));

        List<TalkUser> talkUserList = talkUserService.findAll(Talk.State.CONFIRMED);

        // List<TalkAdmin> talkList = talkAdminService.findAll(Talk.State.CONFIRMED);
        List<Schedule> scheduleList = talkUserList.stream().map(t -> {
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

        return scheduleList;
    }
}
