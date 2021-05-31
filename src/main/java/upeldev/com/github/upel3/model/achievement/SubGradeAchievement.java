package upeldev.com.github.upel3.model.achievement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import upeldev.com.github.upel3.model.*;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class SubGradeAchievement extends StudentAchievement {

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany
    private Set<SubGrade> subGrades;

    public SubGradeAchievement(SubGrade subGrade, User student, Course course, Achievement achievement){
        super(student, course, achievement);
        this.subGrades = new HashSet<>();
        update(subGrade);
    }

    public void update(SubGrade subGrade){

        SubActivity subActivity = subGrade.getSubActivity();

        switch (achievement.getType()){
            case MAXED_SUBACTIVITIES:
                if(subGrade.getValue() >= subActivity.getMaxValue() && subActivity.getMaxValue() != 0){
                    subGrades.add(subGrade);
                }
                else{
                    subGrades.remove(subGrade);
                }
                break;
        }

        isAchieved = subGrades.size() >= achievement.getLowerLimit();

    }

    public double getQuantity(){
        return subGrades.size();
    }
}
