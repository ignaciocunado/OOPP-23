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
    @Inject
    public TagOverviewCtrl(ServerUtils server, MainCtrl mainCtrl, VBox vbox){
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.vbox = vbox;
    }

    public void cancel(){
        mainCtrl.showNewBoardOverview();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colorPicker.setOnAction(event -> this.circle.setFill(colorPicker.getValue()));
    }

    public int colourToInt(){
        Color newColour = colorPicker.getValue();
        double red = newColour.getRed();
        double green = newColour.getGreen();
        double blue = newColour.getBlue();
        int colour = ((int)red << 16) | ((int)green << 8) | (int)blue;
        return colour;
    }

    public void createTag() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane tagPane = loader.load(getClass().getResource("TagCopy.fxml").openStream());
        TagOverviewCtrl ctrl = loader.getController();
        Tag newTag = new Tag(newTitle.getText(), colourToInt());
        tagPane.setId(Integer.toString(newTag.getId()));
        ctrl.update(newTag.getId(), this);
        vbox.getChildren().add(tagPane);

    }

    private void update(int id, TagOverviewCtrl tagOverviewCtrl) {
        this.id = id;
        this.tagOverviewCtrl = tagOverviewCtrl;
    }

    private URL getLocation(String... parts) {
        var path = Path.of("", parts).toString();
        return MyFXML.class.getClassLoader().getResource(path);
    }

}
