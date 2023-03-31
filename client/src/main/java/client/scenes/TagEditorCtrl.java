package client.scenes;

import commons.entities.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class TagEditorCtrl {

    @FXML
    private Pane background;

    @FXML
    private TextField name;

    private TagOverviewCtrl tagOverviewCtrl;
    private int id;
    private Tag tag;



    @FXML
    void updateInfo(MouseEvent event) {

    }

    /** update methods for tagoverviewctrl
     * @param id id of the object
     * @param tagOverviewCtrl ctrl of tagoverview
     * @param name name of the tag
     * @param color color of the tag
     * @param tag the updated tag
     */
    public void update(int id,TagOverviewCtrl tagOverviewCtrl, String name, int color, Tag tag){
        this.id = id;
        this.tagOverviewCtrl = tagOverviewCtrl;
        this.name.setText(name);
        this.background.setStyle("-fx-background-color:#" + Integer.toHexString(color));
        this.tag = tag;
    }

    /**
     * method for remove tag
     */
    public void removeTag() {
        tagOverviewCtrl.removeTag(this.tag.getId());
    }
}
