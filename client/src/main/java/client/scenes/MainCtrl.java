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

import commons.entities.Board;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;
    private Stage boardSettingsStage;

    private LandingOverviewCtrl landingOverviewCtrl;
    private Scene landingOverview;

    private BoardOverviewCtrl boardOverviewCtrl;
    private Scene boardOverview;

    private BoardSettingsCtrl boardSettingsCtrl;
    private Scene boardSettings;

    /**
     * Initialize main controller with all FXML controllers
     * @param primaryStage main stage for FXML views
     * @param landingOverview the landing overview
     * @param boardOverview the main board overview
     */
    public void initialize(Stage primaryStage, Pair<LandingOverviewCtrl, Parent> landingOverview,
            Pair<BoardOverviewCtrl, Parent> boardOverview, Pair<BoardSettingsCtrl, Parent> boardSettings) {
        this.primaryStage = primaryStage;
        this.boardSettingsStage = new Stage();

        this.landingOverviewCtrl = landingOverview.getKey();
        this.landingOverview = new Scene(landingOverview.getValue());

        this.boardOverviewCtrl = boardOverview.getKey();
        this.boardOverview = new Scene(boardOverview.getValue());

        this.boardSettingsCtrl = boardSettings.getKey();
        this.boardSettings = new Scene(boardSettings.getValue());

        this.boardSettingsStage.setScene(this.boardSettings);
        this.boardSettingsStage.initModality(Modality.APPLICATION_MODAL);
        this.boardSettingsStage.initOwner(this.primaryStage);

        primaryStage.initStyle(StageStyle.DECORATED);
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
     * Shows the a new board overview scene
     */
    public void showNewBoardOverview() {
        primaryStage.setTitle("Talio: Task List Organiser");
        primaryStage.setScene(this.boardOverview);
        boardOverviewCtrl.refresh(new Board("",""));
    }

    /**
     * Shows an existing board overview scene
     */
    public void showExistingBoardOverview() {
        primaryStage.setTitle("Talio: Task List Organiser");
        primaryStage.setScene(this.boardOverview);
        boardOverviewCtrl.refresh(new Board("",""));
    }

    /**
     * Shows an existing board overview scene
     */
    public void showBoardSettings() {
        this.boardSettingsStage.setTitle("Talio: Task List Organiser (Settings)");
        this.boardSettingsStage.showAndWait();
    }

    /**
     * Method to close the app
     */
    public void closeApp() {
        System.exit(0);
    }

    /**
     * Method to minimize the current window
     */
    public void minimizeWindow() {
        primaryStage.setIconified(true);
    }

}