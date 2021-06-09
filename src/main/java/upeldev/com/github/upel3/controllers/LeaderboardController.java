package upeldev.com.github.upel3.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import upeldev.com.github.upel3.model.Activity;
import upeldev.com.github.upel3.model.Course;
import upeldev.com.github.upel3.model.Grade;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.services.ActivityService;
import upeldev.com.github.upel3.services.CourseService;
import upeldev.com.github.upel3.services.GradeService;
import upeldev.com.github.upel3.services.UserService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;

@Controller
public class LeaderboardController {
    private final UserService userService;
    private final CourseService courseService;
    private final GradeService gradeService;
    private final ActivityService activityService;

    @Autowired
    public LeaderboardController(UserService userService, CourseService courseService, GradeService gradeService, ActivityService activityService){
        this.userService = userService;
        this.courseService = courseService;
        this.gradeService = gradeService;
        this.activityService = activityService;
    }

    @RequestMapping(value = "/leaderboard_lecturer",  method = RequestMethod.GET)
    public String leaderboardCourse(
            Model model,
            Principal principal,
            HttpServletRequest request)
    {
        Long id = Long.parseLong(request.getParameter("id"));
        Course currentCourse = courseService.findCourseById(id);
        model.addAttribute("course", currentCourse);

        Map<Double, User> userGradeMap = new HashMap<Double, User>();
        
        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("user", currentUser);

        Set<User> users = currentCourse.getEnrolledStudents();

        for (User user : users) {
            double userValue = courseService.getUserValueInCourse(currentCourse, user);
            userGradeMap.put(userValue, user);
        }

        Map<Double, User> sortedUserGradeMap = new TreeMap<Double, User>(userGradeMap).descendingMap();
        model.addAttribute("studentGrades", sortedUserGradeMap);

        return "leaderboard_lecturer";
    }

    @RequestMapping(value = "/leaderboard_student",  method = RequestMethod.GET)
    public String leaderboardCoursePersonal(
            Model model,
            Principal principal,
            HttpServletRequest request)
    {
        Long id = Long.parseLong(request.getParameter("id"));
        Course currentCourse = courseService.findCourseById(id);
        model.addAttribute("course", currentCourse);

        Map<Double, String> userGradeMap = new HashMap<Double, String>();

        double currentUserValue = 0;
        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("user", currentUser);

        Set<User> users = currentCourse.getEnrolledStudents();

        for (User user : users) {
            double userValue = courseService.getUserValueInCourse(currentCourse, user);
            userGradeMap.put(userValue, user.getEmail());
            if(currentUser == user)
                currentUserValue = userValue;
        }
        NavigableMap<Double, String> sortedUserGradeMap = new TreeMap<Double, String>(userGradeMap).descendingMap();

        int leaderboardNumber = 0;
        leaderboardNumber = sortedUserGradeMap.headMap(currentUserValue).size() + 1;
        model.addAttribute("number", leaderboardNumber);
        model.addAttribute("sum", currentUserValue);

        return "leaderboard_student";
    }

    @RequestMapping(value = "/activity_leaderboard/{courseId}/{activityId}", method = RequestMethod.GET)
    public String leaderboardActivity(@PathVariable("courseId") Long courseId,
                                      @PathVariable("activityId") Long activityId,
                                      Model model, Principal principal) {
        Course currentCourse = courseService.findCourseById(courseId);
        Activity currentActivity = activityService.findActivityById(activityId);
        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("course", currentCourse);
        model.addAttribute("activity", currentActivity);
        model.addAttribute("user", currentUser);

        Map<Double, User> userGradeMap = new HashMap<Double, User>();
        Set<User> users = currentCourse.getEnrolledStudents();

        for (User user : users) {
            Grade userGrade = activityService.getStudentGradeInActivity(currentActivity, user);
            double userValue = userGrade.getValue();
            userGradeMap.put(userValue, user);
        }

        Map<Double, User> sortedUserGradeMap = new TreeMap<Double, User>(userGradeMap).descendingMap();
        model.addAttribute("studentGrades", sortedUserGradeMap);

        return "activity_leaderboard";
    }
}
