package client.scenes;

import commons.entities.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
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
    private TagOverviewCtrl tagOverviewCtrl;
    private Tag tag;


    /**
     * Removes a tag from a card
     */
    public void removeTag() {
        if(cardEditorCtrl != null )cardEditorCtrl.removeTag(id);
        else {
            tagOverviewCtrl.removeTag(id);
        }
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

    /** update methods for tagoverviewctrl
     * @param id id of the object
     * @param tagOverviewCtrl ctrl of tagoverview
     * @param name name of the tag
     * @param color color of the tag
     */
    public void update(int id,TagOverviewCtrl tagOverviewCtrl, String name, int color, Tag tag){
        this.id = id;
        System.out.println(id);
        this.tagOverviewCtrl = tagOverviewCtrl;
        this.name.setText(name);
        this.background.setStyle("-fx-background-color:#" + Integer.toHexString(color));
        this.tag = tag;
    }

    public void updateInfo() {
        tagOverviewCtrl.updateEditorPane(tag);
    }

}
