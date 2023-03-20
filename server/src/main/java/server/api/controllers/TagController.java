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
import org.springframework.web.bind.annotation.*;
import server.database.TagRepository;

@RestController
@RequestMapping("/api/tag")
public class TagController {

    private final TagRepository tagRepo;

    /**
     * RestAPI Controller for the tags route
     * @param tagRepo repository for tags
     */
    public TagController(final TagRepository tagRepo) {
        this.tagRepo = tagRepo;
    }

    /**
     * Handler for editing a tag
     * @param id the tag id
     * @param tag the new tag data
     * @return the updated tag
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Tag> editTag(@PathVariable final Integer id, @RequestBody final Tag tag) {
        if (!this.tagRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        final Tag savedTag = this.tagRepo.getById(id);
        savedTag.setName(tag.getName());
        savedTag.setColour(tag.getColour());

        return new ResponseEntity<>(this.tagRepo.save(savedTag), new HttpHeaders(), HttpStatus.OK);
    }

}