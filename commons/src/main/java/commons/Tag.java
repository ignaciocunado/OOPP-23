package commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;
@Entity
public final class Tag {

    @Id @GeneratedValue
    private int id;
    private String name;
    private int colour;

    /**
     * Empty constructor for JPA
     */
    public Tag() {}

    /**
     * Constructor for a new tag
     * @param name of the tag to display
     * @param colour of the tag, encoded in decimal
     */
    public Tag(final String name, final int colour) {
        this.name = name;
        this.colour = colour;
    }

    /**
     * setter for id
     * @param id new id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets unique id of the tag
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of the tag
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the colour of the tag
     * @return the colour
     */
    public int getColour() {
        return colour;
    }

    /**
     * Sets the name of the tag
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the colour of the tag
     * @param colour the new colour
     */
    public void setColour(int colour) {
        this.colour = colour;
    }

    /**
     * Equals method
     * @param o the object to compare to
     * @return whether it's equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;
        Tag tag = (Tag) o;
        return id == tag.id && colour == tag.colour && Objects.equals(name, tag.name);
    }

    /**
     * Hashcode
     * @return the hashcode of the tag
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, colour);
    }

    /**
     * To string method for the tag
     * @return a string representing the tag class
     */
    @Override
    public String toString() {
        return String.format("<Tag id=%d name=%s colour=%d>", this.id, this.name, this.colour);
    }
}
