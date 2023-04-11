package server.api.controllers;

import commons.entities.Card;
import commons.entities.CardList;
import commons.entities.Tag;
import commons.entities.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.BindingResult;
import server.api.repositories.TestCardListRepository;
import server.api.repositories.TestCardRepository;
import server.api.repositories.TestTagRepository;
import server.api.repositories.TestTaskRepository;
import server.database.CardListRepository;
import server.exceptions.EntityNotFoundException;
import server.exceptions.InvalidRequestException;
import server.services.CardService;

import static org.junit.jupiter.api.Assertions.*;

class CardControllerTest {

    private TestCardRepository cardRepo;
    private TestTagRepository tagRepo;
    private TestTaskRepository taskRepo;
    private CardListRepository listRepo;
    private CardController controller;

    private BindingResult hasErrorResult;
    private BindingResult noErrorResult;
    private CardService cardService;


    @BeforeEach
    public void setup() {
        cardRepo = new TestCardRepository();
        tagRepo = new TestTagRepository();
        taskRepo = new TestTaskRepository();
        listRepo = new TestCardListRepository();
        cardService = new CardService(listRepo, cardRepo, tagRepo, taskRepo);
        controller = new CardController(cardService, Mockito.mock(SimpMessagingTemplate.class));

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
        assertEquals(this.cardRepo.findById(1).get().getTags().size(), 1);
        assertEquals(this.cardRepo.findById(1).get().getTags().get(0), tag);
    }

    @Test
    public void assignTagAlreadyAssignedTest() {
        cardRepo.save(new Card("Study ADS", "Do weblab"));
        final Tag tag = new Tag("ADS", 0);
        tag.setId(10);
        tagRepo.save(tag);
        this.controller.assignTag(1, 1);
        assertTrue(tagRepo.existsById(1));
        assertEquals(tag, tagRepo.getById(1));
        assertEquals(this.cardRepo.findById(1).get().getTags().size(), 1);
        assertEquals(this.cardRepo.findById(1).get().getTags().get(0), tag);
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
        Assertions.assertThrows(EntityNotFoundException.class, () -> controller.assignTag(1, 1));
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
        final Card card = cardRepo.save(new Card());
        Assertions.assertThrows(EntityNotFoundException.class, () -> controller.deleteTag(card.getId(),125));
    }

    @Test
    public void deleteTagCardNotFoundTest() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> controller.deleteTag(10,0));
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

    @Test
    public void moveTest() {
        final Card card = this.cardRepo.save(new Card());
        CardList deleteList = new CardList("title");
        deleteList.addCard(card);
        this.listRepo.save(deleteList);
        final CardList addList = this.listRepo.save(new CardList("title"));

        Assertions.assertTrue(this.listRepo.findById(deleteList.getId()).get().getCards().size() > 0);
        this.controller.move(card.getId(), addList.getId(), 0);
        Assertions.assertTrue(this.listRepo.findById(deleteList.getId()).get().getCards().size() == 0);
        Assertions.assertTrue(this.listRepo.findById(addList.getId()).get().getCards().size() > 0);
    }

    @Test
    public void moveSameListTest() {
        final Card card = this.cardRepo.save(new Card());
        final Card card2 = this.cardRepo.save(new Card());
        CardList list = new CardList("title");
        list.addCard(card);
        list.addCard(card2);
        this.listRepo.save(list);

        this.controller.move(card2.getId(), list.getId(), 0);
        Assertions.assertEquals(this.listRepo.findById(list.getId()).get().getCards().get(0), card2);
    }

    @Test
    public void moveNoCardFoundTest() {
        final CardList deleteList = this.listRepo.save(new CardList("title"));
        Assertions.assertThrows(EntityNotFoundException.class, () -> controller.move(12,
            deleteList.getId(), 0));
    }

    @Test
    public void moveCardNotInListTest() {
        final Card card = this.cardRepo.save(new Card());
        final CardList deleteList = this.listRepo.save(new CardList("title"));
        Assertions.assertThrows(EntityNotFoundException.class, () -> controller.move(card.getId(),
            deleteList.getId(), 0));
    }

    @Test
    public void moveListNotFoundTest() {
        final CardList cardList = new CardList("");
        final Card card = this.cardRepo.save(new Card());
        cardList.addCard(card);
        this.listRepo.save(cardList);
        Assertions.assertThrows(EntityNotFoundException.class, () -> controller.move(card.getId(),
            12, 0));
    }
}
