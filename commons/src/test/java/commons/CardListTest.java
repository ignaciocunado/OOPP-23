package commons;

import commons.entities.Card;
import commons.entities.CardList;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CardListTest {

    @Test
    void constructorTest() {
        new CardList();
    }
    @Test
    void getIdTest() {
        CardList list = new CardList("new");
        assertEquals(list.getId() ,0);
    }

    @Test
    void getTitleTest() {
        CardList list = new CardList("new");
        assertEquals(list.getTitle(), "new");
    }

    @Test
    void getCardsNullTest() {
        CardList list = new CardList("new");
        assertEquals(list.getCards(),List.of());
    }
    @Test
    void getCardsTest() {
        CardList list = new CardList("new");
        Card card = new Card("newCard", "this is a new card");
        list.addCard(card);
        assertEquals(list.getCards(), List.of(card));
    }

    @Test
    void setTitleTest() {
        CardList list = new CardList("new");
        list.setTitle("newer");
        assertEquals(list.getTitle(), "newer");
    }

    @Test
    void addCardTest() {
        CardList list = new CardList("new");
        Card card = new Card("newCard", "this is a new card");
        list.addCard(card);
        assertNotNull(list.getCards());
    }

    @Test
    void removeCardTest() {
        CardList list = new CardList("new");
        Card card = new Card("newCard", "this is a new card");
        list.addCard(card);
        list.removeCard(card);
        assertEquals(list.getCards(),List.of());
    }

    @Test
    void editCardIndexRightwardsTest() {
        CardList list = new CardList("new");
        Card card = new Card("newCard", "this is a new card");
        Card card2 = new Card("newCard2", "this is a new card");
        Card card3 = new Card("newCard3", "this is a new card");
        Card card4 = new Card("newCard4", "this is a new card");
        list.addCard(card);
        list.addCard(card2);
        list.addCard(card3);
        list.addCard(card4);
        list.editCardIndex(card2, 3);
        assertEquals(card2, list.getCards().get(3));
        assertEquals(card3, list.getCards().get(1));
        assertEquals(card4, list.getCards().get(2));
    }

    @Test
    void editCardIndexLeftwardsTest() {
        CardList list = new CardList("new");
        Card card = new Card("newCard", "this is a new card");
        Card card2 = new Card("newCard2", "this is a new card");
        Card card3 = new Card("newCard3", "this is a new card");
        Card card4 = new Card("newCard4", "this is a new card");
        list.addCard(card);
        list.addCard(card2);
        list.addCard(card3);
        list.addCard(card4);
        list.editCardIndex(card3, 0);
        assertEquals(card3, list.getCards().get(0));
        assertEquals(card, list.getCards().get(1));
        assertEquals(card2, list.getCards().get(2));
        assertEquals(card4, list.getCards().get(3));
    }

    @Test
    void toStingTest() {
        CardList list = new CardList("new");
        assertEquals("List: title - new, id - 0.", list.toString());
    }

    @Test
    void testEquals1Test() {
        CardList list1 = new CardList("new");
        CardList list2 = new CardList("new");
        assertTrue(list1.equals(list2));
    }
    @Test
    void testEquals2Test() {
        CardList list1 = new CardList("new");
        CardList list2 = new CardList("newer");
        assertFalse(list1.equals(list2));
    }

    @Test
    void testHashCodeTest() {
        CardList list1 = new CardList("new");
        CardList list2 = new CardList("newer");
        assertNotEquals(list1.hashCode(), list2.hashCode());
    }
}