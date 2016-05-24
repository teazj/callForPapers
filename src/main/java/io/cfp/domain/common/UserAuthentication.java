package io.cfp.domain.common;

import io.cfp.entity.Role;
import io.cfp.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserAuthentication implements Authentication {

    private final User user;
    private final List<SimpleGrantedAuthority> authorities;
    private boolean authenticated = true;

    public UserAuthentication(User user, Collection<Role> roles) {
        this.user = user;
        authorities = roles.stream()
            .map(role -> {
                final String name = role.getName();
                user.addRole(name);
                return new SimpleGrantedAuthority(name); })
            .collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority(Role.AUTHENTICATED));
    }

    @Override
    public String getName() {
        return user.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return "****";
    }

    @Override
    public User getDetails() {
        return user;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}
