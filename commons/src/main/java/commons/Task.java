package commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Task {
    @Id @GeneratedValue
    private int id;
    private String name;
    private boolean completed;

    /**
     * Empty constructor for JPA
     */
    public Task() {
    }

    /**
     * Constructor to initialize new task
     * @param name of the task to display
     * @param completed if the task is done or not it
     */
    public Task(String name, boolean completed) {
        this.name = name;
        this.completed = completed;
    }

    /**
     * @return gets unique ID of task
     */
    public int getId() {
        return id;
    }

    /**
     * @return the name of the task
     */
    public String getName() {
        return name;
    }

    /** sets the name of the task
     * @param name string value indicating the name of the task
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return returns a boolean value indicating if the task is done or not
     */
    public boolean isCompleted() {
        return completed;
    }

    /** sets the boolean value of the task
     * @param completed the new boolean value
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * @param o the object we are comparing to
     * @return a boolean value indicating if they are equal or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && completed == task.completed &&
            Objects.equals(name, task.name);
    }

    /**
     * Hashcode
     * @return the hashcode of the task
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, completed);
    }
}
