package client.scenes;

import commons.entities.Tag;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class TagEditorCtrl {

    @FXML
    private Pane background;

    @FXML
    private TextField name;
    private int id;
    private TagOverviewCtrl tagOverviewCtrl;
    private Tag tag;



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
        this.name.focusedProperty().addListener(this::titleEdited);
        this.background.setStyle("-fx-background-color:#" + Integer.toHexString(color));
        this.tag = tag;
    }

    /** method to edit just the title of the tag
     * @param tag the tag we want to edit
     */
    public void editTag(Tag tag){
        this.name.setText(tag.getName());
        this.background.setStyle("-fx-background-color:#" + Integer.toHexString(tag.getColour()));
    }

    /** helper method to edit a tag
     * @param observable o
     */
    private void titleEdited(Observable observable) {
        if (!(observable instanceof ReadOnlyBooleanProperty)) {
            return;
        }
        final ReadOnlyBooleanProperty focused = (ReadOnlyBooleanProperty) observable;
        if (focused.getValue()) {
            return;
        }
        tag.setName(name.getText());
        tagOverviewCtrl.editTag(this.tag.getId(), name.getText(), tagOverviewCtrl.colourToInt());
    }

    /**
     * method for remove tag
     */
    public void removeTag() {
        tagOverviewCtrl.removeTag(this.tag.getId());
    }
}
