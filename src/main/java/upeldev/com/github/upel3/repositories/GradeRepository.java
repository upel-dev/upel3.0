package upeldev.com.github.upel3.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import upeldev.com.github.upel3.model.Activity;
import upeldev.com.github.upel3.model.Grade;

import java.util.List;

public interface GradeRepository extends CrudRepository<Grade, Integer> {

    Grade findGradeById(@Param("activityId")  Long activityId);

    @Query(value = "SELECT ig FROM Grade ig " +
            "INNER JOIN Activity a on a.id = ig.activity.id " +
            "INNER JOIN Course c on c.id = a.course.id " +
            "WHERE c.id = :courseId")
    List<Grade> findGradeByCourse(@Param("courseId") Long courseId);


    List<Grade> findGradeByActivity(Activity activity);

    long deleteById(Long id);
}
