package server.api.controllers;

import commons.entities.Card;
import commons.entities.Tag;
import commons.entities.Board;
import commons.entities.CardList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.validation.BindingResult;
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

import static org.junit.jupiter.api.Assertions.*;

public final class BoardControllerTest {

    private TextService textService;
    private TestBoardRepository boardRepo;
    private TestCardListRepository cardListRepo;
    private TestCardRepository cardRepo;
    private TestTagRepository tagRepo;
    private BoardController boardController;
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
        final CardListService cardListService = new CardListService(this.cardListRepo);
        final BoardService boardService = new BoardService(this.textService,
                tagService,
                cardListService,
                this.boardRepo
        );


        this.boardController = new BoardController(boardService);
        this.hasErrorResult = Mockito.mock(BindingResult.class);
        this.noErrorResult = Mockito.mock(BindingResult.class);

        Mockito.when(hasErrorResult.hasErrors()).thenReturn(true);
        Mockito.when(noErrorResult.hasErrors()).thenReturn(false);
    }

    @Test
    public void createBoardTest() {
        this.boardController.createBoard(new Board("","name", "password"));
        final Board board = new Board("aaaaaaaaaa","name", "password");
        board.setId(1);

        Assertions.assertEquals(this.boardRepo.getById(1), board);
    }

    @Test
    public void getBoardTest() {
        this.boardRepo.save(new Board("aaaaaaaaab","name", "password"));
        final Board board = new Board("aaaaaaaaaa","name", "password");
        this.boardRepo.save(board);
        this.boardRepo.save(new Board("aaaaaaaaac","name", "password"));
        this.boardRepo.save(new Board("aaaaaaaaad","name", "password"));

        board.setId(2);
        Assertions.assertEquals(this.boardController.getBoard("aaaaaaaaaa").getBody(), board);
    }

    @Test
    public void getBoardNotFoundTest() {
        this.boardRepo.save(new Board("aaaaaaaaab","name", "password"));
        this.boardRepo.save(new Board("aaaaaaaaac","name", "password"));
        this.boardRepo.save(new Board("aaaaaaaaad","name", "password"));

        Assertions.assertThrows(EntityNotFoundException.class, () -> this.boardController.getBoard("doesntexist"));
    }

    @Test
    public void createListTest() {
        this.boardRepo.save(new Board("aaaaaaaaab","name", "password"));
        this.boardController.createList(1, new CardList("New List"), noErrorResult);
        Assertions.assertTrue(this.boardRepo.findById(1).get().getLists().size() > 0);
        Assertions.assertTrue(this.cardListRepo.count() > 0);
    }

    @Test
    public void createInvalidListTest() {
        this.boardRepo.save(new Board("aaaaaaaaab","name", "password"));
        Assertions.assertThrows(InvalidRequestException.class, () -> this.boardController.createList(1, new CardList(""), hasErrorResult));
    }

    @Test
    public void createListNotFoundTest() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.boardController.createList(1, new CardList("New List"), noErrorResult));
    }

    @Test
    public void deleteListTest() {
        this.boardRepo.save(new Board("aaaaaaaaab","name", "password"));
        final CardList list = this.boardController.createList(1, new CardList("New List"), noErrorResult).getBody().getLists().get(0);

        assertTrue(this.boardRepo.findById(1).get().getLists().size() > 0);
        this.boardController.deleteList(1, list.getId());
        assertTrue(this.boardRepo.findById(1).get().getLists().size() == 0);
        assertTrue(this.cardRepo.count() == 0);
    }

    @Test
    public void deleteListNotFoundBoardTest() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.boardController.deleteList(1, 1));
    }

    @Test
    public void deleteListNotFoundListTest() {
        this.boardRepo.save(new Board("aaaaaaaaab","name", "password"));

        Assertions.assertThrows(EntityNotFoundException.class, () -> this.boardController.deleteList(1, 1000));
    }

    @Test
    public void deleteListWithCardsTest() {
        this.boardRepo.save(new Board("aaaaaaaaab","name", "password"));
        final CardList list = this.boardController.createList(1, new CardList("New List"), noErrorResult).getBody().getLists().get(0);
        Card card = new Card("title", "desc");
        list.addCard(card);

        assertTrue(this.boardRepo.findById(1).get().getLists().size() > 0);
        assertTrue(list.getCards().get(0).equals(card));

        boardController.deleteList(1, list.getId());
        assertTrue(this.boardRepo.findById(1).get().getLists().size() == 0);
        assertEquals(0, cardRepo.count());

    }

    @Test
    public void createTagTest() {
        boardRepo.save(new Board("aa","name", "aaa"));
        this.boardController.createTag(1, new Tag("New Tag", 0), noErrorResult);
        final Tag tag = new Tag("New Tag", 0);
        tag.setId(1);
        assertTrue(tagRepo.existsById(1));
        assertEquals(tagRepo.getById(1), tag);
    }

    @Test
    public void createTagBoardNotFoundTest() {
        assertThrows(EntityNotFoundException.class, () -> boardController.createTag(10, new Tag(), noErrorResult));
    }

    @Test
    public void createTagThrowTest() {
        boardRepo.save(new Board("aa","name", "aaa"));
        final Tag tag = new Tag("New Tag", -1);

        assertThrows(InvalidRequestException.class, () -> boardController.createTag(1, tag, hasErrorResult));
    }

    @Test
    public void deleteTagTest() {
        boardRepo.save(new Board("aaa","name", "aaa"));
        this.boardController.createTag(1, new Tag("New Tag", 0), noErrorResult);
        final Tag tag = new Tag("New Tag", 0);
        tag.setId(1);
        boardController.deleteTag(1, 1);

        assertEquals(0, this.boardRepo.getById(1).getTags().size());
    }

    @Test
    public void deleteTagWithCardsTest() {
        boardRepo.save(new Board("aaa","name", "aaa"));
        this.boardController.createTag(1, new Tag("New Tag", 0), noErrorResult);
        final CardList list = this.boardController.createList(1, new CardList("New List"), noErrorResult).getBody().getLists().get(0);
        final Tag tag = new Tag("New Tag", 0);
        tag.setId(1);
        Card card = new Card("title", "description");
        card.setId(1);
        list.addCard(card);
        list.getCards().get(0).addTag(tag);
        this.cardRepo.save(card);

        boardController.deleteTag(1, 1);
        assertEquals(0, this.cardRepo.getById(1).getTags().size());
    }

    @Test
    public void deleteTagThrowNoBoardTest() {
        boardRepo.save(new Board("aa","name", "aaa"));
        final Tag tag = new Tag("New Tag", -1);

        assertThrows(EntityNotFoundException.class, () -> boardController.deleteTag(2, 1));
    }

    @Test
    public void deleteTagThrowNoTagTest() {
        boardRepo.save(new Board("aa","name", "aaa"));
        this.boardController.createTag(1, new Tag("New Tag", 0), noErrorResult);
        final Tag tag = new Tag("New Tag", -1);

        assertThrows(EntityNotFoundException.class, () -> boardController.deleteTag(1, 2));
    }

    @Test
    public void editPasswordTest() {
        this.boardRepo.save(new Board("title","name", "password"));
        final Board board1 = new Board("title","name", "password");
        board1.setId(1);
        final Board board2 = new Board("title","name", "new password");
        board2.setId(1);

        Assertions.assertEquals(this.boardRepo.findById(1).get(), board1);
        this.boardController.editPassword(1, new Board("title","name", "new password"), noErrorResult);
        Assertions.assertEquals(this.boardRepo.findById(1).get(), board2);
    }

    @Test
    public void editInvalidBoardTest() {
        Assertions.assertThrows(InvalidRequestException.class, () -> this.boardController.editPassword(5, new Board("","", ""), hasErrorResult));
    }

    @Test
    public void editBoardNotFoundTest() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.boardController.editPassword(5, new Board("title", "name", "password"), noErrorResult));
    }
}
