package upeldev.com.github.upel3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
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

    public List<Activity> findAll(){
        return activityRepository.findAll();
    }

    public Activity save(Activity activityDTO){
        return activityRepository.save(activityDTO);
    }




}
