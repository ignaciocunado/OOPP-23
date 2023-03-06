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
    public void testEmptyContstructor() {
        new Card();
    }

    @Test
    public void testID(){
        assertEquals(card.getId(), 0);
    }

    @Test
    public void testgetTitle() {
        assertEquals(card.getTitle(), "Card");
    }

    @Test
    public void testgetDescription() {
        assertEquals(card.getDescription(), "This is a card");
    }

    @Test
    public void testgetNestedTaskList() {
        assertEquals(card.getNestedTaskList(), new ArrayList<Task>());
    }

    @Test
    public void testgetTags() {
        assertEquals(card.getTags(), new ArrayList<Tag>());
    }

    @Test
    public void testSetTitle() {
        card.setTitle("NEWCard");
        assertEquals(card.getTitle(), "NEWCard");
    }

    @Test
    public void testSetDescription() {
        card.setDescription("NEWDescription");
        assertEquals(card.getDescription(), "NEWDescription");
    }

    @Test
    public void testAddTag() {
        card.addTag(tag);
        ArrayList<Tag> tagsList = new ArrayList<>();
        tagsList.add(tag);
        assertEquals(1, card.getTags().size());
        assertEquals(card.getTags(), tagsList);
    }

    @Test
    public void equalsHashCode() {
        Card card1 = new Card("CardEquals", "123");
        Card card2 = new Card("CardEquals", "123");

        assertEquals(card1, card2);
        assertEquals(card1.hashCode(), card2.hashCode());
    }

    @Test
    public void equals2() {
        Card card1 = new Card("CardEquals", "123");
        Card card2 = new Card("CardNotEquals", "123");

        assertNotEquals(card1, card2);
    }

    @Test
    public void testToString() {
        assertEquals(card.toString(), "ID: 0 Title: Card Description: This is a card Tasks: " +
            "[] Tags: []");
    }
}