package upeldev.com.github.upel3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upeldev.com.github.upel3.model.Course;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.repositories.CourseRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository){
        this.courseRepository = courseRepository;
    }

    public Course save(Course courseDTO){ return courseRepository.save(courseDTO); }

    public Course createCourse(String name, String accessCode, String description, User lecturer){
        Course course = new Course(name, accessCode, lecturer);
        if(description != null) course.setDescription(description);
        return save(course);
    }

    public List<Course> findAll(){
        return StreamSupport.stream(courseRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public void addStudent(Course course, User student){
        course.addStudent(student);
        courseRepository.save(course);
    }


}
