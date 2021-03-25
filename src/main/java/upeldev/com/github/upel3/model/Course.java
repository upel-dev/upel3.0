package upeldev.com.github.upel3.model;

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
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    private List<User> enrolledStudents = new ArrayList<>();

    public Course(String name, String accessCode, User lecturer){
        this.name = name;
        this.lecturer = lecturer;
    }

    public void addStudent(User student){
        if(!enrolledStudents.contains(student)) enrolledStudents.add(student);
    }




}
