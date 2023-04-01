package client.scenes;

import commons.entities.Tag;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class TagInBoardCtrl {

    @FXML
    private Pane background;
    private Tag tag;

    public void update(Tag tag) {
        this.tag = tag;
        System.out.println(tag.print());
        background.setStyle("-fx-background-color:#" + Integer.toHexString(tag.getColour()) +
                ";-fx-background-radius: 10");
    }
}
