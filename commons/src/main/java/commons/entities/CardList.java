package commons.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Entity
public final class CardList {

    @Id
    @GeneratedValue
    private int id;

    @NotBlank
    private String title;
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Card> cards;
    @NotNull
    private String colour;
    /**
     * Empty constructor for JPA
     */
    public CardList() {}

    /**
     * Constructor for a new card list
     * @param title title of a list
     */
    public CardList(String title) {
        this.title = title;
        cards = new ArrayList<>();
        this.colour = "";
    }

    /**
     * Constructor for a new card list
     * @param title title of a list
     * @param colour colour
     */
    public CardList(String title, String colour) {
        this.title = title;
        cards = new ArrayList<>();
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
     * Getter for the id of a list
     * @return an id of the list
     */
    public int getId() {
        return id;
    }

    /** setter of id
     * @param id int unique value representing the id of a card list
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter for the title of a list
     * @return a title of a list
     */

    public String getTitle() {
        return title;
    }

    /**
     * Getter for a list of cards
     * @return list of cards
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Setter for the title of a list
     * @param title a new title of a list
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Adds a card to the list of cards
     * @param card a card that is going to be added
     * @return true, after the card was added
     */
    public boolean addCard(Card card) {
        return cards.add(card);
    }

    /**
     * Removes a card from the list of cards
     * @param card card that needs to be removed
     * @return true if removed correctly, false otherwise
     * TO DO - removeCard, id as an argument???
     */
    public boolean removeCard(Card card) {
        return cards.remove(card);
    }

    /**
     * Method for deleting a card by its id
     * @param id int variable representing the id of the card
     * @return boolean value representing whether the card has been removed or not
     */
    public boolean removeCardById(final int id){
        return this.cards.removeIf(card -> card.getId() == id);
    }

    /**
     * Creates a human-readable string representation of list
     * @return human-readable string of list
     */
    @Override
    public String toString() {
        return "List: title - " + getTitle() + ", id - " + getId() + ".";
    }

    /**
     * Checks if the o Object is equal to the list
     * @param o Object that is being compared
     * @return true if o is equal to the list, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardList list = (CardList) o;
        return id == list.id && Objects.equals(cards, list.cards)
                && Objects.equals(title, list.title);
    }

    /**
     * Creates a unique integer for this list
     * @return hash as an integer
     */
    @Override
    public int hashCode() {
        return Objects.hash(cards, title, id);
    }
}
