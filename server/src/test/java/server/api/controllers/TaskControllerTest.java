package server.api.controllers;

import commons.entities.Card;
import commons.entities.CardList;
import commons.entities.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.BindingResult;
import server.api.repositories.TestCardRepository;
import server.api.repositories.TestTaskRepository;
import server.exceptions.EntityNotFoundException;
import server.exceptions.InvalidRequestException;
import server.services.TaskService;

import java.util.Arrays;

class TaskControllerTest {

    private TestCardRepository cardRepo;
    private TestTaskRepository taskRepo;
    private TaskController taskController;

    private BindingResult hasErrorResult;
    private BindingResult noErrorResult;
    private TaskService taskService;

    @BeforeEach
    public void setup() {
        this.cardRepo = new TestCardRepository();
        this.taskRepo = new TestTaskRepository();
        this.taskService = new TaskService(taskRepo, cardRepo);
        this.taskController = new TaskController(taskService, Mockito.mock(SimpMessagingTemplate.class));

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

    @Test
    public void moveUpTest() {
        final Task task = this.taskRepo.save(new Task("Task 1", false));
        final Task task1 = this.taskRepo.save(new Task("Task 2", false));

        Card card = new Card("title", "desc");
        card.addTask(task);
        card.addTask(task1);
        card = this.cardRepo.save(card);

        Assertions.assertEquals(Arrays.asList(task, task1), card.getNestedTaskList());
        this.taskController.move(task1.getId(), "up");
        Assertions.assertEquals(Arrays.asList(task1, task), card.getNestedTaskList());
    }

    @Test
    public void moveDownTest() {
        final Task task = this.taskRepo.save(new Task());
        final Task task1 = this.taskRepo.save(new Task());

        Card card = new Card("title", "desc");
        card.getNestedTaskList().add(task);
        card.getNestedTaskList().add(task1);
        card = this.cardRepo.save(card);

        Assertions.assertEquals(Arrays.asList(task, task1), card.getNestedTaskList());
        this.taskController.move(task.getId(), "down");
        Assertions.assertEquals(Arrays.asList(task1, task), card.getNestedTaskList());
    }

    @Test
    public void moveCardNotFoundTest() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.taskController.move(-1, "up"));
    }

}