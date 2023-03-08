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
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class LandingOverviewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    /**
     * Constructor to inject necessary classes into the controller
     * @param server
     * @param mainCtrl
     */
    @Inject
    public LandingOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
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
        refresh();
    }

    /**
     * Swaps the current view to the boards
     * and loads it with the board's information
     */
    public void joinBoard() {
        this.mainCtrl.showBoardOverview();
    }

    /**
     * Swaps the current view to the boards
     * creates that board, and loads that
     * board's information
     */
    public void createBoard() {
        this.mainCtrl.showBoardOverview();
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
}