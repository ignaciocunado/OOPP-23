package server.api;

import commons.Card;
import commons.Tag;
import commons.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
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
        card.setId(1);
        assertEquals(cardRepo.getById(1), card);
        Card newCard = new Card("OOPP", null);
        controller.editCard(1, newCard);
        assertEquals(cardRepo.getById(1).getTitle(), "OOPP");
    }

    @Test
    public void cardRepoTest() {
        cardRepo.save(new Card("Title", "title"));
        cardRepo.save(new Card("Title", "ewewew"));
        assertEquals(2, cardRepo.findAll().size());
        assertEquals(1, cardRepo.findAll().get(0).getId());
        assertEquals(2, cardRepo.findAll().get(1).getId());
    }

    @Test
    public void taskRepoTest() {
        taskRepo.save(new Task("Title", false));
        taskRepo.save(new Task("Title", false));
        assertEquals(2, taskRepo.findAll().size());
        assertEquals(1, taskRepo.findAll().get(0).getId());
        assertEquals(2, taskRepo.findAll().get(1).getId());
    }

    @Test
    public void tagRepoTest() {
        tagRepo.save(new Tag("Title", 0));
        tagRepo.save(new Tag("Title", 0));
        assertEquals(2, tagRepo.findAll().size());
        assertEquals(1, tagRepo.findAll().get(0).getId());
        assertEquals(2, tagRepo.findAll().get(1).getId());
    }

    @Test
    public void editCardTitleNotFoundTest() {
        assertEquals(new ResponseEntity<>(HttpStatus.BAD_REQUEST), controller.editCard(0,
            new Card("Test",  "Test")));
    }

    @Test
    public void editCardDescriptionTest() {
        cardRepo.save(new Card("ADS", "ADS"));
        Card card = new Card("ADS", "ADS");
        card.setId(1);
        assertEquals(cardRepo.getById(1), card);
        controller.editCard(1, new Card("ADS", "Binary Search Trees"));
        assertEquals(cardRepo.getById(1).getDescription(), "Binary Search Trees");
    }

    @Test
    public void editCardDescriptionNotFoundTest() {
        assertEquals(new ResponseEntity<>(HttpStatus.BAD_REQUEST), controller.editCard(0,
            new Card("Test", "Test")));
    }

    @Test
    public void createTagTest() {
        cardRepo.save(new Card("Study ADS", "Do weblab"));
        this.controller.createTag(1, new Tag("ADS", 0));
        final Tag tag = new Tag("ADS", 0);
        tag.setId(1);
        assertTrue(tagRepo.existsById(1));
        assertEquals(tag, tagRepo.getById(1));
    }

    @Test
    public void createTagFaultyTest() {
        cardRepo.save(new Card("Study ADS", "Weblav"));
        Tag faulty1 = new Tag(null, 23);
        assertEquals(new ResponseEntity<>(HttpStatus.BAD_REQUEST), controller.createTag(0, faulty1));
        faulty1.setName("");
        faulty1.setColour(-5);
        assertEquals(new ResponseEntity<>(HttpStatus.BAD_REQUEST), controller.createTag(0, faulty1));
    }

    @Test
    public void deleteTagTest() {
        cardRepo.save(new Card("Study ADS", "Do weblab"));
        Tag toAdd =  new Tag("ADS", 0);
        toAdd.setId(1);
        this.controller.createTag(1, toAdd);
        assertTrue(cardRepo.getById(1).getTags().size() > 0);
        assertTrue(tagRepo.existsById(1));
        this.controller.deleteTag(1,1);
        assertFalse(tagRepo.existsById(1));
        assertEquals(0, cardRepo.getById(1).getTags().size());
    }

    @Test
    public void deleteTagNotFoundTest() {
        assertEquals(new ResponseEntity<>(HttpStatus.BAD_REQUEST), controller.deleteTag(0,125));
    }

    @Test
    public void deleteTagNotInACard() {
        cardRepo.save(new Card("Study ADS", "Do weblab"));
        cardRepo.save(new Card("Study OOPP", "Do Git"));
        this.controller.createTag(1, new Tag("ADS", 0));
        assertEquals(new ResponseEntity<>(HttpStatus.BAD_REQUEST), controller.deleteTag(2,1));
    }

    @Test
    public void createTaskTest() {
        cardRepo.save(new Card("Study ADS", "Do weblab"));
        this.controller.createTask(1, new Task("ADS", false));
        final Task task = new Task("ADS", false);
        task.setId(1);
        assertTrue(taskRepo.existsById(1));
        assertEquals(task, taskRepo.getById(1));
    }

    @Test
    public void createTaskFaultyTest() {
        cardRepo.save(new Card("Study ADS", "Weblav"));
        Task faulty1 = new Task(null, false);
        assertEquals(new ResponseEntity<>(HttpStatus.BAD_REQUEST), controller.createTask(0, faulty1));
    }

    @Test
    public void deleteTaskTest() {
        cardRepo.save(new Card("Study ADS", "Do weblab"));
        Task toAdd =  new Task("ADS", false);
        toAdd.setId(1);
        this.controller.createTask(1, toAdd);
        assertTrue(cardRepo.getById(1).getNestedTaskList().size() > 0);
        assertTrue(taskRepo.existsById(1));
        this.controller.deleteTask(1,1);
        assertFalse(taskRepo.existsById(1));
        assertEquals(0, cardRepo.getById(1).getNestedTaskList().size());
    }

    @Test
    public void deleteTaskNotFoundTest() {
        assertEquals(new ResponseEntity<>(HttpStatus.BAD_REQUEST), controller.deleteTask(0,15));
    }

    @Test
    public void deleteTaskNotInACard() {
        cardRepo.save(new Card("Study ADS", "Do weblab"));
        cardRepo.save(new Card("Study OOPP", "Do Git"));
        this.controller.createTask(1, new Task("OOPP", true));
        assertEquals(new ResponseEntity<>(HttpStatus.BAD_REQUEST), controller.deleteTask(2,1));
    }
}