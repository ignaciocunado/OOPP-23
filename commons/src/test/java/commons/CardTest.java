package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {
    private Card card;
    private Tag tag;

    @BeforeEach
    public void setup() {
        card = new Card("Card", "This is a card");
        tag = new Tag("Tag", 69);
    }

    @Test
    public void emptyContstructorTest() {
        new Card();
    }

    @Test
    public void getIdTest(){
        assertEquals(card.getId(), 0);
    }

    @Test
    public void getTitleTest() {
        assertEquals(card.getTitle(), "Card");
    }

    @Test
    public void getDescriptionTest() {
        assertEquals(card.getDescription(), "This is a card");
    }

    @Test
    public void getNestedTaskListTest() {
        assertEquals(card.getNestedTaskList(), new ArrayList<Task>());
    }

    @Test
    public void getTagsTest() {
        assertEquals(card.getTags(), new ArrayList<Tag>());
    }

    @Test
    public void setTitleTest() {
        card.setTitle("NEWCard");
        assertEquals(card.getTitle(), "NEWCard");
    }

    @Test
    public void setDescriptionTest() {
        card.setDescription("NEWDescription");
        assertEquals(card.getDescription(), "NEWDescription");
    }

    @Test
    public void addTagTest() {
        card.addTag(tag);
        ArrayList<Tag> tagsList = new ArrayList<>();
        tagsList.add(tag);
        assertEquals(1, card.getTags().size());
        assertEquals(card.getTags(), tagsList);
    }

    @Test
    public void equalsHashCodeTest() {
        Card card1 = new Card("CardEquals", "123");
        Card card2 = new Card("CardEquals", "123");

        assertEquals(card1, card2);
        assertEquals(card1.hashCode(), card2.hashCode());
    }

    @Test
    public void equalsTest2() {
        Card card1 = new Card("CardEquals", "123");
        Card card2 = new Card("CardNotEquals", "123");

        assertNotEquals(card1, card2);
    }

    @Test
    public void toStringTest() {
        assertEquals(card.toString(), "<Card id=0 title=Card description=This is a card>");
    }
}