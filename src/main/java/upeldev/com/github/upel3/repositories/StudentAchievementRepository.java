package upeldev.com.github.upel3.repositories;

import org.springframework.data.repository.CrudRepository;
import upeldev.com.github.upel3.model.Grade;
import upeldev.com.github.upel3.model.achievement.StudentAchievement;

public interface StudentAchievementRepository<T> extends CrudRepository<StudentAchievement<T>, Integer> {

}
