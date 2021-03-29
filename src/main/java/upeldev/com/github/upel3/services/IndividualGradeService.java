package upeldev.com.github.upel3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upeldev.com.github.upel3.model.Grade;
import upeldev.com.github.upel3.model.IndividualGrade;
import upeldev.com.github.upel3.repositories.IndividualGradeRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class IndividualGradeService {
    private final IndividualGradeRepository individualGradeRepository;

    @Autowired
    public IndividualGradeService(IndividualGradeRepository individualGradeRepository) {
        this.individualGradeRepository = individualGradeRepository;
    }

    public List<IndividualGrade> findAll(){
        return StreamSupport.stream(individualGradeRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public IndividualGrade save(IndividualGrade individualGradeDTO){
        return individualGradeRepository.save(individualGradeDTO);
    }
}
