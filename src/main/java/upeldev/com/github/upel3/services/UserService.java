package upeldev.com.github.upel3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import upeldev.com.github.upel3.model.Role;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.repositories.UserRepository;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    public List<User> findAllStudents(){
        List<User> studentList = StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        studentList.removeIf(user -> isAdmin(user) || isLecturer(user));
        return studentList;
    }

    public User findById(Long id){
        return userRepository.findUserById(id);
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

    public boolean isLecturer(User user){
        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equals(Role.LECTURER.getName()));
    }

    private boolean emailHasRightFormat(String email){
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public User registerNewUser(User userDto){
        String email = userDto.getEmail();

        if (!emailHasRightFormat(email)) {
            throw new IllegalArgumentException(String.format("Email %s is invalid", email));
        }

        if (findByEmail(email) != null){
            throw new IllegalArgumentException(String.format("User with email %s already exists", email));
        }

        if (userDto.getRoles().contains(Role.STUDENT) && userDto.getIndexNumber() == null){
            throw new IllegalArgumentException("Student has to have an index number");
        }

        String passwordEncrypted = new BCryptPasswordEncoder().encode(userDto.getPassword());
        userDto.setPassword(passwordEncrypted);

        return save(userDto);
    }
}
