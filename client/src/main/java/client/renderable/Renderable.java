package client.renderable;

import javafx.scene.Node;

import java.io.IOException;

public interface Renderable {

    Node render() throws IOException;

}
