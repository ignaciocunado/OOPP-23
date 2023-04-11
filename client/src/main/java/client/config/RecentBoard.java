package client.config;

import java.util.Objects;

public final class RecentBoard {
    private String key;


    /**
     * RecentBoard constructor
     * @param key of the RecentBoard
     */
    public RecentBoard(final String key) {
        this.key = key;
    }

    /**
     * Empty constructor for JPA
     */
    public RecentBoard() {}

    /**
     * Gets the key of the RecentBoard
     * @return the key of the RecentBoard
     */
    public String getKey() {
        return key;
    }

    /**
     * Equals method
     * @param o other object to compare this to
     * @return true iff both objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecentBoard that = (RecentBoard) o;
        return key.equals(that.key);
    }

    /**
     * Hashcode method, nobody ever uses it but its here
     * @return idk a number lol
     */
    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
