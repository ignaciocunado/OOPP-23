package client.scenes;

import client.config.Config;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.entities.Board;
import commons.entities.CardList;
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
    private ColorPicker backgroundColourBoard;
    @FXML
    private ColorPicker textColourBoard;
    @FXML
    private ColorPicker backgroundColourList;
    @FXML
    private ColorPicker textColourList;
    @FXML
    private AnchorPane window;
    @FXML
    private Button resetButton;
    @FXML
    private Button saveButtonColour;
    @FXML
    private Button saveButtonPassword;
    @FXML
    private Button saveButtonColour1;
    @FXML
    private Button resetButton1;


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
            saveButtonColour1.setStyle("-fx-background-color:  #123456");
            resetButton1.setStyle("-fx-background-color:  #123456");

        }
        else {
            window.setStyle("-fx-background-color: " + currentBoard.getColour());
            resetButton.setStyle("-fx-background-color: " + getRGBShade());
            saveButtonPassword.setStyle("-fx-background-color: " + getRGBShade());
            saveButtonColour.setStyle("-fx-background-color: " + getRGBShade());
            saveButtonColour1.setStyle("-fx-background-color: " + getRGBShade());
            resetButton1.setStyle("-fx-background-color: " + getRGBShade());
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

        this.boardOverviewCtrl.refresh(currentBoard);
    }

    /**
     * Saves the colour chosen and applies it
     */
    public void saveBoardColours() {
        Double redDouble =  backgroundColourBoard.getValue().getRed()*255;
        Double greenDouble = backgroundColourBoard.getValue().getGreen()*255;
        Double blueDouble =  backgroundColourBoard.getValue().getBlue()*255;
        int red = redDouble.intValue();
        int green = greenDouble.intValue();
        int blue = blueDouble.intValue();
        currentBoard.setColour("rgb(" + red  + "," +
               green + ", " +
                blue + ")");
        server.editBoard(currentBoard.getId(), currentBoard);
        this.boardOverviewCtrl.refresh(currentBoard);
//        mainCtrl.closeBoardSettings();
    }

    /**
     * Resets the colours of the lists
     */
    public void resetListColours() {
        for(CardList cardList : currentBoard.getLists()) {
            cardList.setColour("");
            server.editCardList(cardList.getId(), cardList.getTitle(), cardList.getColour());
        }
        this.boardOverviewCtrl.refresh(currentBoard);
    }

    /**
     * Saves the colour chosen and applies it
     */
    public void saveListColours() {
        Double redDouble =  backgroundColourList.getValue().getRed()*255;
        Double greenDouble = backgroundColourList.getValue().getGreen()*255;
        Double blueDouble =  backgroundColourList.getValue().getBlue()*255;
        int red = redDouble.intValue();
        int green = greenDouble.intValue();
        int blue = blueDouble.intValue();
        for(CardList cardList : currentBoard.getLists()) {
            cardList.setColour("rgb(" + red  + "," +
                    green + ", " +
                    blue + ")");
            server.editCardList(cardList.getId(), cardList.getTitle(), cardList.getColour());
        }
        this.boardOverviewCtrl.refresh(currentBoard);
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
