package upeldev.com.github.upel3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import upeldev.com.github.upel3.auth.Upel3UserDetailsService;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.services.UserService;

@Component
public class DataLoader {

    private final UserService userService;

    @Autowired
    public DataLoader(UserService userService) {
        this.userService = userService;
    }

    public void populateUsers() {
        Upel3UserDetailsService uds = new Upel3UserDetailsService(userService);
        User john = uds.registerNewUser(new User("John", "Doe", "john@gmail.com", "1234"));
        User kate = uds.registerNewUser(new User("Kate", "Smith", "kate@gmail.com", "1234"));
    }
}
