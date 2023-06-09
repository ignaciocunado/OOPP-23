/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package server.api.controllers;

import commons.entities.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import server.database.TagRepository;
import server.exceptions.EntityNotFoundException;
import server.exceptions.InvalidRequestException;

@RestController
@RequestMapping("/api/tag")
public class TagController {

    private final TagRepository tagRepo;
    private final SimpMessagingTemplate msgs;
    /**
     * RestAPI Controller for the tags route
     * @param tagRepo repository for tags
     * @param msgs      object to send messages to connected websockets
     */
    public TagController(final TagRepository tagRepo, final SimpMessagingTemplate msgs) {
        this.msgs = msgs;
        this.tagRepo = tagRepo;
    }

    /**
     * Handler for editing a tag
     * @param id the tag id
     * @param tag the new tag data
     * @param errors wrapping object for potential validating errors
     * @return the updated tag
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Tag> editTag(@PathVariable final Integer id,
                                       @Validated @RequestBody final Tag tag,
                                       final BindingResult errors) {
        if (errors.hasErrors()) {
            throw new InvalidRequestException(errors);
        }
        if (!this.tagRepo.existsById(id)) {
            throw new EntityNotFoundException("No tag with id " + id);
        }

        final Tag savedTag = this.tagRepo.getById(id);
        savedTag.setName(tag.getName());
        savedTag.setColour(tag.getColour());
        this.tagRepo.save(savedTag);
        msgs.convertAndSend("topic/tag", savedTag);
        return new ResponseEntity<>(savedTag, new HttpHeaders(), HttpStatus.OK);
    }

}