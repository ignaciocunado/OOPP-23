package client.scenes;

import client.MyFXML;
import commons.entities.Card;
import commons.entities.Tag;
import javafx.fxml.FXML;
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
    private Text name;


    /**
     * Removes a tag from a card
     */
    public void removeTag() {

    }


    public void editData(Tag tag) {
        this.name.setText(tag.getName());
        this.background.setStyle("-fx-background-color:#" + Integer.toString(tag.getColour()));
    }
}
