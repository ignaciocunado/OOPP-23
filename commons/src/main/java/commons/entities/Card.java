package commons.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Entity
public final class Card {

    @Id
    @GeneratedValue
    private int id;

    @NotBlank
    private String title;

    @NotNull
    private String description;
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Task> nestedTaskList;
    @ManyToMany
    private List<Tag> tags;

    /**
     * Empty constructor for JPA
     */
    public Card() {}

    /**
     * Constructor for a new card
     * @param title title of the card
     * @param description description of the card
     */
    public Card(String title, String description) {
        this.title = title;
        this.description = description;
        this.nestedTaskList = new ArrayList<Task>();
        this.tags = new ArrayList<Tag>();
    }

    /**
     * Getter for the ID of a card
     * @return automatically generated ID of a card
     */
    public int getId() {
        return id;
    }

    /**
     *  setter for id
     * @param id new id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter for the title of a card
     * @return title of a card
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter for the description of card
     * @return description of a card
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter for the nested task list of a card
     * @return list of tasks
     */
    public List<Task> getNestedTaskList() {
        return nestedTaskList;
    }

    /**
     * Getter for the list of tags of a card
     * @return List of tags
     */
    public List<Tag> getTags() {
        return tags;
    }

    /**
     * Setter for the title of a card
     * @param title new title for the card
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Setter for the description of a card
     * @param description new description for the card
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * setter for the Task list
     * @param nestedTaskList new list of Tasks
     */
    public void setNestedTaskList(List<Task> nestedTaskList) {
        this.nestedTaskList = nestedTaskList;
    }

    /**
     * Setter for the Tag list
     * @param tags new list of Tags
     */
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    /**
     * Method which adds a new tag to a card
     * @param tag the tag to be added to the card
     * @return true iff the tag was successfully added
     */
    public boolean addTag(Tag tag) {
        return this.tags.add(tag);
    }

    /**
     * Method which adds a task to a card
     * @param task the task to be added to the nested task list
     * @return true iff the task was successfully added
     */
    public boolean addTask(Task task) {
        return this.nestedTaskList.add(task);
    }

    /**
     * Method which removes a specific Tag from a card
     * @param tag the Tag to be removed from the card
     * @return true iff the Tag was successfully removed
     */
    public boolean removeTag(Tag tag) {
        return this.tags.remove(tag);
    }

    /**
     * Removes a Tag from a card iff it exists
     * @param id id of the Tag to remove
     * @return true iff the tag was successfully removed
     */
    public boolean removeTagById(int id) {
        return tags.removeIf(tag -> tag.getId() == id);
    }

    /**
     * Method which removes a specific task from a card
     * @param task the task to be removed from the card
     * @return true iff the task was successfully removed
     */
    public boolean removeTask(Task task) {
        return this.nestedTaskList.remove(task);
    }

    /**
     * Removes a Task from a card iff it exists
     * @param id id of the Task to remove
     * @return true iff the Task was successfully removed
     */
    public boolean removeTaskById(int id) {
        return nestedTaskList.removeIf(task -> task.getId() == id);
    }

    /**
     * Calculates how many subtasks have been completed
     * @return decimal
     */
    public double calculateProgress() {
        if(nestedTaskList.isEmpty()) {
            return 0;
        }
        return (double) nestedTaskList.stream().filter(task ->
                task.isCompleted() == true).toList().size() / (double) nestedTaskList.size();
    }

    /**
     * Equals method
     * @param o other object to compare this object to
     * @return true iff o is also a Card with the same properties.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return id == card.id && title.equals(card.title) && description.equals(card.description) &&
                nestedTaskList.equals(card.nestedTaskList) && tags.equals(card.tags);
    }

    /**
     * HashCode method
     * @return a unique code representing this card
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, nestedTaskList, tags);
    }

    /**
     * To string method for a card
     * @return a human readable string representing a card
     */
    @Override
    public String toString() {
        return String.format("<Card id=%d title=%s description=%s>", this.id, this.title,
            this.description);

    }
}
