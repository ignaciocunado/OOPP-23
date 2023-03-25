package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.entities.Card;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;

public final class CardCtrl {

    private ServerUtils server;
    private CardListCtrl cardListCtrl;

    @FXML
    private Pane cardPane;
    @FXML
    private TextField cardTitleField;
    @FXML
    private TextField cardDescriptionField;

    private Card card;

    /**
     * The wrapping controller for a card
     * @param server the server functions
     */
    @Inject
    public CardCtrl(final ServerUtils server) {
        this.server = server;
    }

    /**
     * Additional setter to inject the correct card list controller
     * @param cardListCtrl the card list controller
     */
    public void setCardListCtrl(final CardListCtrl cardListCtrl) {
        this.cardListCtrl = cardListCtrl;
    }

    /**
     * Refreshes card with the card data provided
     * @param card the data
     */
    public void refresh(final Card card) {
        this.card = card;

        this.cardTitleField.setText(this.card.getTitle());
        this.cardDescriptionField.setText(this.card.getDescription());
    }

    /**
     * Initialisation method initialising FXML objects
     */
    @FXML
    public void initialize() {
        cardPane.setOnDragDetected(event -> {
            final Dragboard db = cardPane.startDragAndDrop(TransferMode.MOVE);
            final ClipboardContent content = new ClipboardContent();
            content.putString(Integer.toString(this.card.getId()));
            db.setContent(content);
            event.consume();
        });
        cardTitleField.focusedProperty().addListener(this::handleCardDetailsChanged);
        cardDescriptionField.focusedProperty().addListener(this::handleCardDetailsChanged);
    }

    @FXML
    private void handleDeleteCard() {
        this.cardListCtrl.removeCard(this.card.getId());
    }

    private void handleCardDetailsChanged(final Observable observable) {
        if (!(observable instanceof ReadOnlyBooleanProperty)) return; // Doesn't happen
        final ReadOnlyBooleanProperty focused = (ReadOnlyBooleanProperty) observable;
        if (focused.getValue()) return; // If focuses then don't save yet

        this.server.editCard(
                this.card.getId(),
                cardTitleField.getText(),
                cardDescriptionField.getText()
        );
    }
}
