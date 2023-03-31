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
package client;

import static com.google.inject.Guice.createInjector;
import java.io.IOException;

import client.config.Config;
import client.scenes.*;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.util.Pair;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    public static final MyFXML FXML = new MyFXML(INJECTOR);

    /**
     * main
     *
     * @param args
     */
    public static void main(String[] args) {
        final Config config = INJECTOR.getInstance(Config.class);
        config.loadConfiguration();

        launch();
    }

    /**
     * Loads the controllers and initialises the main controller
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     */
    @Override
    public void start(Stage primaryStage) {
        final Pair<ServerOverviewCtrl, Parent> serverOverview =
                FXML.load(ServerOverviewCtrl.class, "client", "scenes", "ServerOverview.fxml");
        final Pair<LandingOverviewCtrl, Parent> landingOverview =
                FXML.load(LandingOverviewCtrl.class, "client", "scenes", "LandingOverview.fxml");
        final Pair<BoardOverviewCtrl, Parent> boardOverview =
                FXML.load(BoardOverviewCtrl.class, "client", "scenes", "BoardOverview.fxml");
        final Pair<CardEditorCtrl, Parent> cardEditor =
            FXML.load(CardEditorCtrl.class, "client", "scenes", "CardEditor.fxml");
        final Pair<BoardHistoryOverviewCtrl, Parent> boardHistoryOverview =
            FXML.load(BoardHistoryOverviewCtrl.class, "client", "scenes", "BoardHistory.fxml");
        final Pair<AdminPasswordCtrl, Parent> adminPasswordOverview =
            FXML.load(AdminPasswordCtrl.class, "client", "scenes", "AdminPassword.fxml");
        final Pair<BoardSettingsCtrl, Parent> boardSettingsOverview =
                FXML.load(BoardSettingsCtrl.class, "client", "scenes", "BoardSettings.fxml");
        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(
                primaryStage,
                serverOverview,
                landingOverview,
                boardOverview,
                cardEditor,
                boardHistoryOverview,
                adminPasswordOverview,
                boardSettingsOverview
        );
    }

    /**
     * Saves Workspaces when app is stopped
     * @throws IOException if file is not found
     */
    @Override
    public void stop() throws IOException {
        final Config config = INJECTOR.getInstance(Config.class);
        config.saveConfig();
    }

}