package lab3.services;

import lab3.dao.OwnerDao;
import lab3.entities.Owner;
import lombok.RequiredArgsConstructor;
import lab3.entities.Role;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class OwnerDetailsService implements UserDetailsService {
    private final OwnerDao ownerDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Owner owner = ownerDao.findByName(username);

        return User.withUsername(owner.getName())
                .password(owner.getPassword())
                .roles(owner.getRoles().stream()
                        .map(Role::name)
                        .toArray(String[]::new))
                .build();
    }
}
