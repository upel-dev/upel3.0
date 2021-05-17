package upeldev.com.github.upel3.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import upeldev.com.github.upel3.model.Grade;
import upeldev.com.github.upel3.model.StudentGrade;
import upeldev.com.github.upel3.model.User;

import java.util.List;

public interface StudentGradeRepository extends CrudRepository<StudentGrade, Integer> {

    List<Grade> findGradeByUser(User user);

    @Query(value = "SELECT ig FROM StudentGrade ig " +
            "INNER JOIN Activity a on a.id = ig.activity.id " +
            "INNER JOIN Course c on c.id = a.course.id " +
            "WHERE c.id = :courseId and ig.user.id = :userId")
    List<Grade> findGradeByCourseAndUser(@Param("courseId") Long courseId, @Param("userId") Long userId);


    @Query(value = "SELECT ig FROM StudentGrade ig " +
            "INNER JOIN Activity a on a.id = ig.activity.id " +
            "INNER JOIN Course c on c.id = a.course.id " +
            "WHERE c.id = :courseId and ig.user.id = :userId and a.id = :activityId")

    List<Grade> findGradeByCourseAndUserAndActivity(@Param("courseId") Long courseId, @Param("userId") Long userId, @Param("activityId") Long activityId);
}
