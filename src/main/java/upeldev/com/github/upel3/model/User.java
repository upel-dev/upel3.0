package upeldev.com.github.upel3.model;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String indexNumber;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Grade> grade = new ArrayList<>();

    @ManyToMany(mappedBy = "enrolledStudents", cascade = CascadeType.MERGE)
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Course> coursesEnrolledIn = new HashSet<>();

    @ManyToMany(mappedBy = "lecturers", cascade = CascadeType.MERGE)
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Course> coursesLectured = new HashSet<>();


    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public void enrollInCourse(Course course){
        coursesEnrolledIn.add(course);
    }

    public void addLecturedCourse(Course course){
        if(!roles.contains(Role.LECTURER))
            throw new UnsupportedOperationException("Only a user with role LECTURER can be added as a lecturer.");

        coursesLectured.add(course);
    }

}
