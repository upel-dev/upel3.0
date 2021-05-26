package upeldev.com.github.upel3.model.achievement;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class AchievementEvent<T> extends ApplicationEvent {

    private final T value;

    public AchievementEvent(Object source, T value) {
        super(source);
        this.value = value;

    }
}
