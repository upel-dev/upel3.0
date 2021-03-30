package upeldev.com.github.upel3.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import upeldev.com.github.upel3.model.Grade;

import java.util.List;

public interface GradeRepository extends CrudRepository<Grade, Integer> {

    @Query(value = "SELECT g FROM Grade g WHERE g.id = :gradeId")
    Grade findGradeById(@Param("gradeId") Long gradeId);

    @Query(value = "SELECT g FROM Grade g")
    List<Grade> findAll();

    @Query(value = "SELECT g FROM Course c INNER JOIN Grade g on c.id = g.course.id WHERE g.course.id = :courseId")
    List<Grade> findGradeByCourse(@Param("courseId") Long courseId);
}
