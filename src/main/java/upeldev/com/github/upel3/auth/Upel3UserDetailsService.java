package upeldev.com.github.upel3.auth;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.services.UserService;

@Service
public class Upel3UserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public Upel3UserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        if(new Validator().validateEmailAddress(username))
            user = userService.findByEmail(username);
        else throw new IllegalArgumentException("Email is incorrect");
        if (user == null) throw new UsernameNotFoundException("Could not find user");
        return new Upel3UserDetails(user);
    }

    public User registerNewUser(User userDto){
        String email = userDto.getEmail();
        if (userService.findByEmail(email) != null){
            throw new IllegalArgumentException(String.format("User with email %s already exists", email));
        }

        String passwordEncrypted = new BCryptPasswordEncoder().encode(userDto.getPassword());
        userDto.setPassword(passwordEncrypted);

        return userService.save(userDto);
    }
}
