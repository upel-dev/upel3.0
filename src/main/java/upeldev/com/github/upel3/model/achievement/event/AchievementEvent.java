package upeldev.com.github.upel3.model.achievement.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public abstract class AchievementEvent<T> extends ApplicationEvent {

    private final T value;
    private final EventType type;

    public AchievementEvent(Object source, T value, EventType type) {
        super(source);
        this.value = value;
        this.type = type;
    }
}
