package upeldev.com.github.upel3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upeldev.com.github.upel3.model.Activity;
import upeldev.com.github.upel3.model.Course;
import upeldev.com.github.upel3.model.Grade;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.model.achievement.Achievement;
import upeldev.com.github.upel3.model.achievement.AchievementType;
import upeldev.com.github.upel3.model.achievement.GradeAchievement;
import upeldev.com.github.upel3.model.achievement.StudentAchievement;
import upeldev.com.github.upel3.repositories.GradeAchievementRepository;
import upeldev.com.github.upel3.repositories.StudentAchievementRepository;

import java.util.List;

@Service
public class StudentAchievementService {

    private final GradeAchievementRepository gradeAchievementRepository;
    private final StudentAchievementRepository studentAchievementRepository;

    @Autowired
    public StudentAchievementService(
            GradeAchievementRepository gradeAchievementRepository,
            StudentAchievementRepository studentAchievementRepository){
        this.gradeAchievementRepository = gradeAchievementRepository;
        this.studentAchievementRepository = studentAchievementRepository;
    }

    public GradeAchievement save(GradeAchievement studentAchievement){
        return gradeAchievementRepository.save(studentAchievement);
    }

    public GradeAchievement findByUserAndType(User user, AchievementType type){
        return gradeAchievementRepository.findByUserAndType(user, type);
    }

    public GradeAchievement createOrUpdate(Grade grade, AchievementType type){

        Activity activity = grade.getActivity();
        Course course = activity.getCourse();
        User user = grade.getUser();

        GradeAchievement achievement = findByUserAndType(grade.getUser(), AchievementType.PASSED_ACTIVITIES);

        if(achievement == null) {
            achievement = new GradeAchievement(grade, user, course, type);
        }
        else{
            achievement.update(grade);
        }
        return save(achievement);
    }

    public List<StudentAchievement<?>> findAllUsersAchievements(User user){
        return studentAchievementRepository.findAllByUser(user);

    }

}
