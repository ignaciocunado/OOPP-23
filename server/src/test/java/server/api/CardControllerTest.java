package server.api;

import commons.Card;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.*;

import static org.junit.jupiter.api.Assertions.*;

class CardControllerTest {

    private CardService service;
    private CardRepositoryTest cardRepo;
    private TagRepositoryTest tagRepo;
    private TaskRepositoryTest taskRepo;
    private CardController controller;

    @BeforeEach
    public void setup() {
        service = new CardService();
        cardRepo = new CardRepositoryTest();
        tagRepo = new TagRepositoryTest();
        taskRepo = new TaskRepositoryTest();
        controller = new CardController(service, cardRepo,tagRepo,taskRepo);
    }

    @Test
    public void editCardTitleTest() {

    }

    @Test
    public void editCardTitleNotFoundTest() {

    }

    @Test
    public void editCardDescriptionTest() {

    }

    @Test
    public void editCardDescriptionNotFoundTest() {

    }

    @Test
    public void createTagTest() {
        cardRepo.save(new Card("Study ADS", "Do weblab"));
        this.controller.createTag(0, new Tag("ADS", 0));
        final Tag tag = new Tag("ADS", 0);
        System.out.println(cardRepo.getById(0));
        assertEquals(tag, tagRepo.getById(0));
    }

    @Test
    public void deleteTagTest() {

    }

    @Test
    public void deleteTagNotFoundTest() {

    }

    @Test
    public void createTaskTest() {

    }


    @Test
    public void deleteTaskTest() {

    }

    @Test
    public void deleteTaskNotFoundTest() {

    }
}