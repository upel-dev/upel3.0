package upeldev.com.github.upel3.model.achievement;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import upeldev.com.github.upel3.model.Course;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    private Course course;

    private AchievementType type;

    public Achievement(AchievementType type, Course course){
        this.type = type;
        this.course = course;
    }

}
