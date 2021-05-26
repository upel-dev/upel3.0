package upeldev.com.github.upel3.model.achievement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import upeldev.com.github.upel3.model.Activity;
import upeldev.com.github.upel3.model.Grade;
import upeldev.com.github.upel3.services.StudentAchievementService;

@Component
public class AchievementManager {

    private final StudentAchievementService studentAchievementService;

    @Autowired
    public AchievementManager(StudentAchievementService studentAchievementService){
        this.studentAchievementService = studentAchievementService;
    }

    @EventListener
    public void handleAchievementEvent(AchievementEvent<Grade> gradeEvent){

        Grade grade = gradeEvent.getValue();
        Activity activity = grade.getActivity();

        GradeAchievement achievement;

        if(grade.getValue() == activity.getValue() && activity.getValue() != 0){

            achievement = studentAchievementService.findByUserAndType(grade.getUser(), AchievementType.MAXED_ACTIVITIES);

            if(achievement == null){
                achievement = new GradeAchievement(
                                grade,
                                grade.getUser(),
                                activity.getCourse(),
                                AchievementType.MAXED_ACTIVITIES);
            }
            else{
                achievement.update(grade);
            }
            studentAchievementService.save(achievement);

        }
        if(grade.getValue() >= activity.getPassValue()){

            achievement = studentAchievementService.findByUserAndType(grade.getUser(), AchievementType.PASSED_ACTIVITIES);
            if(achievement == null) {
                achievement =
                        new GradeAchievement(
                                grade,
                                grade.getUser(),
                                activity.getCourse(),
                                AchievementType.PASSED_ACTIVITIES
                        );
            }
            else{
                achievement.update(grade);
            }
            studentAchievementService.save(achievement);
        }

    }
}
