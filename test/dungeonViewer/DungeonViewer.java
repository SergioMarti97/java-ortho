package dungeonViewer;

import dungeonViewer.builders.BlockBuilder;
import dungeonViewer.io.UtilsBlockWorld;
import engine3d.Perspective;
import engine3d.PipeLine;
import engine3d.RenderFlags;
import engine3d.matrix.Mat4x4;
import engine3d.matrix.MatrixMath;
import engine3d.vectors.Vec4df;
import javafx.scene.input.KeyCode;
import olcPGEApproach.AbstractGame;
import olcPGEApproach.GameContainer;
import olcPGEApproach.gfx.HexColors;
import olcPGEApproach.gfx.images.ImageTile;
import olcPGEApproach.vectors.points2d.Vec2di;
import orthographicViewer.CellSide;

import java.util.Arrays;

public class DungeonViewer implements AbstractGame {

    private final String path = "C:\\Users\\Sergio\\IdeaProjects\\JAVAFX\\Javafx-DungeonOrthographicViewer\\testresources\\dungeon\\sMartiTo_World.txt";

    private PipeLine pipeLine;

    private BlockWorld world;

    private ImageTile imageTile;

    private Block pointer;

    private Vec4df translation;

    private Vec4df rotation;

    @Override
    public void initialize(GameContainer gc) {
        pipeLine = new PipeLine(gc.getRenderer().getP(), gc.getRenderer().getW(), gc.getRenderer().getH());
        pipeLine.setPerspective(Perspective.ORTHOGONAL);
        pipeLine.getRenderer3D().setRenderFlag(RenderFlags.RENDER_FULL_TEXTURED);

        translation = new Vec4df();
        rotation = new Vec4df();

        imageTile = new ImageTile("dungeon/dg_features32.png", 32, 32);

        world = UtilsBlockWorld.readSceneFromFile(path);

        pointer = BlockBuilder.buildBlock(new Vec2di(0, 0));
        pointer.setPosition(new Vec2di());

        pipeLine.setCameraOrigin(new Vec4df(world.getSize().getX() / 2.0f, 5.0f, -10.0f));
        pipeLine.getCameraObj().rotX(0.230f);
    }

    private void updateCameraPosition(GameContainer gc, float dt) {
        float vel = 12.5f;
        if ( gc.getInput().isKeyHeld(KeyCode.UP) ) {
            pipeLine.getCameraObj().getOrigin().addToX(-(float)Math.sin(pipeLine.getCameraObj().getCameraRot().getY()) * vel * dt);
            pipeLine.getCameraObj().getOrigin().addToZ((float)Math.cos(pipeLine.getCameraObj().getCameraRot().getY()) * vel * dt);
        }
        if ( gc.getInput().isKeyHeld(KeyCode.DOWN) ) {
            pipeLine.getCameraObj().getOrigin().addToX((float)Math.sin(pipeLine.getCameraObj().getCameraRot().getY()) * vel * dt);
            pipeLine.getCameraObj().getOrigin().addToZ(-(float)Math.cos(pipeLine.getCameraObj().getCameraRot().getY()) * vel * dt);
        }
        if ( gc.getInput().isKeyHeld(KeyCode.RIGHT) ) {
            pipeLine.getCameraObj().getOrigin().addToX(-vel * dt);
        }
        if ( gc.getInput().isKeyHeld(KeyCode.LEFT) ) {
            pipeLine.getCameraObj().getOrigin().addToX(vel * dt);
        }
    }

    /**
     * This method manages the user input for camera rotation
     * @param gc the GameContainer object with te Input object needed to manage user inputs
     * @param dt the elapsed time between frames
     */
    private void updateCameraRotation(GameContainer gc, float dt) {
        if ( gc.getInput().isKeyHeld(KeyCode.W) ) {
            pipeLine.getCameraObj().rotX(-2.0f * dt);
        }
        if ( gc.getInput().isKeyHeld(KeyCode.S) ) {
            pipeLine.getCameraObj().rotX(2.0f * dt);
        }
        if ( gc.getInput().isKeyHeld(KeyCode.A) ) {
            pipeLine.getCameraObj().rotY(-2.0f * dt);
        }
        if ( gc.getInput().isKeyHeld(KeyCode.D) ) {
            pipeLine.getCameraObj().rotY(2.0f * dt);
        }
    }

    /**
     * This method manage the user input for camera zooming
     * @param gc the GameContainer object with te Input object needed to manage user inputs
     * @param dt the elapsed time between frames
     */
    private void updateCameraZoom(GameContainer gc, float dt) {
        if ( gc.getInput().isKeyHeld(KeyCode.Q) ) {
            Vec4df forward = MatrixMath.vectorMul(pipeLine.getCameraObj().getLookDirection(), + 12.5f * dt);
            pipeLine.getCameraObj().setOrigin(MatrixMath.vectorAdd(pipeLine.getCameraObj().getOrigin(), forward));
        }
        if ( gc.getInput().isKeyHeld(KeyCode.Z) ) {
            Vec4df forward = MatrixMath.vectorMul(pipeLine.getCameraObj().getLookDirection(),  - 12.5f * dt);
            pipeLine.getCameraObj().setOrigin(MatrixMath.vectorAdd(pipeLine.getCameraObj().getOrigin(), forward));
        }

    }

    /**
     * This method manages the user input for control the camera
     * @param gc the game container object with the input object
     * @param dt the elapsed time between frames
     */
    private void updateCamera(GameContainer gc, float dt) {
        updateCameraPosition(gc, dt);
        updateCameraRotation(gc, dt);
        updateCameraZoom(gc, dt);
        pipeLine.setMatView(pipeLine.getCameraObj().getMatView());
    }

    /**
     * This method manages the user inputs for render flags
     * @param gc the object GameContainer with the Input Object
     */
    private void updateRenderFlags(GameContainer gc) {
        if ( gc.getInput().isKey(KeyCode.NUMPAD0) ) {
            pipeLine.getRenderer3D().setRenderFlag(RenderFlags.RENDER_WIRE);
        }
        if ( gc.getInput().isKey(KeyCode.NUMPAD1) ) {
            pipeLine.getRenderer3D().setRenderFlag(RenderFlags.RENDER_FLAT);
        }
        if ( gc.getInput().isKey(KeyCode.NUMPAD2) ) {
            pipeLine.getRenderer3D().setRenderFlag(RenderFlags.RENDER_SMOOTH_FLAT);
        }
        if ( gc.getInput().isKey(KeyCode.NUMPAD3) ) {
            pipeLine.getRenderer3D().setRenderFlag(RenderFlags.RENDER_TEXTURED);
        }
        if ( gc.getInput().isKey(KeyCode.NUMPAD4) ) {
            pipeLine.getRenderer3D().setRenderFlag(RenderFlags.RENDER_FULL_TEXTURED);
        }
        if ( gc.getInput().isKey(KeyCode.NUMPAD5) ) {
            if ( pipeLine.getPerspective() == Perspective.NORMAL ) {
                pipeLine.setPerspective(Perspective.ORTHOGONAL);
            }
            if ( pipeLine.getPerspective() == Perspective.ORTHOGONAL ) {
                pipeLine.setPerspective(Perspective.NORMAL);
            }
        }
    }

    @Override
    public void update(GameContainer gc, float elapsedTime) {
        updateCamera(gc, elapsedTime);
        updateRenderFlags(gc);

        if (gc.getInput().isKeyHeld(KeyCode.J)) {
            pointer.getPosition().addToX(1);
            if ( pointer.getPosition().getX() > world.getSize().getX() ) {
                pointer.getPosition().setX(world.getSize().getX());
            }
            translation.setX(-pointer.getPosition().getX() - 0.5f);
        }
        if (gc.getInput().isKeyHeld(KeyCode.L)) {
            pointer.getPosition().addToX(-1);
            if ( pointer.getPosition().getX() < 0 ) {
                pointer.getPosition().setX(0);
            }
            translation.setX(-pointer.getPosition().getX() - 0.5f);
        }
        if (gc.getInput().isKeyHeld(KeyCode.I)) {
            pointer.getPosition().addToY(1);
            if ( pointer.getPosition().getY() > world.getSize().getY() ) {
                pointer.getPosition().setY(world.getSize().getY());
            }
            translation.setZ(-pointer.getPosition().getY() - 0.5f);
        }
        if (gc.getInput().isKeyHeld(KeyCode.K)) {
            pointer.getPosition().addToY(-1);
            if ( pointer.getPosition().getY() < 0 ) {
                pointer.getPosition().setY(0);
            }
            translation.setZ(-pointer.getPosition().getY() - 0.5f);
        }

        if ( gc.getInput().isKeyHeld(KeyCode.E) ) {
            rotation.addToY(1.5f * elapsedTime);
        }
        if ( gc.getInput().isKeyHeld(KeyCode.R) ) {
            rotation.addToY(-1.5f * elapsedTime);
        }
        if ( gc.getInput().isKeyHeld(KeyCode.F) ) {
            rotation.addToX(1.5f * elapsedTime);
        }
        if ( gc.getInput().isKeyHeld(KeyCode.G) ) {
            rotation.addToX(-1.5f * elapsedTime);
        }

        if (gc.getInput().isKeyHeld(KeyCode.SPACE)) {
            Block b = BlockBuilder.buildBlock(new Vec2di(3, 0));
            b.setWall(CellSide.TOP, new Vec2di(3, 3));
            b.getPosition().setX(pointer.getPosition().getX());
            b.getPosition().setY(pointer.getPosition().getY());
            b.transform();
            world.setBlock(pointer.getPosition().getX(), pointer.getPosition().getY(), b);
        }

        Mat4x4 transMat = MatrixMath.matrixMakeTranslation(translation);
        Mat4x4 rotMat = MatrixMath.matrixMakeRotation(rotation);
        pipeLine.setTransform(MatrixMath.matrixMultiplyMatrix(transMat, rotMat));
    }

    @Override
    public void render(GameContainer gc) {
        Arrays.fill(gc.getRenderer().getP(), HexColors.DARK_BLUE);

        world.drawWorld(pipeLine, imageTile);
        pointer.render(pipeLine, imageTile);
        pipeLine.getRenderer3D().clearDepthBuffer();

        //pipeLine.getCameraObj().showInformation(gc.getRenderer(), 10, 10, HexColors.WHITE);
    }

    public void saveWorld() {
        UtilsBlockWorld.writeSceneToFile(path, world, new Vec2di(imageTile.getTileW(), imageTile.getTileH()));
    }

}
