package upeldev.com.github.upel3.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Grade implements Aggregator {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @JsonIgnore
    @ManyToOne
    private User user;

    @JsonIgnore
    @ManyToOne
    private Activity activity;

    @Column
    private String description;

    @OneToMany(
            mappedBy = "grade",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<SubGrade> subGrades = new ArrayList<>();

    public Grade(User user, Activity activity){
        this.user = user;
        this.activity = activity;
    }

    public double getValue(){
        double value = 0;
        switch (activity.getAggregation()){
            case SUM: return ElementAggregation.countSum(subGrades);
            case AVG: return ElementAggregation.countAvg(subGrades);
            case WAVG: return ElementAggregation.countWavg(subGrades);
        }
        return value;
    }

}
