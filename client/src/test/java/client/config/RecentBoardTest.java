package client.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecentBoardTest {

    private RecentBoard recentBoard;
    private RecentBoard recentBoard2;
    @BeforeEach
    void setup() {
        this.recentBoard = new RecentBoard("key");
        this.recentBoard2 = new RecentBoard(null);
    }
    @Test
    void getKeyTest() {
        assertEquals("key", recentBoard.getKey());
        assertNull(recentBoard2.getKey());
    }
}