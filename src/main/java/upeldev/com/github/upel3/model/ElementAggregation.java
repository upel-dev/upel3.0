package upeldev.com.github.upel3.model;

import lombok.Getter;

import java.util.List;

@Getter
public enum ElementAggregation {
    SUM,
    AVG,
    WAVG;

    public static<T extends Aggregable> double countSum(List<T> subElements){
        return subElements.stream().mapToDouble(Aggregable::getAggregableValue).sum();
    }

    public static<T extends Aggregable> double countAvg(List<T> subElements){
        return subElements.stream().mapToDouble(Aggregable::getAggregableValue).average().orElse(0);
    }

    public static<T extends Aggregable> double countWavg(List<T> subElements){
        double numerator = subElements.stream().mapToDouble(subElement -> subElement.getAggregableValue() * subElement.getAggregableWeight()).sum();
        double denominator = subElements.stream().mapToDouble(subElement -> subElement.getAggregableWeight()).sum();
        return numerator / denominator;
    }

    @Override
    public String toString() {
        switch (this) {
            case SUM: return "Suma";
            case AVG: return "Średnia";
            case WAVG: return "Średnia ważona";
        }
        return "";
    }
}
