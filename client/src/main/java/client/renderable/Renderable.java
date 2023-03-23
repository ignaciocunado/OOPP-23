package client.renderable;

import javafx.scene.Node;

import java.io.IOException;

public interface Renderable {

    /**
     * Method that renders the renderable object
     * @return the javafx node representation
     * @throws IOException
     */
    Node render() throws IOException;

}
