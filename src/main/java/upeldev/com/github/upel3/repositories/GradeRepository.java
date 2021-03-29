package upeldev.com.github.upel3.repositories;

import org.springframework.data.repository.CrudRepository;
import upeldev.com.github.upel3.model.Grade;
import upeldev.com.github.upel3.model.User;

import java.util.List;

public interface GradeRepository extends CrudRepository<Grade, Integer> {
    Grade findGradeById(Long id);
}
