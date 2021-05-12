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
public class SubActivity implements Aggregable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;

    @Column
    private int maxValue;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private Activity activity;

    @Column
    private double weight = 1;

    @OneToMany(
            mappedBy = "subActivity",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<SubGrade> subGrades = new ArrayList<>();

    public SubActivity(Activity activity, int maxValue, String name){
        this.activity = activity;
        this.maxValue = maxValue;
        this.name = name;
    }

    @Override
    public double getAggregableValue() {
        return getMaxValue();
    }

    @Override
    public double getAggregableWeight() {
        return getWeight();
    }
}
