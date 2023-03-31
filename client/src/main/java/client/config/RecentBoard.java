package client.config;

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

}
