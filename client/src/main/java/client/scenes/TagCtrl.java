package client.scenes;

import client.MyFXML;
import commons.entities.Card;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

public class TagCtrl {

    @FXML
    private Pane background;

    @FXML
    private Text title;

    /**
     * Constructor
     */
    public TagCtrl() {

    }

    /**
     * Adds a tag to a card
     * @param currentCard Card to add the tag to
     * @param box Box in which to add the tag
     * @throws IOException
     */
    public void addTag(Card currentCard, HBox box) throws IOException {
        Pane taskPane = FXMLLoader.load(getLocation("client", "scenes", "Tag.fxml"));
        box.getChildren().add(taskPane);
    }

    /**
     * Removes a tag from a card
     */
    public void removeTag() {

    }


    /**
     * Gets the location of a resource with the given String elements
     * @param parts Strings of where to find the resource
     * @return the URL of the requested resource
     */
    private URL getLocation(String... parts) {
        var path = Path.of("", parts).toString();
        return MyFXML.class.getClassLoader().getResource(path);
    }

}
