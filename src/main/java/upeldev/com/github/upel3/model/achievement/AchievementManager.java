package upeldev.com.github.upel3.model.achievement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import upeldev.com.github.upel3.model.*;
import upeldev.com.github.upel3.model.achievement.event.AchievementEvent;
import upeldev.com.github.upel3.model.achievement.event.EventType;
import upeldev.com.github.upel3.model.achievement.event.GradeEvent;
import upeldev.com.github.upel3.model.achievement.event.SubGradeEvent;
import upeldev.com.github.upel3.services.AchievementService;
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
    public void handleAchievementEvent(GradeEvent gradeEvent){

        Grade grade = gradeEvent.getValue();
        Activity activity = grade.getActivity();
        Course course = activity.getCourse();

        Set<AchievementType> activeTypes = achievementService.findAchievementTypesInCourse(course);

        if(activeTypes.contains(AchievementType.MAXED_ACTIVITIES)) {
            studentAchievementService.createOrUpdate(grade, AchievementType.MAXED_ACTIVITIES);
        }
        if(activeTypes.contains(AchievementType.PASSED_ACTIVITIES)){
            studentAchievementService.createOrUpdate(grade, AchievementType.PASSED_ACTIVITIES);
        }
    }

    @EventListener
    public void handleAchievementEvent(SubGradeEvent subGradeEvent){


        SubGrade subGrade = subGradeEvent.getValue();
        SubActivity subActivity = subGrade.getSubActivity();
        Course course = subActivity.getActivity().getCourse();

        Set<AchievementType> activeTypes = achievementService.findAchievementTypesInCourse(course);

        switch (subGradeEvent.getType()){
            case ADD_EDIT:
                if(activeTypes.contains(AchievementType.MAXED_SUBACTIVITIES)) {
                    studentAchievementService.createOrUpdate(subGrade, AchievementType.MAXED_SUBACTIVITIES);
                }
                break;
            case REMOVE:
                activeTypes.forEach(activeType -> studentAchievementService.remove(subGrade, activeType));
                break;
        }



    }
}
