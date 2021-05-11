package upeldev.com.github.upel3.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import upeldev.com.github.upel3.model.*;
import upeldev.com.github.upel3.services.*;

import java.security.Principal;
import java.util.List;

@Controller
public class ActivitySettingsController {
    private final UserService userService;
    private final CourseService courseService;
    private final ActivityService activityService;
    private final GradeService gradeService;
    private final SubGradeService subGradeService;
    private final SubActivityService subActivityService;

    @Autowired
    public ActivitySettingsController(UserService userService, CourseService courseService, ActivityService activityService,
                                      GradeService gradeService, SubGradeService subGradeService, SubActivityService subActivityService){
        this.userService = userService;
        this.courseService = courseService;
        this.activityService = activityService;
        this.subActivityService = subActivityService;
        this.gradeService = gradeService;
        this.subGradeService = subGradeService;
    }

    @RequestMapping(value = "/activity_settings/{courseId}/{activityId}", method = RequestMethod.GET)
    public String editGrades(@PathVariable("courseId") Long courseId,
                             @PathVariable("activityId") Long activityId,
                             Model model, Principal principal){
        User currentUser = userService.findByEmail(principal.getName());

        Course currentCourse = courseService.findCourseById(courseId);
        model.addAttribute("user", currentUser);

        Activity currentActivity = activityService.findActivityById(activityId);
        model.addAttribute("activity", currentActivity);
        List<SubActivity> subActivities =  currentActivity.getSubActivities();

        try{
            if(!currentUser.getRoles().contains(Role.ADMIN) && !currentCourse.getLecturers().contains(currentUser)){
                String errorMsg = "Nie masz wystarczających uprawnień, aby edytować aktywność";
                model.addAttribute("errorMsg", errorMsg);
                return "redirect:/error";
            }
        }
        catch (IllegalArgumentException e){
            String errorMsg = "Podano nieprawidłowe argumenty podczas tworzenia kursu.";
            model.addAttribute("errorMsg", errorMsg);
            return "error";
        }
        model.addAttribute("course", currentCourse);
        model.addAttribute("activity", currentActivity);
        return "activity_settings";
    }

    @RequestMapping(value = "/activity_settings/passval/{courseId}/{activityId}", method = RequestMethod.GET)
    public String editPassValue(@PathVariable("courseId") Long courseId,
                                @PathVariable("activityId") Long activityId,
                                @RequestParam(value = "passValue") Integer passValue,
                                Model model, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());

        Course currentCourse = courseService.findCourseById(courseId);
        model.addAttribute("user", currentUser);

        Activity currentActivity = activityService.findActivityById(activityId);

        activityService.changePassValue(currentActivity, passValue);

        return String.format("redirect:/activity_settings/%d/%d", courseId, activityId);
    }

    @RequestMapping(value = "/activity_settings/description/{courseId}/{activityId}", method = RequestMethod.GET)
    public String editDescription(@PathVariable("courseId") Long courseId,
                                @PathVariable("activityId") Long activityId,
                                @RequestParam(value = "description") String description,
                                Model model, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());

        Course currentCourse = courseService.findCourseById(courseId);
        model.addAttribute("user", currentUser);

        Activity currentActivity = activityService.findActivityById(activityId);

        activityService.changeDescription(currentActivity, description);

        return String.format("redirect:/activity_settings/%d/%d", courseId, activityId);
    }

    @RequestMapping(value = "/activity_settings/subactivity_edit/{courseId}/{activityId}/{subActivityId}", method = RequestMethod.GET)
    public String editSubActivity(@PathVariable("courseId") Long courseId,
                                  @PathVariable("activityId") Long activityId,
                                  @PathVariable("subActivityId") Long subActivityId,
                                  @RequestParam(value = "subActivityName") String subActivityName,
                                  @RequestParam(value = "subActivityMax") Integer subActivityMax,
                                  Model model, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());

        Course currentCourse = courseService.findCourseById(courseId);
        model.addAttribute("user", currentUser);

        Activity currentActivity = activityService.findActivityById(activityId);

        for(SubActivity subActivity : subActivityService.findByActivity(currentActivity)){
            if(subActivity.getId().equals(subActivityId)){
                if(!subActivityName.isEmpty()){
                    subActivityService.changeName(subActivity, subActivityName);
                }
                if(subActivityMax >= 0){
                    subActivityService.changeMaxValue(subActivity, subActivityMax);
                }
                break;
            }
        }

        return String.format("redirect:/activity_settings/%d/%d", courseId, activityId);
    }

    @RequestMapping(value = "/activity_settings/subactivity_delete/{courseId}/{activityId}/{subActivityId}", method = RequestMethod.GET)
    public String deleteSubActivity(@PathVariable("courseId") Long courseId,
                                  @PathVariable("activityId") Long activityId,
                                  @PathVariable("subActivityId") Long subActivityId,
                                  Model model, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());

        Course currentCourse = courseService.findCourseById(courseId);
        model.addAttribute("user", currentUser);

        Activity currentActivity = activityService.findActivityById(activityId);

        for(SubActivity subActivity : subActivityService.findByActivity(currentActivity)){
            if(subActivity.getId().equals(subActivityId)){
                activityService.removeSubActivity(currentActivity, subActivity);
                break;
            }
        }

        return String.format("redirect:/activity_settings/%d/%d", courseId, activityId);
    }

    @RequestMapping(value = "/activity_settings/name/{courseId}/{activityId}", method = RequestMethod.GET)
    public String changeActivityName(@PathVariable("courseId") Long courseId,
                                     @PathVariable("activityId") Long activityId,
                                     @RequestParam(value = "activityName") String activityName,
                                    Model model, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());

        Course currentCourse = courseService.findCourseById(courseId);
        model.addAttribute("user", currentUser);

        Activity currentActivity = activityService.findActivityById(activityId);

        if(!activityName.isEmpty()) {
            activityService.changeName(currentActivity, activityName);
        }

        return String.format("redirect:/activity_settings/%d/%d", courseId, activityId);
    }

    @RequestMapping(value = "/activity_settings/grade_aggregation/{courseId}/{activityId}", method = RequestMethod.POST)
    public String editAggregation(@PathVariable("courseId") Long courseId,
                                @PathVariable("activityId") Long activityId,
                                @ModelAttribute(value = "activity") Activity activity,
                                Model model, Principal principal) {

        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("user", currentUser);

        Course currentCourse = courseService.findCourseById(courseId);

        Activity currentActivity = activityService.findActivityById(activityId);
        model.addAttribute("activity", currentActivity);

        if(activity.getAggregation() != null){
            activityService.changeAggregation(currentActivity, activity.getAggregation());
        }

        return String.format("redirect:/activity_settings/%d/%d", courseId, activityId);

    }
}
