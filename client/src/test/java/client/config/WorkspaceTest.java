package client.config;

import commons.entities.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class WorkspaceTest {

    private Workspace workspace;

    @BeforeEach
    void setUp() {
        this.workspace = new Workspace("connection");
    }

    @Test
    void getConnectionUriTest() {
        assertEquals("connection", workspace.getConnectionUri());
    }

    @Test
    void getBoardsTest() {
        assertEquals(0, workspace.getBoards().size());
        assertEquals(new ArrayList<>(), workspace.getBoards());
        workspace.addBoard("key");
        assertEquals(1, workspace.getBoards().size());
        assertTrue(workspace.getBoards() instanceof ArrayList);
        assertTrue(workspace.getBoards().get(0) instanceof RecentBoard);
    }

    @Test
    void addBoardTest() {
        workspace.addBoard("key");
        assertEquals(1, workspace.getBoards().size());
        workspace.addBoard("key");
        assertEquals(1, workspace.getBoards().size());
    }

    @Test
    void deleteBoardTest() {
        workspace.addBoard("key");
        workspace.addBoard("secondKey");
        workspace.deleteBoard("key");
        assertEquals(1, workspace.getBoards().size());
        assertEquals("secondKey", workspace.getBoards().get(0).getKey());
        assertFalse(workspace.getBoards().contains(new RecentBoard("key")));
    }

    @Test
    void equalsTest1() {
        Workspace workspace1 = new Workspace("connection");
        assertEquals(workspace1, workspace);
    }

    @Test
    void equalsTest2() {
        Workspace workspace1 = new Workspace("dddd");
        assertNotEquals(workspace1, workspace);
    }

    @Test
    void hashCodeTest() {
        Workspace workspace1 = new Workspace("connection");
        assertEquals(workspace1.hashCode(), workspace.hashCode());
    }
}