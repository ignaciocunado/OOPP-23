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

    @Test
    public void getColourTest() {
        CardList list = new CardList("new");
        assertEquals("rgb(35,69,103)", list.getColour());
    }

    @Test
    public void getColourTest2() {
        CardList list = new CardList("new");
        list.setColour("1");
        assertEquals("1", list.getColour());
    }

    @Test
    public void setColourTest() {
        CardList list = new CardList("new");
        list.setColour("w333");
        assertEquals("w333", list.getColour());
    }

    @Test
    public void setIdTest() {
        CardList list = new CardList("new");
        assertEquals(0, list.getId());
        list.setId(69);
        assertEquals(69, list.getId());
    }

    @Test
    public void removeCardbyIdTest() {
        CardList list = new CardList("new");
        Card card = new Card("dd", "dd");
        card.setId(999);
        assertEquals(0, list.getCards().size());
        list.addCard(card);
        assertEquals(1, list.getCards().size());
        list.removeCardById(0);
        assertEquals(1, list.getCards().size());
        list.removeCardById(999);
        assertEquals(0, list.getCards().size());
    }

    @Test
    public void testNewConstructor() {
        CardList newCardList = new CardList("name", "colour", "textColour");
        assertEquals("colour", newCardList.getColour());
        assertEquals("textColour", newCardList.getTextColour());
    }

    @Test
    public void getTextColourTest() {
        assertEquals(new CardList("d").getTextColour(), "white");
    }

    @Test
    public void setTextColourTest() {
        CardList list = new CardList("d");
        list.setTextColour("aaaaa");
        assertEquals(list.getTextColour(), "aaaaa");
    }
}