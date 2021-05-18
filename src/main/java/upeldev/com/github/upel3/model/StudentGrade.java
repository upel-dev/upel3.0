package upeldev.com.github.upel3.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class StudentGrade extends Grade {

    @JsonIgnore
    @ManyToOne
    private User user;

    public StudentGrade(User user, Activity activity) {
        super(activity);
        this.user = user;
    }

    @Override
    public String getGradeOwnerName() {
        return user.getFirstName() + " " + user.getLastName();
    }

    @Override
    public String getGradeOwnerUsername() {
        return user.getEmail();
    }
}
