package upeldev.com.github.upel3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import upeldev.com.github.upel3.model.*;
import upeldev.com.github.upel3.repositories.GradeRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class GradeService {
    private final GradeRepository gradeRepository;

    @Autowired
    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    public boolean canUserAddGrade(Grade gradeDTO, User user){
        if(user.getRoles().contains(Role.ADMIN) || user.getRoles().contains(Role.LECTURER)){
            User lecturer = gradeDTO.getActivity().getCourse().getLecturer();
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
    public List<Grade> findGradeByUser(User user){
        return gradeRepository.findGradeByUser(user);
    }

    public Grade findGradeById(Long id){
        return gradeRepository.findGradeById(id);
    }


    public List<Grade> findAll(){
        return StreamSupport.stream(gradeRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public List<Grade> findGradeByCourse(Course course){
        return gradeRepository.findGradeByCourse(course.getId());
    }

    public List<Grade> findGradeByCourseAndUser(Course course, User user){
        return gradeRepository.findGradeByCourseAndUser(course.getId(), user.getId());
    }

    public Grade save(Grade gradeDTO){
        return gradeRepository.save(gradeDTO);
    }
}
