package client.scenes;

import client.utils.ServerUtils;
import commons.entities.Tag;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import jdk.jfr.Event;

import javax.inject.Inject;
import java.net.URL;
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
    private Tag newTag;
    @Inject
    public TagOverviewCtrl(ServerUtils server, MainCtrl mainCtrl){
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void cancel(){
        mainCtrl.showNewBoardOverview();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colorPicker.setOnAction(event -> this.circle.setFill(colorPicker.getValue()));
    }
}
