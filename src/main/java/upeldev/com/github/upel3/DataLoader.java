package upeldev.com.github.upel3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import upeldev.com.github.upel3.model.*;
import upeldev.com.github.upel3.services.*;

import java.util.List;

@Component
public class DataLoader {

    private final UserService userService;
    private final CourseService courseService;
    private final ActivityService activityService;
    private final GradeService gradeService;
    private final SubActivityService subActivityService;
    private final SubGradeService subGradeService;
    @Autowired
    public DataLoader(UserService userService, CourseService courseService, ActivityService activityService, GradeService gradeService, SubActivityService subActivityService, SubGradeService subGradeService) {
        this.userService = userService;
        this.courseService = courseService;
        this.activityService = activityService;
        this.subActivityService = subActivityService;
        this.gradeService = gradeService;
        this.subGradeService = subGradeService;
    }

    public void populateData(){
        populateUsers();
        populateCourses();
        populateActivity();
        populateGrade();
        populateSubActivity();
        populateSubGrade();
    }

    public void populateCourses(){
        Course firstCourse = courseService.createCourse("First course", "first description", userService.findByEmail("kate@gmail.com"));
        courseService.addStudentToCourse(firstCourse.getId(), "benjamin@gmail.com");
        courseService.addStudentToCourse(firstCourse.getId(), "bmw@gmail.com");

        Course secondCourse = courseService.createCourse("Second course", "second description", userService.findByEmail("kate@gmail.com"));
        courseService.addStudentToCourse(secondCourse.getId(), "benjamin@gmail.com");

        userService.hideCourse(userService.findByEmail("benjamin@gmail.com"), secondCourse);

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

        User bmw = new User("Benjamin", "Bmw", "bmw@gmail.com", "1234");
        bmw.getRoles().add(Role.STUDENT);
        bmw.setIndexNumber("123457");
        userService.registerNewUser(bmw);

    }
    private void populateActivity(){
        List<Course> allCourses = courseService.findAll();
        for(int i = 0; i < 5; i++){
            Activity activityToAppend = new Activity(allCourses.get(i%allCourses.size()), 100, "Kolokwium " + i);
            activityToAppend.setDescription("Opis tej aktywności pewnie jest bardzo znaczący xd");
            activityService.save(activityToAppend);
        }

    }
    private void populateGrade() {
        List<User> users = userService.findAll();
        List<Activity> activities = activityService.findAll();

        for (int i = 0; i < 25; i++) {
            User user = users.get(i % users.size());
            Activity activity = activities.get(i % activities.size());
            Course course = activity.getCourse();
            if(!activity.getCourse().getLecturers().contains(user) && activity.getCourse().getEnrolledStudents().contains(user) && gradeService.findGradeByCourseUserActivity(course, user, activity).size()==0) {
                Grade grade = new Grade(user, activity);
                grade.setDescription("Opis " + i);
                gradeService.save(grade);
            }
        }
    }

    private void populateSubActivity() {
        List<Activity> activities = activityService.findAll();

        for (int i = 0; i < 25; i++) {
            SubActivity subActivity = new SubActivity(activities.get(i%activities.size()), 10, "Zadanie " + i);
            subActivityService.save(subActivity);
        }
    }
    public void populateSubGrade(){
        List<SubActivity> subActivities = subActivityService.findAll();
        for(SubActivity subActivity : subActivities){
            for(Grade grade : subActivity.getActivity().getGrade()){
                SubGrade subGrade = new SubGrade(subActivity, grade, (int)(Math.random()*40));
                subGradeService.save(subGrade);
            }
        }
    }
}
