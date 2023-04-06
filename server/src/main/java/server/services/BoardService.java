package server.services;

import commons.entities.Board;
import commons.entities.CardList;
import commons.entities.Tag;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;
import server.exceptions.EntityNotFoundException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Service
public final class BoardService {

    private final TextService textService;
    private final TagService tagService;
    private CardListService cardListService;
    private final BoardRepository boardRepository;

    private final Map<UUID, Consumer<List<Board>>> listeners;

    /**
     * Creates a board service using some other services
     *
     * @param textService     the text service
     * @param tagService      the tag service
     * @param cardListService the card list service
     * @param boardRepository the repository for board data
     */
    public BoardService(final TextService textService,
                        final TagService tagService,
                        final CardListService cardListService,
                        final BoardRepository boardRepository) {
        this.textService = textService;
        this.tagService = tagService;
        this.cardListService = cardListService;
        this.boardRepository = boardRepository;
        this.listeners = new ConcurrentHashMap<>();
    }

    /**
     * Creates a new board with a randomly generated key and the specified password
     *
     * @param name the name for the new board, or null if no password is required
     * @param password the password for the new board, or null if no password is required
     * @return the new board
     */
    public Board createBoard(final String name, final String password) {
        final String newKey = this.textService.randomAlphanumericalString(10);
        final Board board = new Board(newKey, name.equals("") ? "New Board" : name,
                password == null ? "" : password);
        final Board savedBoard = this.boardRepository.save(board);
        this.listeners.values().forEach(Runnable::run);
        return savedBoard;
    }

    /**
     * Gets the board with the specified key
     *
     * @param key the key of the board to retrieve
     * @return the board with the specified key
     * @throws EntityNotFoundException if no board with the specified key exists
     */
    public Board getBoard(final String key) {
        final Optional<Board> boardOpt = this.boardRepository.findBoardByKey(key);
        if (boardOpt.isEmpty()) {
            throw new EntityNotFoundException("No board with key " + key);
        }

        return boardOpt.get();
    }

    /**
     * Gets all boards
     *
     * @return the board with the specified key
     * @throws EntityNotFoundException if no board with the specified key exists
     */
    public List<Board> getAllBoards() {
        return this.boardRepository.findAll();
    }

    /**
     * Gets all boards eagerly by
     * Initializing all fields
     *
     * @return the boards with all fields initialized
     */
    public List<Board> getAllBoardsEagerly() {
        final List<Board> boards = this.boardRepository.findAll();
        Hibernate.initialize(boards);
        boards.forEach(board -> {
            Hibernate.initialize(board);
            Hibernate.initialize(board.getTags());

            board.getLists().forEach(list -> {
                Hibernate.initialize(list);

                list.getCards().forEach(card -> {
                    Hibernate.initialize(card);
                    Hibernate.initialize(card.getTags());
                    Hibernate.initialize(card.getNestedTaskList());
                });
            });
        });
        return boards;
    }

    /**
     * Adds the specified tag to the board with the specified id
     *
     * @param id  the id of the board to add the tag to
     * @param tag the tag to add to the board
     * @return the updated board
     * @throws EntityNotFoundException if no board with the specified id exists
     */
    public Board addTagToBoard(final int id, final Tag tag) {
        final Optional<Board> boardOpt = this.boardRepository.findById(id);
        if (boardOpt.isEmpty()) {
            throw new EntityNotFoundException("No board with id " + id);
        }

        final Tag savedTag = this.tagService.createTag(tag);

        final Board board = boardOpt.get();
        board.addTag(savedTag);
        return this.boardRepository.save(board);
    }

    /**
     * Deletes a tag from a board
     *
     * @param id    The id of the board
     * @param tagId The id of the tag to be deleted
     * @return The updated board after the tag has been removed
     * @throws EntityNotFoundException If the board with the specified id cannot be found
     * or if the tag with the specified id is not in the board.
     */
    public Board deleteTagFromBoard(final int id, final int tagId) {
        final Optional<Board> boardOpt = this.boardRepository.findById(id);
        if (boardOpt.isEmpty()) {
            throw new EntityNotFoundException("No board with id " + id);
        }

        this.tagService.deleteTagFromCards(tagId);

        final Board board = boardOpt.get();
        if (!board.removeTagById(tagId)) {
            throw new EntityNotFoundException("No tag with id " + tagId + " in this board");
        }
        return this.boardRepository.save(board);
    }

    /**
     * Creates a list in a board
     *
     * @param id The id of the board
     * @param cardList The list to be added
     * @return The updated board after the list has been added
     * @throws EntityNotFoundException If the board with the specified id cannot be found
     */
    public Board createListInBoard(final int id, final CardList cardList) {
        final Optional<Board> boardOpt = this.boardRepository.findById(id);
        if (boardOpt.isEmpty()) {
            throw new EntityNotFoundException("No board with id " + id);
        }

        final CardList savedCardList = this.cardListService.createCardList(cardList);

        final Board board = boardOpt.get();
        board.addList(savedCardList);
        return this.boardRepository.save(board);
    }

    /**
     * Deletes a list from a board
     *
     * @param id The id of the board
     * @param listId The id of the list to be deleted
     * @return The updated board after the list has been removed
     * @throws EntityNotFoundException If the board with the specified id cannot be found
     * or if the list with the specified id is not in the board
     */
    public Board deleteListFromBoard(final int id, final int listId) {
        final Optional<Board> boardOpt = this.boardRepository.findById(id);
        if (boardOpt.isEmpty()) {
            throw new EntityNotFoundException("No board with id " + id);
        }

        final Board board = boardOpt.get();
        if (!board.removeListById(listId)) {
            throw new EntityNotFoundException("Board contains no list with id " + listId);
        }
        return this.boardRepository.save(board);
    }


    /**
     * Changes the password of a board
     *
     * @param id The id of the board
     * @param board The updated board with the new data
     * @return The updated board after the data has been changed
     * @throws EntityNotFoundException If the board with the specified id cannot be found
     */
    public Board editBoard(final int id, final Board board) {
        final Optional<Board> boardOpt = this.boardRepository.findById(id);
        if (boardOpt.isEmpty()) {
            throw new EntityNotFoundException("No board with id " + id);
        }

        final Board editedBoard = boardOpt.get();
        editedBoard.setPassword(board.getPassword());
        editedBoard.setName(board.getName());
        return this.boardRepository.save(editedBoard);
    }

    /**
     * Deletes an existing board
     *
     * @param key the key of the board
     * @return the board which has just been deleted
     */
    public Board deleteBoard(String key) {
        final Optional<Board> boardOpt = this.boardRepository.findBoardByKey(key);
        if (boardOpt.isEmpty()) {
            throw new EntityNotFoundException("No board with id " + key);
        }
        final Board board = boardOpt.get();
        this.boardRepository.deleteById(board.getId());
        return board;
    }

    /**
     * Adds a listener for when boards are added or deleted
     * @param listener the event to run
     * @return the id of the runner for removing this
     */
    public UUID addAllBoardListener(final Consumer<List<Board>> listener) {
        final UUID uuid = UUID.randomUUID();
        this.listeners.put(uuid, listener);
        return uuid;
    }

    /**
     * Removes a listener for when boards are added or deleted
     * @param uuid the id of the runner for removing
     */
    public void removeAllBoardListener(final UUID uuid) {
        this.listeners.remove(uuid);
    }



}
