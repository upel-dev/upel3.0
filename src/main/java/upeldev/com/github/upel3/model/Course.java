package upeldev.com.github.upel3.model;

import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @OneToOne
    private AccessCode accessCode;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<User> lecturers = new ArrayList<>();

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<User> enrolledStudents = new ArrayList<>();

    @OneToMany
            (
                    mappedBy = "course",
                    cascade = CascadeType.ALL,
                    orphanRemoval = true,
                    fetch = FetchType.EAGER
            )
    private List<Activity> activity = new ArrayList<>();


    public Course(String name, String description){
        this.name = name;
        this.description = description;
        this.accessCode = new AccessCode();
    }

    public void addStudent(User student){
        if(!enrolledStudents.contains(student)) enrolledStudents.add(student);
    }

    public void addLecturer(User lecturer){
        if(!lecturers.contains(lecturer)) lecturers.add(lecturer);
    }

    public void addActivity(Activity activity){
        if(!this.activity.contains(activity)) this.activity.add(activity);
    }


}
