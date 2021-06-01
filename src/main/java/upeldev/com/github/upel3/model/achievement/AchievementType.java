package upeldev.com.github.upel3.model.achievement;


public enum AchievementType {

    MAXED_ACTIVITIES,
    PASSED_ACTIVITIES,
    MAXED_SUBACTIVITIES;

    @Override
    public String toString(){
        switch (this){
            case MAXED_ACTIVITIES: return "Wzorowy student";
            case PASSED_ACTIVITIES: return "Łowca zaliczeń";
            case MAXED_SUBACTIVITIES: return "Pogromca zadań";
        }
        return null;
    }

    public String getDescription(){
        switch (this){
            case MAXED_ACTIVITIES: return "Zdobądź maksymalną liczbę punktów w aktywności";
            case PASSED_ACTIVITIES: return "Zdaj aktywność";
            case MAXED_SUBACTIVITIES: return "Zdobądź maksymalną ocenę w zadaniu";
        }
        return null;
    }



}
