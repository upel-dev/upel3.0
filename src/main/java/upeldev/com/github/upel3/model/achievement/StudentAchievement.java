package upeldev.com.github.upel3.model.achievement;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import upeldev.com.github.upel3.model.Course;
import upeldev.com.github.upel3.model.User;

import javax.persistence.*;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class StudentAchievement<T> {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    protected boolean isAchieved = false;

    @Transient
    protected AchievementType type;

    @ManyToOne
    protected User student;

    @ManyToOne
    protected Course course;

    public StudentAchievement(User student, Course course, AchievementType type){
        this.student = student;
        this.course = course;
        this.type = type;

        System.out.println("New achievement of type " + type);
    }




}
