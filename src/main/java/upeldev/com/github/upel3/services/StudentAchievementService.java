package upeldev.com.github.upel3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upeldev.com.github.upel3.model.Grade;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.model.achievement.AchievementType;
import upeldev.com.github.upel3.model.achievement.GradeAchievement;
import upeldev.com.github.upel3.model.achievement.StudentAchievement;
import upeldev.com.github.upel3.repositories.GradeAchievementRepository;

@Service
public class StudentAchievementService {

    private final GradeAchievementRepository gradeAchievementRepository;

    @Autowired
    public StudentAchievementService(GradeAchievementRepository gradeAchievementRepository){
        this.gradeAchievementRepository = gradeAchievementRepository;
    }

    public void save(GradeAchievement studentAchievement){
        gradeAchievementRepository.save(studentAchievement);
    }

    public GradeAchievement findByUserAndType(User user, AchievementType type){
        return gradeAchievementRepository.findByUserAndType(user, type);
    }
}
