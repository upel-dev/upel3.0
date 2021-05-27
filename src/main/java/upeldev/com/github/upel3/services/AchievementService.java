package upeldev.com.github.upel3.services;

import org.springframework.stereotype.Service;
import upeldev.com.github.upel3.model.Course;
import upeldev.com.github.upel3.model.achievement.Achievement;
import upeldev.com.github.upel3.model.achievement.AchievementType;
import upeldev.com.github.upel3.repositories.AchievementRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AchievementService {

    private final AchievementRepository achievementRepository;

    public AchievementService(AchievementRepository achievementRepository){
        this.achievementRepository = achievementRepository;
    }

    public void save(Achievement achievementDTO){
        achievementRepository.save(achievementDTO);
    }

    public List<Achievement> findAllByCourse(Course course){
        return achievementRepository.findAllByCourse(course);
    }

    public Set<AchievementType> findAchievementTypesInCourse(Course course){
        return findAllByCourse(course)
                .stream()
                .map(Achievement::getType)
                .collect(Collectors.toSet());
    }

}
