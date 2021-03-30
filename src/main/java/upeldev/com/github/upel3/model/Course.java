package upeldev.com.github.upel3.model;

import lombok.*;

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
    //replace with a HashMap - students and their marks
    private List<User> enrolledStudents = new ArrayList<>();

    @EqualsAndHashCode.Exclude @ToString.Exclude
    @OneToMany
            (
                    mappedBy = "course",
                    cascade = CascadeType.ALL,
                    orphanRemoval = true,
                    fetch = FetchType.EAGER
            )
    private List<Grade> grade = new ArrayList<>();

    public Course(String name, String accessCode, User lecturer){
        this.name = name;
        this.accessCode = accessCode;
        this.lecturer = lecturer;
    }

    public void addStudent(User student){
        if(!enrolledStudents.contains(student)) enrolledStudents.add(student);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id.equals(course.id);
    }


}
