package server.api.controllers;

import commons.CardList;
import commons.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.support.hierarchical.HierarchicalTestExecutorService;
import org.springframework.http.HttpStatus;
import server.api.repositories.TestCardRepository;
import server.api.repositories.TestTaskRepository;

import static org.junit.jupiter.api.Assertions.*;

class TaskControllerTest {

    private TestCardRepository cardRepo;
    private TestTaskRepository taskRepo;
    private TaskController taskController;

    @BeforeEach
    public void setup() {
        this.cardRepo = new TestCardRepository();
        this.taskRepo = new TestTaskRepository();
        this.taskController = new TaskController(taskRepo);
    }

    @Test
    void editTaskTitleTest() {
        this.taskRepo.save(new Task("title", true));
        final Task task = new Task("title", true);
        task.setId(1);
        final Task edited = new Task("new title", true);
        edited.setId(1);

        Assertions.assertEquals(this.taskRepo.findById(1).get(), task);
        this.taskController.editTask(1, new Task("new title", true));
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
        this.taskController.editTask(1, new Task("title", false));
        Assertions.assertEquals(this.taskRepo.findById(1).get(), edited);
    }

    @Test
    void editTaskNotFoundTest() {
        Assertions.assertEquals(this.taskController.editTask(1, new Task()).getStatusCode(), HttpStatus.NOT_FOUND);
    }
}