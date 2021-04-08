package upeldev.com.github.upel3.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private int minValue = 0;

    @Column
    private int maxValue = 100;

    @Column(nullable = false)
    private String name;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private Course course;

    @OneToMany(
            mappedBy = "activity",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Grade> grade = new ArrayList<>();



    public Activity(Course course, int minValue, int maxValue, String name){
        this.course = course;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.name = name;
    }




}
