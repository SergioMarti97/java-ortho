package dungeonViewer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import olcPGEApproach.GameContainer;

import java.net.URL;
import java.util.ResourceBundle;

public class DungeonViewerController implements Initializable {

    @FXML
    private Button btnSave;

    @FXML
    private ImageView imgView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DungeonViewer dungeonViewer = new DungeonViewer();
        GameContainer gc = new GameContainer(
                dungeonViewer,
                (int)imgView.getFitWidth(),
                (int)imgView.getFitHeight(),
                imgView);
        imgView.setImage(gc.getImg());

        btnSave.setOnAction(event -> dungeonViewer.saveWorld());

        gc.getTimer().start();
    }

}
