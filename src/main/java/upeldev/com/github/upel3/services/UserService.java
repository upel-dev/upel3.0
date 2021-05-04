package upeldev.com.github.upel3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import upeldev.com.github.upel3.model.Activity;
import upeldev.com.github.upel3.model.Course;
import upeldev.com.github.upel3.model.Role;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.repositories.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    public User findByEmail(String email){ return userRepository.findUserByEmail(email); }

    public User findByIndexNumber(String indexNumber) { return userRepository.findUserByIndexNumber(indexNumber); }

    public User findById(Long id) { return userRepository.findUserById(id); }

    public User save(User userDTO){
        return userRepository.save(userDTO);
    }

    public boolean isAdmin(User user){
        return user.getRoles().stream()
                .anyMatch(role -> role.equals(Role.ADMIN));
    }

    public boolean isLecturer(User user){
        return user.getRoles().stream()
                .anyMatch(role -> role.equals(Role.LECTURER));
    }

    public boolean isStudent(User user){
        return user.getRoles().stream()
                .anyMatch(role -> role.equals(Role.STUDENT));
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

    public void hideCourse(User user, Course course){
        user.addHiddenCourse(course);
        userRepository.save(user);
    }

    public void showCourse(User user, Course course){
        user.removeCourseFromHidden(course);
        userRepository.save(user);
    }

    public Set<Course> getAllVisibleCourses(User user){

        Set<Course> visibleCourses = new HashSet<>();

        user.getCoursesLectured().stream()
                .filter(course -> !user.getHiddenCourses().contains(course))
                .forEach(visibleCourses::add);

        user.getCoursesEnrolledIn().stream()
                .filter(course -> !user.getHiddenCourses().contains(course))
                .forEach(visibleCourses::add);

        return visibleCourses;
    }

    public Set<Course> getAllHiddenCourses(User user){
        return user.getHiddenCourses();
    }

    public boolean isCourseHidden(User user, Course course){
        return user.getHiddenCourses().contains(course);
    }

    public void changePassword(User user, String newPassword) {
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userRepository.save(user);
    }
}
