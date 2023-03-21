package commons;

import commons.entities.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task("Task", true);
    }

    @Test
    void emptyConstructorTest(){
        new Task();
    }

    @Test
    public void getIdTest() {
        assertEquals(task.getId(), 0);
    }

    @Test
    public void getNameTest() {
        assertEquals(task.getName(), "Task");
    }

    @Test
    public void setNameTest() {
        task.setName("Tasks");
        assertEquals(task.getName(), "Tasks");
    }

    @Test
    public void isCompletedTest() {
        assertEquals(task.isCompleted(), true);
    }

    @Test
    public void setCompleted() {
        task.setCompleted(false);
        assertEquals(task.isCompleted(), false);
    }

    @Test
    public void equalsTest() {
        Task task1 = new Task("Task", true);
        Task task2 = new Task("Task", true);

        assertEquals(task1, task2);
    }

    @Test
    public void notEqualsTest() {
        Task task3 = new Task("Task", true);
        Task task4 = new Task("Task", false);

        assertNotEquals(task3, task4);
    }

    @Test
    public void hashCodeTest() {
        Task task5 = new Task("Task", true);
        Task task6 = new Task("Task", true);

        assertEquals(task5,task6);
        assertEquals(task5.hashCode(),task6.hashCode());
    }

    @Test
    public void toStringTest() {
        assertEquals(task.toString(), "<Task id=0 name=Task completed=TRUE>");
    }
}