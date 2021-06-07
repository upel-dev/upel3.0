package upeldev.com.github.upel3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import upeldev.com.github.upel3.model.*;
import upeldev.com.github.upel3.model.achievement.event.SubGradeEvent;
import upeldev.com.github.upel3.repositories.SubGradeRepository;

import java.util.List;

@Service
public class SubGradeService {
    private final SubGradeRepository subGradeRepository;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public SubGradeService(SubGradeRepository subGradeRepository, ApplicationEventPublisher publisher){
        this.subGradeRepository = subGradeRepository;
        this.publisher = publisher;
    }

    public SubGrade save(SubGrade subGradeDTO){
        SubGrade subGrade = subGradeRepository.save(subGradeDTO);
        publisher.publishEvent(new SubGradeEvent(this, subGrade));
        return subGrade;
    }

    public List<SubGrade> findAll(){ return subGradeRepository.findAll(); }

    public void changeDescription(SubGrade subGrade, String newDescription){
        if (newDescription == null || newDescription.isEmpty()){
            throw new IllegalArgumentException("New sub grade description cannot be empty");
        }

        subGrade.setDescription(newDescription);
        subGradeRepository.save(subGrade);
    }

    public void changeValue(SubGrade subGrade, int newValue){
        subGrade.setValue(newValue);
        subGradeRepository.save(subGrade);
    }

    public long deleteById(Long id) {
        return subGradeRepository.deleteById(id);
    }

}
