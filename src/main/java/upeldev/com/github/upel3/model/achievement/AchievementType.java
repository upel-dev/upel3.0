package upeldev.com.github.upel3.model.achievement;

public enum AchievementType {

    MAXED_ACTIVITIES,
    PASSED_ACTIVITIES;

    @Override
    public String toString(){
        switch (this){
            case MAXED_ACTIVITIES: return "Maxed activities";
            case PASSED_ACTIVITIES: return "Passed activities";
        }
        return null;
    }

}
