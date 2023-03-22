package client.scenes;

import commons.entities.Card;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CardEditorControl {

    @FXML
    private HBox tags;
    @FXML
    private VBox nestedTaskList;
    private Card currentCard;
}
