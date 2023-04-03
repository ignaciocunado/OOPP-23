package server.api.controllers;

import commons.entities.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.BindingResult;
import server.api.repositories.TestCardRepository;
import server.api.repositories.TestTaskRepository;
import server.exceptions.EntityNotFoundException;
import server.exceptions.InvalidRequestException;

class TaskControllerTest {

    private TestCardRepository cardRepo;
    private TestTaskRepository taskRepo;
    private TaskController taskController;

    private BindingResult hasErrorResult;
    private BindingResult noErrorResult;

    @BeforeEach
    public void setup() {
        this.cardRepo = new TestCardRepository();
        this.taskRepo = new TestTaskRepository();
        this.taskController = new TaskController(taskRepo, Mockito.mock(SimpMessagingTemplate.class));

        this.hasErrorResult = Mockito.mock(BindingResult.class);
        this.noErrorResult = Mockito.mock(BindingResult.class);

        Mockito.when(hasErrorResult.hasErrors()).thenReturn(true);
        Mockito.when(noErrorResult.hasErrors()).thenReturn(false);
    }

    @Test
    void editTaskTitleTest() {
        this.taskRepo.save(new Task("title", true));
        final Task task = new Task("title", true);
        task.setId(1);
        final Task edited = new Task("new title", true);
        edited.setId(1);

        Assertions.assertEquals(this.taskRepo.findById(1).get(), task);
        this.taskController.editTask(1, new Task("new title", true), noErrorResult);
        Assertions.assertEquals(this.taskRepo.findById(1).get(), edited);
    }

    @Test
    void editTaskBooleanTest() {
        this.taskRepo.save(new Task("title", true));
        final Task task = new Task("title", true);
        task.setId(1);
        final Task edited = new Task("title", false);
        edited.setId(1);


        Assertions.assertEquals(this.taskRepo.findById(1).get(), task);
        this.taskController.editTask(1, new Task("title", false), noErrorResult);
        Assertions.assertEquals(this.taskRepo.findById(1).get(), edited);
    }

    @Test
    void editInvalidTaskTest() {
        this.taskRepo.save(new Task("title", true));
        Assertions.assertThrows(InvalidRequestException.class, () -> this.taskController.editTask(1, new Task("", true), hasErrorResult));
    }

    @Test
    void editTaskNotFoundTest() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.taskController.editTask(1, new Task("title", false), noErrorResult));
    }
}