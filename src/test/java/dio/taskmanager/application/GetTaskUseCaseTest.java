package dio.taskmanager.application;

import dio.taskmanager.application.output.TaskOutput;
import dio.taskmanager.domain.Task;
import dio.taskmanager.domain.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class GetTaskUseCaseTest {
    @Mock
    TaskRepository repository;

    @InjectMocks
    GetTaskUseCase useCase;

    @Test
    void shouldGetTaskSuccessfully() {
        Task task1 = new Task("Mock Task 1", Optional.empty());
        Task task2 = new Task("Mock Task 2", Optional.empty());

        when(repository.findAll())
                .thenReturn(List.of(task1, task2));

        List<TaskOutput> resultTasks = useCase.execute();

        assertEquals(2, resultTasks.size());
    }
}