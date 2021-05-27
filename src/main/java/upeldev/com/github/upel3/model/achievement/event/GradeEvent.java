package upeldev.com.github.upel3.model.achievement.event;

import upeldev.com.github.upel3.model.Grade;

public class GradeEvent extends AchievementEvent<Grade> {
    public GradeEvent(Object source, Grade value) {
        super(source, value);
    }
}
