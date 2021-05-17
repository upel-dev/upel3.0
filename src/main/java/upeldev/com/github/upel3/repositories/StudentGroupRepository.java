package upeldev.com.github.upel3.repositories;

import org.springframework.data.repository.CrudRepository;
import upeldev.com.github.upel3.model.Course;
import upeldev.com.github.upel3.model.StudentGroup;
import upeldev.com.github.upel3.model.User;

import java.util.List;

public interface StudentGroupRepository extends CrudRepository<StudentGroup, Integer> {

    StudentGroup findById(Long id);

    List<StudentGroup> findByCourse(Course course);

    List<StudentGroup> findByName(String name);

    List<StudentGroup> findByStudentsContains(User user);

}
