package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private Board board;
    private CardList list;
    private Card card;

    @BeforeEach
    public void setup() {
        board = new Board("abc", "def");
        card = new Card("Card1", "This is a card");
        list = new CardList();
    }

    @Test
    public void emptyContstructorTest() {
        new Board();
    }

    @Test
    public void IDTest(){
        assertEquals(board.getId(), 0);
    }

    @Test
    public void getKeyTest() {
        assertEquals(board.getKey(), "abc");
    }

    @Test
    public void getPasswordTest() {
        assertEquals(board.getPassword(), "def");
    }

    @Test
    public void getListOfCardListTest() {
        assertEquals(board.getListsOnBoard(), new ArrayList<CardList>());
    }

    @Test
    public void setPasswordTest() {
        board.setPassword("NewPassword");
        assertEquals(board.getPassword(), "NewPassword");
    }

    @Test
    public void addListTest() {
        CardList l1 = new CardList("cardList");
        board.addList(l1);
        List<CardList> listsOnBoard = new ArrayList<>();
        listsOnBoard.add(l1);
        assertEquals(1, board.getListsOnBoard().size());
        assertEquals(board.getListsOnBoard(), listsOnBoard);
    }

    @Test
    public void removeListTest() {
        CardList l1 = new CardList("cardList");
        board.addList(l1);
        List<CardList> listsOnBoard = new ArrayList<>();
        board.removeList(l1);
        assertEquals(0, board.getListsOnBoard().size());
        assertEquals(board.getListsOnBoard(), listsOnBoard);
    }

    @Test
    public void equalsHashCodeTest() {
        Board b1 = new Board("BoardEquals", "pword");
        Board b2 = new Board("BoardEquals", "pword");

        assertEquals(b1, b2);
        assertEquals(b1.hashCode(), b2.hashCode());
    }

    @Test
    public void notEqualsTest() {
        Board b1 = new Board("BoardEquals", "pword");
        Board b2 = new Board("BoardNotEquals", "pword");

        assertNotEquals(b1, b2);
    }

    @Test
    public void toStringTest() {
        assertEquals(board.toString(), "<Board id=0 key=abc password=def>");
    }
}