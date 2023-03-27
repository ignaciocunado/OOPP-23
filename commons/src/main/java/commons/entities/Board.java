package commons.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public final class Board {

    @Id @GeneratedValue
    private int id;

    @NotBlank
    private String key;

    private String password;
    @OneToMany
    private List<Tag> tags;
    @OneToMany
    private List<CardList> listsOnBoard;

    /**
     * Empty constructor for JPA
     */
    public Board() {}

    /**
     * Constructor for a new board
     * @param key to enter the board (view-only if board is secured with password)
     * @param password to enter and edit the board
     */
    public Board(String key, String password) {
        this.key = key;
        this.password = password;
        this.listsOnBoard = new ArrayList<>();
        this.tags = new ArrayList<>();
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
    public List<CardList> getListsOnBoard() {
        return listsOnBoard;
    }

    /** Gets the tags that are on this board
     * @return the list of tags as an ArrayList
     */
    public List<Tag> getTagsOnBoard() {
        return tags;
    }
    /**
     * Adds a new CardList on this board
     * @param newList the new CardList to be added
     * @return boolean for whether the CardList has been added successfully
     */
    public boolean addList(CardList newList) {
        return listsOnBoard.add(newList);
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
        return listsOnBoard.remove(listToDelete);
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
        return this.listsOnBoard.removeIf(cardList -> cardList.getId() == id);
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
            Objects.equals(password, board.password) &&
            Objects.equals(listsOnBoard, board.listsOnBoard);
    }

    /**
     * Hashcode
     * @return hashcode of the Board
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, key, password, listsOnBoard);
    }

    /**
     * To string method for the board
     * @return a string representing the board class
     */
    @Override
    public String toString() {
        return String.format("<Board id=%d key=%s password=%s>", this.id, this.key, this.password);
    }
}
