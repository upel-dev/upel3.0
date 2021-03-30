package upeldev.com.github.upel3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import upeldev.com.github.upel3.model.*;
import upeldev.com.github.upel3.repositories.GradeRepository;

import java.util.List;

@Service
public class GradeService {
    private final GradeRepository gradeRepository;

    @Autowired
    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    public Grade findGradeById(Long id){
        return gradeRepository.findGradeById(id);
    }

    public List<Grade> findGradeByCourse(Course course){
        return gradeRepository.findGradeByCourse(course.getId());
    }

    public boolean canUserAddGrade(Grade grade, User user){
        if(user.getRoles().contains(Role.ADMIN) || user.getRoles().contains(Role.LECTURER)){
            User lecturer = grade.getCourse().getLecturer();
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

    public List<Grade> findAll(){
        return gradeRepository.findAll();
    }

    public Grade save(Grade gradeDTO){
        return gradeRepository.save(gradeDTO);
    }




}
