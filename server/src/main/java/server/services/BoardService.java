package server.services;

import commons.entities.Board;
import commons.entities.CardList;
import commons.entities.Tag;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;
import server.exceptions.EntityNotFoundException;

import java.util.Optional;

@Service
public final class BoardService {

    private final TextService textService;
    private final TagService tagService;
    private CardListService cardListService;
    private final BoardRepository boardRepository;

    public BoardService(final TextService textService,
                        final TagService tagService,
                        final CardListService cardListService,
                        final BoardRepository boardRepository) {
        this.textService = textService;
        this.tagService = tagService;
        this.cardListService = cardListService;
        this.boardRepository = boardRepository;
    }

    public Board createBoard(final String password) {
        final String newKey = this.textService.randomAlphanumericalString(10);
        final Board board = new Board(newKey, password == null ? "" : password);
        return this.boardRepository.save(board);
    }

    public Board getBoard(final String key) {
        final Optional<Board> boardOpt = this.boardRepository.findBoardByKey(key);
        if (boardOpt.isEmpty()) {
            throw new EntityNotFoundException("No board with key " + key);
        }

        return boardOpt.get();
    }

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

    public Board deleteTagFromBoard(final int id, final int tagId) {
        final Optional<Board> boardOpt = this.boardRepository.findById(id);
        if (boardOpt.isEmpty()){
            throw new EntityNotFoundException("No board with id " + id);
        }

        this.tagService.deleteTagFromCards(tagId);

        final Board board = boardOpt.get();
        if (!board.removeTagById(tagId)) {
            throw new EntityNotFoundException("No tag with id " + tagId + " in this board");
        }
        return this.boardRepository.save(board);
    }

    public Board createListInBoard(final int id, final CardList cardList) {
        final Optional<Board> boardOpt = this.boardRepository.findById(id);
        if (boardOpt.isEmpty()){
            throw new EntityNotFoundException("No board with id " + id);
        }

        final CardList savedCardList = this.cardListService.createCardList(cardList);

        final Board board = boardOpt.get();
        board.addList(savedCardList);
        return this.boardRepository.save(board);
    }

    public Board deleteListFromBoard(final int id, final int listId) {
        final Optional<Board> boardOpt = this.boardRepository.findById(id);
        if (boardOpt.isEmpty()){
            throw new EntityNotFoundException("No board with id " + id);
        }

        final Board board = boardOpt.get();
        if (!board.removeListById(listId)) {
            throw new EntityNotFoundException("Board contains no list with id " + listId);
        }
        return this.boardRepository.save(board);
    }

    public Board changePassword(final int id, final Board board) {
        final Optional<Board> boardOpt = this.boardRepository.findById(id);
        if (boardOpt.isEmpty()){
            throw new EntityNotFoundException("No board with id " + id);
        }

        final Board editedBoard = boardOpt.get();
        editedBoard.setPassword(board.getPassword());
        return this.boardRepository.save(editedBoard);
    }
}
