package orthographicViewer;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import olcPGEApproach.AbstractGame;
import olcPGEApproach.GameContainer;
import olcPGEApproach.gfx.HexColors;
import olcPGEApproach.gfx.images.Image;
import olcPGEApproach.gfx.images.ImageTile;
import olcPGEApproach.vectors.points2d.Vec2df;
import olcPGEApproach.vectors.points2d.Vec2di;
import olcPGEApproach.vectors.points3d.Vec3df;

import java.util.ArrayList;
import java.util.HashMap;

public class OrthographicViewer implements AbstractGame {

    private final String path = "C:\\Users\\Sergio\\IdeaProjects\\JAVAFX\\Javafx-DungeonOrthographicViewer\\testresources\\dungeon\\sMartiTo_World2.txt";

    private World world;

    private Vec2df cameraPos;

    private Vec3df cameraPos3d;

    private float cameraAngle;

    private float cameraAngleTarget;

    private float cameraPitch;

    private float cameraZoom;

    private Vec2di cursor;

    private Image imgSelect;

    private final Vec2di posCullCube = new Vec2di();

    private final HashMap<CellSide, Boolean> isVisible = new HashMap<CellSide, Boolean>() {{
        put(CellSide.BOTTOM, false);
        put(CellSide.SOUTH, false);
        put(CellSide.NORTH, false);
        put(CellSide.EAST, false);
        put(CellSide.WEST, false);
        put(CellSide.TOP, false);
    }};

    private final ArrayList<Quad> quads = new ArrayList<>();

    private ImageTile imgTile;

    private final Vec2di tileCursor = new Vec2di();

    private boolean showImageTile = false;

    @Override
    public void initialize(GameContainer gc) {
        imgTile = new ImageTile("/dungeon/dg_features32_sorted.png", 32, 32);

        imgSelect = imgTile.getTileImage(0, 9);

        world = UtilsCellWorld.readWorldFromFile(path);

        cameraPos = new Vec2df();
        cameraPos3d = new Vec3df();
        cameraAngle = 0.0f;
        cameraAngleTarget = cameraAngle;
        cameraPitch = 5.5f;
        cameraZoom = 16.0f;

        cursor = new Vec2di();
    }

    private Vec3df[] createCube(
            Vec2di cellPos,
            float angle,
            float pitch,
            float scale,
            Vec3df camera,
            int w,
            int h) {
        final int numVertex = 8;
        // Unit cube
        Vec3df[] unitCube, rotCube, worldCube, projCube;

        unitCube = new Vec3df[numVertex];
        unitCube[0] = new Vec3df(0.0f, 0.0f, 0.0f);
        unitCube[1] = new Vec3df(scale, 0.0f, 0.0f);
        unitCube[2] = new Vec3df(scale, -scale, 0.0f);
        unitCube[3] = new Vec3df(0.0f, -scale, 0.0f);
        unitCube[4] = new Vec3df(0.0f, 0.0f, scale);
        unitCube[5] = new Vec3df(scale, 0.0f, scale);
        unitCube[6] = new Vec3df(scale, -scale, scale);
        unitCube[7] = new Vec3df(0.0f, -scale, scale);

        // Translate the cube in X-Z Plane
        for ( int i = 0; i < numVertex; i++ ) {
            unitCube[i].addToX(cellPos.getX() * scale - camera.getX());
            unitCube[i].addToY(- camera.getY());
            unitCube[i].addToZ(cellPos.getY() * scale - camera.getZ());
        }

        // Rotate cube in Y-Axis around the origin
        rotCube = new Vec3df[numVertex];
        float s = (float)Math.sin(angle);
        float c = (float)Math.cos(angle);
        for ( int i = 0; i < numVertex; i++ ) {
            rotCube[i] = new Vec3df();
            rotCube[i].setX(unitCube[i].getX() * c + unitCube[i].getZ() * s);
            rotCube[i].setY(unitCube[i].getY());
            rotCube[i].setZ(unitCube[i].getX() * -s + unitCube[i].getZ() * c);
        }

        worldCube = new Vec3df[numVertex];
        s = (float)Math.sin(pitch);
        c = (float)Math.cos(pitch);
        for ( int i = 0; i < numVertex; i++ ) {
            worldCube[i] = new Vec3df();
            worldCube[i].setX(rotCube[i].getX());
            worldCube[i].setY(rotCube[i].getY() * c - rotCube[i].getZ() * s);
            worldCube[i].setZ(rotCube[i].getY() * s - rotCube[i].getZ() * c);
        }

        projCube = new Vec3df[numVertex];
        for ( int i = 0; i < numVertex; i++ ) {
            projCube[i] = new Vec3df();
            projCube[i].setX(worldCube[i].getX() + w * 0.5f);
            projCube[i].setY(worldCube[i].getY() + h * 0.5f);
            projCube[i].setZ(worldCube[i].getZ());
        }

        return projCube;
    }

    private void makeFace(
            int v1,
            int v2,
            int v3,
            int v4,
            Vec3df[] projCube,
            Cell cell,
            CellSide side,
            ArrayList<Quad> render
    ) {
        Vec3df[] points = new Vec3df[] { projCube[v1], projCube[v2], projCube[v3], projCube[v4]};
        Vec2di textPos = cell.getFace(side);
        render.add(new Quad(points, imgTile.getTileImage(textPos.getX(), textPos.getY())));
    }

    private void getFaceQuad(
            Vec2di cellPos,
            float angle,
            float pitch,
            float scale,
            Vec3df camera,
            ArrayList<Quad> render,
            int w, int h) {

        Vec3df[] projCube = createCube(cellPos, angle, pitch, scale, camera, w, h);
        Cell cell = world.getCell(cellPos);

        if (!cell.isWall()) {
            if (isVisible.get(CellSide.BOTTOM)) {
                makeFace(4, 0, 1, 5, projCube, cell, CellSide.BOTTOM, render);
            }
        } else {
            if (isVisible.get(CellSide.SOUTH)) {
                makeFace(3, 0, 1, 2, projCube, cell, CellSide.SOUTH, render);
            }
            if (isVisible.get(CellSide.NORTH)) {
                makeFace(6, 5, 4, 7, projCube, cell, CellSide.NORTH, render);
            }
            if (isVisible.get(CellSide.EAST)) {
                makeFace(7, 4, 0, 3, projCube, cell, CellSide.EAST, render);
            }
            if (isVisible.get(CellSide.WEST)) {
                makeFace(2, 1, 5, 6, projCube, cell, CellSide.WEST, render);
            }
            if (isVisible.get(CellSide.TOP)) {
                makeFace(7, 3, 2, 6, projCube, cell, CellSide.TOP, render);
            }
        }

    }

    private boolean checkNormal(Vec3df[] cube, int v1, int v2, int v3) {
        // return (b - a).cross(c - a) > 0
        // return this.x * vec.getY() - this.y * vec.getX();
        float x1 = cube[v2].getX() - cube[v1].getX(); // (b.x - a.x)
        float y1 = cube[v2].getY() - cube[v1].getY(); // (b.y - a.y)
        float x2 = cube[v3].getX() - cube[v1].getX(); // (c.x - a.x)
        float y2 = cube[v3].getY() - cube[v1].getY(); // (c.y - a.y)
        return (x1 * y2 - y1 * x2) > 0;
    }

    private void calculateVisibleFaces(Vec3df[] cube) {
        isVisible.replace(CellSide.BOTTOM, checkNormal(cube, 4, 0, 1));
        isVisible.replace(CellSide.SOUTH, checkNormal(cube, 3, 0, 1));
        isVisible.replace(CellSide.NORTH, checkNormal(cube, 6, 5, 4));
        isVisible.replace(CellSide.EAST, checkNormal(cube, 7, 4, 0));
        isVisible.replace(CellSide.WEST, checkNormal(cube, 2, 1, 5));
        isVisible.replace(CellSide.TOP, checkNormal(cube, 7, 3, 2));
    }

    @Override
    public void update(GameContainer gc, float elapsedTime) {
        if (gc.getInput().isKeyDown(KeyCode.ENTER)) {
            System.out.println("¡Guardado!");
            UtilsCellWorld.writeWorldToFile(path, world, 32, 32);
        }

        if (gc.getInput().isKeyHeld(KeyCode.TAB)) {
            showImageTile = true;
        }

        if (gc.getInput().isKeyUp(KeyCode.TAB)) {
            showImageTile = false;
        }

        if (showImageTile) {
            if (gc.getInput().isButtonDown(MouseButton.PRIMARY)) {
                tileCursor.setX((int)gc.getInput().getMouseX() / 32);
                tileCursor.setY((int)gc.getInput().getMouseY() / 32);
            }
        }

        if ( gc.getInput().isKeyHeld(KeyCode.W) ) {
            cameraPitch += elapsedTime;
        }
        if ( gc.getInput().isKeyHeld(KeyCode.S) ) {
            cameraPitch -= elapsedTime;
        }

        if ( gc.getInput().isKeyHeld(KeyCode.D) ) {
            cameraAngleTarget += elapsedTime;
        }
        if ( gc.getInput().isKeyHeld(KeyCode.A) ) {
            cameraAngleTarget -= elapsedTime;
        }

        if ( gc.getInput().isKeyHeld(KeyCode.Q) ) {
            cameraZoom += 5.0f * elapsedTime;
        }
        if ( gc.getInput().isKeyHeld(KeyCode.Z) ) {
            cameraZoom -= 5.0f * elapsedTime;
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

        cameraAngle += (cameraAngleTarget - cameraAngle) * 10.0f * elapsedTime;

        // Numeric keys apply selected tile to specific face
        if (gc.getInput().isKeyDown(KeyCode.DIGIT1)) world.getCell(cursor).setFace(CellSide.NORTH, new Vec2di(tileCursor));
        if (gc.getInput().isKeyDown(KeyCode.DIGIT2)) world.getCell(cursor).setFace(CellSide.EAST, new Vec2di(tileCursor));
        if (gc.getInput().isKeyDown(KeyCode.DIGIT3)) world.getCell(cursor).setFace(CellSide.SOUTH, new Vec2di(tileCursor));
        if (gc.getInput().isKeyDown(KeyCode.DIGIT4)) world.getCell(cursor).setFace(CellSide.WEST, new Vec2di(tileCursor));
        if (gc.getInput().isKeyDown(KeyCode.DIGIT5)) world.getCell(cursor).setFace(CellSide.BOTTOM, new Vec2di(tileCursor));
        if (gc.getInput().isKeyDown(KeyCode.DIGIT6)) world.getCell(cursor).setFace(CellSide.TOP, new Vec2di(tileCursor));


        cameraPos.setX(cursor.getX() + 0.5f);
        cameraPos.setY(cursor.getY() + 0.5f);
        cameraPos.multiply(cameraZoom);

        cameraPos3d.setX(cameraPos.getX());
        cameraPos3d.setY(0);
        cameraPos3d.setZ(cameraPos.getY());

        if ( gc.getInput().isKeyDown(KeyCode.SPACE) ) {
            world.getCell(cursor).setWall(!world.getCell(cursor).isWall());
        }
    }

    @Override
    public void render(GameContainer gc) {

        quads.clear();

        Vec3df[] cullCube = createCube(posCullCube, cameraAngle, cameraPitch, cameraZoom, cameraPos3d, gc.getRenderer().getW(), gc.getRenderer().getH());
        calculateVisibleFaces(cullCube);

        for ( int y = 0; y < world.getSize().getY(); y++ ) {
            for ( int x = 0; x < world.getSize().getX(); x++ ) {
                getFaceQuad(
                        new Vec2di(x, y),
                        cameraAngle,
                        cameraPitch,
                        cameraZoom,
                        cameraPos3d,
                        quads,
                        gc.getRenderer().getW(),
                        gc.getRenderer().getH());
            }
        }

        quads.sort((q1, q2) -> {
            float z1 = (q1.getPoints()[0].getZ() + q1.getPoints()[1].getZ() + q1.getPoints()[2].getZ() + q1.getPoints()[3].getZ()) * 0.25f;
            float z2 = (q2.getPoints()[0].getZ() + q2.getPoints()[1].getZ() + q2.getPoints()[2].getZ() + q2.getPoints()[3].getZ()) * 0.25f;
            // z1 < z2
            return Float.compare(z2, z1);
        });

        gc.getRenderer().clear(HexColors.BLACK);

        for (Quad q : quads) {
            gc.getRenderer().drawWarpedDecal(
                    q.getImg(),
                    new Vec2df[] {
                            new Vec2df(q.getPoints()[0].getX(), q.getPoints()[0].getY()),
                            new Vec2df(q.getPoints()[1].getX(), q.getPoints()[1].getY()),
                            new Vec2df(q.getPoints()[2].getX(), q.getPoints()[2].getY()),
                            new Vec2df(q.getPoints()[3].getX(), q.getPoints()[3].getY()),
                    },
                    false);
        }

        quads.clear();

        getFaceQuad(cursor, cameraAngle, cameraPitch, cameraZoom, cameraPos3d, quads, gc.getRenderer().getW(), gc.getRenderer().getH());
        for (Quad q : quads) {
            gc.getRenderer().drawWarpedDecal(
                    imgSelect,
                    new Vec2df[] {
                            new Vec2df(q.getPoints()[0].getX(), q.getPoints()[0].getY()),
                            new Vec2df(q.getPoints()[1].getX(), q.getPoints()[1].getY()),
                            new Vec2df(q.getPoints()[2].getX(), q.getPoints()[2].getY()),
                            new Vec2df(q.getPoints()[3].getX(), q.getPoints()[3].getY()),
                    },
                    false);
        }

        gc.getRenderer().drawImage(imgTile.getTileImage(tileCursor.getX(), tileCursor.getY()), 10, 10);

        if (showImageTile) {
            gc.getRenderer().drawImage(imgTile, 10, 10);
            gc.getRenderer().drawRect(
                    tileCursor.getX() * 32 + 10,
                    tileCursor.getY() * 32 + 10,
                    32,
                    32,
                    HexColors.WHITE);
        }

        //gc.getRenderer().drawImage(new Image("/dungeon/dg_features32.png"), 10, 10);
        gc.getRenderer().drawText(cursor.toString(), 10, 10, HexColors.WHITE);

    }

}
