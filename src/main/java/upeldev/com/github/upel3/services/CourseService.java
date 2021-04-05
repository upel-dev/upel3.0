package upeldev.com.github.upel3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upeldev.com.github.upel3.model.Course;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.repositories.CourseRepository;
import upeldev.com.github.upel3.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, UserRepository userRepository){
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public Course save(Course courseDTO){ return courseRepository.save(courseDTO); }

    public Course createCourse(String name, String description, User lecturer){
        Course course = new Course(name, lecturer, description);
        return save(course);
    }

    public List<Course> findAll(){
        return StreamSupport.stream(courseRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public void addStudent(Course course, User student){
        course.addStudent(student);
        student.enrollInCourse(course);
        courseRepository.save(course);
        userRepository.save(student);
    }

    public void addLecturer(Course course, User lecturer){
        course.addLecturer(lecturer);
        lecturer.addLecturedCourse(course);
        courseRepository.save(course);
        userRepository.save(lecturer);
    }


}
