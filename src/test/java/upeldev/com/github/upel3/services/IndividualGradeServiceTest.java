package upeldev.com.github.upel3.services;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import upeldev.com.github.upel3.model.Course;
import upeldev.com.github.upel3.model.IndividualGrade;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IndividualGradeServiceTest {

    @Resource(name="individualGradeService")
    public IndividualGradeService individualGradeService;

    @Resource(name="courseService")
    public CourseService courseService;

    @Test
    void findIndividualGradeByCourseTest() {
        List<Course> courses = courseService.findAll();
        for(Course course : courses){
            List<IndividualGrade> individualGrades = individualGradeService.findIndividualGradeByCourse(course);
//            course.getId()
            for(IndividualGrade ig : individualGrades){
                assertEquals(course.getId() ,ig.getGrade().getCourse().getId());
            }
        }
    }
}