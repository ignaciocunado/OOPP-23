package client.scenes;

import client.MyFXML;
import commons.entities.Card;
import commons.entities.CardList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashSet;


public class CardWrapper {

    private final HashSet<Integer> cardsIds = new HashSet<>();
    private Pane paneBeingDragged;
    private VBox originalVBox;
    private Card cardBeingDragged;
    private CardList originalCardList;
    @FXML
    private HBox tagList;

    private MainCtrl mainCtrl;

    /**
     * Initiates the CardWrapper class
     * @param mainCtrl mainCtrl
     */
    public CardWrapper(MainCtrl mainCtrl){
        this.mainCtrl = mainCtrl;
    }

    /**
     * Gets the Pane currently being dragged
     * @return the corresponding Pane
     */
    public Pane getPaneBeingDragged() {
        return paneBeingDragged;
    }

    /**
     * Gets the VBox from which the Pane is being dragged
     * @return the corresponding VBox
     */
    public VBox getOriginalVBox() {
        return originalVBox;
    }

    /**
     * Gets the Card object currently being dragged
     * @return the corresponding Card
     */
    public Card getCardBeingDragged() {
        return cardBeingDragged;
    }

    /**
     * Gets the CardList object from which the Card is being dragged
     * @return the corresponding CardList
     */
    public CardList getOriginalCardList() {
        return originalCardList;
    }

    /**
     * Adds a Card to the current List object and displays it
     * @param vbox the VBox associated to the List
     * @param currentList the current CardList
     */
    public void addCard(VBox vbox, CardList currentList) throws IOException {
        Pane outerCardPane = FXMLLoader.load(getLocation("client", "scenes", "CardTemplate.fxml"));
        vbox.getChildren().add(vbox.getChildren().size() - 1, outerCardPane);
        int counter = 1;
        while (cardsIds.contains(counter)) {
            counter++;
        }
        cardsIds.add(counter);
        outerCardPane.setId(String.valueOf(counter));
        Card newCard = new Card("Title", "Description");
        currentList.addCard(newCard);
        setCardMethods(vbox, currentList, outerCardPane, newCard);
    }

    /**
     * Adds an existing Card to a list
     * @param vbox the VBox associated to the List
     * @param draggingCard the Pane which is being dragged
     * @param cardsAbove the number of Cards above what the user can see
     * @param sceneY mouse horizontal position
     * @param currentList the List to which the Card will be added
     */
    public void addExistingCard(VBox vbox, Pane draggingCard, int cardsAbove,
                                double sceneY, CardList currentList) {
        if (vbox.getChildren().size() <= 1) {
            vbox.getChildren().add(0, draggingCard);
        }
        else if (sceneY < 300) {
            vbox.getChildren().add(cardsAbove, draggingCard);
        }
        else if (sceneY < 550) {
            if (cardsAbove + 1 == vbox.getChildren().size()) {
                vbox.getChildren().add(vbox.getChildren().size() - 1, draggingCard);
            }
            else {
                vbox.getChildren().add(cardsAbove + 1, draggingCard);
            }
        }
        else {
            if (cardsAbove + 2 == vbox.getChildren().size()) {
                vbox.getChildren().add(vbox.getChildren().size() - 1, draggingCard);
            }
            else {
                vbox.getChildren().add(cardsAbove + 2, draggingCard);
            }
        }
        setCardMethods(vbox, currentList,
                draggingCard, cardBeingDragged);
    }

    /**
     * Sets the methods of a new Card which has just been created
     * @param vbox the VBox containing the newly added Card
     * @param currentList the CardList to which the Card now belongs to
     * @param outerCardPane the Pane of the Card
     * @param newCard the Card which has just been added
     */
    public void setCardMethods(VBox vbox, CardList currentList, Pane outerCardPane, Card newCard) {
        Pane innerCardPane = (Pane) outerCardPane.getChildren().get(0);
        outerCardPane.setOnDragDetected(event -> {
            Dragboard db = innerCardPane.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString("Pane source text");
            db.setContent(content);
            paneBeingDragged = outerCardPane;
            originalVBox = vbox;
            cardBeingDragged = newCard;
            originalCardList = currentList;
            event.consume();
        });


        innerCardPane.getChildren().get(1).setOnMouseClicked(event ->
                removeCard(outerCardPane, newCard, vbox, currentList));

        Text cardTitle = (Text) innerCardPane.getChildren().get(3);
        cardTitle.setText("Card: " + outerCardPane.getId());
        //cardTitle.setOnKeyPressed(event -> refreshCardTitle(newCard, cardTitle));


        Text cardDescription = (Text) innerCardPane.getChildren().get(4);
        cardDescription.setText("Description: ");
        /*
        cardDescription.setOnKeyPressed(event -> {
            refreshCardDescription(newCard, cardDescription);
        });
        */


        innerCardPane.setOnMouseClicked(event -> {
            refreshCardTitle(newCard, cardTitle);
            refreshCardDescription(newCard, cardDescription);
            try {
                mainCtrl.showCardEditor(newCard);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            cardTitle.setText(newCard.getTitle());
            cardDescription.setText(newCard.getDescription());
        });
    }

    /**
     * Removes an existing Card from the List
     * @param paneToBeRemoved the Pane which needs to be deleted
     * @param cardToBeRemoved the Card which needs to be deleted
     * @param vbox the VBox associated to the List
     * @param currentList the current CardList
     */
    public void removeCard(Pane paneToBeRemoved, Card cardToBeRemoved,
                           VBox vbox, CardList currentList){
        cardsIds.remove(paneToBeRemoved.getId());
        vbox.getChildren().remove(paneToBeRemoved);
        currentList.removeCard(cardToBeRemoved);
    }

    /**
     * Removes an existing Card from a List
     */
    public void removeExistingCard() {
        originalVBox.getChildren().remove(paneBeingDragged);
        originalCardList.getCards().remove(cardBeingDragged);
    }
    /**
     * Refreshes the title of a Card
     * @param selectedCard the selected Card
     * @param selectedText the TextField associated to the Card
     */
    public void refreshCardTitle(Card selectedCard, Text selectedText){
        selectedCard.setTitle(selectedText.getText());
        System.out.println(selectedCard);
    }

    /**
     * Refreshes the description of a Card
     * @param selectedCard the selected Card
     * @param selectedText the TextField associated to the Card
     */
    public void refreshCardDescription(Card selectedCard, Text selectedText){
        selectedCard.setDescription(selectedText.getText());
        System.out.println(selectedCard);
    }

    /**
     * Gets the location of a resource with the given String elements
     * @param parts Strings of where to find the resource
     * @return the URL of the requested resource
     */
    private URL getLocation(String... parts) {
        var path = Path.of("", parts).toString();
        return MyFXML.class.getClassLoader().getResource(path);
    }

}
