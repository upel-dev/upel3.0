package upeldev.com.github.upel3.model;

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
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;

    @Column
    private String accessCode;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @ManyToOne
    private User lecturer;

    @ManyToMany
    //replace with a HashMap - students and their marks
    private List<User> enrolledStudents = new ArrayList<>();

    @OneToMany
            (
                    mappedBy = "course",
                    cascade = CascadeType.ALL,
                    orphanRemoval = true,
                    fetch = FetchType.EAGER
            )
    private List<Activity> activity = new ArrayList<>();

    public Course(String name, String accessCode, User lecturer){
        this.name = name;
        this.accessCode = accessCode;
        this.lecturer = lecturer;
    }

    public void addStudent(User student){
        if(!enrolledStudents.contains(student)) enrolledStudents.add(student);
    }



}
