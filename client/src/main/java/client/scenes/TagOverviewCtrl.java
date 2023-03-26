package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.Initializable;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class TagOverviewCtrl{

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public TagOverviewCtrl(ServerUtils server, MainCtrl mainCtrl){
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void cancel(){
        mainCtrl.showNewBoardOverview();
    }

}
