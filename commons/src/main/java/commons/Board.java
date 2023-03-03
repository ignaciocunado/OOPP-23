package commons;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Board {
    @Id @GeneratedValue
    private int id;
    private String key;
    private String password;
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
     * Sets the password for the board
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the list of Lists on this board
     * @return the list of Lists as an ArrayList
     */
    public List<CardList> getListsOnBoard() {
        return listsOnBoard;
    }

    /**
     * Adds a new List on this board
     * @param newList the new List to be added
     * @return boolean for whether the List has been added successfully
     */
    public boolean addList(CardList newList) {
        return listsOnBoard.add(newList);
    }

    /**
     * Removes a given List from this board
     * @param listToDelete the List to be deleted
     * @return boolean for whether the List has been deleted successfully
     */
    public boolean removeList(CardList listToDelete) {
        return listsOnBoard.remove(listToDelete);
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
        return id == board.id && key == board.key &&
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
}
