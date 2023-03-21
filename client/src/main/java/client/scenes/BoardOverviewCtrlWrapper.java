package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.Card;
import commons.CardList;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BoardOverviewCtrlWrapper implements Initializable {
    private final BoardOverviewCtrl boardOverviewCtrl;

    /**
     * Creates a new instance of BoardOverviewCtrlWrapper with the given parameters.
     *
     * @param server the ServerUtils object used to communicate with the server
     * @param mainCtrl the MainCtrl object that controls the application
     */
    public BoardOverviewCtrlWrapper(ServerUtils server, MainCtrl mainCtrl) {
        this.boardOverviewCtrl = new BoardOverviewCtrl(server, mainCtrl);
    }

    /**
     * Initializes the wrapped BoardOverviewCtrl object with the given location and resources.
     *
     * @param location the location of the FXML file for the scene
     * @param resources the resources for the scene
     */
    public void initialize(URL location, ResourceBundle resources){
        boardOverviewCtrl.initialize(location, resources);
    }

    /**
     * Refreshes the wrapped BoardOverviewCtrl object with the given currentBoard.
     *
     * @param currentBoard the current board being displayed
     */
    public void refresh(Board currentBoard){
        boardOverviewCtrl.refresh(currentBoard);
    }

    /**
     * Closes the wrapped BoardOverviewCtrl object.
     */
    public void closeApp() {
        boardOverviewCtrl.closeApp();
    }

    /**
     * Minimizes the wrapped BoardOverviewCtrl object.
     */
    public void minimizeApp(){
        boardOverviewCtrl.minimizeApp();
    }

    /**
     * Adds a new list to the wrapped BoardOverviewCtrl object.
     *
     * @throws IOException if an error occurs while adding the list
     */
    public void addList() throws IOException {
        boardOverviewCtrl.addList();
    }

    /**
     * Removes a list from the wrapped BoardOverviewCtrl object.
     *
     * @param paneToBeRemoved the pane to be removed
     * @param listToBeRemoved the list to be removed
     */
    public void removeList(Pane paneToBeRemoved, CardList listToBeRemoved){
        boardOverviewCtrl.removeList(paneToBeRemoved, listToBeRemoved);
    }

    /**
     * Adds a new card to the wrapped BoardOverviewCtrl object.
     *
     * @param vbox the vbox to which the card is being added
     * @param currentList the current list
     * @throws IOException if an error occurs while adding the card
     */
    public void addCard(VBox vbox, CardList currentList) throws IOException{
        boardOverviewCtrl.addCard(vbox, currentList);
    }

    /**
     * Removes a card from the wrapped BoardOverviewCtrl object.
     *
     * @param paneToBeRemoved the pane to be removed
     * @param cardToBeRemoved the card to be removed
     * @param vbox the vbox from which the card is being removed
     * @param currentList the current list
     */
    public void removeCard(Pane paneToBeRemoved, Card cardToBeRemoved,
                           VBox vbox, CardList currentList){
        boardOverviewCtrl.removeCard(paneToBeRemoved, cardToBeRemoved, vbox, currentList);
    }

    /**
     * Refreshes the title of a list in the wrapped BoardOverviewCtrl object.
     *
     * @param selectedList the selected list
     * @param selectedText the selected text
     */
    public void refreshListTitle(CardList selectedList, TextField selectedText) {
        boardOverviewCtrl.refreshListTitle(selectedList, selectedText);
    }

    /**
     * Refreshes the title of a card in the wrapped BoardOverviewCtrl object.
     *
     * @param selectedCard the selected card
     * @param selectedText the selected text
     */
    public void refreshCardTitle(Card selectedCard, TextField selectedText){
        boardOverviewCtrl.refreshCardTitle(selectedCard, selectedText);
    }

    /**
     * Refreshes the description of a card in the wrapped BoardOverviewCtrl object.
     *
     * @param selectedCard the selected list
     * @param selectedText the selected text
     */
    public void refreshCardDescription(Card selectedCard, TextField selectedText){
        boardOverviewCtrl.refreshCardDescription(selectedCard, selectedText);
    }
}
