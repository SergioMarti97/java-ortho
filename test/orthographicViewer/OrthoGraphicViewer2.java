package orthographicViewer;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import maze.Maze;
import maze.MazeMaker;
import olcPGEApproach.AbstractGame;
import olcPGEApproach.GameContainer;
import olcPGEApproach.gfx.HexColors;
import olcPGEApproach.gfx.images.Image;
import olcPGEApproach.gfx.images.ImageTile;
import olcPGEApproach.vectors.points2d.Vec2df;
import olcPGEApproach.vectors.points2d.Vec2di;

public class OrthoGraphicViewer2 implements AbstractGame {

    private final String path = "C:\\Users\\Sergio\\IdeaProjects\\JAVAFX\\Javafx-DungeonOrthographicViewer\\testresources\\dungeon\\";

    private final String fileName = "sMartiTo_Maze.txt";

    private IsometricTileViewer isoViewer;

    private World world;

    private Maze maze;

    private Cell[] cells;

    private ImageTile imgTile;

    private Image imgSelect;

    private float cameraAngleTarget;

    private final Vec2di cursor = new Vec2di();

    private final Vec2di tileCursor = new Vec2di();

    private boolean showImageTile = false;

    @Override
    public void initialize(GameContainer gameContainer) {
        //world = UtilsCellWorld.readWorldFromFile(path + fileName);

        final int pathWidth = 2;
        final int gap = 1;
        final int fullPathWidth = pathWidth + gap;
        final int mazeW = 16;
        final int mazeH = 16;
        final int worldW = mazeW * fullPathWidth;
        final int worldH = mazeH * fullPathWidth;

        maze = new Maze(mazeW, mazeH, pathWidth, gap);
        MazeMaker.initialize();

        world = new World(worldW, worldH);

        imgTile = new ImageTile("/dungeon/dg_features32_sorted.png", 32, 32);
        imgSelect = imgTile.getTileImage(0, 9);
        isoViewer = new IsometricTileViewer(new Vec2df(), 0.0f, 5.5f, 16.0f, imgTile);
        cameraAngleTarget = isoViewer.getCamera().getAngle();

        Cell visited = new Cell(false, new Vec2di(2, 3));
        visited.getFaces().replace(CellSide.BOTTOM, new Vec2di(1, 12));
        visited.getFaces().replace(CellSide.EAST, new Vec2di(1, 3));
        visited.getFaces().replace(CellSide.NORTH, new Vec2di(1, 3));

        Cell noVisited = new Cell(false, new Vec2di(2, 3));
        noVisited.getFaces().replace(CellSide.BOTTOM, new Vec2di(0, 4));

        Cell wall = new Cell(true, new Vec2di(2, 3));
        wall.getFaces().replace(CellSide.BOTTOM, new Vec2di(0, 3));
        wall.getFaces().replace(CellSide.EAST, new Vec2di(1, 3));
        wall.getFaces().replace(CellSide.NORTH, new Vec2di(1, 3));

        cells = new Cell[] {visited, noVisited, wall};
        //MazeMaker.makeMaze(maze);
        //MazeMaker.translateMaze(maze, world.getCells(), world.getSize().getX(), cells[0], cells[1], cells[2]);

        cursor.setX(world.getSize().getX() / 2);
        cursor.setY(world.getSize().getY() / 2);
    }

    @Override
    public void update(GameContainer gc, float elapsedTime) {
        if (gc.getInput().isKeyDown(KeyCode.ENTER)) {
            System.out.println("¡Guardado!");
            UtilsCellWorld.writeWorldToFile(path + fileName, world, imgTile.getTileW(), imgTile.getTileH());
        }

        if (gc.getInput().isKeyHeld(KeyCode.TAB)) {
            showImageTile = true;
        }

        if (gc.getInput().isKeyUp(KeyCode.TAB)) {
            showImageTile = false;
        }

        if (showImageTile) {
            if (gc.getInput().isButtonDown(MouseButton.PRIMARY)) {
                tileCursor.setX((int)gc.getInput().getMouseX() / imgTile.getTileW());
                tileCursor.setY((int)gc.getInput().getMouseY() / imgTile.getTileH());
            }
        }

        if ( gc.getInput().isKeyHeld(KeyCode.W) ) {
            isoViewer.getCamera().addToPitch(elapsedTime);
        }
        if ( gc.getInput().isKeyHeld(KeyCode.S) ) {
            isoViewer.getCamera().addToPitch(-elapsedTime);
        }

        if ( gc.getInput().isKeyHeld(KeyCode.D) ) {
            cameraAngleTarget += elapsedTime;
        }
        if ( gc.getInput().isKeyHeld(KeyCode.A) ) {
            cameraAngleTarget -= elapsedTime;
        }

        if ( gc.getInput().isKeyHeld(KeyCode.Q) ) {
            isoViewer.getCamera().addToZoom(25.0f * elapsedTime);
        }
        if ( gc.getInput().isKeyHeld(KeyCode.Z) ) {
            isoViewer.getCamera().addToZoom(-25.0f * elapsedTime);
        }

        if ( gc.getInput().isKeyDown(KeyCode.LEFT) ) {
            System.out.println("Has apretado la izquierda");
            cursor.addToX(-1);
        }
        if ( gc.getInput().isKeyDown(KeyCode.RIGHT) ) {
            System.out.println("Has apretado la derecha");
            cursor.addToX(1);
        }
        if ( gc.getInput().isKeyDown(KeyCode.UP) ) {
            System.out.println("Has apretado arriba");
            cursor.addToY(-1);
        }
        if ( gc.getInput().isKeyDown(KeyCode.DOWN) ) {
            System.out.println("Has apretado abajo");
            cursor.addToY(1);
        }
        if ( cursor.getX() < 0 ) {
            cursor.setX(0);
        }
        if ( cursor.getY() < 0 ) {
            cursor.setY(0);
        }
        if ( cursor.getX() >= world.getSize().getX() ) {
            cursor.setX(world.getSize().getX() - 1);
        }
        if ( cursor.getY() >= world.getSize().getY() ) {
            cursor.setY(world.getSize().getY() - 1);
        }

        // Numpad keys used to rotate camera to fixed angles
        if (gc.getInput().isKeyDown(KeyCode.NUMPAD2)) cameraAngleTarget = 3.14159f * 0.0f;
        if (gc.getInput().isKeyDown(KeyCode.NUMPAD1)) cameraAngleTarget = 3.14159f * 0.25f;
        if (gc.getInput().isKeyDown(KeyCode.NUMPAD4)) cameraAngleTarget = 3.14159f * 0.5f;
        if (gc.getInput().isKeyDown(KeyCode.NUMPAD7)) cameraAngleTarget = 3.14159f * 0.75f;
        if (gc.getInput().isKeyDown(KeyCode.NUMPAD8)) cameraAngleTarget = 3.14159f * 1.0f;
        if (gc.getInput().isKeyDown(KeyCode.NUMPAD9)) cameraAngleTarget = 3.14159f * 1.25f;
        if (gc.getInput().isKeyDown(KeyCode.NUMPAD6)) cameraAngleTarget = 3.14159f * 1.5f;
        if (gc.getInput().isKeyDown(KeyCode.NUMPAD3)) cameraAngleTarget = 3.14159f * 1.75f;

        isoViewer.getCamera().addToAngle((cameraAngleTarget - isoViewer.getCamera().getAngle()) * 10.0f * elapsedTime);

        // Numeric keys apply selected tile to specific face
        if (gc.getInput().isKeyDown(KeyCode.DIGIT1)) world.getCell(cursor).setFace(CellSide.NORTH, new Vec2di(tileCursor));
        if (gc.getInput().isKeyDown(KeyCode.DIGIT2)) world.getCell(cursor).setFace(CellSide.EAST, new Vec2di(tileCursor));
        if (gc.getInput().isKeyDown(KeyCode.DIGIT3)) world.getCell(cursor).setFace(CellSide.SOUTH, new Vec2di(tileCursor));
        if (gc.getInput().isKeyDown(KeyCode.DIGIT4)) world.getCell(cursor).setFace(CellSide.WEST, new Vec2di(tileCursor));
        if (gc.getInput().isKeyDown(KeyCode.DIGIT5)) world.getCell(cursor).setFace(CellSide.BOTTOM, new Vec2di(tileCursor));
        if (gc.getInput().isKeyDown(KeyCode.DIGIT6)) world.getCell(cursor).setFace(CellSide.TOP, new Vec2di(tileCursor));

        isoViewer.getCamera().getPos().setX(cursor.getX() + 0.5f);
        isoViewer.getCamera().getPos().setY(cursor.getY() + 0.5f);
        isoViewer.getCamera().getPos().multiply(isoViewer.getCamera().getZoom());

        if ( gc.getInput().isKeyDown(KeyCode.SPACE) ) {
            System.out.println("Has apretado el espacio");
            world.getCell(cursor).setWall(!world.getCell(cursor).isWall());
        }

        MazeMaker.makeMaze1Iteration(maze);
        MazeMaker.translateMaze(maze, world.getCells(), world.getSize().getX(), cells[0], cells[1], cells[2]);
    }

    @Override
    public void render(GameContainer gc) {
        isoViewer.render(gc.getRenderer(), world, false);
        isoViewer.render(gc.getRenderer(), world, cursor, imgSelect, false);

        final Vec2di offset = new Vec2di(10, 10);

        gc.getRenderer().drawImage(imgTile.getTileImage(tileCursor.getX(), tileCursor.getY()), offset.getX(), 4 * offset.getY());

        if (showImageTile) {
            gc.getRenderer().drawImage(imgTile, offset.getX(), offset.getY());
            gc.getRenderer().drawRect(
                    tileCursor.getX() * imgTile.getTileW() + offset.getX(),
                    tileCursor.getY() * imgTile.getTileH() + offset.getY(),
                    imgTile.getTileW(),
                    imgTile.getTileH(),
                    HexColors.WHITE);
        }

        gc.getRenderer().drawText(cursor.toString(), offset.getX(), offset.getY(), HexColors.WHITE);
    }

}
