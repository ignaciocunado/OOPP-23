package client.scenes;

import commons.entities.Tag;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class TagInBoardCtrl {

    @FXML
    private Pane background;

    /**
     * Updates the rendered object with the properties of the tag
     * @param tag tag which it represents
     */
    public void update(Tag tag) {
        background.setStyle("-fx-background-color:#" + Integer.toHexString(tag.getColour()) +
                ";-fx-background-radius: 10");
    }
}
