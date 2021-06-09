package upeldev.com.github.upel3.model.achievement.event;

import upeldev.com.github.upel3.model.SubGrade;

public class SubGradeEvent extends AchievementEvent<SubGrade> {
    public SubGradeEvent(Object source, SubGrade value, EventType type) {
        super(source, value, type);
    }
}
