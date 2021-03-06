package upeldev.com.github.upel3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upeldev.com.github.upel3.model.*;
import upeldev.com.github.upel3.model.achievement.*;
import upeldev.com.github.upel3.repositories.AchievementRepository;
import upeldev.com.github.upel3.repositories.StudentAchievementRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class StudentAchievementService {

    private final StudentAchievementRepository studentAchievementRepository;
    private final AchievementRepository achievementRepository;

    @Autowired
    public StudentAchievementService(
            StudentAchievementRepository studentAchievementRepository, AchievementRepository achievementRepository){
        this.studentAchievementRepository = studentAchievementRepository;
        this.achievementRepository = achievementRepository;
    }

    public StudentAchievement save(StudentAchievement studentAchievement){
        return studentAchievementRepository.save(studentAchievement);
    }

    public StudentAchievement findByUserAndAchievement(User user, Achievement achievement){
        return studentAchievementRepository.findByUserAndAchievement(user, achievement);
    }

    public GradeAchievement createOrUpdate(Grade grade, AchievementType type){

        Activity activity = grade.getActivity();
        Course course = activity.getCourse();
        User user = grade.getUser();

        Achievement achievement = achievementRepository.findByCourseAndType(course, type);

        GradeAchievement studentAchievement = (GradeAchievement) findByUserAndAchievement(grade.getUser(), achievement);
        if(studentAchievement == null) {
            studentAchievement = new GradeAchievement(grade, user, course, achievement);
        }
        else{
            studentAchievement.update(grade);
        }
        return (GradeAchievement) save(studentAchievement);
    }


    private SubGradeAchievement getSubGradeAchievement(SubGrade subGrade, AchievementType type){
        Activity activity = subGrade.getSubActivity().getActivity();
        Course course = activity.getCourse();
        User user = subGrade.getGrade().getUser();

        Achievement achievement = achievementRepository.findByCourseAndType(course, type);

        StudentAchievement studentAchievement = findByUserAndAchievement(user, achievement);


        return studentAchievement instanceof  SubGradeAchievement ? (SubGradeAchievement) studentAchievement : null;
    }

    public void createOrUpdate(SubGrade subGrade, AchievementType type){

        Activity activity = subGrade.getSubActivity().getActivity();
        Course course = activity.getCourse();
        User user = subGrade.getGrade().getUser();

        Achievement achievement = achievementRepository.findByCourseAndType(course, type);

        SubGradeAchievement subGradeAchievement = getSubGradeAchievement(subGrade, type);

        if(subGradeAchievement == null) {
            subGradeAchievement = new SubGradeAchievement(subGrade, user, course, achievement);
        }
        else{
            subGradeAchievement.update(subGrade);
        }
        save(subGradeAchievement);
    }

    public void remove(SubGrade subGrade, AchievementType type){

        SubGradeAchievement subGradeAchievement = getSubGradeAchievement(subGrade, type);

        if(subGradeAchievement == null) {
            return;
        }
        else{
            subGradeAchievement.remove(subGrade);
        }
        save(subGradeAchievement);

    }

    public List<StudentAchievement> findAllUsersAchievements(User user){
        return studentAchievementRepository.findAllByUser(user);

    }

    public List<StudentAchievement> findAllByUserAndCourse(User user, Course course){
        return studentAchievementRepository.findByUserAndCourse(user, course);
    }

}
