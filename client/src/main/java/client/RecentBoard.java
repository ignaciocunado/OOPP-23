package client;

public final class RecentBoard {
    private String key;
    private String server;


    public RecentBoard(final String key, final String server) {
        this.key = key;
        this.server = server;
    }
    public RecentBoard() {}

    public String getKey() {
        return key;
    }

    public String getServer() {
        return server;
    }
}
