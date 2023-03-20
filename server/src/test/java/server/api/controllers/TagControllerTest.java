package server.api.controllers;

import commons.entities.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import server.api.repositories.TestTagRepository;

public final class TagControllerTest {

    private TestTagRepository tagRepo;
    private TagController tagController;

    @BeforeEach
    public void setup() {
        this.tagRepo = new TestTagRepository();
        this.tagController = new TagController(tagRepo);
    }

    @Test
    public void editTagTest() {
        this.tagRepo.save(new Tag("", 0));
        final Tag tag = new Tag("", 0);
        tag.setId(1);
        final Tag tag2 = new Tag("Edited", 10);
        tag2.setId(1);

        Assertions.assertEquals(this.tagRepo.findById(1).get(), tag);
        this.tagController.editTag(1, new Tag("Edited", 10));
        Assertions.assertEquals(this.tagRepo.findById(1).get(), tag2);
    }

    @Test
    public void editTagNotFoundTest() {
        Assertions.assertEquals(this.tagController.editTag(10, new Tag()).getStatusCode(), HttpStatus.NOT_FOUND);
    }

}
