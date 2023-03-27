package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.entities.Card;
import javafx.fxml.FXML;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;

public final class CardCtrl {

    private ServerUtils server;
    private MainCtrl mainCtrl;

    private CardListCtrl cardListCtrl;

    @FXML
    private Pane cardPane;
    @FXML
    private Text cardTitle;
    @FXML
    private Text cardDescription;

    private Card card;

    /**
     * The wrapping controller for a card
     *
     * @param server   the server functions
     * @param mainCtrl
     */
    @Inject
    public CardCtrl(final ServerUtils server, final MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Additional setter to inject the correct card list controller
     *
     * @param cardListCtrl the card list controller
     */
    public void setCardListCtrl(final CardListCtrl cardListCtrl) {
        this.cardListCtrl = cardListCtrl;
    }

    /**
     * Refreshes card with the card data provided
     *
     * @param card the data
     */
    public void refresh(final Card card) {
        this.card = card;

        this.cardTitle.setText(this.card.getTitle());
        this.cardDescription.setText(this.card.getDescription());
    }

    public Card getCard() {
        return this.card;
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
    }

    @FXML
    private void handleDeleteCard() {
        this.cardListCtrl.removeCard(this.card.getId());
    }

    @FXML
    private void handleEditCard() {
        this.mainCtrl.showCardEditor(this);
    }

}
