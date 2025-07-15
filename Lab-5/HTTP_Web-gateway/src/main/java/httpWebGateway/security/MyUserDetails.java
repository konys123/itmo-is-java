package httpWebGateway.security;

import httpWebGateway.entities.User;
import httpWebGateway.entities.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class MyUserDetails implements UserDetails {
    @Getter
    private final Long id;
    private final String username;
    private final String password;
    private final Set<GrantedAuthority> authorities;
    private final Set<Role> roles;


    public MyUserDetails(User User) {
        this.id = User.getId();
        this.username = User.getName();
        this.password = User.getPassword();
        this.roles = User.getRoles();
        this.authorities = User.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());
    }

    public Set<Role> getRolesSet() {
        return roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

}