package commons;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

public class Tag {

    @Id @GeneratedValue
    private int id;

    private String name;
    private int colour;

    public Tag() {}

    public Tag(final String name, final int colour) {
        this.name = name;
        this.colour = colour;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getColour() {
        return colour;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColour(int colour) {
        this.colour = colour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;
        Tag tag = (Tag) o;
        return getId() == tag.getId() && getColour() == tag.getColour() && Objects.equals(getName(), tag.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getColour());
    }

}
