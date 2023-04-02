package client.scenes;

import client.utils.ServerUtils;
import commons.entities.Board;
import commons.entities.Tag;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TagOverviewCtrl implements Initializable{

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private Circle circle;
    @FXML
    private TextField newTitle;
    @FXML
    public VBox vbox;
    private Board board;
    private TagEditorCtrl tagEditorCtrl;

    /** Constructor to inject necessary classes into the controller
     * @param server serverUtils
     * @param mainCtrl mainCtrl of the server
     * @param vbox the vbox to display the tags in
     */
    @Inject
    public TagOverviewCtrl(ServerUtils server, MainCtrl mainCtrl, VBox vbox){
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.vbox = vbox;
    }

    /** getter for color
     * @return the color
     */
    public ColorPicker getColorPicker() {
        return colorPicker;
    }

    /** getter for title
     * @return the new title
     */
    public TextField getNewTitle() {
        return newTitle;
    }

    /** ethod used to show the color picked in the color picker by filling a circle
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colorPicker.setOnAction(event -> this.circle.setFill(colorPicker.getValue()));
    }

    /** method to convert from type color to type int
     * @return the colour in int values
     */
    public int colourToInt(){
        Color color = colorPicker.getValue();
        return Integer.decode(String.format( "0x%02X%02X%02X",
            (int)( color.getRed() * 255 ),
            (int)( color.getGreen() * 255 ),
            (int)( color.getBlue() * 255 )));
    }

    /** method for converting from type color to type int, but with a parameter given
     * @param color the color we want to convert to int
     * @return an int value representing the color
     */
    public int colourToIntForColor(Color color){
        return Integer.decode(String.format( "0x%02X%02X%02X",
                (int)( color.getRed() * 255 ),
                (int)( color.getGreen() * 255 ),
                (int)( color.getBlue() * 255 )));
    }

    /** method to create a tag and add it to the list of tags (vbox)
     * @throws IOException
     */
    public void createTag() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane tagPane = loader.load(getClass().getResource("TagCopy.fxml").openStream());
        TagEditorCtrl ctrl = loader.getController();
        String name = newTitle.getText();
        int colour = colourToInt();
        board = server.createTag(board.getId(), name,colour);
        Tag newTag = board.getTags().get(board.getTags().size() - 1);
        tagPane.setId(Integer.toString(newTag.getId()));
        ctrl.update(newTag.getId(), this, newTitle.getText(), colourToInt(), newTag);
        vbox.getChildren().add(tagPane);
    }
    /** method to remove a tag by id
     * @param id id of the tag
     */
    public void removeTag(int id){
        vbox.getChildren().removeIf(pane -> Integer.parseInt(pane.getId()) == id);
        server.deleteTag(board.getId(), id);
    }

    /**
     * method to access the color red
     */
    public void colorRed() {
        this.colorPicker.setValue(Color.rgb(255,0,0));
        circle.setFill(colorPicker.getValue());
    }

    /** method to edit a tag from the server
     * @param tagId int value representing the id of the tag
     * @param title string representing the title of the tag
     * @param colour int color representing the color of the tag
     */
    public void editTag(int tagId, String title, int colour) {
        this.server.editTag(tagId, title,
            colour);
    }

    /**
     * Refreshes this scene with the right info and renders the tags
     * @param boardOverviewCtrl ctrl
     */
    public void refresh(BoardOverviewCtrl boardOverviewCtrl){
        vbox.getChildren().clear();
        this.board = boardOverviewCtrl.getBoard();
        try{
            for(Tag tag : board.getTags())  {
                FXMLLoader loader = new FXMLLoader();
                Pane tagPane = loader.load(getClass().getResource("TagCopy.fxml").openStream());
                TagEditorCtrl ctrl = loader.getController();
                tagPane.setId(Integer.toString(tag.getId()));
                ctrl.editTag(tag);
                ctrl.update(tag.getId(), this, tag.getName(), tag.getColour(), tag);
                vbox.getChildren().add(tagPane);
            }
        }
        catch (IOException e) {
        }
    }
}
