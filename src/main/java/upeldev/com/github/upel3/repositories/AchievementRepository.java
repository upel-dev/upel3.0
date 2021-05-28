package upeldev.com.github.upel3.repositories;

import org.springframework.data.repository.CrudRepository;
import upeldev.com.github.upel3.model.Course;
import upeldev.com.github.upel3.model.achievement.Achievement;

import java.util.List;

public interface AchievementRepository extends CrudRepository<Achievement, Long> {

    List<Achievement> findAllByCourse(Course course);
}
