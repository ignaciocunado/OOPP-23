package server.database;

import commons.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
    /**
     * Query that returns all ids of cards with a given tag
     * @param tagId an id of a tag
     * @return List of ids of cards with a given tag
     */
    @Query("SELECT ct.id FROM Card AS ct JOIN ct.tags tag WHERE tag.id = ?1")
    List<Integer> selectCardsWithTag(final int tagId);
}
