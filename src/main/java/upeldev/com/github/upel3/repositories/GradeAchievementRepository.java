package upeldev.com.github.upel3.repositories;

import org.springframework.data.repository.CrudRepository;
import upeldev.com.github.upel3.model.Grade;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.model.achievement.AchievementType;
import upeldev.com.github.upel3.model.achievement.GradeAchievement;
import upeldev.com.github.upel3.model.achievement.StudentAchievement;

public interface GradeAchievementRepository extends CrudRepository<GradeAchievement, Integer> {

    GradeAchievement findByUserAndType(User user, AchievementType type);
}
