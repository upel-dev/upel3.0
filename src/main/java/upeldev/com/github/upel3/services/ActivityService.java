package upeldev.com.github.upel3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upeldev.com.github.upel3.model.*;
import upeldev.com.github.upel3.repositories.ActivityRepository;

import java.util.List;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;

    @Autowired
    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
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
        if (newDescription == null || newDescription.isEmpty()){
            throw new IllegalArgumentException("New activity description cannot be empty");
        }

        activity.setDescription(newDescription);
        activityRepository.save(activity);
    }

    public void changeName(Activity activity, String newName){
        if (newName == null || newName.isEmpty()){
            throw new IllegalArgumentException("New activity name cannot be empty");
        }

        activity.setName(newName);
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

}
