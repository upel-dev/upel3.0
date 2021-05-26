package upeldev.com.github.upel3.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Activity implements Aggregator, Aggregable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;

    @Column
    private double passValue = 0;

    @Column
    private double weight = 1.0;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Enumerated
    private ElementAggregation aggregation = ElementAggregation.SUM;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private Course course;

    @OneToMany(
            mappedBy = "activity",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<Grade> grades = new ArrayList<>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(
            mappedBy = "activity",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<SubActivity> subActivities = new ArrayList<>();



    public Activity(Course course, double passValue, String name){
        this.course = course;
        this.passValue = passValue;
        this.name = name;
    }

    @Override
    public double getValue(){
        double value = 0;
        switch (aggregation){
            case SUM: return ElementAggregation.countSum(subActivities);
            case AVG: return ElementAggregation.countAvg(subActivities);
            case WAVG: return ElementAggregation.countWavg(subActivities);
        }
        return value;
    }

    @Override
    public double getAggregableValue() {
        return getValue();
    }

    @Override
    public double getAggregableWeight() {
        return getWeight();
    }
}
