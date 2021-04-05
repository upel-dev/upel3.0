package upeldev.com.github.upel3.model;

import lombok.*;

import javax.persistence.*;
import java.util.*;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String accessCode;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @ManyToMany
    private List<User> lecturers = new ArrayList<>();

    @ManyToMany
    private List<User> enrolledStudents = new ArrayList<>();

    @OneToMany
            (
                    mappedBy = "course",
                    cascade = CascadeType.ALL,
                    orphanRemoval = true,
                    fetch = FetchType.EAGER
            )
    private List<Activity> activity = new ArrayList<>();


    public Course(String name, User lecturer, String description){
        this.name = name;
        this.description = description;
        this.lecturers.add(lecturer);
    }

    public void addStudent(User student){
        if(!enrolledStudents.contains(student)) enrolledStudents.add(student);
    }

    public void addLecturer(User lecturer){
        if(!lecturers.contains(lecturer)) lecturers.add(lecturer);
    }


}
