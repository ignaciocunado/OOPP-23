package client.scenes;

import client.MyFXML;
import commons.entities.Card;
import commons.entities.Tag;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
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
    private Text tag;
    private Card currentCard;

    /**
     * Constructor
     */
    public TagCtrl() {

    }



    /**
     * Removes a tag from a card
     */
    public void removeTag() {
        tag.setText("ddddd");
    }

    public void renderTag(Card currentCard, HBox tags) throws IOException {
        for(Tag tag : currentCard.getTags()) {
            Pane tagPane = FXMLLoader.load(getLocation("client", "scenes", "Tag.fxml"));
            tagPane.setId(Integer.toString(tag.getId()));
            Text title = (Text) tagPane.getChildren().get(0);
            title.setText(tag.getName());
            tags.getChildren().add(tagPane);
        }
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
