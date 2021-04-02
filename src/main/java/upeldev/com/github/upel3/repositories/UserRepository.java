package upeldev.com.github.upel3.repositories;

import org.springframework.data.repository.CrudRepository;
import upeldev.com.github.upel3.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findUserByEmail(String email);

    User findUserById(Long id);
}
