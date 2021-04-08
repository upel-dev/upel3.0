package upeldev.com.github.upel3.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
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
        User user = userService.findByEmail(username);
        if (user == null) throw new UsernameNotFoundException("Could not find user");
        return new Upel3UserDetails(user);
    }
}
