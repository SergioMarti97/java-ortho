package dungeonViewer;

import engine3d.PipeLine;
import engine3d.matrix.Mat4x4;
import engine3d.matrix.MatrixMath;
import olcPGEApproach.gfx.images.ImageTile;
import olcPGEApproach.vectors.points2d.Vec2di;
import orthographicViewer.CellSide;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a block of
 * the world
 * It contains a hash map with the walls
 * a flag to store if the block is a wall
 * or not
 */
public class Block {

    /**
     * If the block is a wall
     */
    private boolean isWall;

    /**
     * The walls of the block
     */
    private HashMap<CellSide, Wall> walls;

    /**
     * The position of the block
     */
    private Vec2di position;

    /**
     * Void constructor
     */
    public Block() {
        walls = new HashMap<>();
    }

    /**
     * Constructor
     * @param isWall the block is a wall?
     * @param walls the walls of the block
     */
    public Block(HashMap<CellSide, Wall> walls, boolean isWall) {
        position = new Vec2di();
        this.isWall = isWall;
        this.walls = walls;
        transform();
    }

    /**
     * Constructor
     * @param worldX the position X on world
     * @param worldY the position Y on world
     * @param isWall the block is a wall?
     * @param walls the walls of the block
     */
    public Block(int worldX, int worldY, HashMap<CellSide, Wall> walls, boolean isWall) {
        position = new Vec2di(worldX, worldY);
        this.isWall = isWall;
        this.walls = walls;
        transform();
    }

    /**
     * This method transform the meshes of the walls
     * to translated them to the x and y coordinates
     * of the block
     */
    public void transform() {
        Mat4x4 matTranslation = MatrixMath.matrixMakeTranslation(position.getX(), 0.0f, position.getY());
        for ( Map.Entry<CellSide, Wall> e : walls.entrySet() ) {
            MatrixMath.transformMesh(matTranslation, e.getValue().getWall());
        }
    }

    /**
     * This method draws the block on screen
     * @param pipeLine the pipeline object to draw 3d graphics
     * @param imageTile the image tile with the images
     */
    public void render(PipeLine pipeLine, ImageTile imageTile) {
        Mat4x4 matTranslation = MatrixMath.matrixMakeTranslation(position.getX(), 0.0f, position.getY());
        pipeLine.setTransform(MatrixMath.matrixMultiplyMatrix(matTranslation, pipeLine.getWorldMatrix()));
        if ( isWall ) {
            for ( Map.Entry<CellSide, Wall> e : walls.entrySet()  ) {
                pipeLine.renderMesh(
                        e.getValue().getWall(),
                        imageTile.getTileImage(
                                e.getValue().getTexturePos().getX(),
                                e.getValue().getTexturePos().getY()
                        )
                );
            }
        } else {
            Wall w = walls.get(CellSide.BOTTOM);
            pipeLine.renderMesh(w.getWall(),
                    imageTile.getTileImage(
                            w.getTexturePos().getX(),
                            w.getTexturePos().getY()
                    )
            );
        }
    }

    /*
    * Getters and Setters
    */

    public void setWall(CellSide side, Vec2di texturePos) {
        if ( walls.containsKey(side) ) {
            walls.get(side).setTexturePos(texturePos);
        }
    }

    public Vec2di getPosition() {
        return position;
    }

    public void setPosition(Vec2di position) {
        this.position = position;
        transform();
    }

    public boolean isWall() {
        return isWall;
    }

    public void setWall(boolean wall) {
        isWall = wall;
    }

    public HashMap<CellSide, Wall> getWalls() {
        return walls;
    }

    public void setWalls(HashMap<CellSide, Wall> walls) {
        this.walls = walls;
    }

}
