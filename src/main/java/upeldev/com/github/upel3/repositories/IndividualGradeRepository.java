package upeldev.com.github.upel3.repositories;

import org.springframework.data.repository.CrudRepository;
import upeldev.com.github.upel3.model.IndividualGrade;
import upeldev.com.github.upel3.model.User;

import java.util.List;

public interface IndividualGradeRepository extends CrudRepository<IndividualGrade, Integer> {
    IndividualGrade findIndividualGradeById(Long Id);
    List<IndividualGrade> findGradeByUser(User user);
}
