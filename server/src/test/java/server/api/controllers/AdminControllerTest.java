package server.api.controllers;

import commons.entities.Board;
import commons.entities.Card;
import commons.entities.CardList;
import commons.entities.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.async.DeferredResult;
import server.api.repositories.TestBoardRepository;
import server.api.repositories.TestCardListRepository;
import server.api.repositories.TestCardRepository;
import server.api.repositories.TestTagRepository;
import server.api.services.TestTextService;
import server.exceptions.EntityNotFoundException;
import server.exceptions.InvalidRequestException;
import server.services.BoardService;
import server.services.CardListService;
import server.services.TagService;
import server.services.TextService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public final class AdminControllerTest {

    private TextService textService;
    private TestBoardRepository boardRepo;
    private TestCardListRepository cardListRepo;
    private TestCardRepository cardRepo;
    private TestTagRepository tagRepo;
    private BoardController boardController;
    private AdminController adminController;
    private BindingResult hasErrorResult;
    private BindingResult noErrorResult;

    @BeforeEach
    public void setup() {
        this.textService = new TestTextService();
        this.boardRepo = new TestBoardRepository();
        this.cardRepo = new TestCardRepository();
        this.cardListRepo = new TestCardListRepository();
        this.tagRepo = new TestTagRepository();

        final TagService tagService = new TagService(this.tagRepo, this.cardRepo);
        final CardListService cardListService = new CardListService(this.cardRepo, this.cardListRepo);
        final BoardService boardService = new BoardService(this.textService,
            tagService,
            cardListService,
            this.boardRepo
        );

        this.adminController = new AdminController(boardService);
        this.boardController = new BoardController(boardService, Mockito.mock(SimpMessagingTemplate.class));
        this.hasErrorResult = Mockito.mock(BindingResult.class);
        this.noErrorResult = Mockito.mock(BindingResult.class);

        Mockito.when(hasErrorResult.hasErrors()).thenReturn(true);
        Mockito.when(noErrorResult.hasErrors()).thenReturn(false);
    }

    @Test
    public void getAllBoardsTest() {
        this.boardController.createBoard(new Board("","name", "password"));
        final Board board = new Board("aaaaaaaaaa","name", "password");
        board.setId(1);
        Assertions.assertEquals(this.boardRepo.findAll(), this.adminController.getAllBoards().getBody());
    }

    @Test
    public void getAllBoardsUpdatesAddBoardNestedRelationsTest() throws InterruptedException, ExecutionException {
        final CompletableFuture<String> fut = new CompletableFuture<>();
        final DeferredResult<ResponseEntity<List<Board>>> result =
                this.adminController.getAllBoardsUpdates();

        final Board boardWithRelations = this.boardRepo.save(new Board("", "Test", ""));
        final Card card = this.cardRepo.save(new Card("Title", "Description"));
        final CardList list = new CardList("Title");
        list.addCard(card);
        this.cardListRepo.save(list);
        boardWithRelations.addList(list);

        final Board board = this.boardController.createBoard(new Board("", "Test", "")).getBody();
        result.setResultHandler(resultHandler -> {
            final ResponseEntity<List<Board>> b = (ResponseEntity<List<Board>>) resultHandler;
            Assertions.assertEquals(b.getBody().get(0), boardWithRelations);
            Assertions.assertEquals(b.getBody().get(1), board);
            fut.complete("done");
        });
        fut.get();
    }

    @Test
    public void getAllBoardsUpdatesAddBoardTest() throws InterruptedException, ExecutionException {
        final CompletableFuture<String> fut = new CompletableFuture<>();
        final DeferredResult<ResponseEntity<List<Board>>> result =
                this.adminController.getAllBoardsUpdates();

        final Board board = this.boardController.createBoard(new Board("", "Test", "")).getBody();
        result.setResultHandler(resultHandler -> {
            final ResponseEntity<List<Board>> b = (ResponseEntity<List<Board>>) resultHandler;
            Assertions.assertEquals(b.getBody().get(0), board);
            fut.complete("done");
        });
        fut.get();
    }

    @Test
    public void getAllBoardsUpdatesRemoveBoardTest() throws InterruptedException, ExecutionException {
        final CompletableFuture<String> fut = new CompletableFuture<>();
        final DeferredResult<ResponseEntity<List<Board>>> result =
                this.adminController.getAllBoardsUpdates();

        this.boardRepo.save(new Board("a", "Test", ""));
        final Board b2 = this.boardRepo.save(new Board("b", "Test", ""));
        this.boardController.deleteBoard("a").getBody();
        result.setResultHandler(resultHandler -> {
            final ResponseEntity<List<Board>> b = (ResponseEntity<List<Board>>) resultHandler;
            Assertions.assertEquals(b.getBody().get(0), b2);
            fut.complete("done");
        });
        fut.get();
    }

}
