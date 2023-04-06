package client.scenes;

import client.config.Config;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.entities.Board;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

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
    @FXML
    private AnchorPane window;
    @FXML
    private Button resetButton;
    @FXML
    private Button saveButtonColour;
    @FXML
    private Button saveButtonPassword;


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
        if(currentBoard.getColour().equals("")) {
            window.setStyle("-fx-background-color: rgb(35,69,103)");
            resetButton.setStyle("-fx-background-color:  #123456");
            saveButtonColour.setStyle("-fx-background-color:  #123456");
            saveButtonPassword.setStyle("-fx-background-color:  #123456");

        }
        else {
            window.setStyle("-fx-background-color: " + currentBoard.getColour());
            resetButton.setStyle("-fx-background-color: " + getRGBShade());
            saveButtonPassword.setStyle("-fx-background-color: " + getRGBShade());
            saveButtonColour.setStyle("-fx-background-color: " + getRGBShade());
        }
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
        Double redDouble =  backgroundColour.getValue().getRed()*255;
        Double greenDouble = backgroundColour.getValue().getGreen()*255;
        Double blueDouble =  backgroundColour.getValue().getBlue()*255;
        int red = redDouble.intValue();
        int green = greenDouble.intValue();
        int blue = blueDouble.intValue();
        currentBoard.setColour("rgb(" + red  + "," +
               green + ", " +
                blue + ")");
        server.editBoard(currentBoard.getId(), currentBoard);
        this.boardOverviewCtrl.refresh(currentBoard);
        mainCtrl.closeBoardSettings();
    }

    /**
     * Gets a shade of RGB to add to panes and buttons
     * @return string representing RGB colour
     */
    public String getRGBShade() {
        Color color = Color.web(currentBoard.getColour());
        double newRed = color.getRed()*0.75*255;
        double newGreen = color.getGreen()*0.75*255;
        double newBlue = color.getBlue()*0.75*255;
        return "rgb(" + newRed + "," + newGreen + "," + newBlue + ")";
    }
}
