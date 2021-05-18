package upeldev.com.github.upel3.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EqualsAndHashCode
public class StudentGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    private Course course;

    @ManyToMany
    private Set<User> students = new HashSet<>();

    public StudentGroup(String name, Course course, Set<User> students){
        this.name = name;
        this.course = course;
        this.students = students;
    }

}
