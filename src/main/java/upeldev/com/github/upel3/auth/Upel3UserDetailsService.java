package upeldev.com.github.upel3.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import upeldev.com.github.upel3.model.Role;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.services.UserService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class Upel3UserDetailsService implements UserDetailsService {
    private static final String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private final Pattern pattern;

    private final UserService userService;

    @Autowired
    public Upel3UserDetailsService(UserService userService) {
        this.userService = userService;
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByEmail(username);
        if (user == null) throw new UsernameNotFoundException("Could not find user");
        return new Upel3UserDetails(user);
    }

    public User registerNewUser(User userDto){
        String email = userDto.getEmail();

        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(String.format("Email %s is invalid", email));
        }

        if (userService.findByEmail(email) != null){
            throw new IllegalArgumentException(String.format("User with email %s already exists", email));
        }

        if (userDto.getRoles().contains(Role.STUDENT) && userDto.getIndexNumber() == null){
            throw new IllegalArgumentException("Student has to have an index number");
        }

        String passwordEncrypted = new BCryptPasswordEncoder().encode(userDto.getPassword());
        userDto.setPassword(passwordEncrypted);

        return userService.save(userDto);
    }
}
