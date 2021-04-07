package upeldev.com.github.upel3.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import upeldev.com.github.upel3.model.Course;
import upeldev.com.github.upel3.model.Role;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.repositories.AccessCodeRepository;
import upeldev.com.github.upel3.repositories.CourseRepository;
import upeldev.com.github.upel3.repositories.UserRepository;

import java.util.List;


@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccessCodeRepository accessCodeRepository;

    private CourseService courseService;


    @BeforeEach
    public void setup(){
        courseService = new CourseService(courseRepository, userRepository, accessCodeRepository);
    }

    @Test
    @DisplayName("Should create a new course")
    void shouldCreateCourse(){

        String name = "courseName";
        String description = "courseDescription";
        User lecturer = new User("name", "lastName", "name@mail.com", "password");
        lecturer.getRoles().add(Role.LECTURER);

        Course course = new Course(name, lecturer, description);

        Mockito.when(courseRepository.save(course))
                .thenReturn(course);

        Assertions.assertThat(courseService.createCourse(name, description, lecturer))
                .isEqualTo(course);

    }

    @Test
    @DisplayName("Should find a course by name")
    void shouldFindByName(){

        String name = "someName";
        User lecturer = new User("name", "lastName", "name@mail.com", "password");
        lecturer.getRoles().add(Role.LECTURER);
        Course course = new Course(name, lecturer, null);

        Mockito.when(courseRepository.findByName(name))
                .thenReturn(List.of(course));

        Assertions.assertThat(courseService.findByName(name)).isEqualTo(List.of(course));

    }

    @Test
    @DisplayName("Should find a course by phrase")
    void shouldFindByPhrase(){

        String phrase = "phrase";
        User lecturer = new User("name", "lastName", "name@mail.com", "password");
        lecturer.getRoles().add(Role.LECTURER);
        Course course = new Course("name", lecturer, null);

        Mockito.when(courseRepository.findByNameContains(phrase))
                .thenReturn(List.of(course));

        Assertions.assertThat(courseService.findByPhrase(phrase))
                .isEqualTo(List.of(course));

    }


}
