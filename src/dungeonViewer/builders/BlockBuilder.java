package dungeonViewer.builders;

import dungeonViewer.Block;
import dungeonViewer.Wall;
import olcPGEApproach.vectors.points2d.Vec2di;
import orthographicViewer.CellSide;

import java.util.HashMap;

/**
 * This class contains the methods and
 * data needed for build default blocks
 */
public class BlockBuilder {

    public static Block buildGround(Vec2di texturePos) {
        HashMap<CellSide, Wall> walls = new HashMap<>();
        walls.put(CellSide.BOTTOM, WallBuilder.buildWall(CellSide.BOTTOM, texturePos));
        return new Block(walls, false);
    }

    public static Block buildWalls(Vec2di texturePos) {
        HashMap<CellSide, Wall> walls = new HashMap<>();
        walls.put(CellSide.NORTH, WallBuilder.buildWall(CellSide.NORTH, texturePos));
        walls.put(CellSide.SOUTH, WallBuilder.buildWall(CellSide.SOUTH, texturePos));
        walls.put(CellSide.EAST, WallBuilder.buildWall(CellSide.EAST, texturePos));
        walls.put(CellSide.WEST, WallBuilder.buildWall(CellSide.WEST, texturePos));
        return new Block(walls, true);
    }

    public static Block buildBlock(Vec2di texturePos) {
        HashMap<CellSide, Wall> walls = new HashMap<>();
        walls.put(CellSide.TOP, WallBuilder.buildWall(CellSide.TOP, texturePos));
        walls.put(CellSide.NORTH, WallBuilder.buildWall(CellSide.NORTH, texturePos));
        walls.put(CellSide.SOUTH, WallBuilder.buildWall(CellSide.SOUTH, texturePos));
        walls.put(CellSide.EAST, WallBuilder.buildWall(CellSide.EAST, texturePos));
        walls.put(CellSide.WEST, WallBuilder.buildWall(CellSide.WEST, texturePos));
        return new Block(walls, true);
    }

    public static Block buildFullBlock(Vec2di texturePos) {
        HashMap<CellSide, Wall> walls = new HashMap<>();
        walls.put(CellSide.TOP, WallBuilder.buildWall(CellSide.TOP, texturePos));
        walls.put(CellSide.BOTTOM, WallBuilder.buildWall(CellSide.BOTTOM, texturePos));
        walls.put(CellSide.NORTH, WallBuilder.buildWall(CellSide.NORTH, texturePos));
        walls.put(CellSide.SOUTH, WallBuilder.buildWall(CellSide.SOUTH, texturePos));
        walls.put(CellSide.EAST, WallBuilder.buildWall(CellSide.EAST, texturePos));
        walls.put(CellSide.WEST, WallBuilder.buildWall(CellSide.WEST, texturePos));
        return new Block(walls, true);
    }

}
