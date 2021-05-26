package upeldev.com.github.upel3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upeldev.com.github.upel3.model.Grade;
import upeldev.com.github.upel3.model.achievement.StudentAchievement;
import upeldev.com.github.upel3.repositories.GradeAchievementRepository;

@Service
public class StudentAchievementService {

    private final GradeAchievementRepository gradeAchievementRepository;

    @Autowired
    public StudentAchievementService(GradeAchievementRepository gradeAchievementRepository){
        this.gradeAchievementRepository = gradeAchievementRepository;
    }

    public void save(StudentAchievement<Grade> studentAchievement){
        gradeAchievementRepository.save(studentAchievement);
    }
}
