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
import upeldev.com.github.upel3.repositories.AccessCodeRepository;
import upeldev.com.github.upel3.repositories.ActivityRepository;
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

    @Mock
    private ActivityRepository activityRepository;

    private CourseService courseService;


    @BeforeEach
    public void setup(){
        courseService = new CourseService(courseRepository, userRepository, accessCodeRepository, activityRepository);
    }

    @Test
    @DisplayName("Should create a new course")
    void shouldCreateCourse(){

        String name = "courseName";
        String description = "courseDescription";

        Course course = new Course(name, description);

        Mockito.when(courseRepository.save(course))
                .thenReturn(course);

        Assertions.assertThat(courseService.createCourse(name, description, null))
                .isEqualTo(course);

    }

    @Test
    @DisplayName("Should find a course by name")
    void shouldFindByName(){

        String name = "someName";
        Course course = new Course(name, null);

        Mockito.when(courseRepository.findByName(name))
                .thenReturn(List.of(course));

        Assertions.assertThat(courseService.findByName(name)).isEqualTo(List.of(course));

    }

    @Test
    @DisplayName("Should find a course by phrase")
    void shouldFindByPhrase(){

        String phrase = "phrase";
        Course course = new Course("name", null);

        Mockito.when(courseRepository.findByNameContains(phrase))
                .thenReturn(List.of(course));

        Assertions.assertThat(courseService.findByPhrase(phrase))
                .isEqualTo(List.of(course));

    }


}
