package upeldev.com.github.upel3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upeldev.com.github.upel3.model.*;
import upeldev.com.github.upel3.repositories.SubActivityRepository;
import upeldev.com.github.upel3.repositories.SubGradeRepository;

import java.util.List;

@Service
public class SubGradeService {
    private final SubGradeRepository subGradeRepository;

    @Autowired
    public SubGradeService(SubGradeRepository subGradeRepository){
        this.subGradeRepository = subGradeRepository;
    }

    public SubGrade save(SubGrade subGradeDTO){ return subGradeRepository.save(subGradeDTO); }

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
}
