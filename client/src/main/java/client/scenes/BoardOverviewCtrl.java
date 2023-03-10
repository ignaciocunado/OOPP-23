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

import client.MyFXML;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class BoardOverviewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private HBox hbox;

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
        hbox.setPadding(new Insets(20,20,20,0));
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
     *
     * @return
     */
    public void addList() throws IOException {
        Pane listPane = FXMLLoader.load(getLocation("client", "scenes", "ListTemplate.fxml"));
        hbox.getChildren().add(listPane);
        listPane.setId(String.valueOf(hbox.getChildren().indexOf(listPane)));
        for (int i = 0; i < listPane.getChildren().size(); i++) {
            if (listPane.getChildren().get(i).getClass() == Pane.class) {
                Pane current = (Pane) listPane.getChildren().get(i);
                current.getChildren().get(0).setOnMouseClicked(event-> {
                    try {
                        addList();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            if (listPane.getChildren().get(i).getClass() == Button.class) {
                listPane.getChildren().get(i).setOnMouseClicked(event-> {
                    removeList(listPane.getId());
                });
            }
            if (listPane.getChildren().get(i).getClass() == Text.class) {
                Text title = (Text) listPane.getChildren().get(i);
                title.setText("Title"+listPane.getId());
            }
        }
    }

    private URL getLocation(String... parts) {
        var path = Path.of("", parts).toString();
        return MyFXML.class.getClassLoader().getResource(path);
    }


    /**
     * Removes an existing List from the Board
     * @param paneId the ID of the List which needs to be deleted
     */
    public void removeList(String paneId) {
        Pane toBeRemoved = (Pane) hbox.getChildren().get(Integer.parseInt(paneId));
        hbox.getChildren().remove(toBeRemoved);
    }

}