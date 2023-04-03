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
    private Stage createTagStage;
    private Stage boardSettingsStage;
    private Stage cardEditorStage;
    private Stage boardHistoryStage;

    private BoardHistoryOverviewCtrl boardHistoryOverviewCtrl;
    private Scene boardHistoryOverview;

    private ServerOverviewCtrl serverOverviewCtrl;
    private Scene serverOverview;

    private LandingOverviewCtrl landingOverviewCtrl;
    private Scene landingOverview;

    private BoardOverviewCtrl boardOverviewCtrl;
    private Scene boardOverview;

    private TagOverviewCtrl tagOverviewCtrl;
    private Scene tagOverview;

    private BoardSettingsCtrl boardSettingsCtrl;
    private Scene boardSettings;

    private CardEditorCtrl cardEditorCtrl;
    private Scene cardEditorScene;

    /**
     * Initialize main controller with all FXML controllers
     *
     * @param primaryStage    main stage for FXML views
     * @param landingOverview the landing overview
     * @param boardOverview   the main board overview
     * @param tagOverview the tag overview
     * @param cardEditor stage for the card editor
     * @param boardHistory board history overview
     * @param boardSettings board settings ctrl
     * @param serverOverview server overview
     */
    public void initialize(Stage primaryStage, Pair<LandingOverviewCtrl, Parent> landingOverview,
                           Pair<BoardOverviewCtrl, Parent> boardOverview,
                           Pair<CardEditorCtrl, Parent> cardEditor,
                           Pair<TagOverviewCtrl, Parent> tagOverview,
                           Pair<BoardHistoryOverviewCtrl, Parent> boardHistory,
                           Pair<BoardSettingsCtrl, Parent> boardSettings,
                           Pair<ServerOverviewCtrl, Parent> serverOverview) {

        this.primaryStage = primaryStage;
        this.createTagStage =  new Stage();
        this.boardSettingsStage = new Stage();
        this.cardEditorStage = new Stage();
        this.boardHistoryStage = new Stage();

        this.serverOverviewCtrl = serverOverview.getKey();
        this.serverOverview = new Scene(serverOverview.getValue());

        this.landingOverviewCtrl = landingOverview.getKey();
        this.landingOverview = new Scene(landingOverview.getValue());

        this.boardHistoryOverviewCtrl = boardHistory.getKey();
        this.boardHistoryOverview = new Scene(boardHistory.getValue());

        this.boardOverviewCtrl = boardOverview.getKey();
        this.boardOverview = new Scene(boardOverview.getValue());

        this.tagOverviewCtrl = tagOverview.getKey();
        this.tagOverview = new Scene(tagOverview.getValue());

        this.boardSettingsCtrl = boardSettings.getKey();
        this.boardSettings = new Scene(boardSettings.getValue());

        this.boardSettingsStage.setScene(this.boardSettings);
        this.boardSettingsStage.initModality(Modality.APPLICATION_MODAL);
        this.boardSettingsStage.initOwner(this.primaryStage);

        this.cardEditorCtrl = cardEditor.getKey();
        this.cardEditorScene = new Scene(cardEditor.getValue());

        boardHistoryOverview.getStylesheets().add(getClass().
                getResource("assets/style/textStyle.css").toExternalForm());
        boardHistoryStage.initModality(Modality.APPLICATION_MODAL);

        cardEditorStage.setScene(cardEditorScene);
        cardEditorStage.initModality(Modality.APPLICATION_MODAL);
        cardEditorStage.setTitle("Card Editor");
        cardEditorStage.setScene(cardEditorScene);
        cardEditorScene.getStylesheets().add(getClass().getResource("assets/style/comboBox.css")
            .toExternalForm());
        createTagStage.initModality(Modality.APPLICATION_MODAL);

        primaryStage.initStyle(StageStyle.DECORATED);
        showServerOverview();
        primaryStage.show();
    }

    /**
     * Shows the server landing overview scene
     */
    public void showServerOverview() {
        primaryStage.setTitle("Talio: Task List Organiser");
        primaryStage.setScene(this.serverOverview);
    }

    /**
     * Shows the main landing overview scene
     */
    public void showLandingOverview() {
        primaryStage.setTitle("Talio: Task List Organiser");
        primaryStage.setScene(this.landingOverview);
    }

    /**
     * Shows the board overview scene
     *
     * @param board the board
     */
    public void showBoardOverview(final Board board) {
        primaryStage.setTitle("Talio: Task List Organiser");
        primaryStage.setScene(this.boardOverview);
        boardOverviewCtrl.refresh(board);
    }

    /**
     * render card view
     *
     * @param cardCtrl
     */
    public void showCardEditor(final CardCtrl cardCtrl) {
        cardEditorCtrl.refresh(cardCtrl);
        cardEditorStage.showAndWait();
    }

    /**
     * Shows the overview of the board history
     */
    public void showHistory() {
        boardHistoryOverviewCtrl.refresh();
        boardHistoryStage.setTitle("Board Visitation History");
        boardHistoryStage.setScene(boardHistoryOverview);
        boardHistoryStage.showAndWait();
    }

    /**
     * Closes the overview of the board history
     */
    public void closeHistory() {
        boardHistoryStage.close();
    }

    /**
     * Shows an existing board overview scene
     */
    public void showBoardSettings() {
        this.boardSettingsStage.setTitle("Talio: Task List Organiser (Settings)");
        this.boardSettingsStage.showAndWait();
    }

    /**
     * closes card editor stage
     */
    public void closeCardEditor() {
        cardEditorStage.close();
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


    /**
     * Method for the tag overview
     */
    public void showTagOverview() {
        createTagStage.setTitle("Talio: Tag list Overview");
        createTagStage.setScene(this.tagOverview);
        tagOverviewCtrl.refresh(boardOverviewCtrl);
        createTagStage.showAndWait();
    }
}