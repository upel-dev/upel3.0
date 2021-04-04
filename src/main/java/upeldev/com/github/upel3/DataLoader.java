package upeldev.com.github.upel3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import upeldev.com.github.upel3.auth.Upel3UserDetailsService;
import upeldev.com.github.upel3.model.*;
import upeldev.com.github.upel3.services.ActivityService;
import upeldev.com.github.upel3.services.GradeService;
import upeldev.com.github.upel3.services.CourseService;
import upeldev.com.github.upel3.services.UserService;

import java.util.List;

@Component
public class DataLoader {

    private final UserService userService;
    private final CourseService courseService;
    private final ActivityService activityService;
    private final GradeService gradeService;

    @Autowired
    public DataLoader(UserService userService, CourseService courseService, ActivityService activityService, GradeService gradeService) {
        this.userService = userService;
        this.courseService = courseService;
        this.activityService = activityService;
        this.gradeService = gradeService;
    }

    public void populateData(){
        populateUsers();
        populateCourses();
        populateActivity();
        populateGrade();
    }

    public void populateCourses(){
        courseService.createCourse("First course", "1234", "first description", userService.findByEmail("kate@gmail.com"));
        courseService.createCourse("Second course", "1234", "second description", userService.findByEmail("kate@gmail.com"));
    }

    public void populateUsers() {
        Upel3UserDetailsService uds = new Upel3UserDetailsService(userService);

        User john = new User("John", "Doe", "john@gmail.com", "1234");
        john.getRoles().add(Role.ADMIN);
        uds.registerNewUser(john);

        User kate = new User("Kate", "Smith", "kate@gmail.com", "1234");
        kate.getRoles().add(Role.LECTURER);
        uds.registerNewUser(kate);

        User benjamin = new User("Benjamin", "Ford", "benjamin@gmail.com", "1234");
        benjamin.getRoles().add(Role.STUDENT);
        benjamin.setIndexNumber("123456");
        uds.registerNewUser(benjamin);
    }
    private void populateActivity(){
        List<Course> allCourses = courseService.findAll();
        for(int i = 0; i < 5; i++){
            Activity activityToAppend = new Activity(allCourses.get(i%allCourses.size()), 0, 100, "Kolokwium " + i);
            activityService.save(activityToAppend);
        }

    }
    private void populateGrade() {
        List<User> users = userService.findAll();
        List<Activity> activities = activityService.findAll();

        for (int i = 0; i < 25; i++) {
            Grade grade = new Grade(users.get(i % users.size()), activities.get(i % activities.size()), 50);
            gradeService.save(grade);
        }
    }

}
