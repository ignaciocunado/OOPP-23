package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import client.utils.WebsocketUtils;
import com.google.inject.Inject;
import commons.entities.Board;
import commons.entities.Card;
import commons.entities.CardList;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;

import java.util.Objects;

public final class CardListCtrl {

    private ServerUtils server;

    @FXML
    private TextField listTitleField;
    @FXML
    private VBox cards;

    private CardList cardList;
    private BoardOverviewCtrl boardOverviewCtrl;
    private WebsocketUtils websocket;

    /**
     * The wrapping controller for a card list
     * @param server the server functions
     * @param ctrl the board controller
     * @param websocket
     */
    @Inject
    public CardListCtrl(final WebsocketUtils websocket,
                        final ServerUtils server, final BoardOverviewCtrl ctrl) {
        this.websocket = websocket;
        this.server = server;
        this.boardOverviewCtrl = ctrl;
    }

    /**
     * Initialisation method initialising FXML objects
     */
    @FXML
    public void initialize() {
        this.listTitleField.focusedProperty().addListener(this::handleTitleChanged);
        this.setupDragAndDrop();
        this.websocket.addCardListListener(cardList -> {
            if (cardList == null || this.cardList.getId() != cardList.getId()) return;
            Platform.runLater(() -> this.refresh(cardList));
        });
    }

    /**
     * Refreshes card list with the card list data provided
     * @param cardList the data
     */
    public void refresh(final CardList cardList) {
        this.cardList = cardList;
        this.listTitleField.setText(this.cardList.getTitle());

        final Node addButton = this.cards.getChildren().get(this.cards.getChildren().size() - 1);
        this.cards.getChildren().clear();
        for (final Card card : this.cardList.getCards()) {
            var pair = Main.FXML.load(CardCtrl.class, "client", "scenes", "CardTemplate.fxml");
            this.cards.getChildren().add(pair.getValue());
            final CardCtrl ctrl = pair.getKey();
            ctrl.setCardListCtrl(this);
            ctrl.refresh(card);
        }
        this.cards.getChildren().add(addButton);
    }

    /**
     * Handles the add card button
     */
    public void handleAddCard() {
        final CardList cardList = this.server.createCard(this.cardList.getId(), "New Card", "");
        this.refresh(cardList);
    }

    /**
     * Handles the delete list button
     */
    @FXML
    private void handleDeleteList() {
        this.boardOverviewCtrl.removeListById(this.cardList.getId());
    }

    private void handleTitleChanged(final Observable observable) {
        if (!(observable instanceof ReadOnlyBooleanProperty)) return; // Doesn't happen
        final ReadOnlyBooleanProperty focused = (ReadOnlyBooleanProperty) observable;
        if (focused.getValue()) return; // If focuses then don't save yet

        this.server.renameList(this.cardList.getId(), listTitleField.getText());
    }

    /**
     * Removes the card from the list based on the id
     * @param cardId the card id
     */
    public void removeCard(final int cardId) {
        final CardList cardList = this.server.deleteCard(this.cardList.getId(), cardId);
        this.refresh(cardList);
    }

    /**
     * Sets the necessary actions when dragging a Card over a List (part of a List's methods)
     */
    private void setupDragAndDrop() {
        cards.setOnDragOver(event -> {
            event.consume();

            // If it's a card then accept the drag
            if (event.getGestureSource() instanceof Node
                    && Objects.equals(((Node) event.getGestureSource()).getId(), "cardPane")) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
        });

        cards.setOnDragDropped(event -> {
            event.consume();

            final Dragboard dragBoard = event.getDragboard();
            if (!dragBoard.hasString()) {
                event.setDropCompleted(false);
                return;
            }

            final int draggedCardId = Integer.parseInt(dragBoard.getString());

            // 128 + 2.5 is the card height plus half of the space between them
            int position = (int) Math.round(event.getY() / (128 + 2.5));

            this.server.moveCard(draggedCardId, this.cardList.getId(), position);
            this.boardOverviewCtrl.refresh();
        });
    }

    /**
     * getter for board
     * @return board
     */
    public Board getBoard() {
        return boardOverviewCtrl.getBoard();
    }
}
