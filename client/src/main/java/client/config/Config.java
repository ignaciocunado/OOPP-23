package client.config;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public final class Config {

    private Workspace currentWorkspace;
    private List<Workspace> workspaces = new ArrayList<>();
    private final File configFile;

    /**
     * Connects Config class to the config file where board history is stored
     */
    public Config() {
        this.configFile = new File("./config.json");
    }

    /**
     * Loads the RecentBoard objects into the recentBoards list
     */
    public void loadConfiguration() {
        final ObjectMapper mapper = new ObjectMapper();
        final TypeFactory typeFactory = mapper.getTypeFactory();

        try {
            this.workspaces = mapper.readValue(this.configFile,
                typeFactory.constructCollectionType(List.class, Workspace.class));
        } catch (IOException e) {
            e.printStackTrace();
            this.workspaces = new ArrayList<>();
        }
    }

    public Workspace setCurrentWorkspace(final String connectionUri) {
        final Optional<Workspace> workspaceOpt =
                this.workspaces.stream().filter(workspace -> workspace.getConnectionUri().equals(connectionUri)).findAny();
        if (workspaceOpt.isEmpty()) {
            final Workspace workspace = new Workspace(connectionUri);
            this.workspaces.add(workspace);
            this.currentWorkspace = workspace;
            return workspace;
        }

        this.currentWorkspace = workspaceOpt.get();
        return this.currentWorkspace;
    }

    public Workspace getCurrentWorkspace() {
        return this.currentWorkspace;
    }
    public List<Workspace> getWorkspaces() {
        return this.workspaces;
    }


    /**
     * Saves the RecentBoards present in the recentBoards list into the config file
     */
    public void saveConfig() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(this.configFile, this.workspaces);
    }
}
