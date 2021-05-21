package upeldev.com.github.upel3.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import upeldev.com.github.upel3.model.Grade;
import upeldev.com.github.upel3.model.StudentGroup;

import java.util.List;

public interface GroupGradeRepository extends CrudRepository<GroupGrade, Integer> {

    List<Grade> findGradeByGroup(StudentGroup group);

    @Query(value = "SELECT ig FROM GroupGrade ig " +
            "INNER JOIN Activity a on a.id = ig.activity.id " +
            "INNER JOIN Course c on c.id = a.course.id " +
            "WHERE c.id = :courseId and ig.group.id = :groupId")
    List<Grade> findGradeByCourseAndGroup(@Param("courseId") Long courseId, @Param("groupId") Long groupId);

    @Query(value = "SELECT ig FROM GroupGrade ig " +
            "INNER JOIN Activity a on a.id = ig.activity.id " +
            "INNER JOIN Course c on c.id = a.course.id " +
            "WHERE c.id = :courseId and ig.group.id = :groupId and a.id = :activityId")

    List<Grade> findGradeByCourseAndGroupAndActivity(@Param("courseId") Long courseId, @Param("groupId") Long groupId, @Param("activityId") Long activityId);
}
