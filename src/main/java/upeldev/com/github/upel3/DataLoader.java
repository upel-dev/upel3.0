package upeldev.com.github.upel3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
    private void populateUsers() {
        userService.save(new User("John", "john@gmail.com"));
        User kate = userService.save(new User("Kate", "kate@gmail.com"));
        courseService.createCourse("calculus", "xd", null, kate);
        userService.save(new User("Kate", "kate@gmail.com"));
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

    }
}
