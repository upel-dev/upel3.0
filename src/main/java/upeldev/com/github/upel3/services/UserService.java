package upeldev.com.github.upel3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upeldev.com.github.upel3.model.Role;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll(){
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public User findByEmail(String email){
        return userRepository.findUserByEmail(email);
    }

    public User save(User userDTO){
        return userRepository.save(userDTO);
    }

    public boolean isAdmin(User user){
        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equals(Role.ADMIN.getName()));
    }

    public User findById(Long id){
        return userRepository.findUserById(id);
    }
}
