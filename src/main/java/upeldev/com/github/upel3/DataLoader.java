package upeldev.com.github.upel3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import upeldev.com.github.upel3.model.*;
import upeldev.com.github.upel3.model.achievement.Achievement;
import upeldev.com.github.upel3.model.achievement.AchievementType;
import upeldev.com.github.upel3.model.achievement.StudentAchievement;
import upeldev.com.github.upel3.services.*;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class DataLoader {

    private final UserService userService;
    private final CourseService courseService;
    private final ActivityService activityService;
    private final GradeService gradeService;
    private final SubActivityService subActivityService;
    private final SubGradeService subGradeService;
    private final StudentGroupService studentGroupService;
    private final AchievementService achievementService;
    private final StudentAchievementService studentAchievementService;

    @Autowired
    public DataLoader(UserService userService,
                      CourseService courseService,
                      ActivityService activityService,
                      GradeService gradeService,
                      SubActivityService subActivityService,
                      SubGradeService subGradeService,
                      StudentGroupService studentGroupService,
                      AchievementService achievementService,
                      StudentAchievementService studentAchievementService
    ) {
        this.userService = userService;
        this.courseService = courseService;
        this.activityService = activityService;
        this.subActivityService = subActivityService;
        this.gradeService = gradeService;
        this.subGradeService = subGradeService;
        this.studentGroupService = studentGroupService;
        this.achievementService = achievementService;
        this.studentAchievementService = studentAchievementService;

    }

    public void populateData(){
        populateUsers();
        populateCourses();
        populateAchievements();
        populateActivity();
        populateGrade();
        populateSubActivity();
        populateSubGrade();
        populateGroups();
    }

    public void populateCourses(){
        Course firstCourse = courseService.createCourse("Systemy operacyjne", "Celem wyk??adu jest zapoznanie z budow?? i zasad?? dzia??ania systemu operacyjnego, jego elementami sk??adowymi oraz z problemami jakie nale??y rozwi??za??, buduj??c nowy system.", userService.findByEmail("kate@gmail.com"));

        courseService.addStudentToCourse(firstCourse.getId(), "benjamin@gmail.com");
        courseService.addStudentToCourse(firstCourse.getId(), "bmw@gmail.com");

        firstCourse.setPassValue(100.0);
        courseService.save(firstCourse);

        Course secondCourse = courseService.createCourse("In??ynieria oprogramowania", "Cykl ??ycia oprogramowania. Proces produkcji oprogramowania. In??ynieria system??w komputerowych. Zarz??dzanie przedsi??wzi??ciami w in??ynierii oprogramowania. Wymagania wobec oprogramowania i proces in??ynierii wymaga??. ", userService.findByEmail("kate@gmail.com"));
        courseService.addStudentToCourse(secondCourse.getId(), "benjamin@gmail.com");

        secondCourse.setPassValue(100.0);

        userService.hideCourse(userService.findByEmail("benjamin@gmail.com"), secondCourse);

        courseService.save(secondCourse);
    }

    public void populateGroups(){
        Set<User> students = new HashSet<>();
        students.add(userService.findByEmail("benjamin@gmail.com"));
        //students.add(userService.findByEmail("bmw@gmail.com"));

        List<Course> courses = courseService.findAll();
        Course course = courses.get(0);

        studentGroupService.createStudentGroup("First Group", course, students);
    }

    public void populateUsers() {
        User john = new User("Jan", "Kowalski", "john@gmail.com", "1234");
        john.getRoles().add(Role.ADMIN);
        userService.registerNewUser(john);

        User kate = new User("Katarzyna", "Kowal", "kate@gmail.com", "1234");
        kate.getRoles().add(Role.LECTURER);
        userService.registerNewUser(kate);

        User benjamin = new User("Benjamin", "Ford", "benjamin@gmail.com", "1234");
        benjamin.getRoles().add(Role.STUDENT);
        benjamin.setIndexNumber("123456");
        userService.registerNewUser(benjamin);

        User bmw = new User("Zygfryd", "Kaczmarczyk", "bmw@gmail.com", "1234");
        bmw.getRoles().add(Role.STUDENT);
        bmw.setIndexNumber("123457");
        userService.registerNewUser(bmw);

    }
    private void populateActivity(){

        List<Course> allCourses = courseService.findAll();

        List<String> names = List.of("Zestaw", "Kolokwium");

        List<String> descriptions = List.of(
                "Zaprojektuj i przygotuj zestaw funkcji (bibliotek??) do zarz??dzania tablic?? blok??w",
                "Celem zadania jest napisanie programu por??wnuj??cego wydajno???? systemowych i bibliotecznych funkcji wej??cia/wyj??cia. Program operowa?? b??dzie na przechowywanej w pliku tablicy rekord??w.",
                "Napisz program monitor do robienia kopii zapasowych plik??w, zawartych w pliku lista podawanym jako argument programu.",
                "Napisz dwa programy: sender program wysy??aj??cy sygna??y SIGUSR1 i catcher - program zliczaj??cy ilo???? odebranych sygna????w."
        );

        for(int c = 0; c < allCourses.size(); c++) {
            for (int i = 0; i < descriptions.size(); i++) {
                Activity newActivity = new Activity(allCourses.get(c), 10, names.get(c) + " "  + (i + 1));
                newActivity.setDescription(descriptions.get(i));
                activityService.save(newActivity);
            }
        }

    }
    private void populateGrade() {

        List<User> users = userService.findAll();
        List<Activity> activities = activityService.findAll();

        for (Activity activity : activities) {
            Set<User> usersInCourse = activity.getCourse().getEnrolledStudents();
            Iterator<User> userIterator = usersInCourse.iterator();

            for (int j = 0; j < usersInCourse.size(); j++) {

                User currentUser = userIterator.next();

                Grade grade = new Grade(currentUser, activity);
                grade.setDescription("Opis " + (j + 1));
                gradeService.save(grade);

            }
        }

    }

    private void populateSubActivity() {
        List<Activity> activities = activityService.findAll();

        for (int i = 0; i < 25; i++) {
            SubActivity subActivity = new SubActivity(activities.get(i%activities.size()), 10, "Zadanie " + (i + 1));
            subActivityService.save(subActivity);
        }
    }
    public void populateSubGrade(){
        List<SubActivity> subActivities = subActivityService.findAll();
        for(SubActivity subActivity : subActivities){
            for(Grade grade : subActivity.getActivity().getGrades()){
                SubGrade subGrade = new SubGrade(subActivity, grade, (int)(Math.random()*subActivity.getMaxValue()));
                grade.getSubGrades().add(subGrade);
                subGradeService.save(subGrade);
                gradeService.save(grade);
            }

        }
    }

    public void populateAchievements(){

        Course course = courseService.findAll().get(0);
        Achievement passedActivityAchievement = new Achievement(AchievementType.PASSED_ACTIVITIES, course);
        Achievement maxActivityAchievement = new Achievement(AchievementType.MAXED_ACTIVITIES, course);
        Achievement maxedSubActivityAchievement = new Achievement(AchievementType.MAXED_SUBACTIVITIES, course);
        achievementService.save(passedActivityAchievement);
        achievementService.save(maxedSubActivityAchievement);
        achievementService.save(maxActivityAchievement);
    }
}
