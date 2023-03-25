package client.scenes;

import commons.entities.Tag;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;


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
        this.background.setStyle("-fx-background-color:#" + Integer.toHexString(tag.getColour()) +
            ";-fx-background-radius: 10");

    }
}
