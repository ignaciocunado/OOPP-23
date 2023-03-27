package client.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public final class ServerUtilsTest {

    // TODO: Maybe do integration testing here but probably not

    //    @Test
    //    public void createBoardTest() {
    //        final ServerUtils utils = new ServerUtils("http://localhost:8080/");
    //        System.out.println(utils.createBoard("test"));
    //    }

    //    @Test
    //    public void getBoardTest() {
    //        final ServerUtils utils = new ServerUtils("http://localhost:8080/");
    //        System.out.println(utils.getBoard("0Pzi3e8Hrm"));
    //    }

    @Test
    public void getServerTest() {
        final ServerUtils utils = new ServerUtils("server");
        Assertions.assertEquals(utils.getServer(), "server");
    }


    @Test
    public void setServerTest() {
        final ServerUtils utils = new ServerUtils();
        utils.setServer("new server");
        Assertions.assertEquals(utils.getServer(), "new server");
    }

}
