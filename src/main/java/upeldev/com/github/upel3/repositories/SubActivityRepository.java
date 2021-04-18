package upeldev.com.github.upel3.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import upeldev.com.github.upel3.model.Activity;
import upeldev.com.github.upel3.model.Grade;
import upeldev.com.github.upel3.model.SubActivity;

import java.util.List;

public interface SubActivityRepository extends CrudRepository<SubActivity, Integer> {

    SubActivity findSubActivityById(Long id);

    List<SubActivity> findAll();

    List<SubActivity> findByActivity(Activity activity);
}
