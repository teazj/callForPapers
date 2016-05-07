package io.cfp.repository;

import io.cfp.entity.Role;
import io.cfp.repository.config.RepositoriesTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {RepositoriesTest.class})
@DirtiesContext
public class RoleRepositoryTest {

    private static final int USER_ID = 10;
    private static final String EVENT_ID = "EVENT_ID";

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void should() {
        final List<Role> returnedRoles = roleRepository.findByUserIdAndEventId(USER_ID, EVENT_ID);
        assertThat(returnedRoles).isNotEmpty();
    }


}
