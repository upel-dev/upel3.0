package upeldev.com.github.upel3.model.achievement;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
public class StudentAchievement {

    @EventListener
    public<T> void handleAchievementEvent(AchievementEvent<T> event){
        System.out.println(event.getValue());
    }

}
