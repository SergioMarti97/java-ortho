package orthographicViewer;

import olcPGEApproach.vectors.points2d.Vec2di;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a structure
 * of a cell in the world
 * It stores if it is a wall or not,
 * and the images what is going to
 * render when is a wall or floor
 *
 * In future, it must store more information
 * to manages different behaviour,
 * to make the world more interesting
 * such as:
 *  - can it be broken by bombs?
 *  - can it be moved?
 *  - can it emit light?
 *  - etc...
 */
public class Cell {

    /**
     * If the Cell is a wall
     */
    private boolean isWall = false;

    /**
     * The ids to store the sprites
     * It's only needed to store
     * the position of the image on
     * TileImage (which contains all images)
     */
    private HashMap<CellSide, Vec2di> faces;

    /**
     * Null constructor
     */
    public Cell() {
        faces = new HashMap<>();
        setWalls();
    }

    public Cell(boolean isWall) {
        this.isWall = isWall;
        faces = new HashMap<>();
        setWalls();
    }

    public Cell(boolean isWall, Vec2di texCords) {
        this.isWall = isWall;
        faces = new HashMap<>();
        setWalls(texCords);
    }

    /**
     * Parametrized constructor
     */
    public Cell(boolean isWall, HashMap<CellSide, Vec2di> faces) {
        this.isWall = isWall;
        this.faces = faces;
    }

    /**
     * Copy constructor
     * @param c instance to copy
     */
    public Cell(Cell c) {
        this.isWall = c.isWall;
        this.faces = new HashMap<>();
        for (Map.Entry<CellSide, Vec2di> e : c.getFaces().entrySet()) {
            faces.put(e.getKey(), new Vec2di(e.getValue()));
        }
    }


    // Some useful methods
    private void setWalls() {
        faces.put(CellSide.BOTTOM, new Vec2di(0, 0));
        faces.put(CellSide.TOP, new Vec2di(0, 0));
        faces.put(CellSide.WEST, new Vec2di(0, 0));
        faces.put(CellSide.EAST, new Vec2di(0, 0));
        faces.put(CellSide.NORTH, new Vec2di(0, 0));
        faces.put(CellSide.SOUTH, new Vec2di(0, 0));
    }

    private void setWalls(Vec2di texCords) {
        faces.put(CellSide.BOTTOM, new Vec2di(texCords));
        faces.put(CellSide.TOP, new Vec2di(texCords));
        faces.put(CellSide.WEST, new Vec2di(texCords));
        faces.put(CellSide.EAST, new Vec2di(texCords));
        faces.put(CellSide.NORTH, new Vec2di(texCords));
        faces.put(CellSide.SOUTH, new Vec2di(texCords));
    }

    /*
    * Getters & Setters
    */

    public boolean isWall() {
        return isWall;
    }

    public void setWall(boolean wall) {
        isWall = wall;
    }

    public HashMap<CellSide, Vec2di> getFaces() {
        return faces;
    }

    public void setFaces(HashMap<CellSide, Vec2di> faces) {
        this.faces = faces;
    }

    public Vec2di getFace(CellSide cellSide) {
        return faces.get(cellSide);
    }

    public void setFace(CellSide cellSide, Vec2di img) {
        faces.replace(cellSide, img);
    }
}
