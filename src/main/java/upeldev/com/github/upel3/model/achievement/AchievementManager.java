package upeldev.com.github.upel3.model.achievement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import upeldev.com.github.upel3.model.Activity;
import upeldev.com.github.upel3.model.Course;
import upeldev.com.github.upel3.model.Grade;
import upeldev.com.github.upel3.services.AchievementService;
import upeldev.com.github.upel3.services.ActivityService;
import upeldev.com.github.upel3.services.StudentAchievementService;

import java.util.Set;

@Component
public class AchievementManager {

    private final StudentAchievementService studentAchievementService;
    private final AchievementService achievementService;

    @Autowired
    public AchievementManager(
            AchievementService achievementService,
            StudentAchievementService studentAchievementService){

        this.achievementService = achievementService;
        this.studentAchievementService = studentAchievementService;
    }

    @EventListener
    public void handleAchievementEvent(AchievementEvent<Grade> gradeEvent){

        Grade grade = gradeEvent.getValue();
        Activity activity = grade.getActivity();
        Course course = activity.getCourse();

        Set<AchievementType> activeTypes = achievementService.findAchievementTypesInCourse(course);

        if(activeTypes.contains(AchievementType.MAXED_ACTIVITIES) &&
                grade.getValue() == activity.getValue() && activity.getValue() != 0) {
            studentAchievementService.createOrUpdate(grade, AchievementType.MAXED_ACTIVITIES);
        }
        if(activeTypes.contains(AchievementType.PASSED_ACTIVITIES) &&
                grade.getValue() >= activity.getPassValue()){
            studentAchievementService.createOrUpdate(grade, AchievementType.PASSED_ACTIVITIES);
        }

    }
}
