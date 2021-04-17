package upeldev.com.github.upel3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
        Course firstCourse = courseService.createCourse("First course", "first description", userService.findByEmail("kate@gmail.com"));
        Course secondCourse = courseService.createCourse("Second course", "second description", userService.findByEmail("kate@gmail.com"));
        courseService.addStudentToCourse(firstCourse, userService.findByEmail("benjamin@gmail.com"));
    }

    public void populateUsers() {
        User john = new User("John", "Doe", "john@gmail.com", "1234");
        john.getRoles().add(Role.ADMIN);
        userService.registerNewUser(john);

        User kate = new User("Kate", "Smith", "kate@gmail.com", "1234");
        kate.getRoles().add(Role.LECTURER);
        userService.registerNewUser(kate);

        User benjamin = new User("Benjamin", "Ford", "benjamin@gmail.com", "1234");
        benjamin.getRoles().add(Role.STUDENT);
        benjamin.setIndexNumber("123456");
        userService.registerNewUser(benjamin);

        User ron = new User("Ron", "Dill", "ron@gmail.com", "1234");
        ron.getRoles().add(Role.STUDENT);
        ron.setIndexNumber("123457");
        userService.registerNewUser(ron);
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
            User user = users.get(i % users.size());
            Activity activity = activities.get(i % activities.size());
            if(!activity.getCourse().getLecturers().contains(user) && activity.getCourse().getEnrolledStudents().contains(user)) {
                Grade grade = new Grade(user, activity, 50);
                gradeService.save(grade);
            }
        }
    }

}
