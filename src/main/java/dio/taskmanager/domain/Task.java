package dio.taskmanager.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.jmx.export.naming.IdentityNamingStrategy;
import org.springframework.util.Assert;

import java.util.Optional;

// Anotação do Lombok Setter e Getter
@Getter
@Setter
public class Task {
    private TaskId id;
    private String title;
    private Optional<String> description;
    private TaskStatus status;

    public Task(String title, Optional<String> description) {
        Assert.notNull(title, "Title must not be null");

        this.id = new TaskId();
        this.title = title;
        this.description = description;
        this.status = TaskStatus.PENDING;
    }
}
