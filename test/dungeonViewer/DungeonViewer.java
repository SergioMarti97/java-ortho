package dungeonViewer;

import dungeonViewer.builders.BlockBuilder;
import dungeonViewer.io.UtilsBlockWorld;
import engine3d.Perspective;
import engine3d.PipeLine;
import engine3d.RenderFlags;
import engine3d.matrix.Mat4x4;
import engine3d.matrix.MatrixMath;
import engine3d.mesh.Triangle;
import engine3d.vectors.Vec4df;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
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

    private final ReadOnlyStringWrapper stringMouseWorld = new ReadOnlyStringWrapper(this, "mouse pos", "Mouse position: ");

    private Vec2di mouseWorld;

    private Mat4x4 matProj;

    @Override
    public void initialize(GameContainer gc) {
        pipeLine = new PipeLine(gc.getRenderer().getP(), gc.getRenderer().getW(), gc.getRenderer().getH());
        pipeLine.setPerspective(Perspective.NORMAL);
        pipeLine.getRenderer3D().setRenderFlag(RenderFlags.RENDER_FULL_TEXTURED);

        translation = new Vec4df();
        rotation = new Vec4df();

        imageTile = new ImageTile("dungeon/dg_features32_sorted.png", 32, 32);

        world = UtilsBlockWorld.readSceneFromFile(path);

        pointer = BlockBuilder.buildBlock(new Vec2di(0, 9));
        pointer.transform(MatrixMath.matrixMakeScale(1.01f, 1.01f, 1.01f));
        pointer.setPosition(new Vec2di(world.getSize().getX() / 2, world.getSize().getY() / 2));


        pipeLine.setCameraOrigin(new Vec4df(world.getSize().getX() / 2.0f, 5.0f, -10.0f));
        //pipeLine.getCameraObj().rotX(0.230f);

        mouseWorld = new Vec2di();

        matProj = MatrixMath.matrixMakeProjection(
                90.0f,
                (float)gc.getRenderer().getH() / (float) gc.getRenderer().getW(),
                0.1f,
                1000.0f);
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

    /**
     * This method updates the position of the pointer
     * @param gc the object GameContainer with the Input Object
     */
    private void updatePointerPosition(GameContainer gc) {
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
    }

    /**
     * This method updates the world rotation in y and x axis
     * @param gc the game container with the Input Object
     * @param elapsedTime the elapsed time between frames
     */
    private void updateWorldRotation(GameContainer gc, float elapsedTime) {
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
    }

    private Vec4df calPlaneN(Triangle triangle) {
        Vec4df vec1 = MatrixMath.vectorSub(triangle.getP()[1], triangle.getP()[0]);
        Vec4df vec2 = MatrixMath.vectorSub(triangle.getP()[2], triangle.getP()[0]);
        return MatrixMath.vectorCrossProduct(vec1, vec2);
    }

    private Vec4df calPlaneP(Vec4df p, Vec4df n) {
        Vec4df diff = MatrixMath.vectorMul(p, -1);
        float sum = MatrixMath.vectorDotProduct(diff, n);
        if ( sum != 0 ) {
            return new Vec4df(
                    n.getX() / sum,
                    n.getY() / sum,
                    n.getZ() / sum
            );
        } else {
            return n;
        }
    }

    private Vec4df calPlaneP(Triangle triangle) {
        return calPlaneP(triangle.getP()[0], calPlaneN(triangle));
    }

    private Vec4df calculateMouseWorldPosition3d(float screenWidth, float screenHeight, float mouseX, float mouseY) {
        // Create a point at matrix, if you recall, this is the inverse of the look at matrix used by the camera
        Mat4x4 matView = MatrixMath.matrixPointAt(
                pipeLine.getCameraObj().getOrigin(),
                pipeLine.getCameraObj().getLookDirection(),
                pipeLine.getCameraObj().getUp());

        // Assume the origin of the mouse ray is the middle of the screen...
        Vec4df mouseOrigin = new Vec4df();

        // ... and then a ray is cast to the mouse location from the origin, here we translate
        // the mouse coordinates into viewport coordinates
        Vec4df mouseDir = new Vec4df(
                2.0f * ((mouseX / screenWidth) - 0.5f) / matProj.getM()[0][0],
                2.0f * ((mouseY / screenHeight) - 0.5f) / matProj.getM()[1][1],
                1.0f,
                0.0f);

        Mat4x4 inverseProjectionMatrix = MatrixMath.matrixQuickInverse(matProj);

        mouseOrigin = MatrixMath.matrixMultiplyVector(inverseProjectionMatrix, mouseOrigin);
        mouseDir = MatrixMath.matrixMultiplyVector(inverseProjectionMatrix, mouseDir);

        // Now transform the origin point and ray direction by the inverse of the camera
        mouseOrigin = MatrixMath.matrixMultiplyVector(matView, mouseOrigin);
        mouseDir = MatrixMath.matrixMultiplyVector(matView, mouseDir);

        // Extend the mouse ray to large length
        mouseDir = MatrixMath.vectorMul(mouseDir, 1000.0f);

        // Offset the mouse ray by the mouse origin
        mouseDir = MatrixMath.vectorAdd(mouseOrigin, mouseDir);

        // All of our intersections for mouse checks occur in the ground plane (z = 0), so
        // define a plane at that location
        //Triangle tri = world.getBlock( 0, 0).getWalls().get( CellSide.BOTTOM).getWall().getTris().get(0);
        //Vec4df planeP = tri.getP()[0];
        Vec4df planeP = calPlaneP(world.getBlock(0, 0).getWalls().get(CellSide.BOTTOM).getWall().getTris().get(0));
        Vec4df planeN = calPlaneN(world.getBlock(0, 0).getWalls().get(CellSide.BOTTOM).getWall().getTris().get(0));
        //Vec4df planeP = new Vec4df();
        //Vec4df planeN = new Vec4df(0.0f, 0.0f, 1.0f);

        // Calculate Mouse Location in plane, by doing a line/plane intersection test
        return MatrixMath.vectorIntersectPlane(planeP, planeN,mouseDir, mouseOrigin);
    }

    private void updateMouseWorldPosition(float screenWidth, float screenHeight, float mouseX, float mouseY, Vec2di mouseWorld) {

        Vec4df mouse3d = calculateMouseWorldPosition3d(screenWidth, screenHeight, mouseX, mouseY);

        mouseWorld.setX(-(int)mouse3d.getZ());
        mouseWorld.setY((int)mouse3d.getX());

        // con el plano calculado a partir del triangulo funciona un poco mejor
        //mouseWorld.setX(-(int)mouse3d.getZ());
        //mouseWorld.setY((int)mouse3d.getX());
    }

    @Override
    public void update(GameContainer gc, float elapsedTime) {
        updateRenderFlags(gc);
        updateCamera(gc, elapsedTime);


        updateMouseWorldPosition(
                (float)gc.getRenderer().getW(),
                (float)gc.getRenderer().getH(),
                (float)gc.getInput().getMouseX(),
                (float)gc.getInput().getMouseY(),
                mouseWorld);
        stringMouseWorld.set(
                "Mouse screen pos: " + gc.getInput().getMouseX() + "x " + gc.getInput().getMouseY() + "y" + '\n' +
                        "Mouse world pos: " + mouseWorld.getX() + "x " + mouseWorld.getY() + "y"
        );

        if ( gc.getInput().isButtonHeld(MouseButton.PRIMARY) ) {
            pointer.setPosition(mouseWorld);
        }

        updatePointerPosition(gc);
        updateWorldRotation(gc, elapsedTime);
        Mat4x4 transMat = MatrixMath.matrixMakeTranslation(translation);
        Mat4x4 rotMat = MatrixMath.matrixMakeRotation(rotation);
        pipeLine.setTransform(MatrixMath.matrixMultiplyMatrix(transMat, rotMat));

        if (gc.getInput().isKeyHeld(KeyCode.SPACE)) {
            Block b = BlockBuilder.buildBlock(new Vec2di(3, 0));
            b.setWall(CellSide.TOP, new Vec2di(3, 3));
            b.getPosition().setX(pointer.getPosition().getX());
            b.getPosition().setY(pointer.getPosition().getY());
            b.transform();
            world.setBlock(pointer.getPosition().getX(), pointer.getPosition().getY(), b);
        }

    }

    private static float cos(float val) {
        //make 3 phase-shifted triangle waves
        float v = Math.abs((val % 2) -1);
        //use cubic beizer curve to approximate the (cos+1)/2 function
        v = v * v * ( 3 - 2 * v );
        return v;
    }

    private static int buildHexColor(int r, int g, int b) {
        return 0xff << 24 | r << 16 | g << 8 | b;
    }

    /**
     * The same as above, but it takes floating point numbers
     */
    private static int buildHexColor(float r, float g, float b) {
        return buildHexColor((int)(255 * r), (int)(255 * g), (int)(255 * b));
    }

    /**
     * Cosine approach
     */
    private static int buildColorCosine(int val, float added) {
        float q = val * 0.1f + added;

        float r = cos(q);
        float g = cos(q + 0.66f);
        float b = cos(q + 1.33f);

        return buildHexColor(r, g, b);
    }

    @Override
    public void render(GameContainer gc) {
        Arrays.fill(gc.getRenderer().getP(), HexColors.DARK_BLUE);

        world.drawWorld(pipeLine, imageTile);
        pointer.render(pipeLine, imageTile);
        pipeLine.getRenderer3D().clearDepthBuffer();

        /*Vec2di mouseTestPos = new Vec2di();
        for ( int y = 0; y < gc.getRenderer().getH(); y++ ) {
            for ( int x = 0; x < gc.getRenderer().getW(); x++ ) {
                updateMouseWorldPosition(gc.getRenderer().getW(), gc.getRenderer().getH(), x, y, mouseTestPos);
                gc.getRenderer().setPixel(x, y, buildColorCosine(mouseTestPos.mag2(), 0));
            }
        }*/

        pipeLine.getCameraObj().showInformation(gc.getRenderer(), 10, 10, HexColors.WHITE);
    }

    public void saveWorld() {
        UtilsBlockWorld.writeSceneToFile(path, world, new Vec2di(imageTile.getTileW(), imageTile.getTileH()));
    }

    public Vec2di getMouseWorld() {
        return mouseWorld;
    }

    public ReadOnlyStringWrapper getStringMouseWorld() {
        return stringMouseWorld;
    }

}
