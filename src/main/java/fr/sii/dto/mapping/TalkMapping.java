package fr.sii.dto.mapping;

import org.springframework.stereotype.Component;

import fr.sii.config.mapping.Mapping;
import fr.sii.dto.TalkUser;
import fr.sii.dto.user.UserProfil;
import fr.sii.entity.Talk;
import fr.sii.entity.User;
import ma.glasnost.orika.MapperFactory;

@Component
public class TalkMapping implements Mapping {

    @Override
    public void mapClasses(MapperFactory mapperFactory) {
        mapperFactory.classMap(Talk.class, TalkUser.class)
            .field("user", "speaker")
            .field("track.id", "track")
            .field("track.libelle", "trackLabel")
            .field("talkFormat.id", "format")
            .byDefault()
            .register();

        mapperFactory.classMap(User.class, UserProfil.class)
            .byDefault()
            .register();

    }
}
