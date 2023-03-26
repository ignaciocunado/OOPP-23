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
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

import java.lang.ref.PhantomReference;

public class MainCtrl {

    private Stage primaryStage;


    private LandingOverviewCtrl landingOverviewCtrl;
    private Scene landingOverview;

    private BoardOverviewCtrl boardOverviewCtrl;
    private Scene boardOverview;

    private TagOverviewCtrl tagOverviewCtrl;

    private Scene tagOverview;

    /**
     * Initialize main controller with all FXML controllers
     *
     * @param primaryStage    main stage for FXML views
     * @param landingOverview the landing overview
     * @param boardOverview   the main board overview
     * @param tagOverview the tag overview
     */
    public void initialize(Stage primaryStage, Pair<LandingOverviewCtrl, Parent> landingOverview,
                           Pair<BoardOverviewCtrl, Parent> boardOverview,
                           Pair<TagOverviewCtrl, Parent> tagOverview) {
        this.primaryStage = primaryStage;

        this.landingOverviewCtrl = landingOverview.getKey();
        this.landingOverview = new Scene(landingOverview.getValue());

        this.boardOverviewCtrl = boardOverview.getKey();
        this.boardOverview = new Scene(boardOverview.getValue());

        this.tagOverviewCtrl = tagOverview.getKey();
        this.tagOverview = new Scene(tagOverview.getValue());

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
     * Method for the tag overview
     */
    public void showTagOverview(){
        primaryStage.setTitle("Talio: Tag list Overview");
        primaryStage.setScene(this.tagOverview);
//        proportionsTagOverview();
    }

    /**
     * proprtions of the new window
     */
//    public void proportionsTagOverview(){
//        primaryStage.setMaxHeight(400);
//        primaryStage.setMaxWidth(780);
//        primaryStage.setMinHeight(400);
//        primaryStage.setMinWidth(780);
//    }

    /**
     * Shows an existing board overview scene
     */
    public void showExistingBoardOverview() {
        primaryStage.setTitle("Talio: Task List Organiser");
        primaryStage.setScene(this.boardOverview);
        boardOverviewCtrl.refresh(new Board("",""));
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