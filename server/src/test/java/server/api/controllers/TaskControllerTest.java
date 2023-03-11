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
        final Task task1 = new Task("title", true);
        task1.setId(1);
        final Task task2 = new Task("new title", true);
        task2.setId(1);

        Assertions.assertEquals(this.taskRepo.findById(1).get(), task1);
        this.taskController.editTaskNameAndBoolean(1, new Task());
        task1.setName("new title");
        taskRepo.save(task1);
        Assertions.assertEquals(this.taskRepo.findById(1).get(), task2);
    }

    @Test
    void editTaskBooleanTest() {
        this.taskRepo.save(new Task("title", true));
        final Task task1 = new Task("title", true);
        task1.setId(1);
        final Task task2 = new Task("title", false);
        task2.setId(1);

        Assertions.assertEquals(this.taskRepo.findById(1).get(), task1);
        this.taskController.editTaskNameAndBoolean(1, new Task());
        task2.setCompleted(true);
        taskRepo.save(task1);
        Assertions.assertEquals(this.taskRepo.findById(1).get(), task2);
    }

    @Test
    void editTaskNotFoundTest() {
        Assertions.assertEquals(this.taskController.editTaskNameAndBoolean(1, new Task()).getStatusCode(), HttpStatus.NOT_FOUND);
    }
}