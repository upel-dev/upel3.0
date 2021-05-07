package upeldev.com.github.upel3.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import upeldev.com.github.upel3.model.Activity;
import upeldev.com.github.upel3.model.Course;

import java.util.List;

public interface ActivityRepository extends CrudRepository<Activity, Integer> {

    Activity findActivityById(@Param("activityId") Long activityId);

    List<Activity> findAll();

    List<Activity> findActivityByCourse(Course course);
}
