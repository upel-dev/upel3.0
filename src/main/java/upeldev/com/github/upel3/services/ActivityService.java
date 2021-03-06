package upeldev.com.github.upel3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upeldev.com.github.upel3.model.*;
import upeldev.com.github.upel3.repositories.ActivityRepository;
import upeldev.com.github.upel3.repositories.GradeRepository;
import upeldev.com.github.upel3.repositories.SubActivityRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final SubActivityRepository subActivityRepository;

    @Autowired
    public ActivityService(ActivityRepository activityRepository, SubActivityRepository subActivityRepository) {
        this.activityRepository = activityRepository;

        this.subActivityRepository = subActivityRepository;
    }

    public Activity findActivityById(Long id){
        return activityRepository.findActivityById(id);
    }

    public List<Activity> findActivityByCourse(Course course){
        return activityRepository.findActivityByCourse(course);
    }

    public boolean canUserAddActivity(Activity activity, User user){

        if(user.getRoles().contains(Role.ADMIN)) return true;
        if(!user.getRoles().contains(Role.LECTURER)) return false;

        return user.getCoursesLectured().contains(activity.getCourse());
    }

    public Activity createActivity(Course course, int passValue, String name){

        Activity activity = new Activity(course, passValue, name);
        return save(activity);
    }

    public List<Activity> findAll(){
        return activityRepository.findAll();
    }

    public Activity save(Activity activityDTO){
        return activityRepository.save(activityDTO);
    }

    public void changeDescription(Activity activity, String newDescription){
        activity.setDescription(Objects.requireNonNullElse(newDescription, ""));
        activityRepository.save(activity);
    }

    public void changeName(Activity activity, String newName){
        if (newName == null || newName.isEmpty()){
            throw new IllegalArgumentException("New activity name cannot be empty");
        }

        activity.setName(newName);
        activityRepository.save(activity);
    }

    public Grade getStudentGradeInActivity(Activity activity, User user){
        if(!activity.getCourse().getEnrolledStudents().contains(user)){
            throw new IllegalArgumentException("User must be a student of the course");
        }
        List<Grade> studentGrade =  activity.getGrades()
                .stream()
                .filter(grade -> grade.getUser().equals(user))
                .collect(Collectors.toList());

        if(studentGrade.isEmpty()) return null;

        return studentGrade.get(0);
    }

    public void changeAggregation(Activity activity, ElementAggregation aggregation){
        if(aggregation == null){
            throw new IllegalArgumentException("Aggregation cannot be empty");
        }
        activity.setAggregation(aggregation);
        activityRepository.save(activity);
    }

    public void changePassValue(Activity activity, int newPassValue){
        activity.setPassValue(newPassValue);
        activityRepository.save(activity);
    }

    public void removeGrade(Activity activity, Grade grade){
        activity.getGrades().remove(grade);
        activityRepository.save(activity);
    }

    public void removeSubActivity(Activity activity, SubActivity subActivity){
        activity.getSubActivities().remove(subActivity);
        activityRepository.save(activity);
    }

    public void addSubActivity(Activity activity, SubActivity subActivity){
        subActivityRepository.save(subActivity);
        activity.getSubActivities().add(subActivity);
        activity.getGrades().forEach(
                grade -> {
                    SubGrade subGrade = new SubGrade(subActivity, grade, 0.0);
                    grade.getSubGrades().add(subGrade);
                }
        );
        activityRepository.save(activity);
    }

}
