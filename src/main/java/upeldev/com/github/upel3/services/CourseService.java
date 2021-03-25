package upeldev.com.github.upel3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upeldev.com.github.upel3.model.Course;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.repositories.CourseRepository;

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

    public void addStudent(Course course, User student){
        course.addStudent(student);
        //student.addCourse(course);
        courseRepository.save(course);
        //studentRepository.save(student);
    }


}
