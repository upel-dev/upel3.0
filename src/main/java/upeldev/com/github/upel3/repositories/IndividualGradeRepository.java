package upeldev.com.github.upel3.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import upeldev.com.github.upel3.model.IndividualGrade;

import java.util.List;

public interface IndividualGradeRepository extends CrudRepository<IndividualGrade, Integer> {

    @Query(value = "SELECT ig FROM IndividualGrade ig WHERE ig.id = :gradeId")
    IndividualGrade findIndividualGradeById(@Param("gradeId")  Long gradeId);

    @Query(value = "SELECT ig FROM IndividualGrade ig WHERE ig.user.id = :userId")
    List<IndividualGrade> findGradeByUser(@Param("userId") Long userId);

    @Query(value = "SELECT ig FROM IndividualGrade ig " +
            "INNER JOIN Grade g on g.id = ig.grade.id " +
            "INNER JOIN Course c on c.id = g.course.id " +
            "WHERE c.id = :courseId")
    List<IndividualGrade> findIndividualGradeByCourse(@Param("courseId") Long courseId);
}
