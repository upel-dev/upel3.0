package upeldev.com.github.upel3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import upeldev.com.github.upel3.model.*;
import upeldev.com.github.upel3.model.achievement.GradeAchievement;
import upeldev.com.github.upel3.model.achievement.event.AchievementEvent;
import upeldev.com.github.upel3.model.achievement.event.EventType;
import upeldev.com.github.upel3.model.achievement.event.GradeEvent;
import upeldev.com.github.upel3.model.achievement.event.SubGradeEvent;
import upeldev.com.github.upel3.repositories.GradeRepository;
import upeldev.com.github.upel3.repositories.StudentAchievementRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class GradeService {
    private final GradeRepository gradeRepository;
    private final StudentAchievementRepository studentAchievementRepository;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public GradeService(GradeRepository gradeRepository, StudentAchievementRepository studentAchievementRepository, ApplicationEventPublisher publisher) {
        this.gradeRepository = gradeRepository;
        this.studentAchievementRepository = studentAchievementRepository;
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
        Grade grade = gradeRepository.save(gradeDTO);
        publisher.publishEvent(new GradeEvent(this, grade, EventType.ADD_EDIT));
        return grade;
    }

    public void changeDescription(Grade grade, String newDescription){
        if (newDescription == null || newDescription.isEmpty()){
            throw new IllegalArgumentException("New grade description cannot be empty");
        }

        grade.setDescription(newDescription);
        gradeRepository.save(grade);
    }

    public void removeSubGrade(Grade grade, SubGrade subGrade){

        publisher.publishEvent(new SubGradeEvent(this, subGrade, EventType.REMOVE));
        grade.getSubGrades().remove(subGrade);

        gradeRepository.save(grade);
    }
}
