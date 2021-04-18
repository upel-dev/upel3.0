package upeldev.com.github.upel3.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SubGrade {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;

    @Column
    private double value;

    @Column
    private String description;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private SubActivity subActivity;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private Grade grade;


    public SubGrade(SubActivity subActivity, Grade grade, double value){
        this.subActivity = subActivity;
        this.grade = grade;
        this.value = value;
    }
}
