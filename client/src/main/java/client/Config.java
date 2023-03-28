package client;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.sun.javafx.binding.ContentBinding.bind;

@Singleton
public final class Config {

    private List<RecentBoard> recentBoards = new ArrayList<>();
    private final File configFile;
    public Config() {
        this.configFile = new File("./config.json");
    }

    public void loadConfiguration() {
        final ObjectMapper mapper = new ObjectMapper();
        final TypeFactory typeFactory = mapper.getTypeFactory();

        try {
            this.recentBoards = mapper.readValue(this.configFile, typeFactory.constructCollectionType(List.class, RecentBoard.class));
        } catch (IOException e) {
            e.printStackTrace();
            this.recentBoards = new ArrayList<>();
        }
    }

    public void addBoard(String key, String server) {
        for (RecentBoard recentBoard:this.recentBoards) {
            if (recentBoard.getKey().equals(key) && recentBoard.getServer().equals(server)) {
                return;
            }
        }
        this.recentBoards.add(new RecentBoard(key, server));
    }

    public List<RecentBoard> getBoards() {
        return this.recentBoards;
    }

    public void saveBoard() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(this.configFile, this.recentBoards);
    }
}
