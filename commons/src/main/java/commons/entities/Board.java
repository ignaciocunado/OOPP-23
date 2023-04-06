package commons.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public final class Board {

    @Id @GeneratedValue
    private int id;

    @NotBlank
    private String key;
    @NotNull
    private String name;
    @NotNull
    private String password;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tag> tags;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CardList> lists;
    @NotNull
    private String colour;

    /**
     * Empty constructor for JPA
     */
    public Board() {}

    /**
     * Constructor for a new board
     *
     * @param key      to enter the board (view-only if board is secured with password)
     * @param name
     * @param password to enter and edit the board
     */
    public Board(String key, String name, String password) {
        this.key = key;
        this.name = name;
        this.password = password;
        this.lists = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.colour = "";
    }

    /**
     * Optional Constructor for a new board
     *
     * @param key      to enter the board (view-only if board is secured with password)
     * @param name
     * @param password to enter and edit the board
     * @param colour colour
     */
    public Board(String key, String name, String password, String colour) {
        this.key = key;
        this.name = name;
        this.password = password;
        this.lists = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.colour = colour;
    }

    /**
     * Gets the colour of the board
     * @return colour
     */
    public String getColour() {
        return colour;
    }

    /**
     * Sets the colour of the board
     * @param colour colour
     */
    public void setColour(String colour) {
        this.colour = colour;
    }

    /**
     * Gets the id of the board
     * @return id of the board
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the key of the board
     * @return key of the board
     */
    public String getKey() {
        return key;
    }

    /**
     * Gets the name of the board
     * @return name of the board
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the password of the board
     * @return password of the board
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the id of the board
     * @param id
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * Sets the name for the board
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the password for the board
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the list of CardList on this board
     * @return the list of CardList as an ArrayList
     */
    public List<CardList> getLists() {
        return lists;
    }

    /**
     * Sets the lists on the board
     * @param lists the lists to replace them with
     */
    public void setLists(final List<CardList> lists) {
        this.lists = lists;
    }

    /**
     * Gets the tags that are on this board
     * @return the list of tags as an ArrayList
     */
    public List<Tag> getTags() {
        return tags;
    }

    /**
     * Sets the tags on the board
     * @param tags the tags to replace with
     */
    public void setTags(final List<Tag> tags) {
        this.tags = tags;
    }

    /**
     * Adds a new CardList on this board
     * @param newList the new CardList to be added
     * @return boolean for whether the CardList has been added successfully
     */
    public boolean addList(CardList newList) {
        return lists.add(newList);
    }

    /** Adds a new Tag on this board
     * @param tag the new tag that is created
     * @return returns a boolean which indicates if the tag has been added to the board
     */
    public boolean addTag(Tag tag){
        return tags.add(tag);
    }

    /**
     * Removes a given CardList from this board
     * @param listToDelete the CardList to be deleted
     * @return boolean for whether the CardList has been deleted successfully
     */
    public boolean removeList(CardList listToDelete) {
        return lists.remove(listToDelete);
    }

    /** Removes a given tag from this board
     * @param tag the tag that will be deleted
     * @return a boolean value whether the tag has been deleted or not
     */
    public boolean removeTag(Tag tag) {
        return tags.remove(tag);
    }

    /**
     * Removes a given tag from this board
     * @param tagId id of the tag that is going to be deleted
     * @return a boolean value whether the tag has been deleted or not
     */
    public boolean removeTagById(final int tagId){
        return tags.removeIf(tag -> tag.getId() == tagId);
    }
    /**
     * Removes a given CardList from this board
     * @param id the id of the CardList to be deleted
     * @return boolean for whether the CardList has been deleted successfully
     */
    public boolean removeListById(final int id) {
        return this.lists.removeIf(cardList -> cardList.getId() == id);
    }

    /**
     * Equals method
     * @param o the object to compare
     * @return whether the two objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Board)) return false;
        Board board = (Board) o;
        return id == board.id && Objects.equals(key, board.key) &&
            Objects.equals(name, board.name) &&
            Objects.equals(password, board.password) &&
            Objects.equals(lists, board.lists);
    }

    /**
     * Hashcode
     * @return hashcode of the Board
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, key, name, password, lists);
    }

    /**
     * To string method for the board
     * @return a string representing the board class
     */
    @Override
    public String toString() {
        return String.format("<Board id=%d key=%s name=%s password=%s>", this.id, this.key,
                this.name, this.password);
    }
}
