package client.config;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.inject.Singleton;

import java.io.*;
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
     * Loads the configuration
     */
    public void loadConfiguration() {
        try {
            this.loadConfigurationFromReader(new FileReader(this.configFile));
        } catch (FileNotFoundException e) {
            // If file not found use empty config
        }
    }

    /**
     * Loads the RecentBoard objects into the recentBoards list
     * @param reader reader
     */
    public void loadConfigurationFromReader(final Reader reader) {
        final ObjectMapper mapper = new ObjectMapper();
        final TypeFactory typeFactory = mapper.getTypeFactory();

        try {
            this.workspaces = mapper.readValue(reader,
                typeFactory.constructCollectionType(List.class, Workspace.class));
        } catch (IOException e) {
            e.printStackTrace();
            this.workspaces = new ArrayList<>();
        }
    }

    /**
     * Sets the current workspace to the one matching the connection uri
     * or creates a new one with the appropriate uri
     * @param connectionUri the uri
     * @return the newly created or the existing workspace
     */
    public Workspace setCurrentWorkspace(final String connectionUri) {
        final Optional<Workspace> workspaceOpt =
                this.workspaces.stream().filter(workspace ->
                        workspace.getConnectionUri().equals(connectionUri)).findAny();
        if (workspaceOpt.isEmpty()) {
            final Workspace workspace = new Workspace(connectionUri);
            this.workspaces.add(workspace);
            this.currentWorkspace = workspace;
            return workspace;
        }

        this.currentWorkspace = workspaceOpt.get();
        return this.currentWorkspace;
    }

    /**
     * Gets the current workspace
     * @return the workspace or null
     */
    public Workspace getCurrentWorkspace() {
        return this.currentWorkspace;
    }

    /**
     * Gets all existing workspaces
     * @return a list of workspaces
     */
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
