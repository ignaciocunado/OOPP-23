package client.scenes;

import client.MyFXML;
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
    private VBox vbox;
    private Board board;
    @FXML
    Pane tagEditor;

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

    public int colourToInt2(Color color){
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
        Pane tagPane = loader.load(getClass().getResource("Tag.fxml").openStream());
        TagCtrl ctrl = loader.getController();
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
     * Refreshes this scene with the right info and renders the tags
     * @param boardOverviewCtrl ctrl
     */
    public void refresh(BoardOverviewCtrl boardOverviewCtrl){
        this.board = boardOverviewCtrl.getBoard();
        try{
            for(Tag tag : board.getTags())  {
                FXMLLoader loader = new FXMLLoader();
                Pane tagPane = loader.load(getClass().getResource("Tag.fxml").openStream());
                TagCtrl ctrl = loader.getController();
                tagPane.setId(Integer.toString(tag.getId()));
                ctrl.update(tag.getId(), this, tag.getName(), tag.getColour(), tag);
                vbox.getChildren().add(tagPane);
            }
        }
        catch (IOException e) {
        }
    }

    /**
     * fefef
     * @param tag fefef
     */
    public void updateEditorPane(Tag tag) {
        tagEditor.setId(Integer.toString(tag.getId()));
        newTitle.setText(tag.getName());
        Color tagColour = Color.web(Integer.toHexString(tag.getColour()));
        circle.setFill(tagColour);
    }

    /**
     * feefef
     */
    public void editTag() {
        server.editTag(Integer.parseInt(this.tagEditor.getId()), newTitle.getText(), colourToInt2((Color) circle.getFill()));
        //tagEditor.setId(Integer.toString(-1));
    }
}
