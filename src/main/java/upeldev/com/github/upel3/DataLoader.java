package upeldev.com.github.upel3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
        userService.save(new User("John", "john@gmail.com"));
        userService.save(new User("Kate", "kate@gmail.com"));
    }
}
