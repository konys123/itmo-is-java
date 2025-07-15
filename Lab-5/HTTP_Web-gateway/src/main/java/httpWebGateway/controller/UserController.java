package httpWebGateway.controller;


import httpWebGateway.Exceptions.OwnerAlreadyExistException;
import httpWebGateway.dao.UserDao;
import httpWebGateway.dto.UserDto;
import httpWebGateway.entities.Role;
import httpWebGateway.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody UserDto userDto) {
        if (userDao.findByName(userDto.getUsername()) != null)
            throw new OwnerAlreadyExistException("имя " + userDto.getUsername() + " уже занято");
        User user = new User();
        user.setName(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(Set.of(Role.USER));
        userDao.save(user);
        return ResponseEntity.noContent().build();
    }
}
