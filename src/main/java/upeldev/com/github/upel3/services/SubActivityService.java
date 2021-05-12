package upeldev.com.github.upel3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upeldev.com.github.upel3.model.Activity;
import upeldev.com.github.upel3.model.SubActivity;
import upeldev.com.github.upel3.model.SubGrade;
import upeldev.com.github.upel3.repositories.SubActivityRepository;

import java.util.List;

@Service
public class SubActivityService {

    private final SubActivityRepository subActivityRepository;

    @Autowired
    public SubActivityService(SubActivityRepository subActivityRepository){
        this.subActivityRepository = subActivityRepository;
    }

    public List<SubActivity> findAll(){ return subActivityRepository.findAll(); }

    public SubActivity findById(long id){ return subActivityRepository.findById(id); }

    public SubActivity save(SubActivity subActivityDTO){ return subActivityRepository.save(subActivityDTO); }

    public List<SubActivity> findByActivity(Activity activity){return subActivityRepository.findByActivity(activity); }

    public void changeDescription(SubActivity subActivity, String newDescription){
        if (newDescription == null || newDescription.isEmpty()){
            throw new IllegalArgumentException("New sub activity description cannot be empty");
        }

        subActivity.setDescription(newDescription);
        subActivityRepository.save(subActivity);
    }

    public void changeName(SubActivity subActivity, String newName){
        if (newName == null || newName.isEmpty()){
            throw new IllegalArgumentException("New sub activity name cannot be empty");
        }

        subActivity.setName(newName);
        subActivityRepository.save(subActivity);
    }

    public void changeMaxValue(SubActivity subActivity, int newMaxValue){
        subActivity.setMaxValue(newMaxValue);
        subActivityRepository.save(subActivity);
    }

    public void removeSubGrade(SubActivity subActivity, SubGrade subGrade){
        subActivity.getSubGrades().remove(subGrade);
        subActivityRepository.save(subActivity);
    }

}
