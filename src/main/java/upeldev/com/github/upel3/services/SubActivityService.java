package upeldev.com.github.upel3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upeldev.com.github.upel3.model.Activity;
import upeldev.com.github.upel3.model.SubActivity;
import upeldev.com.github.upel3.repositories.SubActivityRepository;

import java.util.List;

@Service
public class SubActivityService {

    private final SubActivityRepository subActivityRepository;

    @Autowired
    public SubActivityService(SubActivityRepository subActivityRepository){
        this.subActivityRepository = subActivityRepository;
    }

    public List<SubActivity> findAll(){return subActivityRepository.findAll(); }

    public SubActivity save(SubActivity subActivityDTO){ return subActivityRepository.save(subActivityDTO); }

    public List<SubActivity> findByActivity(Activity activity){return subActivityRepository.findByActivity(activity); }

}
