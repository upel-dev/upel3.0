package upeldev.com.github.upel3.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public enum Role {
    STUDENT,
    LECTURER,
    ADMIN;

    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }
}
