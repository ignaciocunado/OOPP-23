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
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;
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
        hbox.setSpacing(10);
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

    public void addList() {
        try {
            loader = new FXMLLoader(getClass().getResource("ListTemplate.fxml"));
            Pane newList = (Pane)loader.load();
            hbox.getChildren().add(newList);
            //newList.setId(String.valueOf(hbox.getChildren().indexOf(newList)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void removeList() {
        Pane toBeRemoved = (Pane) hbox.getChildren().get(Integer.parseInt(hoverId));
        hbox.getChildren().remove(toBeRemoved);
    }
    public void getParentId(Event event){
        Button btn = null;
        if (event.getSource().getClass() == btn.getClass()) {
            btn = (Button) event.getSource();
            Pane parent = (Pane) btn.getParent();
            hoverId = parent.getId();
        }
    }

}