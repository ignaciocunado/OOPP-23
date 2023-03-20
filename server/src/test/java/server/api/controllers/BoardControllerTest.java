package server.api.controllers;

import commons.entities.Board;
import commons.entities.CardList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import server.api.repositories.TestBoardRepository;
import server.api.repositories.TestCardListRepository;
import server.api.services.TestTextService;
import server.exceptions.EntityNotFoundException;
import server.services.TextService;

public final class BoardControllerTest {

    private TextService textService;
    private TestBoardRepository boardRepo;
    private TestCardListRepository cardRepo;
    private BoardController boardController;

    private BindingResult hasErrorResult;
    private BindingResult noErrorResult;

    @BeforeEach
    public void setup() {
        this.textService = new TestTextService();
        this.boardRepo = new TestBoardRepository();
        this.cardRepo = new TestCardListRepository();
        this.boardController = new BoardController(this.boardRepo, this.cardRepo,this.textService);

        this.hasErrorResult = Mockito.mock(BindingResult.class);
        this.noErrorResult = Mockito.mock(BindingResult.class);

        Mockito.when(hasErrorResult.hasErrors()).thenReturn(true);
        Mockito.when(noErrorResult.hasErrors()).thenReturn(false);
    }

    @Test
    public void createBoardTest() {
        this.boardController.createBoard(new Board("", "password"));
        final Board board = new Board("aaaaaaaaaa", "password");
        board.setId(1);

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

        Assertions.assertThrows(EntityNotFoundException.class, () -> this.boardController.getBoard(100));
    }

    @Test
    public void createListTest() {
        this.boardRepo.save(new Board("aaaaaaaaab", "password"));
        this.boardController.createList(1, new CardList("New List"), noErrorResult);
        Assertions.assertTrue(this.boardRepo.findById(1).get().getListsOnBoard().size() > 0);
        Assertions.assertTrue(this.cardRepo.count() > 0);
    }

    @Test
    public void createListNotFoundTest() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.boardController.createList(1, new CardList("New List"), noErrorResult));
    }

    @Test
    public void deleteListTest() {
        this.boardRepo.save(new Board("aaaaaaaaab", "password"));
        final CardList list = this.boardController.createList(1, new CardList("New List"), noErrorResult).getBody().getListsOnBoard().get(0);

        Assertions.assertTrue(this.boardRepo.findById(1).get().getListsOnBoard().size() > 0);
        this.boardController.deleteList(1, list.getId());
        Assertions.assertTrue(this.boardRepo.findById(1).get().getListsOnBoard().size() == 0);
        Assertions.assertTrue(this.cardRepo.count() == 0);
    }

    @Test
    public void deleteListNotFoundBoardTest() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.boardController.deleteList(1, 1));
    }

    @Test
    public void deleteListNotFoundListTest() {
        this.boardRepo.save(new Board("aaaaaaaaab", "password"));

        Assertions.assertThrows(EntityNotFoundException.class, () -> this.boardController.deleteList(1, 1000));
    }

}
