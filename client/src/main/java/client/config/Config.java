package client;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public final class Config {

    private List<RecentBoard> recentBoards = new ArrayList<>();
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
            this.recentBoards = mapper.readValue(this.configFile,
                typeFactory.constructCollectionType(List.class, RecentBoard.class));
        } catch (IOException e) {
            e.printStackTrace();
            this.recentBoards = new ArrayList<>();
        }
    }

    /**
     * Adds a RecentBoard to the recentBoards list
     * @param key of the Board
     * @param server of the Board
     */
    public void addBoard(String key, String server) {
        for (RecentBoard recentBoard:this.recentBoards) {
            if (recentBoard.getKey().equals(key) && recentBoard.getServer().equals(server)) {
                return;
            }
        }
        this.recentBoards.add(new RecentBoard(key, server));
    }

    /**
     * Gets the RecentBoards in the recentBoards list
     * @return the RecentBoards
     */
    public List<RecentBoard> getBoards() {
        return this.recentBoards;
    }

    /**
     * Saves the RecentBoards present in the recentBoards list into the config file
     * @throws IOException if file is not found
     */
    public void saveBoard() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(this.configFile, this.recentBoards);
    }
}
