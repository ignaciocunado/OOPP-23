package server.database;

import commons.entities.Card;
import commons.entities.CardList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
    /**
     * Query that returns all ids of cards with a given tag
     * @param tagId an id of a tag
     * @return List of ids of cards with a given tag
     */
    @Query("SELECT ct.id FROM Card AS ct JOIN ct.tags tag WHERE tag.id = ?1")
    List<Integer> selectCardsWithTag(final int tagId);

    /**
     * Finds the card from the repository by a taskId
     * @param taskId the taskId
     * @return the card
     */
    @Query("SELECT c from Card c JOIN c.nestedTaskList tasks WHERE tasks.id = :taskId")
    Optional<Card> findByTaskId(final int taskId);
}
