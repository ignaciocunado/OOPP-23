/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.util.ResourceBundle;

public class BoardOverviewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;


//    private ObservableList<Board> data;
    @FXML
    private Pane root;

    @FXML
    private HBox hbox;
    @FXML
    private FXMLLoader loader;

//    @FXML
//    private ArrayList<Pane> lists;

    private String hoverId;
    /**
     * Constructor to inject necessary classes into the controller
     *
     * @param server
     * @param mainCtrl
     */
    @Inject
    public BoardOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initialisation method initialising FXML objects
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hbox.setSpacing(20);
        refresh();
    }

    /**
     * Stub method for refreshing boards
     */
    public void refresh() {}

    /**
     * Method to close the app
     */
    public void closeApp() {
        mainCtrl.closeApp();
    }

    /**
     * Method to minimize the app
     */
    public void minimizeApp() {
        mainCtrl.minimizeWindow();
    }

    /**
     * Adds a List to the board
     */
    public void addList() {
        Pane newList = new Pane();
        newList.setLayoutX(25);
        newList.setLayoutY(51);
        newList.setPrefHeight(500);
        newList.setPrefWidth(250);
        newList.setMinHeight(500);
        newList.setMinWidth(250);
        newList.setStyle("-fx-background-color: rgb(35,69,103); -fx-border-radius: 15; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.7), 10, 0, 0, 0);");

        Button addButton = new Button();
        addButton.setContentDisplay(ContentDisplay.CENTER);
        addButton.setGraphicTextGap(10);
        addButton.setLayoutX(25);
        addButton.setLayoutY(65);
        addButton.setMnemonicParsing(false);
        addButton.setPrefHeight(22);
        addButton.setPrefWidth(200);
        addButton.setMaxWidth(200);
        addButton.setStyle("-fx-background-color: #123456; -fx-padding: 0px;");
        addButton.setText("+");
        addButton.setTextAlignment(TextAlignment.CENTER);
        addButton.setTextFill(Color.WHITE);
        Font f = new Font(25);
        addButton.setFont(f);
        newList.getChildren().add(addButton);

        Text txt = new Text();
        txt.setLayoutX(20);
        txt.setLayoutY(33);
        txt.setStrokeWidth(0);
        txt.setText("Title");
        txt.setWrappingWidth(250);
        txt.setStrokeType(StrokeType.OUTSIDE);
        txt.setStyle("-fx-font-size: 30;");
        txt.setFill(Color.rgb(208,87,87));
        newList.getChildren().add(txt);

        Button deleteButton = new Button();
        deleteButton.setContentDisplay(ContentDisplay.CENTER);
        deleteButton.setAlignment(Pos.CENTER);
        deleteButton.setLayoutX(215);
        deleteButton.setLayoutY(-5);
        deleteButton.setMnemonicParsing(false);
        deleteButton.setPrefHeight(38);
        deleteButton.setPrefWidth(31);
        deleteButton.setMaxWidth(31);
        deleteButton.setStyle("-fx-background-color: rgb(35,69,103); -fx-label-padding: 0px; -fx-padding: 0px; -fx-border-radius: 15; -fx-background-radius: 15;");
        deleteButton.setText("-");
        deleteButton.setTextFill(Color.WHITE);
        Font fDelete = new Font(38);
        deleteButton.setFont(fDelete);
        newList.getChildren().add(deleteButton);

        newList.setId(String.valueOf(hbox.getChildren().indexOf(newList)));
        hbox.getChildren().add(newList);

        /*try {
            loader = new FXMLLoader(getClass().getResource("ListTemplate.fxml"));
            Pane newList = (Pane)loader.load();
            hbox.getChildren().add(newList);
            //newList.setId(String.valueOf(hbox.getChildren().indexOf(newList)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }

    /**
     * Removes a List from the board
     */
    public void removeList() {
        Pane toBeRemoved = (Pane) hbox.getChildren().get(Integer.parseInt(hoverId));
        hbox.getChildren().remove(toBeRemoved);
    }

    /**
     * Gets the id of the parent of the element that generated the event
     * @param event caused by user
     */
    public void getParentId(Event event){
        Button btn = null;
        if (event.getSource().getClass() == btn.getClass()) {
            btn = (Button) event.getSource();
            Pane parent = (Pane) btn.getParent();
            hoverId = parent.getId();
        }
    }

}