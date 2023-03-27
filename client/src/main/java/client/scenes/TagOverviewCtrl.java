package client.scenes;

import client.MyFXML;
import client.utils.ServerUtils;
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
import java.nio.file.Path;
import java.util.ResourceBundle;

public class TagOverviewCtrl implements Initializable{

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private Pane newPane;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private Circle circle;
    @FXML
    private TextField newTitle;
    @FXML
    private VBox vbox;
    private int id;
    private TagOverviewCtrl tagOverviewCtrl;

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

    /**
     * cancel method so you can go back to the board by accessing a button
     */
    public void cancel(){
        mainCtrl.showNewBoardOverview();
    }

    /** method used to show the color picked in the color picker by filling a circle
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
        Color newColour = colorPicker.getValue();
        double red = newColour.getRed();
        double green = newColour.getGreen();
        double blue = newColour.getBlue();
        int colour = ((int)red << 16) | ((int)green << 8) | (int)blue;
        return colour;
    }

    /** method to create a tag and add it to the list of tags (vbox)
     * @throws IOException
     */
    public void createTag() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane tagPane = loader.load(getClass().getResource("TagCopy.fxml").openStream());
        TagOverviewCtrl ctrl = loader.getController();
        Tag newTag = new Tag(newTitle.getText(), colourToInt());
        tagPane.setId(Integer.toString(newTag.getId()));
        ctrl.update(newTag.getId(), this);
        vbox.getChildren().add(tagPane);

    }

    /**
     * Update methods
     * @param id id of the rendered object
     * @param tagOverviewCtrl ctrl
     */

    private void update(int id, TagOverviewCtrl tagOverviewCtrl) {
        this.id = id;
        this.tagOverviewCtrl = tagOverviewCtrl;
    }

    /**
     * Gets the location of a resource with the given String elements
     * @param parts Strings of where to find the resource
     * @return the URL of the requested resource
     */
    private URL getLocation(String... parts) {
        var path = Path.of("", parts).toString();
        return MyFXML.class.getClassLoader().getResource(path);
    }

}
