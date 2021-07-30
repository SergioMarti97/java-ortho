package dungeonViewer.builders;

import dungeonViewer.Block;
import dungeonViewer.BlockWorld;
import olcPGEApproach.vectors.points2d.Vec2di;
import orthographicViewer.CellSide;

/**
 * This class stores the chunk of code to build
 * my world what I was original working
 */
public class WorldBuilder {

    /**
     * default map, useful for debugging
     * It's copied from the javidx9 videos
     */
    public static final String map =
            "################################################################" +
            "#.........#....................##..............................#" +
            "#.........#....................................................#" +
            "#.........#....................................................#" +
            "#.........#....................................................#" +
            "#.........#############........................................#" +
            "#...............#..............................................#" +
            "#...............#..............................................#" +
            "#...............#..............................................#" +
            "#.....#..#..#...#..............................................#" +
            "#...............#..............................................#" +
            "#...............#..............................................#" +
            "#.....#..#..#...........................................####...#" +
            "#..........................................................#...#" +
            "#..........................................................#...#" +
            "#.......................................................#..#...#" +
            "#.......................................................#..#...#" +
            "#.....................######..#......................####..#...#" +
            "#.....................#.......#......................#.....#...#" +
            "#....................##.###.###.........................#..#...#" +
            "#....................##.....#........................#..#..#...#" +
            "#....................##.#####.....................####..#..#...#" +
            "#....................#.#.........................#......#..#...#" +
            "#....................#..#.......................##..#####..#...#" +
            "#..............................................##..##......#...#" +
            "#.................................................##...........#" +
            "#..............................................................#" +
            "#..............................................................#" +
            "#..............................##..............................#" +
            "#..............................##..............................#" +
            "#..............................##..............................#" +
            "################################################################";

    /**
     * The dimensions of the map
     */
    public static final Vec2di mapSize = new Vec2di(64, 32);

    /**
     * This method builds a world from a given map (string, width and height dimensions)
     * @param map the string with the map
     * @param mapSize the dimensions of the map (width and height)
     * @param solid the character of the map what is a block or solid location
     * @param roof the coordinates of the roof texture
     * @param wall the coordinates of the wall texture
     * @param ground the coordinate of the ground texture
     * @return the block world, built from the map
     */
    public static BlockWorld buildWorld(
            String map,
            Vec2di mapSize,
            char solid,
            Vec2di roof,
            Vec2di wall,
            Vec2di ground) {
        BlockWorld world = new BlockWorld(mapSize.getX(), mapSize.getY());
        for (int y = 0; y < world.getSize().getY(); y++) {
            for (int x = 0; x < world.getSize().getX(); x++) {
                Block b;
                if (map.toCharArray()[x + mapSize.getX() * y] == solid) {
                    b = BlockBuilder.buildBlock(wall);
                    b.setWall(CellSide.TOP, roof);
                } else {
                    b = BlockBuilder.buildGround(ground);
                }
                b.getPosition().setX(x);
                b.getPosition().setY(y);
                b.transform();
                world.setBlock(x, y, b);
            }
        }
        return world;
    }

    /**
     * This method is useful for set texture of the wall in rectangle areas
     * @param world the world to modify
     * @param startX the start x position
     * @param endX the end x position
     * @param startY the start y position
     * @param endY the end y position
     * @param side the side to set the texture
     * @param texture the new texture
     */
    public static void setTexture(
            BlockWorld world,
            int startX,
            int endX,
            int startY,
            int endY,
            CellSide side,
            Vec2di texture) {
        for ( int y = startY; y < endY; y++ ) {
            for ( int x = startX; x < endX; x++ ) {
                world.getBlock(x, y).setWall(side, texture);
            }
        }
    }

    /**
     * @return the original world what I was working
     */
    public static BlockWorld buildWorldSergio() {
        BlockWorld world = buildWorld(map, mapSize, '#',
                new Vec2di(3, 3), new Vec2di(4, 0), new Vec2di(0, 5));

        setTexture(world, 3, 7, 10, 16, CellSide.BOTTOM, new Vec2di(3, 5));

        Block b = BlockBuilder.buildBlock(new Vec2di(4, 0));
        b.setWall(CellSide.TOP, new Vec2di(3, 3));
        b.setWall(CellSide.SOUTH, new Vec2di(0, 2));
        b.getPosition().setX(5);
        b.getPosition().setY(4);
        b.transform();
        world.setBlock(5, 4, b);

        return world;
    }

}
