package client.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecentBoardTest {

    private RecentBoard recentBoard;
    private RecentBoard recentBoard2;
    private RecentBoard recentBoard3;

    @BeforeEach
    void setup() {
        this.recentBoard = new RecentBoard("key");
        this.recentBoard2 = new RecentBoard(null);
        this.recentBoard3 = new RecentBoard("key");
    }

    @Test
    void getKeyTest() {
        assertEquals("key", recentBoard.getKey());
        assertNull(recentBoard2.getKey());
    }

    @Test
    void equalsTest() {
        assertEquals(recentBoard,recentBoard3);
    }

    @Test
    void equalsNotTest() {
        RecentBoard leojfnf = new RecentBoard("Project is taking too much time");
        assertNotEquals(leojfnf, recentBoard);
    }

    @Test
    void hashCodeTest() {
        assertEquals(recentBoard.hashCode(), recentBoard3.hashCode());
    }

}