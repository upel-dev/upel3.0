package upeldev.com.github.upel3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import upeldev.com.github.upel3.model.*;
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

    public boolean canUserAddGrade(IndividualGrade individualGradeDTO, User user){
        if(user.getRoles().contains(Role.ADMIN) || user.getRoles().contains(Role.LECTURER)){
            User lecturer = individualGradeDTO.getGrade().getCourse().getLecturer();
            boolean isUserLecturer = lecturer.getId().equals(user.getId());
            if(isUserLecturer || user.getRoles().contains(Role.ADMIN)){
                return true;
            }
            else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This user is not course lecturer");
            }
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This user cannot add grade");
        }
    }

    public List<IndividualGrade> findAll(){
        return StreamSupport.stream(individualGradeRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public List<IndividualGrade> findIndividualGradeByCourse(Course course){
        return individualGradeRepository.findIndividualGradeByCourse(course.getId());
    }

    public IndividualGrade save(IndividualGrade individualGradeDTO){
        return individualGradeRepository.save(individualGradeDTO);
    }
}
