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

@Component
public class AchievementManager {

    private final StudentAchievementService studentAchievementService;
    private final AchievementService achievementService;

    @Autowired
    public AchievementManager(
            AchievementService achievementService,
            StudentAchievementService studentAchievementService
    ){
        this.achievementService = achievementService;
        this.studentAchievementService = studentAchievementService;
    }

    @EventListener
    public void handleAchievementEvent(AchievementEvent<Grade> gradeEvent){
        //TODO: simplify and check whether course contains this achievement
        Grade grade = gradeEvent.getValue();
        Activity activity = grade.getActivity();
        Course course = activity.getCourse();

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
