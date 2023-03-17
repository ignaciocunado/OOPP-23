package server.api.controllers;

import commons.Board;
import commons.Card;
import commons.CardList;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import server.api.repositories.TestBoardRepository;
import server.api.repositories.TestCardListRepository;
import server.api.repositories.TestCardRepository;
import server.database.BoardRepository;

import static org.junit.jupiter.api.Assertions.*;

class CardListControllerTest {

    private TestCardRepository cardRepo;
    private TestCardListRepository cardListRepo;
    private CardListController cardListController;

    private TestBoardRepository boardRepo;

    @BeforeEach
    public void setup() {
        this.cardRepo = new TestCardRepository();
        this.cardListRepo = new TestCardListRepository();
        this.cardListController = new CardListController(cardListRepo, cardRepo);
        this.boardRepo = new TestBoardRepository();
    }


    @Test
    void createCardTestNameAndTitleTest() {
        cardListRepo.save(new CardList("title"));
        final Card card = new Card("New Title", "New Description");
        this.cardListController.createCard(1, card);

        card.setId(1);
        assertEquals(this.cardRepo.getById(1), card);
    }

    @Test
    public void createCardNotFoundTest() {
        Assertions.assertEquals(this.cardListController.createCard(1, new Card("New Title", "New Description")).getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteCardTest() {
        this.cardListRepo.save(new CardList("title"));
        final Card card = this.cardListController.createCard(1, new Card("New Title", "New Description")).getBody().getCards().get(0);

        Assertions.assertTrue(this.cardListRepo.findById(1).get().getCards().size() > 0);
        this.cardListController.deleteCard(1,card.getId());
        Assertions.assertTrue(this.cardListRepo.findById(1).get().getCards().size() == 0);
    }

    @Test
    public void deleteCardNotFoundCardTest() {
        this.cardListRepo.save(new CardList("title"));

        Assertions.assertEquals(this.cardListController.deleteCard(1, 2).getStatusCode(),
            HttpStatus.NOT_FOUND);
    }

    @Test
    public void deleteCardNotFoundListTest() {
        Assertions.assertEquals(this.cardListController.deleteCard(1, 1).getStatusCode(),
            HttpStatus.NOT_FOUND);
    }

    @Test
    void editCardListTitleTest() {
        this.cardListRepo.save(new CardList("title"));
        final CardList cardlist1 = new CardList("title");
        cardlist1.setId(1);
        final CardList cardlist2 = new CardList("new title");
        cardlist2.setId(1);

        Assertions.assertEquals(this.cardListRepo.findById(1).get(), cardlist1);
        this.cardListController.editCardListTitle(1, new CardList());
        cardlist1.setTitle("new title");
        cardListRepo.save(cardlist1);
        Assertions.assertEquals(this.cardListRepo.findById(1).get(), cardlist2);
    }

    @Test
    public void editCardListNotFoundTest() {
        Assertions.assertEquals(this.cardListController.editCardListTitle(5, new CardList()).getStatusCode(), HttpStatus.NOT_FOUND);
    }
}