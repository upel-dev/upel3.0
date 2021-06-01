package upeldev.com.github.upel3.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import upeldev.com.github.upel3.model.Course;
import upeldev.com.github.upel3.model.Grade;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.model.achievement.Achievement;
import upeldev.com.github.upel3.model.achievement.AchievementType;
import upeldev.com.github.upel3.model.achievement.StudentAchievement;

import java.util.List;

public interface StudentAchievementRepository extends CrudRepository<StudentAchievement, Integer> {

    List<StudentAchievement> findAllByUser(User user);

    StudentAchievement findByUserAndAchievement(User user, Achievement achievement);

    List<StudentAchievement> findByUserAndCourse(User user, Course course);


}
