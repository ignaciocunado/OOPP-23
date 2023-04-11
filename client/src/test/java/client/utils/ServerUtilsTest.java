package client.utils;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public final class ServerUtilsTest {

    private static final String SERVER_URI = "http://localhost:8080";
    private static WireMockServer wireMockServer;
    private static ServerUtils serverUtils;

    @BeforeAll
    static void setUp() {
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();

        serverUtils = new ServerUtils(SERVER_URI);
    }

    @Test
    void pingShouldReturnTrueIfServerIsUp() {
        final ResponseDefinitionBuilder res = WireMock.aResponse().withStatus(200)
                .withHeader("Server", "Talio V1");
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/api/ping"))
                .willReturn(res));

        Assertions.assertTrue(this.serverUtils.ping(SERVER_URI));
    }

    @Test
    void pingShouldReturnFalseIfServerIsWrong() {
        final ResponseDefinitionBuilder res = WireMock.aResponse().withStatus(400);
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/api/ping"))
                .willReturn(res));

        Assertions.assertFalse(this.serverUtils.ping(SERVER_URI));
    }

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
