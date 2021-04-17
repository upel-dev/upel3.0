package upeldev.com.github.upel3.repositories;

import org.springframework.data.repository.CrudRepository;
import upeldev.com.github.upel3.model.*;

import java.util.List;

public interface SubGradeRepository extends CrudRepository<SubGrade, Integer> {
    SubGrade findSubGradeById(Long id);
    List<SubGrade> findAll();
}
