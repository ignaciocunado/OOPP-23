package client.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConfigTest {

    private Config config;
    @BeforeEach
    void setUp() {
        config = new Config();
    }

    @Test
    void loadConfigurationTest() {
        this.config.loadConfigurationFromReader(new StringReader("[{\n" +
                "  \"connectionUri\" : \"http://localhost:8080\",\n" +
                "  \"boards\" : [ {\n" +
                "    \"key\" : \"kAiIwfNqoV\"\n" +
                "  } ]\n" +
                "}]"));

        final Workspace workspace = new Workspace("http://localhost:8080");
        workspace.addBoard("kAiIwfNqoV");
        assertEquals(this.config.getWorkspaces().size(), 1);
        assertEquals(this.config.getWorkspaces().get(0), workspace);
    }

    @Test
    void setCurrentWorkspaceNotExistingTest() {
        Workspace workspace1 =new Workspace("KeyNotExisting");
        assertEquals(workspace1, config.setCurrentWorkspace("KeyNotExisting"));
    }

    @Test
    void setCurrentWorkspaceExistingTest() {
        Workspace workspace1 =new Workspace("KeyNotExisting");
        assertEquals(workspace1, config.setCurrentWorkspace("KeyNotExisting"));
        Workspace workspace2 =new Workspace("KeyNotExisting2");
        assertEquals(workspace2, config.setCurrentWorkspace("KeyNotExisting2"));
        assertEquals(workspace1, config.setCurrentWorkspace("KeyNotExisting"));
        assertEquals(2, config.getWorkspaces().size());
    }

    @Test
    void getCurrentWorkspaceTest() {
        Workspace workspace2 =new Workspace("KeyNotExisting2");
        assertEquals(workspace2, config.setCurrentWorkspace("KeyNotExisting2"));
        assertEquals(workspace2, config.getCurrentWorkspace());
    }

    @Test
    void getCurrentWorkspaceNullTest() {
        assertNull(config.getCurrentWorkspace());
    }

    @Test
    void getWorkspacesTest() {
        Workspace workspace1 =new Workspace("KeyNotExisting");
        config.setCurrentWorkspace("KeyNotExisting");
        Workspace workspace2 =new Workspace("KeyNotExisting2");
        config.setCurrentWorkspace("KeyNotExisting2");
        ArrayList<Workspace> list = new ArrayList<>();
        list.add(workspace1);
        list.add(workspace2);
        assertEquals(list, config.getWorkspaces());
    }
}