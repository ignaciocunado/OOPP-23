package client.scenes;

import client.utils.ServerUtils;
import commons.entities.Board;
import commons.entities.Tag;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javax.inject.Inject;
import java.io.IOException;
public class TagOverviewCtrl{

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
    @FXML
    public Pane mainPane;
    @FXML
    private AnchorPane window;
    @FXML
    private Pane inputPane;
    @FXML
    private Button createTagButton;
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
     * method to access the color black
     */
    public void colorBlack() {
        this.colorPicker.setValue(Color.rgb(26,26,26));
        circle.setFill(colorPicker.getValue());
    }

    /**
     * method to access the color pink
     */
    public void colorPink() {
        this.colorPicker.setValue(Color.rgb(255,192,203));
        circle.setFill(colorPicker.getValue());
    }

    /**
     * method to access the color dark blue
     */
    public void colorDarkBlue() {
        this.colorPicker.setValue(Color.rgb(25,51,153));
        circle.setFill(colorPicker.getValue());
    }

    /**
     * method to access the color sky blue
     */
    public void colorSkyBlue() {
        this.colorPicker.setValue(Color.rgb(204,255,255));
        circle.setFill(colorPicker.getValue());
    }

    /**
     * method to access the light green
     */
    public void colorLightGreen() {
        this.colorPicker.setValue(Color.rgb(187,255,148));
        circle.setFill(colorPicker.getValue());
    }

    /**
     * method to access the color bright green
     */
    public void colorBrightGreen() {
        this.colorPicker.setValue(Color.rgb(33,166,33));
        circle.setFill(colorPicker.getValue());
    }

    /**
     * method to access the color red
     */
    public void colorRed() {
        this.colorPicker.setValue(Color.rgb(255,0,0));
        circle.setFill(colorPicker.getValue());
    }

    /**
     * method to access the color yellow
     */
    public void colorYellow() {
        this.colorPicker.setValue(Color.rgb(254,255,25));
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
        colorPicker.setOnAction(event -> this.circle.setFill(colorPicker.getValue()));
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
        mainPane.requestFocus();
        setRightColours();
    }

    /**
     * Sets the right colours
     */
    private void setRightColours() {
        if(board.getColour().equals("rgb(1,35,69)")) {
            window.setStyle("-fx-background-color:rgb(35,69,103)");
        }
        else {
            window.setStyle("-fx-background-color: " + board.getColour());
        }
        inputPane.setStyle("-fx-background-color: " + getRGBShade() + ";" +
                " -fx-background-radius: 5");
        createTagButton.setStyle("-fx-background-color: " + board.getColour());
    }

    /**
     * Gets a shade of RGB to add to panes and buttons
     * @return string representing RGB colour
     */
    public String getRGBShade() {
        Color color = Color.web(board.getColour());
        double newRed = color.getRed()*0.75*255;
        double newGreen = color.getGreen()*0.75*255;
        double newBlue = color.getBlue()*0.75*255;
        return "rgb(" + newRed + "," + newGreen + "," + newBlue + ")";
    }
}
