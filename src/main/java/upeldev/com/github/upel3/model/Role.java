package upeldev.com.github.upel3.model;

import lombok.Getter;

@Getter
public enum Role {
    STUDENT("STUDENT"),
    LECTURER("LECTURER"),
    ADMIN("ADMIN");

    private final String name;

    Role(String name) {
        this.name = name;
    }
}
