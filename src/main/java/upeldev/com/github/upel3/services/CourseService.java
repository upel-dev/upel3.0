package upeldev.com.github.upel3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upeldev.com.github.upel3.model.AccessCode;
import upeldev.com.github.upel3.model.Activity;
import upeldev.com.github.upel3.model.Course;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.repositories.AccessCodeRepository;
import upeldev.com.github.upel3.repositories.ActivityRepository;
import upeldev.com.github.upel3.repositories.CourseRepository;
import upeldev.com.github.upel3.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final AccessCodeRepository accessCodeRepository;
    private final ActivityRepository activityRepository;
    @Autowired
    public CourseService(CourseRepository courseRepository,
                         UserRepository userRepository,
                         AccessCodeRepository accessCodeRepository,
                         ActivityRepository activityRepository){
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.accessCodeRepository = accessCodeRepository;
        this.activityRepository = activityRepository;
    }

    public Course save(Course courseDTO){ return courseRepository.save(courseDTO); }

    public Course createCourse(String name, String description, User lecturer){
        Course course = new Course(name, description);
        course.addLecturer(lecturer);
        accessCodeRepository.save(course.getAccessCode());
//        userRepository.save(lecturer);
        return save(course);
    }

    public List<Course> findAll(){
        return StreamSupport.stream(courseRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Course findCourseById(Long id){
        return courseRepository.findById(id);
    }

    public List<Course> findByName(String name){
        return courseRepository.findByName(name);
    }

    public List<Course> findByPhrase(String phrase){
        return courseRepository.findByNameContains(phrase);
    }

    private void addStudentToCourse(Course course, User student){

        if (course.getEnrolledStudents().contains(student)){
            throw new IllegalArgumentException("Given student is already enrolled to this course");
        }
        course.addStudent(student);
        student.enrollInCourse(course);
        courseRepository.save(course);
    }

    public void addStudentToCourse(Long courseId, Long studentId){

        Course course = courseRepository.findById(courseId);
        User student = userRepository.findUserById(studentId);

        addStudentToCourse(course, student);

    }

    public void addStudentToCourse(Long courseId, String email){

        Course course = courseRepository.findById(courseId);
        User student = userRepository.findUserByEmail(email);
        addStudentToCourse(course, student);
    }

    public void addActivity(Course course, Activity activity){
        course.addActivity(activity);
        activity.setCourse(course);
        courseRepository.save(course);
        activityRepository.save(activity);
    }

    public void addLecturer(Long courseId, Long lecturerId) throws IllegalArgumentException {
        Course course = courseRepository.findById(courseId);
        User lecturer = userRepository.findUserById(lecturerId);

        addLecturer(course, lecturer);

    }


    public void addLecturer(Long courseId, String email) throws IllegalArgumentException {
        Course course = courseRepository.findById(courseId);
        User lecturer = userRepository.findUserByEmail(email);

        addLecturer(course, lecturer);

    }

    private void addLecturer(Course course, User lecturer) throws IllegalArgumentException {
        if(course.getLecturers().contains(lecturer)){
            throw new IllegalArgumentException("Given lecturer is already assigned to this course");
        }
        course.addLecturer(lecturer);
        lecturer.addLecturedCourse(course);
        courseRepository.save(course);
    }

    public Course findCourseByAccessCode(String accessCodeString){
        Optional<AccessCode> accessCodeOptional = accessCodeRepository.findById(accessCodeString);
        if (accessCodeOptional.isEmpty())
            return null;
        AccessCode accessCode = accessCodeOptional.get();
        return courseRepository.findByAccessCode(accessCode);
    }

    public void addStudentToCourseByCode(User student, String courseCode){
        Course course = findCourseByAccessCode(courseCode);
        if (course == null){
            throw new IllegalArgumentException("No such course");
        }
        if (course.getEnrolledStudents().contains(student)){
            throw new IllegalArgumentException("Student is already enrolled to this course");
        }
        addStudentToCourse(course, student);
    }


}
