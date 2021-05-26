package upeldev.com.github.upel3.controllers.new_controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import upeldev.com.github.upel3.model.*;
import upeldev.com.github.upel3.services.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
public class NewGradeController {
    private final UserService userService;
    private final CourseService courseService;
    private final ActivityService activityService;
    private final GradeService gradeService;
    private final SubGradeService subGradeService;
    private final StudentGroupService studentGroupService;

    @Autowired
    public NewGradeController(UserService userService, CourseService courseService, ActivityService activityService, GradeService gradeService, SubGradeService subGradeService, StudentGroupService studentGroupService){
        this.userService = userService;
        this.courseService = courseService;
        this.activityService = activityService;
        this.gradeService = gradeService;
        this.subGradeService = subGradeService;
        this.studentGroupService = studentGroupService;
    }

    @RequestMapping(value = "/new_grade")
    public String newGrade(@RequestParam(value = "courseId")  Long courseId,
                           @RequestParam(value = "activityId")  Long activityId,
                           Model model, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("user", currentUser);
        Course currentCourse = courseService.findCourseById(courseId);
        model.addAttribute("course", currentCourse);

        Activity currentActivity = activityService.findActivityById(activityId);
        model.addAttribute("activity", currentActivity);

        return "new_grade";
    }

    @RequestMapping(value = "/create_grade/{courseId}/{activityId}", method = RequestMethod.GET)
    public String createGrade(@PathVariable("courseId") Long courseId,
                              @PathVariable("activityId") Long activityId,
                              @RequestParam(value = "userName")  String userName,
                              @RequestParam(value = "description")  String description,
                              @RequestParam("subGradeValues") String[] subGradeValues,
                              Model model,Principal principal){
        User currentUser = userService.findByEmail(principal.getName());

        User modifiedUser = userService.findByEmail(userName);

        Course currentCourse = courseService.findCourseById(courseId);
        model.addAttribute("user", currentUser);

        Activity currentActivity = activityService.findActivityById(activityId);


        List<SubActivity> subActivities =  currentActivity.getSubActivities();

        try{
            if(!currentUser.getRoles().contains(Role.ADMIN) && !currentCourse.getLecturers().contains(currentUser)){
                String errorMsg = "Nie masz wystarczających uprawnień, aby utworzyć kurs";
                model.addAttribute("errorMsg", errorMsg);
                return "error";
            }

            if(subActivities.size() != subGradeValues.length){
                String errorMsg = "Nie wypełniono wszystkich wymaganych pól";
                model.addAttribute("errorMsg", errorMsg);
                return "error";
            }

            double[] subGradeValuesDouble;
            try {
                subGradeValuesDouble = Arrays.stream(subGradeValues).mapToDouble(Double::parseDouble).toArray();
            }
            catch (NumberFormatException e){
                String errorMsg = "Nie wypełniono wszystkich wymaganych pól";
                model.addAttribute("errorMsg", errorMsg);
                return "error";
            }


            addOrModifyGrade(description, subGradeValuesDouble, currentCourse, currentActivity, subActivities, modifiedUser);

        }
        catch (IllegalArgumentException e){
            String errorMsg = "Błąd podczas dodawania oceny studentowi.";
            model.addAttribute("errorMsg", errorMsg);
            return "error";
        }

        return "redirect:/activity?id="+activityId;
    }

    @RequestMapping(value = "/create_group_grade/{courseId}/{activityId}", method = RequestMethod.GET)
    public String createGroupGrade(@PathVariable("courseId") Long courseId,
                                   @PathVariable("activityId") Long activityId,
                                   @RequestParam(value = "studentGroupId")  Long studentGroupId,
                                   @RequestParam(value = "description")  String description,
                                   @RequestParam("subGradeValues") String[] subGradeValues,
                                   Model model,Principal principal){
        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("user", currentUser);

        StudentGroup modifiedGroup = studentGroupService.findById(studentGroupId);

        Course currentCourse = courseService.findCourseById(courseId);
        Activity currentActivity = activityService.findActivityById(activityId);
        List<SubActivity> subActivities =  currentActivity.getSubActivities();

        try{
            if(!currentUser.getRoles().contains(Role.ADMIN) && !currentCourse.getLecturers().contains(currentUser)){
                String errorMsg = "Nie masz wystarczających uprawnień, aby utworzyć kurs";
                model.addAttribute("errorMsg", errorMsg);
                return "error";
            }

            if(subActivities.size() != subGradeValues.length){
                String errorMsg = "Nie wypełniono wszystkich wymaganych pól";
                model.addAttribute("errorMsg", errorMsg);
                return "/error";
            }

            double[] subGradeValuesDouble;
            try {
                subGradeValuesDouble = Arrays.stream(subGradeValues).mapToDouble(Double::parseDouble).toArray();
            }
            catch (NumberFormatException e){
                String errorMsg = "Nie wypełniono wszystkich wymaganych pól";
                model.addAttribute("errorMsg", errorMsg);
                return "error";
            }

            for(User modifiedUser : modifiedGroup.getStudents()) {
                addOrModifyGrade(description, subGradeValuesDouble, currentCourse, currentActivity, subActivities, modifiedUser);
            }
        }
        catch (IllegalArgumentException e){
            String errorMsg = "Bład podczas dodawania oceny grupie.";
            model.addAttribute("errorMsg", errorMsg);
            return "error";
        }

        return "redirect:/activity?id="+activityId;
    }

    private void addOrModifyGrade(String description,
                                  double[] subGradeValues,
                                  Course currentCourse, Activity currentActivity,
                                  List<SubActivity> subActivities,
                                  User modifiedUser) {
        if(gradeService.findGradeByCourseAndUserAndActivity(currentCourse, modifiedUser, currentActivity).isEmpty()){
            Grade newGrade = new Grade(modifiedUser, currentActivity);
            newGrade.setDescription(description);
            gradeService.save(newGrade);


            for(int i = 0; i < subActivities.size(); i++){
                SubGrade subGrade = new SubGrade(subActivities.get(i), newGrade, subGradeValues[i]);
                subGradeService.save(subGrade);
            }
        }
        else{
            Grade oldGrade = gradeService.findGradeByCourseAndUserAndActivity(currentCourse, modifiedUser, currentActivity).get(0);
            oldGrade.setDescription(description);
            for(int i = 0; i < subActivities.size(); i++){
                SubGrade subGrade = oldGrade.getSubGrades().get(i);
                subGrade.setValue(subGradeValues[i]);
            }
            gradeService.save(oldGrade);

        }
    }

}
