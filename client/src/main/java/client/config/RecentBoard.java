package client.config;

public final class RecentBoard {
    private String key;
    private String server;


    /**
     * RecentBoard constructor
     * @param key of the RecentBoard
     * @param server of the RecentBoard
     */
    public RecentBoard(final String key, final String server) {
        this.key = key;
        this.server = server;
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
