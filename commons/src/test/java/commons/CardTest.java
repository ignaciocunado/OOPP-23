package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {
    private Card card;
    private Tag tag;
    private Task task;

    @BeforeEach
    public void setup() {
        card = new Card("Card", "This is a card");
        tag = new Tag("Tag", 69);
        task = new Task("Tag", true);
    }

    @Test
    public void emptyConstructorTest() {
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
        assertEquals(card.getTags().get(0), tag);
        assertEquals(card.getTags(), tagsList);
    }

    @Test
    public void addTaskTest() {
        card.addTask(task);
        ArrayList<Task> taskList = new ArrayList<>();
        taskList.add(task);
        assertEquals(1, card.getNestedTaskList().size());
        assertEquals(card.getNestedTaskList().get(0), task);
        assertEquals(card.getNestedTaskList(), taskList);
    }

    @Test
    public void removeTagTest() {
        card.addTag(tag);
        card.removeTag(tag);
        assertEquals(0, card.getTags().size());
        assertEquals(card.getTags(),new ArrayList<Tag>());
    }

    @Test
    public void removeTaskTest() {
        card.addTask(task);
        card.removeTask(task);
        assertEquals(0, card.getNestedTaskList().size());
        assertEquals(card.getNestedTaskList(),new ArrayList<Task>());
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

    @Test
    public void setIdTest() {
        card.setId(69);
        assertEquals(card.getId(), 69);
    }

    @Test
    public void setTasksTest() {
        card.addTask(task);
        Task task2 = new Task("AAAAAA", false);
        List<Task> taskList = new ArrayList<>();
        taskList.add(task2);
        card.setNestedTaskList(taskList);
        assertEquals(taskList, card.getNestedTaskList());
    }

    @Test
    public void setTagsTest() {
        card.addTag(tag);
        Tag tag2 = new Tag("AAAAAA", 0);
        List<Tag> tagList = new ArrayList<>();
        tagList.add(tag2);
        card.setTags(tagList);
        assertEquals(tagList, card.getTags());
    }

    @Test
    public void removeTagByIdTest() {
        card.addTag(tag);
        assertTrue(card.removeTagById(tag.getId()));
    }

    @Test
    public void removeTagByIdNotFoundTest() {
        card.addTag(tag);
        assertFalse(card.removeTagById(tag.getId() + 2));
    }

    @Test
    public void removeTaskByIdTest() {
        card.addTask(task);
        assertTrue(card.removeTaskById(task.getId()));
    }

    @Test
    public void removeTaskByIdNotFoundTest() {
        card.addTask(task);
        assertFalse(card.removeTaskById(task.getId() + 2));
    }
}