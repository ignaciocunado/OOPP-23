package server.api;

import commons.Card;
import commons.Tag;
import commons.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import server.database.*;

import static org.junit.jupiter.api.Assertions.*;

class CardControllerTest {

    private CardRepositoryTest cardRepo;
    private TagRepositoryTest tagRepo;
    private TaskRepositoryTest taskRepo;
    private CardController controller;

    @BeforeEach
    public void setup() {
        cardRepo = new CardRepositoryTest();
        tagRepo = new TagRepositoryTest();
        taskRepo = new TaskRepositoryTest();
        controller = new CardController(cardRepo,tagRepo,taskRepo);
    }

    @Test
    public void editCardTitleTest() {
        cardRepo.save(new Card("ADS", "ADS"));
        Card card = new Card("ADS", "ADS");
        card.setId(0);
        assertEquals(cardRepo.getById(0), card);
        Card newCard = new Card("OOPP", null);
        controller.editCard(0, newCard);
        assertEquals(cardRepo.getById(0).getTitle(), "OOPP");
    }

    @Test
    public void editCardTitleNotFoundTest() {
        assertEquals(ResponseEntity.badRequest().build(), controller.editCard(0,
            new Card("Test",  "Test")));
    }

    @Test
    public void editCardDescriptionTest() {
        cardRepo.save(new Card("ADS", "ADS"));
        Card card = new Card("ADS", "ADS");
        card.setId(0);
        assertEquals(cardRepo.getById(0), card);
        controller.editCard(0, new Card("ADS", "Binary Search Trees"));
        assertEquals(cardRepo.getById(0).getDescription(), "Binary Search Trees");
    }

    @Test
    public void editCardDescriptionNotFoundTest() {
        assertEquals(ResponseEntity.badRequest().build(), controller.editCard(0,
            new Card("Test", "Test")));
    }

    @Test
    public void createTagTest() {
        cardRepo.save(new Card("Study ADS", "Do weblab"));
        this.controller.createTag(0, new Tag("ADS", 0));
        final Tag tag = new Tag("ADS", 0);
        assertTrue(tagRepo.existsById(0));
        assertEquals(tag, tagRepo.getById(0));
    }

    @Test
    public void createTagFaultyTest() {
        cardRepo.save(new Card("Study ADS", "Weblav"));
        Tag faulty1 = new Tag(null, 23);
        assertEquals(ResponseEntity.badRequest().build(), controller.createTag(0, faulty1));
        faulty1.setName("");
        faulty1.setColour(-5);
        assertEquals(ResponseEntity.badRequest().build(), controller.createTag(0, faulty1));
    }

    @Test
    public void deleteTagTest() {
        cardRepo.save(new Card("Study ADS", "Do weblab"));
        this.controller.createTag(0, new Tag("ADS", 0));
        assertTrue(cardRepo.getById(0).getTags().size() > 0);
        assertTrue(tagRepo.existsById(0));
        this.controller.deleteTag(0,0);
        assertEquals(0, cardRepo.getById(0).getTags().size());
        assertFalse(tagRepo.existsById(0));
    }

    @Test
    public void deleteTagNotFoundTest() {
        assertEquals(ResponseEntity.badRequest().build(), controller.deleteTag(0,125));
    }

    @Test
    public void deleteTagNotInACard() {
        cardRepo.save(new Card("Study ADS", "Do weblab"));
        cardRepo.save(new Card("Study OOPP", "Do Git"));
        this.controller.createTag(0, new Tag("ADS", 0));
        assertEquals(ResponseEntity.badRequest().build(), controller.deleteTag(1,0));
    }

    @Test
    public void createTaskTest() {
        cardRepo.save(new Card("Study ADS", "Do weblab"));
        this.controller.createTask(0, new Task("ADS", false));
        final Task task = new Task("ADS", false);
        assertTrue(taskRepo.existsById(0));
        assertEquals(task, taskRepo.getById(0));
    }

    @Test
    public void createTaskFaultyTest() {
        cardRepo.save(new Card("Study ADS", "Weblav"));
        Task faulty1 = new Task(null, false);
        assertEquals(ResponseEntity.badRequest().build(), controller.createTask(0, faulty1));
    }

    @Test
    public void deleteTaskTest() {
        cardRepo.save(new Card("Study ADS", "Do weblab"));
        this.controller.createTask(0, new Task("ADS", false));
        assertTrue(cardRepo.getById(0).getNestedTaskList().size() > 0);
        assertTrue(taskRepo.existsById(0));
        this.controller.deleteTask(0,0);
        assertEquals(0, cardRepo.getById(0).getNestedTaskList().size());
        assertFalse(taskRepo.existsById(0));
    }

    @Test
    public void deleteTaskNotFoundTest() {
        assertEquals(ResponseEntity.badRequest().build(), controller.deleteTask(0,15));
    }

    @Test
    public void deleteTaskNotInACard() {
        cardRepo.save(new Card("Study ADS", "Do weblab"));
        cardRepo.save(new Card("Study OOPP", "Do Git"));
        this.controller.createTask(0, new Task("OOPP", true));
        assertEquals(ResponseEntity.badRequest().build(), controller.deleteTask(1,0));
    }
}