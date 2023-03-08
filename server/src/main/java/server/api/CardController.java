package server.api;

import commons.Card;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/card")
public class CardController {

    private CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }
    /**
     * to do
     * @param id to do
     * @return
     */
    @PatchMapping("/{id}")
    public int editCard(@PathVariable int id) {

    }

    /**
     * to do
     * @return to do
     */
    @PostMapping("/tag")
    public int createTag() {

    }

    /**
     * to do
     * @return to do
     */
    @DeleteMapping("/tag")
    public int deleteTag() {

    }

    /**
     * to do
     * @return to do
     */
    @PostMapping("task")
    public int createTask(){

    }

    /**
     * to do
     * @return to do
     */
    @DeleteMapping("task")
    public int deleteTask() {

    }

}
