package upeldev.com.github.upel3.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private int minValue = 0;

    @Column
    private int maxValue = 100;

    @Column(nullable = false)
    private String name;


    @OneToMany(
            mappedBy = "grade",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<IndividualGrade> individualGrade = new ArrayList<>();

    public Grade(int minValue, int maxValue, String name){
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.name = name;
    }


}
