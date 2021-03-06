package upeldev.com.github.upel3.repositories;

import org.springframework.data.repository.CrudRepository;
import upeldev.com.github.upel3.model.AccessCode;
import upeldev.com.github.upel3.model.Course;

import java.util.List;

public interface CourseRepository extends CrudRepository<Course, Integer> {
    List<Course> findByName(String name);
    List<Course> findByNameContains(String phrase);
    Course findById(Long id);
    Course findByAccessCode(AccessCode accessCode);
}
