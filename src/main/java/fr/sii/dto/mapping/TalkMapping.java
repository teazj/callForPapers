package fr.sii.dto.mapping;

import org.springframework.stereotype.Component;

import fr.sii.config.mapping.Mapping;
import fr.sii.dto.Speaker;
import fr.sii.dto.TalkUser;
import fr.sii.dto.user.UserProfil;
import fr.sii.dto.user.CospeakerProfil;
import fr.sii.entity.Talk;
import fr.sii.dto.TalkAdmin;

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
			.field("talkFormat.id", "format")
            .exclude("cospeakers")
            .customize(new CustomMapper<Talk, TalkUser>() {
                @Override
                public void mapAtoB(Talk talk, TalkUser talkUser, MappingContext context) {
                    if (talk.getCospeakers() != null) {
                        talkUser.setCospeaker(mapperFactory.getMapperFacade().mapAsSet(talk.getCospeakers(), CospeakerProfil.class));
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

        mapperFactory.classMap(Talk.class, TalkAdmin.class)
                .field("user", "speaker")
                .field("track.id", "trackId")
                .field("track.libelle", "trackLabel")
    			.field("talkFormat.id", "format")
                .exclude("cospeakers")
                .customize(new CustomMapper<Talk, TalkAdmin>() {
                    @Override
                    public void mapAtoB(Talk talk, TalkAdmin talkUser, MappingContext context) {
                        if (talk.getCospeakers() != null) {
                            talkUser.setCospeaker(mapperFactory.getMapperFacade().mapAsSet(talk.getCospeakers(), CospeakerProfil.class));
                        }
                    }

                })
                .byDefault()
                .register();


        mapperFactory.classMap(User.class, Speaker.class)
            .byDefault()
            .register();


    }
}
