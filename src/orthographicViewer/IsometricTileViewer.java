package orthographicViewer;

import olcPGEApproach.gfx.HexColors;
import olcPGEApproach.gfx.Renderer;
import olcPGEApproach.gfx.images.Image;
import olcPGEApproach.gfx.images.ImageTile;
import olcPGEApproach.vectors.points2d.Vec2df;
import olcPGEApproach.vectors.points2d.Vec2di;
import olcPGEApproach.vectors.points3d.Vec3df;

import java.util.ArrayList;
import java.util.HashMap;

public class IsometricTileViewer {

    private final IsometricCamera camera;

    private ImageTile imgTile;

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

    public IsometricTileViewer(Vec2df cameraPos, float cameraAngle, float cameraPitch, float cameraZoom, ImageTile imgTile) {
        camera = new IsometricCamera(cameraPos, cameraAngle, cameraPitch, cameraZoom);
        this.imgTile = imgTile;
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
            World world,
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

    private void calFacesNormals(Vec3df[] cube, int x, int y, World w) {
        //calFaceVisibilityOptimization(x, y, w);
        isVisible.replace(CellSide.BOTTOM, checkNormal(cube, 4, 0, 1));
        isVisible.replace(CellSide.NORTH, checkNormal(cube, 6, 5, 4));
        isVisible.replace(CellSide.SOUTH, checkNormal(cube, 3, 0, 1));
        isVisible.replace(CellSide.EAST, checkNormal(cube, 7, 4, 0));
        isVisible.replace(CellSide.WEST, checkNormal(cube, 2, 1, 5));
        isVisible.replace(CellSide.TOP, checkNormal(cube, 7, 3, 2));
        /*if (isVisible.get(CellSide.BOTTOM)) {
            isVisible.replace(CellSide.BOTTOM, checkNormal(cube, 4, 0, 1));
        }
        if (isVisible.get(CellSide.SOUTH)) {
            isVisible.replace(CellSide.SOUTH, checkNormal(cube, 3, 0, 1));
        }
        if (isVisible.get(CellSide.NORTH)) {
            isVisible.replace(CellSide.NORTH, checkNormal(cube, 6, 5, 4));
        }
        if (isVisible.get(CellSide.EAST)) {
            isVisible.replace(CellSide.EAST, checkNormal(cube, 7, 4, 0));
        }
        if (isVisible.get(CellSide.WEST)) {
            isVisible.replace(CellSide.WEST, checkNormal(cube, 2, 1, 5));
        }
        if (isVisible.get(CellSide.TOP)) {
            isVisible.replace(CellSide.TOP, checkNormal(cube, 7, 3, 2));
        }*/
    }
    
    private void calFaceVisibilityOptimization(int x, int y, World w) {
        // test over 4 directions: SOUTH NORTH EAST WEST
        isVisible.replace(CellSide.SOUTH, (y == 0) ? true : !w.getCell(x, y - 1).isWall());
        isVisible.replace(CellSide.NORTH, (y == w.getSize().getY()) ? true : !w.getCell(x, y + 1).isWall());
        //isVisible.replace(CellSide.EAST, (x == w.getSize().getX()) ? true : !w.getCell(x + 1, y).isWall());
        //isVisible.replace(CellSide.WEST, (x == 0) ? true : !w.getCell(x - 1, y).isWall());
    }
    
    public void render(Renderer r, World w, boolean drawTriangles) {
        camera.updatePos3d();

        Vec3df[] cullCube = createCube(
                posCullCube,
                camera.getAngle(),
                camera.getPitch(),
                camera.getZoom(),
                camera.getPos3d(),
                r.getW(), r.getH());

        quads.clear();
        for ( int y = 0; y < w.getSize().getY(); y++ ) {
            for ( int x = 0; x < w.getSize().getX(); x++ ) {
                calFacesNormals(cullCube, x, y, w);
                getFaceQuad(
                        w,
                        new Vec2di(x, y),
                        camera.getAngle(),
                        camera.getPitch(),
                        camera.getZoom(),
                        camera.getPos3d(),
                        quads,
                        r.getW(),
                        r.getH());
            }
        }

        quads.sort((q1, q2) -> {
            float z1 = (q1.getPoints()[0].getZ() + q1.getPoints()[1].getZ() + q1.getPoints()[2].getZ() + q1.getPoints()[3].getZ()) * 0.25f;
            float z2 = (q2.getPoints()[0].getZ() + q2.getPoints()[1].getZ() + q2.getPoints()[2].getZ() + q2.getPoints()[3].getZ()) * 0.25f;
            // z1 < z2
            return Float.compare(z2, z1);
        });

        r.clear(HexColors.BLACK);

        for (Quad q : quads) {
            r.drawWarpedDecal(
                    q.getImg(),
                    new Vec2df[] {
                            new Vec2df(q.getPoints()[0].getX(), q.getPoints()[0].getY()),
                            new Vec2df(q.getPoints()[1].getX(), q.getPoints()[1].getY()),
                            new Vec2df(q.getPoints()[2].getX(), q.getPoints()[2].getY()),
                            new Vec2df(q.getPoints()[3].getX(), q.getPoints()[3].getY()),
                    },
                    drawTriangles);
        }
    }

    public void render(Renderer r, World w, Vec2di cursor, Image img, boolean drawTriangles) {
        quads.clear();

        getFaceQuad(w, cursor,
                camera.getAngle(),
                camera.getPitch(),
                camera.getZoom(),
                camera.getPos3d(),
                quads,
                r.getW(), r.getH());
        for (Quad q : quads) {
            r.drawWarpedDecal(
                    img,
                    new Vec2df[] {
                            new Vec2df(q.getPoints()[0].getX(), q.getPoints()[0].getY()),
                            new Vec2df(q.getPoints()[1].getX(), q.getPoints()[1].getY()),
                            new Vec2df(q.getPoints()[2].getX(), q.getPoints()[2].getY()),
                            new Vec2df(q.getPoints()[3].getX(), q.getPoints()[3].getY()),
                    },
                    drawTriangles);
        }
    }

    // Getters

    public IsometricCamera getCamera() {
        return camera;
    }

    public ImageTile getImgTile() {
        return imgTile;
    }

    // Setters

    public IsometricTileViewer setImgTile(ImageTile imgTile) {
        this.imgTile = imgTile;
        return this;
    }

}
