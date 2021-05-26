package upeldev.com.github.upel3.model.achievement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import upeldev.com.github.upel3.model.Course;
import upeldev.com.github.upel3.model.Grade;
import upeldev.com.github.upel3.model.User;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class GradeAchievement extends StudentAchievement<Grade> {

    @ManyToOne
    private Grade grade;

    public GradeAchievement(Grade grade, User student, Course course, AchievementType type){
        super(student, course, type);
        this.grade = grade;
    }
}
