package upeldev.com.github.upel3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.services.CourseService;
import upeldev.com.github.upel3.services.UserService;

@Component
public class DataLoader {

    private final UserService userService;
    private final CourseService courseService;

    @Autowired
    public DataLoader(UserService userService, CourseService courseService) {
        this.userService = userService;
        this.courseService = courseService;
    }

    public void populateUsers() {
        userService.save(new User("John", "john@gmail.com"));
        User kate = userService.save(new User("Kate", "kate@gmail.com"));
        courseService.createCourse("calculus", "xd", null, kate);
    }
}
