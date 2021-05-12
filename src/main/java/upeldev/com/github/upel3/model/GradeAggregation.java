package upeldev.com.github.upel3.model;

import lombok.Getter;

import java.util.List;

@Getter
public enum GradeAggregation {
    SUM,
    AVG,
    WAVG;

    public static double countSum(List<SubGrade> subGrades){
        return subGrades.stream().mapToDouble(SubGrade::getValue).sum();
    }

    public static double countAvg(List<SubGrade> subGrades){
        return subGrades.stream().mapToDouble(SubGrade::getValue).average().orElse(0);
    }

    public static double countWavg(List<SubGrade> subGrades){
        double numerator = subGrades.stream().mapToDouble(subGrade -> subGrade.getValue() * subGrade.getSubActivity().getWeight()).sum();
        double denominator = subGrades.stream().mapToDouble(subGrade -> subGrade.getSubActivity().getWeight()).sum();
        return numerator / denominator;
    }

    public static double countMaxSum(List<SubActivity> subActivities){
        return subActivities.stream().mapToDouble(SubActivity::getMaxValue).sum();
    }

    public static double countMaxAvg(List<SubActivity> subActivities){
        return subActivities.stream().mapToDouble(SubActivity::getMaxValue).average().orElse(0);
    }

    public static double countMaxWavg(List<SubActivity> subActivities){
        double numerator = subActivities.stream().mapToDouble(subActivity -> subActivity.getMaxValue() * subActivity.getWeight()).sum();
        double denominator = subActivities.stream().mapToDouble(SubActivity::getWeight).sum();
        return numerator / denominator;
    }

    @Override
    public String toString() {
        switch (this) {
            case SUM: return "Suma";
            case AVG: return"Średnia";
            case WAVG: return "Średnia ważona";
        }
        return "";
    }
}
