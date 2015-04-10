package fr.sii.persistance.user;

/**
 * Created by tmaugin on 07/04/2015.
 */
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "people", path = "people")
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    List<User> findByLastName(@Param("name") String name);
}