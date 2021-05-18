package upeldev.com.github.upel3.controllers;

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
import java.util.List;

@Controller
public class EditGradeController {
    private final UserService userService;
    private final CourseService courseService;
    private final ActivityService activityService;
    private final GradeService gradeService;
    private final SubGradeService subGradeService;
    private final StudentGroupService studentGroupService;

    @Autowired
    public EditGradeController(UserService userService,
                               CourseService courseService,
                               ActivityService activityService,
                               GradeService gradeService,
                               SubGradeService subGradeService,
                               StudentGroupService studentGroupService){
        this.userService = userService;
        this.courseService = courseService;
        this.activityService = activityService;
        this.gradeService = gradeService;
        this.subGradeService = subGradeService;
        this.studentGroupService = studentGroupService;
    }

    @RequestMapping(value = "/edit_grade/{courseId}/{activityId}/{userName}", method = RequestMethod.GET)
    public String editGrades(@PathVariable("courseId") Long courseId,
                               @PathVariable("activityId") Long activityId,
                               @PathVariable("userName")  String userName,
                               @RequestParam(value = "description")  String description,
                               @RequestParam("subGrade") double[] subActivityId,
                               Model model, Principal principal){
        User currentUser = userService.findByEmail(principal.getName());

        Course currentCourse = courseService.findCourseById(courseId);
        model.addAttribute("user", currentUser);

        Activity currentActivity = activityService.findActivityById(activityId);
        List<SubActivity> subActivities =  currentActivity.getSubActivities();

        try{
            if(!currentUser.getRoles().contains(Role.ADMIN) && !currentCourse.getLecturers().contains(currentUser)){
                String errorMsg = "Nie masz wystarczających uprawnień, aby utworzyć kurs";
                model.addAttribute("errorMsg", errorMsg);
                return "redirect:/error";
            }

            if(subActivities.size() != subActivityId.length){
                String errorMsg = "Nie wypełniono wszystkich wymaganych pól";
                model.addAttribute("errorMsg", errorMsg);
                return "redirect:/error";
            }

            Grade grade = findModifiedGrade(userName, currentCourse, currentActivity);
            boolean isNewGrade = isGradeNew(userName, currentCourse, currentActivity);
            editGrade(grade, subActivities, description, isNewGrade, subActivityId);
        }
        catch (IllegalArgumentException e){
            String errorMsg = "Podano nieprawidłowe argumenty podczas tworzenia kursu.";
            model.addAttribute("errorMsg", errorMsg);
            return "error";
        }

        return "redirect:/activity?id="+activityId;
    }

    private Grade findModifiedGrade(String userName, Course currentCourse, Activity currentActivity){
        Grade grade = null;

        User modifiedUser = userService.findByEmail(userName);
        StudentGroup modifiedGroup = studentGroupService.findByName(userName).isEmpty() ?
                null : studentGroupService.findByName(userName).get(0);

        if (modifiedUser != null){ // if student grade
            if (gradeService.findGradeByCourseAndUserAndActivity(currentCourse, modifiedUser, currentActivity).isEmpty()){
                grade = new StudentGrade(modifiedUser, currentActivity);
            }
            else{
                grade = gradeService.findGradeByCourseAndUserAndActivity(currentCourse, modifiedUser, currentActivity).get(0);
            }
        }
        else if (modifiedGroup != null){  // if group grade
            if (gradeService.findGradeByCourseAndGroupAndActivity(currentCourse, modifiedGroup, currentActivity).isEmpty()){
                grade = new GroupGrade(modifiedGroup, currentActivity);
            }
            else{
                grade = gradeService.findGradeByCourseAndGroupAndActivity(currentCourse, modifiedGroup, currentActivity).get(0);
            }
        }

        return grade;
    }

    private boolean isGradeNew(String userName, Course currentCourse, Activity currentActivity){
        boolean isNewGrade = false;

        User modifiedUser = userService.findByEmail(userName);
        StudentGroup modifiedGroup = studentGroupService.findByName(userName).isEmpty() ?
                null : studentGroupService.findByName(userName).get(0);

        if ((modifiedUser != null && gradeService.findGradeByCourseAndUserAndActivity(currentCourse, modifiedUser, currentActivity).isEmpty())
                || (modifiedGroup != null && gradeService.findGradeByCourseAndGroupAndActivity(currentCourse, modifiedGroup, currentActivity).isEmpty())){
            isNewGrade = true;
        }

        return isNewGrade;
    }

    private void editGrade(Grade grade, List<SubActivity> subActivities, String description, Boolean isNewGrade, double[] subActivityId){
        grade.setDescription(description);

        if (isNewGrade){
            for(int i = 0; i < subActivities.size(); i++){
                SubGrade subGrade = new SubGrade(subActivities.get(i), grade, subActivityId[i]);
                subGradeService.save(subGrade);
            }
        }
        else {
            for(int i = 0; i < subActivities.size(); i++){
                SubGrade subGrade = grade.getSubGrades().get(i);
                subGrade.setValue(subActivityId[i]);
                subGradeService.save(subGrade);
            }
        }

        gradeService.save(grade);
    }

    @RequestMapping(value = "/edit_grade/passval/{courseId}/{activityId}", method = RequestMethod.GET)
    public String editPassValue(@PathVariable("courseId") Long courseId,
                               @PathVariable("activityId") Long activityId,
                               @RequestParam(value = "passValue") Integer passValue,
                               Model model, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());

        Course currentCourse = courseService.findCourseById(courseId);
        model.addAttribute("user", currentUser);

        Activity currentActivity = activityService.findActivityById(activityId);

        activityService.changePassValue(currentActivity, passValue);

        return "redirect:/activity?id="+activityId;
    }
}
