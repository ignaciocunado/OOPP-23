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

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;

    private LandingOverviewCtrl landingOverviewCtrl;
    private Scene landingOverview;

    private BoardOverviewCtrl boardOverviewCtrl;
    private Scene boardOverview;

    /**
     * Initialize main controller with all FXML controllers
     * @param primaryStage main stage for FXML views
     * @param landingOverview the landing overview
     * @param boardOverview the main board overview
     */
    public void initialize(Stage primaryStage, Pair<LandingOverviewCtrl, Parent> landingOverview,
            Pair<BoardOverviewCtrl, Parent> boardOverview) {
        this.primaryStage = primaryStage;

        this.landingOverviewCtrl = landingOverview.getKey();
        this.landingOverview = new Scene(landingOverview.getValue());

        this.boardOverviewCtrl = boardOverview.getKey();
        this.boardOverview = new Scene(boardOverview.getValue());

        showLandingOverview();
        primaryStage.show();
    }

    /**
     * Shows the main landing overview scene
     */
    public void showLandingOverview() {
        primaryStage.setTitle("Talio: Task List Organiser");
        primaryStage.setScene(this.landingOverview);
        landingOverviewCtrl.refresh();
    }

    /**
     * Shows the main board overview scene
     */
    public void showBoardOverview() {
        primaryStage.setTitle("Talio: Task List Organiser");
        primaryStage.setScene(this.boardOverview);
        boardOverviewCtrl.refresh();
    }

    public void listSetTitle() {

    }

    public void addList() {

    }

    public void deleteList() {

    }
}