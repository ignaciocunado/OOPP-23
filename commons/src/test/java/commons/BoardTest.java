package commons;

import commons.entities.Board;
import commons.entities.Card;
import commons.entities.CardList;
import commons.entities.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private Board board;
    private CardList list;
    private Card card;

    @BeforeEach
    public void setup() {
        board = new Board("abc","def", "ghi");
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
    public void getNameTest() {assertEquals(board.getName(), "def");}

    @Test
    public void getPasswordTest() {
        assertEquals(board.getPassword(), "ghi");
    }

    @Test
    public void getListOfCardListTest() {
        assertEquals(board.getLists(), new ArrayList<CardList>());
    }

    @Test
    public void getTagsTest() {
        assertEquals(board.getTags(), new ArrayList<Tag>());
    }

    @Test
    public void setListsTest() {
        final List<CardList> lists = Arrays.asList(new CardList());
        assertEquals(board.getLists(), new ArrayList<CardList>());
        board.setLists(lists);
        assertEquals(board.getLists(), lists);
    }

    @Test
    public void setTagsTest() {
        final List<Tag> tags = Arrays.asList(new Tag());
        assertEquals(board.getTags(), new ArrayList<Tag>());
        board.setTags(tags);
        assertEquals(board.getTags(), tags);
    }


    @Test
    public void setIdTest() {
        board.setId(10);
        assertEquals(board.getId(), 10);
    }

    @Test
    public void setNameTest() {
        board.setName("NewName");
        assertEquals(board.getName(), "NewName");
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
        assertEquals(1, board.getLists().size());
        assertEquals(board.getLists(), listsOnBoard);
    }

    @Test
    public void addTagTest() {
        Tag tag = new Tag("tag", 20);
        board.addTag(tag);
        List<Tag> tagsOnBoard = new ArrayList<>();
        tagsOnBoard.add(tag);
        assertEquals(1,board.getTags().size());
        assertEquals(board.getTags(), tagsOnBoard);
    }
    @Test
    public void removeListTest() {
        CardList l1 = new CardList("cardList");
        board.addList(l1);
        List<CardList> listsOnBoard = new ArrayList<>();
        board.removeList(l1);
        assertEquals(0, board.getLists().size());
        assertEquals(board.getLists(), listsOnBoard);
    }

    @Test
    public void removeTagTest() {
        Tag tag = new Tag("tag", 0);
        board.addTag(tag);
        List<Tag> tags = new ArrayList<>();
        board.removeTag(tag);
        assertEquals(0, board.getTags().size());
        assertEquals(board.getTags(), tags);
    }

    @Test
    void removeTagByIdTest (){
        Tag tag = new Tag("tag", 0);
        tag.setId(1);
        board.addTag(tag);
        board.removeTagById(tag.getId());
        assertEquals(0, board.getTags().size());
        assertTrue(board.getTags().size() == 0);
    }

    @Test
    public void removeListByIdTest() {
        CardList l1 = new CardList("cardList");
        board.addList(l1);
        List<CardList> listsOnBoard = new ArrayList<>();
        board.removeListById(0);
        assertEquals(0, board.getLists().size());
        assertEquals(board.getLists(), listsOnBoard);
    }

    @Test
    public void equalsHashCodeTest() {
        Board b1 = new Board("BoardEquals","name", "pword");
        Board b2 = new Board("BoardEquals","name", "pword");

        assertEquals(b1, b2);
        assertEquals(b1.hashCode(), b2.hashCode());
    }

    @Test
    public void notEqualsTest() {
        Board b1 = new Board("BoardEquals","name", "pword");
        Board b2 = new Board("BoardNotEquals","name", "pword");

        assertNotEquals(b1, b2);
    }

    @Test
    public void toStringTest() {
        assertEquals(board.toString(), "<Board id=0 key=abc name=def password=ghi colour=rgb(1,35,69)>");
    }

    @Test
    public void toStringTest2() {
        board.setColour("333");
        assertEquals(board.toString(), "<Board id=0 key=abc name=def password=ghi colour=333>");
    }

    @Test
    public void getColourTest() {
        assertEquals("rgb(1,35,69)", board.getColour());
    }

    @Test
    public void getColourTest2() {
        board.setColour("1");
        assertEquals("1", board.getColour());
    }

    @Test
    public void setColourTest() {
        board.setColour("w333");
        assertEquals("w333", board.getColour());
    }

    @Test
    public void testNewConstructor() {
        Board newboard = new Board("name", "password","password", "colour", "green");
        assertEquals("colour", newboard.getColour());
        assertEquals("green", newboard.getFontColour());
    }

    @Test
    public void testFontColour() {
        assertEquals("white", board.getFontColour());
        board.setFontColour("green");
        assertEquals("green", board.getFontColour());
    }

}