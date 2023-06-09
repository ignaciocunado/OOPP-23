package server.services;

import commons.entities.Card;
import commons.entities.CardList;
import commons.entities.Tag;
import commons.entities.Task;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import server.database.CardListRepository;
import server.database.CardRepository;
import server.database.TagRepository;
import server.database.TaskRepository;
import server.exceptions.EntityNotFoundException;

import java.util.Optional;

@Service
public class CardService {

    private final CardListRepository cardListRepository;
    private final CardRepository cardRepository;
    private final TagRepository tagRepository;
    private final TaskRepository taskRepository;

    /** constructor for cardService
     * @param cardListRepository repository for cardlist
     * @param cardRepository repository for card
     * @param tagRepository repository for tag
     * @param taskRepository repository for task
     */
    public CardService(CardListRepository cardListRepository,
                       CardRepository cardRepository, TagRepository tagRepository,
                       TaskRepository taskRepository) {
        this.cardListRepository = cardListRepository;
        this.cardRepository = cardRepository;
        this.tagRepository = tagRepository;
        this.taskRepository = taskRepository;
    }

    /**
     * Edits a Card
     * @param id   id of the Card to edit
     * @param card new Card to take the info from
     * @return the card for the controller
     */
    public Card editCard(int id, Card card){
        if (!cardRepository.existsById(id)) {
            throw new EntityNotFoundException("No card with id " + id);
        }

        Card toEdit = cardRepository.getById(id);
        toEdit.setTitle(card.getTitle());
        toEdit.setDescription(card.getDescription());
        toEdit.setColour(card.getColour());
        cardRepository.save(toEdit);
        return toEdit;
    }
    /**
     * creates a Tag and stores it in a Card
     * @param id id of the Card in which to store the Tag
     * @param tagId the id of the tag that is assigned to the board and card
     * @return the card with the newly assigned tag
     */
    public Card assignTag(int id, int tagId){
        if (!cardRepository.existsById(id)) {
            throw new EntityNotFoundException("No card with id " + id);
        }
        if(!tagRepository.existsById(tagId)) {
            throw new EntityNotFoundException("No tag with id " + tagId);
        }
        Card card = cardRepository.getById(id);
        Tag tag = tagRepository.getById(tagId);
        if(card.getTags().contains(tag)) {
            throw new EntityNotFoundException("tag already assigned");
        }
        card.addTag(tag);
        cardRepository.save(card);
        return card;
    }
    /**
     * deletes a Tag iff it exists
     * @param id    id of the Card
     * @param tagId id of the Tag
     * @return the card without the tag that was deleted
     */
    public Card deleteTag(int id, int tagId){
        if (!cardRepository.existsById(id)) {
            throw new EntityNotFoundException("No card with id " + id);
        }
        if(!tagRepository.existsById(tagId)) {
            throw new EntityNotFoundException("No tag with id " + tagId);
        }
        Card deleteTagFrom = cardRepository.getById(id);
        if (!deleteTagFrom.removeTagById(tagId)) {
            throw new EntityNotFoundException("No tag with id " + tagId + " in card " + id);
        }
        cardRepository.save(deleteTagFrom);
        return deleteTagFrom;
    }
    /**
     * creates a Task and stores it in a Card
     * @param id   id of the Card in which to store the Task
     * @param task the Task to create and add
     * @return the card with the new task
     */
    public Card createTask(int id, Task task){
        if (!cardRepository.existsById(id)) {
            throw new EntityNotFoundException("No card with id " + id);
        }

        taskRepository.save(task);

        Card containsTask = cardRepository.getById(id);
        containsTask.addTask(task);
        cardRepository.save(containsTask);
        return containsTask;
    }
    /**
     * Deletes a Task iff it exists
     * @param id     id of the Card
     * @param taskId id of the Task
     * @return the card with the deleted task
     */
    public Card deleteTask(int id, int taskId){
        if (!cardRepository.existsById(id)) {
            throw new EntityNotFoundException("No card with id " + id);
        }
        Card deleteTaskFrom = cardRepository.getById(id);
        if (!deleteTaskFrom.removeTaskById(taskId)) {
            throw new EntityNotFoundException("No task with id in this card " + id);
        }
        taskRepository.deleteById(taskId);
        cardRepository.save(deleteTaskFrom);
        return deleteTaskFrom;
    }
    /**
     * endpoint for changing the list to which a card is assigned to based on its id
     * @param id integer representing the id of the card
     * @param listId integer representing the list to which the card will be added
     * @param position the position in the children of the list to which the card will be added
     * @return returns the source and destination needed for the websockets
     */
    public Pair<CardList, CardList> move(Integer id, Integer listId, Integer position) {
        if (!this.cardRepository.existsById((id))) {
            throw new EntityNotFoundException("No card with id " + id);
        }
        final Card card = this.cardRepository.getById(id);
        final Optional<CardList> srcOpt = this.cardListRepository.findByCardId(id);
        final Optional<CardList> destOpt = this.cardListRepository.findById(listId);

        if (srcOpt.isEmpty()) {
            throw new EntityNotFoundException("No card list associated with card id " + id);
        }

        if (destOpt.isEmpty()) {
            throw new EntityNotFoundException("No card list with id " + listId);
        }

        final CardList src = srcOpt.get();
        final CardList dest = destOpt.get();

        position = Math.min(dest.getCards().size(), position);
        if (src.getId() == dest.getId()) {
            final int currentPosition = src.getCards().indexOf(card);
            if (currentPosition < position) position--;
        }

        src.removeCard(card);
        // Rounded to closest and can be maximum the amount of children already there.
        dest.getCards().add(position, card);
        this.cardListRepository.save(src);
        this.cardListRepository.save(dest);
        return new ImmutablePair<>(src, dest);
    }
}
