package upeldev.com.github.upel3.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import upeldev.com.github.upel3.model.Activity;
import upeldev.com.github.upel3.model.Course;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ActivityServiceTest {

    @Resource(name= "activityService")
    public ActivityService activityService;

    @Resource(name="courseService")
    public CourseService courseService;

    @Test
    void testFindActivityById() {
        List<Activity> activityList = activityService.findAll();

        for(Activity activity : activityList){
            Activity foundActivity = activityService.findActivityById(activity.getId());
            Assertions.assertEquals(activity, foundActivity);
        }
    }

    @Test
    void testFindActivityByCourse() {
        List<Course> courses = courseService.findAll();
        for(Course course : courses){
            List<Activity> grades1= course.getActivity();
            List<Activity> grades2= activityService.findActivityByCourse(course);

            for(Activity activity : grades1){
                assertTrue(grades2.contains(activity));
            }

        }

    }
}