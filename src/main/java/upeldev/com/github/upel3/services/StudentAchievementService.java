package upeldev.com.github.upel3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upeldev.com.github.upel3.model.*;
import upeldev.com.github.upel3.model.achievement.*;
import upeldev.com.github.upel3.repositories.StudentAchievementRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class StudentAchievementService {

    private final StudentAchievementRepository studentAchievementRepository;

    @Autowired
    public StudentAchievementService(
            StudentAchievementRepository studentAchievementRepository){
        this.studentAchievementRepository = studentAchievementRepository;
    }

    public StudentAchievement save(StudentAchievement studentAchievement){
        return studentAchievementRepository.save(studentAchievement);
    }

    public StudentAchievement findByUserAndType(User user, AchievementType type){
        return studentAchievementRepository.findByUserAndType(user, type);
    }

    public GradeAchievement createOrUpdate(Grade grade, AchievementType type){

        Activity activity = grade.getActivity();
        Course course = activity.getCourse();
        User user = grade.getUser();

        GradeAchievement achievement = (GradeAchievement) findByUserAndType(grade.getUser(), AchievementType.PASSED_ACTIVITIES);
        if(achievement == null) {
            achievement = new GradeAchievement(grade, user, course, type);
        }
        else{
            achievement.update(grade);
        }
        return (GradeAchievement) save(achievement);
    }


    public SubGradeAchievement createOrUpdate(SubGrade subGrade, AchievementType type){

        Activity activity = subGrade.getSubActivity().getActivity();
        Course course = activity.getCourse();
        User user = subGrade.getGrade().getUser();

        SubGradeAchievement achievement = (SubGradeAchievement) findByUserAndType(subGrade.getGrade().getUser(), AchievementType.MAXED_SUBACTIVITIES);

        if(achievement == null) {
            achievement = new SubGradeAchievement(subGrade, user, course, type);
        }
        else{
            achievement.update(subGrade);
        }
        return (SubGradeAchievement) save(achievement);
    }

    public List<StudentAchievement> findAllUsersAchievements(User user){
        return studentAchievementRepository.findAllByUser(user);

    }

}
