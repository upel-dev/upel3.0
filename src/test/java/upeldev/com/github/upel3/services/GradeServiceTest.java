package upeldev.com.github.upel3.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import upeldev.com.github.upel3.model.Course;
import upeldev.com.github.upel3.model.Grade;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GradeServiceTest {

    @Resource(name="gradeService")
    public GradeService gradeService;

    @Resource(name="courseService")
    public CourseService courseService;

    @Test
    void testFindGradeById() {
        List<Grade> gradeList = gradeService.findAll();
        for(Grade grade : gradeList){
            Assertions.assertEquals(grade.getId(), gradeService.findGradeById(grade.getId()).getId());
        }
    }

    @Test
    void testFindGradeByCourse() {
        List<Course> courses = courseService.findAll();
        for(Course course : courses){
            List<Grade> grades1= course.getGrade();
            List<Grade> grades2= gradeService.findGradeByCourse(course);

            for(Grade grade : grades1){
                assertTrue(grades2.contains(grade));
            }

        }

    }
}