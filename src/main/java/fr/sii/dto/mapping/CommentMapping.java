package fr.sii.dto.mapping;

import fr.sii.config.mapping.Mapping;
import fr.sii.dto.CommentUser;
import fr.sii.dto.user.UserProfil;
import fr.sii.entity.Comment;
import fr.sii.entity.User;
import ma.glasnost.orika.MapperFactory;
import org.springframework.stereotype.Component;

@Component
public class CommentMapping implements Mapping {

    @Override
    public void mapClasses(MapperFactory mapperFactory) {
        mapperFactory.classMap(Comment.class, CommentUser.class)
            .byDefault()
            .register();

        mapperFactory.classMap(User.class, UserProfil.class)
            .byDefault()
            .register();


    }
}
