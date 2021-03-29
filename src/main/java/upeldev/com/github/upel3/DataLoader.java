package upeldev.com.github.upel3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import upeldev.com.github.upel3.auth.Upel3UserDetailsService;
import upeldev.com.github.upel3.model.Role;
import upeldev.com.github.upel3.model.Grade;
import upeldev.com.github.upel3.model.IndividualGrade;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.services.GradeService;
import upeldev.com.github.upel3.services.IndividualGradeService;
import upeldev.com.github.upel3.services.CourseService;
import upeldev.com.github.upel3.services.UserService;

import java.util.List;

@Component
public class DataLoader {

    private final UserService userService;
    private final CourseService courseService;
    private final GradeService gradeService;
    private final IndividualGradeService individualGradeService;

    @Autowired
    public DataLoader(UserService userService, CourseService courseService, GradeService gradeService, IndividualGradeService individualGradeService) {
        this.userService = userService;
        this.courseService = courseService;
        this.gradeService = gradeService;
        this.individualGradeService = individualGradeService;
    }

    public void populateData(){
        populateUsers();
        populateGrades();
        populateIndividualGrade();
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
    private void populateGrades(){
        for(int i = 0; i < 5; i++){
            Grade gradeToAppend = new Grade(0, 100, "Kolokwium " + i);
            gradeService.save(gradeToAppend);
        }

    }
    private void populateIndividualGrade(){
        List<User> users = userService.findAll();
        List<Grade> grades = gradeService.findAll();

        for(int i = 0; i < 25; i++){
            IndividualGrade individualGrade = new IndividualGrade(users.get(i%users.size()), grades.get(i%grades.size()), 50);
            individualGradeService.save(individualGrade);
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
}
