package upeldev.com.github.upel3.services;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import upeldev.com.github.upel3.model.Course;
import upeldev.com.github.upel3.model.Grade;
import upeldev.com.github.upel3.model.User;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GradeServiceTest {

    @Resource(name= "gradeService")
    public GradeService gradeService;

    @Resource(name="courseService")
    public CourseService courseService;

    @Resource(name="userService")
    public UserService userService;

    @Test
    void findGradeByCourseTest() {
        List<Course> courses = courseService.findAll();
        for(Course course : courses){
            List<Grade> grades = gradeService.findGradeByCourse(course);

            for(Grade ig : grades){
                assertEquals(course ,ig.getActivity().getCourse());
            }
        }
    }
    @Test
    void findGradeByCourseAndUser(){
        List<Course> courses = courseService.findAll();
        List<User> users = userService.findAll();

        for(User user : users){
            for(Course course : courses){
                List<Grade> grades = gradeService.findGradeByCourseAndUser(course, user);

                for(Grade ig : grades){
                    assertEquals(course.getId() ,ig.getActivity().getCourse().getId());
                    assertEquals(user ,ig.getUser());
                }
            }
        }
    }

    @Test
    public void findGradeByUserTest(){
        List<User> users = userService.findAll();

        for(User user : users){
            List<Grade> grades = gradeService.findGradeByUser(user);
            for(Grade ig : grades){
                assertEquals(user ,ig.getUser());
            }

        }
    }

    @Test
    public void findGradeByIdTest(){
        List<Grade> grades = gradeService.findAll();
            for(Grade ig : grades){
                Grade ig2 = gradeService.findGradeById(ig.getId());
                assertEquals(ig ,ig2);
            }
    }
}