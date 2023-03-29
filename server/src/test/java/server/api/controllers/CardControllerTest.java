package server.api.controllers;

import commons.entities.Card;
import commons.entities.Tag;
import commons.entities.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.validation.BindingResult;
import server.api.repositories.TestTaskRepository;
import server.api.repositories.TestCardRepository;
import server.api.repositories.TestTagRepository;
import server.database.BoardRepository;
import static org.junit.jupiter.api.Assertions.*;
import server.exceptions.EntityNotFoundException;
import server.exceptions.InvalidRequestException;

import java.util.NoSuchElementException;

class CardControllerTest {

    private TestCardRepository cardRepo;
    private TestTagRepository tagRepo;
    private TestTaskRepository taskRepo;
    private CardController controller;
    private BoardRepository boardRepo;

    private BindingResult hasErrorResult;
    private BindingResult noErrorResult;

    @BeforeEach
    public void setup() {
        cardRepo = new TestCardRepository();
        tagRepo = new TestTagRepository();
        taskRepo = new TestTaskRepository();
        controller = new CardController(cardRepo,tagRepo,taskRepo);

        this.hasErrorResult = Mockito.mock(BindingResult.class);
        this.noErrorResult = Mockito.mock(BindingResult.class);

        Mockito.when(hasErrorResult.hasErrors()).thenReturn(true);
        Mockito.when(noErrorResult.hasErrors()).thenReturn(false);
    }

    @Test
    public void editCardTitleTest() {
        cardRepo.save(new Card("ADS", "ADS"));
        Card card = new Card("ADS", "ADS");
        card.setId(1);
        Assertions.assertEquals(cardRepo.getById(1), card);
        Card newCard = new Card("OOPP", "");
        controller.editCard(1, newCard, noErrorResult);
        Assertions.assertEquals(cardRepo.getById(1).getTitle(), "OOPP");
    }

    @Test
    public void cardRepoTest() {
        cardRepo.save(new Card("Title", "title"));
        cardRepo.save(new Card("Title", "ewewew"));
        Assertions.assertEquals(2, cardRepo.findAll().size());
        Assertions.assertEquals(1, cardRepo.findAll().get(0).getId());
        Assertions.assertEquals(2, cardRepo.findAll().get(1).getId());
    }

    @Test
    public void taskRepoTest() {
        taskRepo.save(new Task("Title", false));
        taskRepo.save(new Task("Title", false));
        Assertions.assertEquals(2, taskRepo.findAll().size());
        Assertions.assertEquals(1, taskRepo.findAll().get(0).getId());
        Assertions.assertEquals(2, taskRepo.findAll().get(1).getId());
    }

    @Test
    public void tagRepoTest() {
        tagRepo.save(new Tag("Title", 0));
        tagRepo.save(new Tag("Title", 0));
        Assertions.assertEquals(2, tagRepo.findAll().size());
        Assertions.assertEquals(1, tagRepo.findAll().get(0).getId());
        Assertions.assertEquals(2, tagRepo.findAll().get(1).getId());
    }

    @Test
    public void editInvalidCardTest() {
        Assertions.assertThrows(InvalidRequestException.class, () -> controller.editCard(1, new Card("", null), hasErrorResult));
    }

    @Test
    public void editCardTitleNotFoundTest() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> controller.editCard(0,
                new Card("Test",  "Test"), noErrorResult));
    }

    @Test
    public void editCardDescriptionTest() {
        cardRepo.save(new Card("ADS", "ADS"));
        Card card = new Card("ADS", "ADS");
        card.setId(1);
        Assertions.assertEquals(cardRepo.getById(1), card);
        controller.editCard(1, new Card("ADS", "Binary Search Trees"), noErrorResult);
        Assertions.assertEquals(cardRepo.getById(1).getDescription(), "Binary Search Trees");
    }

    @Test
    public void editCardDescriptionNotFoundTest() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> controller.editCard(0,
                new Card("Test", "Test"), noErrorResult));
    }

    @Test
    public void assignTagTest() {
        cardRepo.save(new Card("Study ADS", "Do weblab"));
        final Tag tag = new Tag("ADS", 0);
        tag.setId(10);
        tagRepo.save(tag);
        this.controller.assignTag(1, 1);
        assertTrue(tagRepo.existsById(1));
        assertEquals(tag, tagRepo.getById(1));
    }


    @Test
    public void createTagNoCardTest() {
        Tag tag = new Tag("I hate conflicts", 0);
        tag.setId(1);
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.controller.assignTag(10, 1));
    }

    @Test
    public void createTagFaultyTest() {
        cardRepo.save(new Card("Study ADS", "Weblav"));
        Tag faulty1 = new Tag(null, 23);
        Assertions.assertThrows(EntityNotFoundException .class, () -> controller.assignTag(1, 1));
    }

    @Test
    public void deleteTagTest() {
        cardRepo.save(new Card("Study ADS", "Do weblab"));
        Tag toAdd =  new Tag("ADS", 0);
        toAdd.setId(1);
        tagRepo.save(toAdd);
        this.controller.assignTag(1, 1);
        Assertions.assertTrue(cardRepo.getById(1).getTags().size() > 0);
        this.controller.deleteTag(1,1);
        Assertions.assertEquals(0, cardRepo.getById(1).getTags().size());
    }

    @Test
    public void deleteTagNotFoundTest() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> controller.deleteTag(0,125));
    }

    @Test
    public void deleteTagNotInACard() {
        cardRepo.save(new Card("Study ADS", "Do weblab"));
        cardRepo.save(new Card("Study OOPP", "Do Git"));
        Tag tag = new Tag("ADS", 0);
        tag.setId(1);
        tagRepo.save(tag);
        this.controller.assignTag(1,1);
        Assertions.assertThrows(EntityNotFoundException.class, () -> controller.deleteTag(2,1));
    }

    @Test
    public void createTaskTest() {
        cardRepo.save(new Card("Study ADS", "Do weblab"));
        this.controller.createTask(1, new Task("ADS", false), noErrorResult);
        final Task task = new Task("ADS", false);
        task.setId(1);
        Assertions.assertTrue(taskRepo.existsById(1));
        Assertions.assertEquals(task, taskRepo.getById(1));
    }

    @Test
    public void createTaskNoCardTest() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.controller.createTask(10, new Task("ADS", false), noErrorResult));
    }

    @Test
    public void createTaskFaultyTest() {
        cardRepo.save(new Card("Study ADS", "Weblav"));
        Task faulty1 = new Task(null, false);
        Assertions.assertThrows(InvalidRequestException.class, () -> controller.createTask(0, faulty1, hasErrorResult));
    }

    @Test
    public void deleteTaskTest() {
        cardRepo.save(new Card("Study ADS", "Do weblab"));
        Task toAdd =  new Task("ADS", false);
        toAdd.setId(1);
        this.controller.createTask(1, toAdd, noErrorResult);
        Assertions.assertTrue(cardRepo.getById(1).getNestedTaskList().size() > 0);
        Assertions.assertTrue(taskRepo.existsById(1));
        this.controller.deleteTask(1,1);
        Assertions.assertFalse(taskRepo.existsById(1));
        Assertions.assertEquals(0, cardRepo.getById(1).getNestedTaskList().size());
    }

    @Test
    public void deleteTaskNotFoundTest() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> controller.deleteTask(0,15));
    }

    @Test
    public void deleteTaskNotInACard() {
        cardRepo.save(new Card("Study ADS", "Do weblab"));
        cardRepo.save(new Card("Study OOPP", "Do Git"));
        this.controller.createTask(1, new Task("OOPP", true), noErrorResult);
        Assertions.assertThrows(EntityNotFoundException.class, () -> controller.deleteTask(2,1));
    }
}