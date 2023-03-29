package server.services;

import commons.entities.Board;
import commons.entities.Card;
import commons.entities.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;
import server.database.CardRepository;
import server.database.TagRepository;
import server.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public final class BoardService {

    private final TextService textService;
    private final BoardRepository boardRepository;
    private final TagRepository tagRepository;
    private final CardRepository cardRepository;

    public BoardService(final TextService textService,
                        final BoardRepository boardRepository,
                        final TagRepository tagRepository,
                        final CardRepository cardRepository) {
        this.textService = textService;
        this.boardRepository = boardRepository;
        this.tagRepository = tagRepository;
        this.cardRepository = cardRepository;
    }

    public Board createBoard(final String password) {
        final String newKey = this.textService.randomAlphanumericalString(10);
        final Board board = new Board(newKey, password);
        return this.boardRepository.save(board);
    }

    public Board getBoard(final String key) {
        final Optional<Board> boardOpt = this.boardRepository.findBoardByKey(key);
        if (boardOpt.isEmpty()) {
            throw new EntityNotFoundException("No board with key " + key);
        }

        return boardOpt.get();
    }

    public Board createTag(final int id, final Tag tag) {
        final Optional<Board> boardOpt = this.boardRepository.findById(id);
        if (boardOpt.isEmpty()) {
            throw new EntityNotFoundException("No board with id " + id);
        }

        final Tag savedTag = this.tagRepository.save(tag);
        final Board board = boardOpt.get();
        board.addTag(savedTag);
        return this.boardRepository.save(board);
    }

    public Board deleteTag(final int id, final int tagId) {
        final Optional<Board> boardOpt = this.boardRepository.findById(id);
        if (boardOpt.isEmpty()){
            throw new EntityNotFoundException("No board with id " + id);
        }
        final Board board = boardOpt.get();
        if (!board.removeTagById(tagId)) {
            throw new EntityNotFoundException("No tag with id " + tagId + " in this board");
        }

        if (cardRepository.count() != 0) {
            List<Integer> cardsId = cardRepository.selectCardsWithTag(tagId);
            for (int cardId : cardsId) {
                Card card = this.cardRepository.getById(cardId);
                card.removeTagById(tagId);
                cardRepository.save(card);
            }
        }

        this.boardRepository.save(board);
        this.tagRepository.deleteById(tagId);
        return board;
    }

}
