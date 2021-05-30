package upeldev.com.github.upel3.repositories;

import org.springframework.data.repository.CrudRepository;
import upeldev.com.github.upel3.model.Course;
import upeldev.com.github.upel3.model.achievement.Achievement;
import upeldev.com.github.upel3.model.achievement.AchievementType;

import java.util.List;

public interface AchievementRepository extends CrudRepository<Achievement, Long> {

    List<Achievement> findAllByCourse(Course course);

    Achievement findByCourseAndType(Course course, AchievementType type);
}
