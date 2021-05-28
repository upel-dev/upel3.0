package upeldev.com.github.upel3.model.achievement;


public enum AchievementType {

    MAXED_ACTIVITIES,
    PASSED_ACTIVITIES,
    MAXED_SUBACTIVITIES;

    @Override
    public String toString(){
        switch (this){
            case MAXED_ACTIVITIES: return "Maxed activities";
            case PASSED_ACTIVITIES: return "Passed activities";
            case MAXED_SUBACTIVITIES: return "Maxed subactivities";
        }
        return null;
    }

}
