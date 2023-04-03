package client.scenes;

import client.utils.ServerUtils;
import client.utils.WebsocketUtils;
import com.google.inject.Inject;
import commons.entities.Board;
import commons.entities.Card;
import javafx.application.Platform;
import commons.entities.Tag;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;

public final class CardCtrl {

    private WebsocketUtils websocket;
    private ServerUtils server;
    private MainCtrl mainCtrl;

    private CardListCtrl cardListCtrl;

    @FXML
    private Pane cardPane;
    @FXML
    private TextField cardTitle;
    @FXML
    private Text cardDescription;
    @FXML
    private HBox tagList;
    private BoardOverviewCtrl boardOverviewCtrl;
    private Card card;

    /**
     * The wrapping controller for a card
     *
     * @param websocket websocket setup
     * @param server   the server functions
     * @param mainCtrl
     */
    @Inject
    public CardCtrl(final WebsocketUtils websocket,
                    final ServerUtils server, final MainCtrl mainCtrl) {
        this.websocket = websocket;
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
     * Gets the cardList controller to which this card us associated
     * @return the CardListCtrl
     */
    public CardListCtrl getCardListCtrl() {
        return this.cardListCtrl;
    }

    /**
     * Sets the controller of the BoardOverview
     * @param boardOverviewCtrl the controller
     */
    public void setBoardOverviewCtrl(final BoardOverviewCtrl boardOverviewCtrl) {
        this.boardOverviewCtrl = boardOverviewCtrl;
        this.boardOverviewCtrl.setCardCtrl(this);
    }

    /**
     * Refreshes card with the card data provided
     *
     * @param card the data
     */
    public void refresh(Card card) {
        this.card = card;
        this.cardTitle.setText(this.card.getTitle());
        this.cardDescription.setText(this.card.getDescription());
        tagList.getChildren().clear();
        try {
            for (Tag tag : card.getTags()) {
                FXMLLoader loader = new FXMLLoader();
                Pane tagPane = loader.load(getClass().getResource("TagInBoardOverview.fxml")
                        .openStream());
                TagInBoardCtrl ctrl = loader.getController();
                ctrl.update(tag);
                tagList.getChildren().add(tagPane);
            }
        }
        catch (IOException e) {

        }
    }

    /**
     * Gets the card this card ctrl is controller
     * @return the card
     */
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
        this.websocket.addCardListener(changedCard -> {
            if (changedCard == null || changedCard.getId() != (card.getId())) return;
            Platform.runLater(() -> this.refresh(changedCard));
        });
        setEditTitle();
    }

    public void setEditTitle() {
        cardPane.setOnKeyTyped(event -> {
            if (event.getCharacter().equals("e")) {
                Pane temp = (Pane) cardPane.getChildren().get(0);
                TextField field = (TextField) temp.getChildren().get(4);
                field.requestFocus();
                field.setOnKeyPressed(event1 -> {
                    if (event1.getCode().equals(KeyCode.ENTER)) {
                        this.editTitle();
                    }
                });
            }
        });
    }

    @FXML
    public void handleDeleteCard() {
        this.cardListCtrl.removeCard(this.card.getId());
    }

    @FXML
    public void handleEditCard() {
        this.mainCtrl.showCardEditor(this);
    }

    /**
     * getter for board
     * @return board
     */
    public Board getBoard() {
        return cardListCtrl.getBoard();
    }

    /**
     * Sets appropriate visual feedback when hovering over a card
     */
    public void mouseHover() {
        this.boardOverviewCtrl.setHoverCard(this.getCardPane());
        cardPane.setOpacity(0.75);
        Pane childPane = (Pane) cardPane.getChildren().get(0);
        childPane.setStyle("-fx-background-color: #123456; -fx-background-radius: 10;" +
            "-fx-border-color: rgb(1,35,69); -fx-border-width: 3px");
    }

    /**
     * Stops visual feedback for hovering over a card
     */
    public void mouseStopHover() {
        cardPane.setOpacity(1);
        Pane childPane = (Pane) cardPane.getChildren().get(0);
        childPane.setStyle("-fx-background-color: #123456; " +
            "-fx-background-radius: 10; -fx-border-width: 0px");
    }

    /**
     * Sets the cardPane
     * @param cardPane the Pane being assigned to it
     */
    public void setCardPane(Pane cardPane) {
        this.cardPane = cardPane;
    }

    /**
     * Gets the cardPane
     * @return the Pane of the current card
     */
    public Pane getCardPane() {
        return this.cardPane;
    }

    /**
     * Edits and saves the title of a card
     */
    public void editTitle() {
        card.setTitle(cardTitle.getText());
        this.server.editCard(this.card.getId(), cardTitle.getText(), cardDescription.getText());
    }
}
