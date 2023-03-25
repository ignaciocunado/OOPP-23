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

    }


}
