package client.scenes;

import client.config.Config;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.entities.Board;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;

public final class BoardSettingsCtrl {
    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    private final Config config;
    private Board currentBoard;
    private BoardOverviewCtrl boardOverviewCtrl;

    @FXML
    private ColorPicker backgroundColour;
    @FXML
    private ColorPicker textColour;

    /**
     * The wrapping controller for a card list
     *
     * @param server the server functions
     * @param mainCtrl the main controller
     * @param config the config file
     */
    @Inject
    public BoardSettingsCtrl(ServerUtils server, MainCtrl mainCtrl, Config config) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.config = config;
    }

    /**
     * Initialisation method initialising FXML objects
     */
    @FXML
    public void initialize() {

    }

    /**
     * Refreshes the currentBoard to the Board which the settings are called from
     * @param currentBoard the current board
     * @param boardOverviewCtrl ctrl
     */
    public void refresh(Board currentBoard, BoardOverviewCtrl boardOverviewCtrl) {
        this.currentBoard = currentBoard;
        this.boardOverviewCtrl = boardOverviewCtrl;
    }

    /**
     * Deletes the current board which the user is in
     */
    public void deleteBoard() {
        this.server.deleteBoard(this.currentBoard.getKey());
        config.getCurrentWorkspace().deleteBoard(this.currentBoard.getKey());
        this.mainCtrl.closeBoardSettings();
        this.mainCtrl.showLandingOverview();
    }

    /**
     * Resets the colour of the board to default
     */
    public void resetBoardColours() {
        currentBoard.setColour("");
        server.editBoard(currentBoard.getId(), currentBoard);
        mainCtrl.closeBoardSettings();
        this.boardOverviewCtrl.refresh(currentBoard);
    }

    /**
     * Saves the colour chosen and applies it
     */
    public void saveBoardColours() {
        currentBoard.setColour("rgb(" + backgroundColour.getValue().getRed()*255 + "," +
                backgroundColour.getValue().getGreen()*255 + ", " +
                backgroundColour.getValue().getBlue()*255 + ")");
        server.editBoard(currentBoard.getId(), currentBoard);
        this.boardOverviewCtrl.refresh(currentBoard);
        mainCtrl.closeBoardSettings();
    }
}
