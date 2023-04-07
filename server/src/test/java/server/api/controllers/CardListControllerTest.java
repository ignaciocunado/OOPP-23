package server.api.controllers;

import commons.entities.Card;
import commons.entities.CardList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.BindingResult;
import server.api.repositories.TestCardListRepository;
import server.api.repositories.TestCardRepository;
import server.exceptions.EntityNotFoundException;
import server.exceptions.InvalidRequestException;

class CardListControllerTest {

    private TestCardRepository cardRepo;
    private TestCardListRepository cardListRepo;
    private CardListController cardListController;

    private BindingResult hasErrorResult;
    private BindingResult noErrorResult;


    @BeforeEach
    public void setup() {
        this.cardRepo = new TestCardRepository();
        this.cardListRepo = new TestCardListRepository();
        this.cardListController = new CardListController(cardListRepo, cardRepo, Mockito.mock(SimpMessagingTemplate.class));

        this.hasErrorResult = Mockito.mock(BindingResult.class);
        this.noErrorResult = Mockito.mock(BindingResult.class);

        Mockito.when(hasErrorResult.hasErrors()).thenReturn(true);
        Mockito.when(noErrorResult.hasErrors()).thenReturn(false);
    }


    @Test
    void createCardTestNameAndTitleTest() {
        cardListRepo.save(new CardList("title"));
        final Card card = new Card("New Title", "New Description");
        card.setId(1);

        this.cardListController.createCard(1, card, noErrorResult);
        Assertions.assertEquals(this.cardRepo.getById(1), card);
    }

    @Test
    void createInvalidCardTest() {
        cardListRepo.save(new CardList("title"));

        final Card card = new Card("", null);
        card.setId(1);

        Assertions.assertThrows(InvalidRequestException.class, () -> this.cardListController.createCard(1, card, hasErrorResult));
    }

    @Test
    public void createCardNotFoundTest() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.cardListController.createCard(1, new Card("New Title", "New Description"), noErrorResult));
    }

    @Test
    void deleteCardTest() {
        this.cardListRepo.save(new CardList("title"));
        final Card card = this.cardListController.createCard(1, new Card("New Title", "New Description"), noErrorResult).getBody().getCards().get(0);

        Assertions.assertTrue(this.cardListRepo.findById(1).get().getCards().size() > 0);
        this.cardListController.deleteCard(1,card.getId());
        Assertions.assertTrue(this.cardListRepo.findById(1).get().getCards().size() == 0);
    }

    @Test
    public void deleteCardNotFoundCardTest() {
        this.cardListRepo.save(new CardList("title"));

        Assertions.assertThrows(EntityNotFoundException.class, () -> this.cardListController.deleteCard(1, 2));
    }

    @Test
    public void deleteCardNotFoundListTest() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.cardListController.deleteCard(1, 1));
    }

    @Test
    void editCardListTest() {
        this.cardListRepo.save(new CardList("title"));
        final CardList cardList1 = new CardList("title");
        cardList1.setId(1);
        final CardList cardList2 = new CardList("new title");
        cardList2.setId(1);
        cardList2.setColour("aaa");
        cardList2.setTextColour("DN");

        Assertions.assertEquals(this.cardListRepo.findById(1).get(), cardList1);
        final CardList toCompare = new CardList("title");
        toCompare.setColour("aaa");
        toCompare.setTextColour("DN");
        this.cardListController.editCardList(1, toCompare, noErrorResult);
        cardList1.setTitle("new title");
        cardListRepo.save(cardList1);
        Assertions.assertEquals(this.cardListRepo.findById(1).get(), cardList2);
    }

    @Test
    public void editInvalidCardListTest() {
        Assertions.assertThrows(InvalidRequestException.class, () -> this.cardListController.editCardList(5, new CardList(""), hasErrorResult));
    }

    @Test
    public void editCardListNotFoundTest() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.cardListController.editCardList(5, new CardList("title"), noErrorResult));
    }
}