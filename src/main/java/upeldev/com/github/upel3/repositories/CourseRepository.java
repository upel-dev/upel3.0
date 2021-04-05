package upeldev.com.github.upel3.repositories;

import org.springframework.data.repository.CrudRepository;
import upeldev.com.github.upel3.model.Course;
import upeldev.com.github.upel3.model.User;

import java.util.List;

public interface CourseRepository extends CrudRepository<Course, Integer> {
}
