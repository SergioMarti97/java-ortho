package orthographicViewer;

import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import maze.MazeViewer;
import olcPGEApproach.AbstractGame;
import olcPGEApproach.GameContainer;

import java.net.URL;
import java.util.ResourceBundle;

public class OrthographicViewerController implements Initializable {

    public ImageView imgView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AbstractGame g = new OrthoGraphicViewer2();
        GameContainer gc = new GameContainer(
                g,
                (int)imgView.getFitWidth(),
                (int)imgView.getFitHeight(),
                imgView
        );
        imgView.setImage(gc.getImg());
        gc.getTimer().setShowFPSOnConsole(true);
        gc.getTimer().start();
    }

}
