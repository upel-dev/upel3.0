package upeldev.com.github.upel3.repositories;

import org.springframework.data.repository.CrudRepository;
import upeldev.com.github.upel3.model.AccessCode;
import upeldev.com.github.upel3.model.Activity;

public interface AccessCodeRepository extends CrudRepository<AccessCode, Integer> {
}
