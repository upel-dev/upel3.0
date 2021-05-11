package upeldev.com.github.upel3.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;

    @Column
    private int passValue = 0;



    @Column(nullable = false)
    private String name;

    @Column
    private String description;

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

    @OneToMany(
            mappedBy = "activity",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<SubActivity> subActivities = new ArrayList<>();



    public Activity(Course course, int passValue, String name){
        this.course = course;
        this.passValue = passValue;
        this.name = name;
    }

    public double getMaxPoints(){
        double value = 0;
        for(SubActivity subActiv : this.subActivities){
            value += subActiv.getMaxValue();
        }
        return value;
    }

}
