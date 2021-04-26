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

    @ManyToMany(cascade = CascadeType.MERGE)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(
            name="LECTURERS_COURSES_LECTURED",
            joinColumns = @JoinColumn(name="COURSE_ID"),
            inverseJoinColumns = @JoinColumn(name="USER_ID")
    )
    private Set<User> lecturers = new HashSet<>();

    @ManyToMany(cascade = CascadeType.MERGE)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(
            name="STUDENT_COURSES_ENROLLED_IN",
            joinColumns = @JoinColumn(name="COURSE_ID"),
            inverseJoinColumns = @JoinColumn(name="USER_ID")
    )
    private Set<User> enrolledStudents = new HashSet<>();

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
        enrolledStudents.add(student);
    }

    public void addLecturer(User lecturer){
        lecturers.add(lecturer);
    }

    public void addActivity(Activity activity){
        if(!this.activity.contains(activity)) this.activity.add(activity);
    }


}
