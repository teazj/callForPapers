package fr.sii.dto.mapping;

import org.springframework.stereotype.Component;

import fr.sii.config.mapping.Mapping;
import fr.sii.dto.TalkUser;
import fr.sii.dto.user.UserProfil;
import fr.sii.dto.user.CospeakerProfil;
import fr.sii.entity.Talk;
import fr.sii.entity.User;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;

@Component
public class TalkMapping implements Mapping {

    @Override
    public void mapClasses(MapperFactory mapperFactory) {
        mapperFactory.classMap(Talk.class, TalkUser.class)
            .field("user", "speaker")
            .field("track.id", "trackId")
            .field("track.libelle", "trackLabel")
            .exclude("cospeakers")
            .customize(new CustomMapper<Talk, TalkUser>() {
                @Override
                public void mapAtoB(Talk talk, TalkUser talkUser, MappingContext context) {
                    if (talk.getCospeakers() != null) {
                        talkUser.setCospeaker(mapperFactory.getMapperFacade().mapAsSet(talk.getCospeakers(), CospeakerProfil.class));
                    }
                }
                @Override
                public void mapBtoA(TalkUser talkUser,Talk talk, MappingContext context) {
                    if (talkUser.getCospeakers() != null) {
                        talk.setCospeakers(mapperFactory.getMapperFacade().mapAsSet(talkUser.getCospeakers(), User.class));
                    }
                }
            })
            .byDefault()
            .register();

        mapperFactory.classMap(User.class, CospeakerProfil.class)
            .byDefault()
            .register();

        mapperFactory.classMap(User.class, UserProfil.class)
            .byDefault()
            .register();

    }
}
