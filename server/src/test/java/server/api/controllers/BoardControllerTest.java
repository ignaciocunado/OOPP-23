package server.api.controllers;

import commons.Board;
import commons.CardList;
import commons.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import server.api.repositories.TestBoardRepository;
import server.api.repositories.TestCardListRepository;
import server.api.repositories.TestTagRepository;
import server.api.services.TestTextService;
import server.services.TextService;

public final class BoardControllerTest {

    private TextService textService;
    private TestBoardRepository boardRepo;
    private TestCardListRepository cardRepo;
    private TestTagRepository tagRepo;
    private BoardController boardController;

    @BeforeEach
    public void setup() {
        this.textService = new TestTextService();
        this.boardRepo = new TestBoardRepository();
        this.cardRepo = new TestCardListRepository();
        this.tagRepo = new TestTagRepository();
        this.boardController = new BoardController(this.boardRepo, this.cardRepo,this.textService, this.tagRepo);
    }

    @Test
    public void createBoardTest() {
        this.boardController.createBoard(new Board("", "password"));
        final Board board = new Board("aaaaaaaaaa", "password");
        board.setId(1);

        Assertions.assertEquals(this.boardRepo.getById(1), board);
    }

    @Test
    public void createTagOnNewBoardTest() {
        boardRepo.save(new Board(",", "aaaaaaa"));
        final Board board = new Board(",","aaaaaaa");
        board.setId(1);
        Tag tag = new Tag("New Tag", 0);
        board.addTag(tag);

        Assertions.assertEquals(this.boardRepo.getById(1), board);
    }
    @Test
    public void getBoardTest() {
        this.boardRepo.save(new Board("aaaaaaaaab", "password"));
        final Board board = new Board("aaaaaaaaaa", "password");
        this.boardRepo.save(board);
        this.boardRepo.save(new Board("aaaaaaaaac", "password"));
        this.boardRepo.save(new Board("aaaaaaaaad", "password"));

        board.setId(2);
        Assertions.assertEquals(this.boardController.getBoard(2).getBody(), board);
    }

    @Test
    public void getBoardNotFoundTest() {
        this.boardRepo.save(new Board("aaaaaaaaab", "password"));
        this.boardRepo.save(new Board("aaaaaaaaac", "password"));
        this.boardRepo.save(new Board("aaaaaaaaad", "password"));

        Assertions.assertEquals(this.boardController.getBoard(100).getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void createListTest() {
        this.boardRepo.save(new Board("aaaaaaaaab", "password"));
        this.boardController.createList(1, new CardList(""));
        Assertions.assertTrue(this.boardRepo.findById(1).get().getListsOnBoard().size() > 0);
        Assertions.assertTrue(this.cardRepo.count() > 0);
    }

    @Test
    public void createListNotFoundTest() {
        Assertions.assertEquals(this.boardController.createList(1, new CardList("")).getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void deleteListTest() {
        this.boardRepo.save(new Board("aaaaaaaaab", "password"));
        final CardList list = this.boardController.createList(1, new CardList("")).getBody().getListsOnBoard().get(0);

        Assertions.assertTrue(this.boardRepo.findById(1).get().getListsOnBoard().size() > 0);
        this.boardController.deleteList(1, list.getId());
        Assertions.assertTrue(this.boardRepo.findById(1).get().getListsOnBoard().size() == 0);
        Assertions.assertTrue(this.cardRepo.count() == 0);
    }

    @Test
    public void deleteListNotFoundBoardTest() {
        Assertions.assertEquals(this.boardController.deleteList(1, 1).getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void deleteListNotFoundListTest() {
        this.boardRepo.save(new Board("aaaaaaaaab", "password"));

        Assertions.assertEquals(this.boardController.deleteList(1, 1000).getStatusCode(), HttpStatus.NOT_FOUND);
    }

}
