package dio.taskmanager.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TaskIdTest {

    @Test
    void criarComUuidConhecidoGuardaEsseMesmoUuid() {
        UUID uuid = UUID.randomUUID();

        TaskId taskId = new TaskId(uuid);

        assertEquals(uuid, taskId.id());
    }

    @Test
    void criarSemUuidNaoPermaneceNull() {
        TaskId taskId = new TaskId();

        // assertNotEquals(null, taskId.id());

        assertNotNull(taskId.id());
    }

    @Test
    void nullERejeitado() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> new TaskId(null));

        assertEquals("id must not be null", exception.getMessage());

    }
}