package commons;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Objects;

public class List {

    private ArrayList<Card> cards;
    private String title;
    @Id
    @GeneratedValue
    private int id;

    /**
     * Empty constructor for JPA
     */
    public List(){}

    /**
     * Constructor
     * @param title title of a list
     */
    public List(String title) {
        this.title = title;
        cards = new ArrayList<>();
    }

    /**
     * Getter for a list of cards
     * @return list of cards
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * Getter for the title of a list
     * @return a title of a list
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter for the id of a list
     * @return an id of the list
     */
    public int getId() {
        return id;
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
    public boolean addCard(Card card){
        cards.add(card);
        return true;
    }

    /**
     * Removes a card from the list of cards
     * @param card card that needs to be removed
     * @return
     * TO DO - removeCard, id as an argument???
     */
    public boolean removeCard(Card card){
        if (!cards.contains(card)){
            return false;
        }
        cards.remove(card);
        return true;
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
        List list = (List) o;
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
