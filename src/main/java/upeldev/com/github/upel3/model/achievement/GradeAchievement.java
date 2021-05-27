package upeldev.com.github.upel3.model.achievement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import upeldev.com.github.upel3.model.Activity;
import upeldev.com.github.upel3.model.Course;
import upeldev.com.github.upel3.model.Grade;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.services.GradeService;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class GradeAchievement extends StudentAchievement<Grade> {

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany
    private Set<Grade> grades;

    private int lowerLimit = 2;

    public GradeAchievement(Grade grade, User student, Course course, AchievementType type){
        super(student, course, type);
        this.grades = new HashSet<>();
        this.grades.add(grade);
    }

    public void update(Grade grade){

        Activity activity = grade.getActivity();

        switch (type){
            case MAXED_ACTIVITIES:
                if(grade.getValue() == activity.getValue() && activity.getValue() != 0){
                    grades.add(grade);
                }
                else{
                    grades.remove(grade);
                }
            break;
            case PASSED_ACTIVITIES:
                if(grade.getValue() >= activity.getPassValue()){
                    grades.add(grade);
                }
                else{
                    grades.remove(grade);
                }

            break;
        }

        if(grades.size() >= lowerLimit) isAchieved = true;
        else isAchieved = false;

    }

    public double getQuantity(){
        return grades.size();
    }

}
