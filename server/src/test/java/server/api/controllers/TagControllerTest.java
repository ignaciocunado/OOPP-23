package server.api.controllers;

import commons.entities.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import server.api.repositories.TestTagRepository;
import server.exceptions.EntityNotFoundException;
import server.exceptions.InvalidRequestException;

public final class TagControllerTest {

    private TestTagRepository tagRepo;
    private TagController tagController;

    private BindingResult hasErrorResult;
    private BindingResult noErrorResult;

    @BeforeEach
    public void setup() {
        this.tagRepo = new TestTagRepository();
        this.tagController = new TagController(tagRepo);

        this.hasErrorResult = Mockito.mock(BindingResult.class);
        this.noErrorResult = Mockito.mock(BindingResult.class);

        Mockito.when(hasErrorResult.hasErrors()).thenReturn(true);
        Mockito.when(noErrorResult.hasErrors()).thenReturn(false);
    }

    @Test
    public void editTagTest() {
        this.tagRepo.save(new Tag("", 0));
        final Tag tag = new Tag("", 0);
        tag.setId(1);
        final Tag tag2 = new Tag("Edited", 10);
        tag2.setId(1);

        Assertions.assertEquals(this.tagRepo.findById(1).get(), tag);
        this.tagController.editTag(1, new Tag("Edited", 10), noErrorResult);
        Assertions.assertEquals(this.tagRepo.findById(1).get(), tag2);
    }

    @Test
    public void editInvalidTagTest() {
        this.tagRepo.save(new Tag("", 0));
        Assertions.assertThrows(InvalidRequestException.class, () -> this.tagController.editTag(1, new Tag("", 1923912212), hasErrorResult));
    }

    @Test
    public void editTagNotFoundTest() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.tagController.editTag(10, new Tag("Edit", 0), noErrorResult));
    }

}
