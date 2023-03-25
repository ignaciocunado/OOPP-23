package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.entities.CardList;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.function.Consumer;

public final class CardListCtrl {

    private ServerUtils server;

    @FXML
    private Pane list;
    @FXML
    private TextField listNameField;
    @FXML
    private ScrollPane scroll;
    @FXML
    private VBox cards;

    private CardList cardList;
    private CardWrapper cardWrapper;
    private BoardOverviewCtrl boardOverviewCtrl;


    /**
     * The wrapping renderable for a card list
     * @param wrapper a wrapper for card methods
     * @param ctrl the board controller
     */
    @Inject
    public CardListCtrl(final ServerUtils server, final BoardOverviewCtrl ctrl, final CardWrapper wrapper) {
        this.server = server;
        this.boardOverviewCtrl = ctrl;
        this.cardWrapper = wrapper;
    }

    @FXML
    public void initialize() {
        this.listNameField.focusedProperty().addListener(this::handleNameChanged);
    }

    /**
     * Refreshes all data associated with a cardlist
     */
    public void refresh() {
        this.listNameField.setText(this.cardList.getTitle());
        this.setDropCardOnListActions(list, this.cardList, scroll, cards);
    }

    /**
     * Handles the add card button
     */
    public void handleAddCard() {
        try {cardWrapper.addCard(cards, this.cardList);} catch (IOException e) {}
    }

    /**
     * Handles the delete list button
     */
    public void handleDeleteList() {
        this.boardOverviewCtrl.removeListById(this.cardList.getId());
    }

    private void handleNameChanged(final Observable observable) {
        if (!(observable instanceof ReadOnlyBooleanProperty)) return; // Doesn't happen
        final ReadOnlyBooleanProperty focused = (ReadOnlyBooleanProperty) observable;
        if(focused.getValue()) return; // If focuses then don't save yet

        this.server.renameList(this.cardList.getId(), listNameField.getText());
    }

    /**
     * Sets the necessary actions when dragging a Card over a List (part of a List's methods)
     * @param paneToDropInto the Pane which the Card is hovering over
     * @param listToDropInto the CardList corresponding to the Pane
     * @param scrollPane the ScrollPane contained in the paneToDropInto
     * @param vbox the VBox to which the Card will be added
     */
    private void setDropCardOnListActions(Pane paneToDropInto, CardList listToDropInto,
                                          ScrollPane scrollPane, VBox vbox) {
        int cardsAbove = (int) (scrollPane.getVvalue() *
                (scrollPane.getContent().getBoundsInLocal().getHeight()
                        - scrollPane.getViewportBounds().getHeight()) / 184.20001220703125);

        paneToDropInto.setOnDragOver(event -> {
            if (event.getGestureSource() != paneToDropInto && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        paneToDropInto.setOnDragDropped((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                event.setDropCompleted(true);
                cardWrapper.removeExistingCard();
                cardWrapper.addExistingCard(vbox, cardWrapper.getPaneBeingDragged(), cardsAbove,
                        event.getSceneY(), listToDropInto);
            } else {
                event.setDropCompleted(false);
            }
            event.consume();
        });
    }

    /**
     * Sets the cardlist to get details from
     * @param cardList the cardlist
     */
    public void setCardList(final CardList cardList) {
        this.cardList = cardList;
    }

}
