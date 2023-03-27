package client.scenes;

import commons.entities.Tag;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.util.HashSet;


public class TagCtrl {

    @FXML
    private Pane background;
    @FXML
    private Text name;
    private int id;
    private CardEditorCtrl cardEditorCtrl;
    private VBox tags;
    private HashSet<Integer> ids = new HashSet<>();


    /**
     * Removes a tag from a card
     */
    public void removeTag() {
        cardEditorCtrl.removeTag(id);
    }

    /**
     * Edits the data of the rendered tag
     * @param tag tag that this rendered object represents
     */
    public void editData(Tag tag) {
        this.name.setText(tag.getName());
        this.background.setStyle("-fx-background-color:#" + Integer.toHexString(tag.getColour()) +
            ";-fx-background-radius: 10");

    }

    /**
     * Update methods
     * @param id id of the rendered object
     * @param cardEditorCtrl ctrl
     */
    public void update(int id, CardEditorCtrl cardEditorCtrl){
        this.id = id;
        this.cardEditorCtrl = cardEditorCtrl;
    }

}
