package dio.taskmanager.application.output;

import dio.taskmanager.domain.Task;
import dio.taskmanager.domain.TaskId;
import dio.taskmanager.domain.TaskStatus;

import java.util.Optional;

public record TaskOutput(String id, String title, Optional<String> description, String status ) {

    public static TaskOutput from(Task task) {
        return new TaskOutput(
                task.getId().id().toString(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus().name()
        );
    }
}
