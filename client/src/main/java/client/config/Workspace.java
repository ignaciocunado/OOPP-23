package client.config;

import java.util.ArrayList;
import java.util.List;

public final class Workspace {

    private String connectionUri;
    private List<RecentBoard> boards;

    /**
     * Creates a workspace based on a connection uri
     * @param connectionUri the connection uri
     */
    public Workspace(final String connectionUri) {
        this.connectionUri = connectionUri;
        this.boards = new ArrayList<>();
    }
    /**
     * Empty constructor for JPA
     */
    public Workspace() {}


    /**
     * Gets the connection uri of the workspace
     * @return the connection uri
     */
    public String getConnectionUri() {
        return connectionUri;
    }

    /**
     * Gets the RecentBoards in the recentBoards list
     * @return the RecentBoards
     */
    public List<RecentBoard> getBoards() {
        return this.boards;
    }

    /**
     * Adds a RecentBoard to the recentBoards list
     * @param key of the Board
     */
    public void addBoard(String key) {
        for (RecentBoard recentBoard:this.boards) {
            if (recentBoard.getKey().equals(key)) {
                this.boards.removeIf(board -> board.getKey().equals(key));
                this.boards.add(0, new RecentBoard(key));
                return;
            }
        }
        this.boards.add(0, new RecentBoard(key));
    }

    /**
     * Deletes a RecentBoard from the recentBoards list
     * @param key of the Board
     */
    public void deleteBoard(String key) {
        for (RecentBoard recentBoard:this.boards) {
            if (recentBoard.getKey().equals(key)) {
                this.boards.removeIf(board -> board.getKey().equals(key));
                return;
            }
        }
    }

}
