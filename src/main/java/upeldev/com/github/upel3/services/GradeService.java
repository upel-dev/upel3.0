package upeldev.com.github.upel3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import upeldev.com.github.upel3.model.*;
import upeldev.com.github.upel3.model.achievement.AchievementEvent;
import upeldev.com.github.upel3.repositories.GradeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class GradeService {
    private final GradeRepository gradeRepository;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public GradeService(GradeRepository gradeRepository, ApplicationEventPublisher publisher) {
        this.gradeRepository = gradeRepository;
        this.publisher = publisher;
    }

    public boolean canUserAddGrade(Grade gradeDTO, User user){

        if(user.getRoles().contains(Role.ADMIN)) return true;
        if(!user.getRoles().contains(Role.LECTURER)) return false;

        return user.getCoursesLectured().contains(gradeDTO.getActivity().getCourse());


    }

    public List<Grade> findGradeByUser(User user){
        return gradeRepository.findGradeByUser(user);
    }

    public List<Grade> findGradeByActivity(Activity activity){
        return gradeRepository.findGradeByActivity(activity);
    }

    public List<Grade> findGradeByCourseAndUserAndActivity(Course course, User user, Activity activity){
        return gradeRepository.findGradeByCourseAndUserAndActivity(course.getId(), user.getId(), activity.getId());
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

    public long deleteById(Long id) {
        return gradeRepository.deleteById(id);
    }

    public Grade save(Grade gradeDTO){
        publisher.publishEvent(new AchievementEvent<>(this, gradeDTO));
        gradeRepository.save(gradeDTO);
        return gradeRepository.save(gradeDTO);
    }

    public void changeDescription(Grade grade, String newDescription){
        if (newDescription == null || newDescription.isEmpty()){
            throw new IllegalArgumentException("New grade description cannot be empty");
        }

        grade.setDescription(newDescription);
        gradeRepository.save(grade);
    }

    public void removeSubGrade(Grade grade, SubGrade subGrade){
        grade.getSubGrades().remove(subGrade);
        gradeRepository.save(grade);
    }
}
