package dungeonViewer.io;

import dungeonViewer.*;
import dungeonViewer.builders.BlockBuilder;
import olcPGEApproach.vectors.points2d.Vec2di;
import orthographicViewer.CellSide;
import orthographicViewer.World;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;

/**
 * Store and read world data
 *
 * The data is store as text file:
 * First line of the file: width and height of the world (in that order)
 * Rest of the file: first value 1-0 value to cell flag "is wall?",
 * 5 spaces and then 12 values, the x and y coordinates of the
 * texture position of the wall, in pixels. 6 walls, and two values
 * for wall, 12 values
 */
public class UtilsBlockWorld {

    /**
     * This method reads the file and builds a block world from
     * the data
     * @param name the path and name of the file where is the data
     * @return a block world build from the text file
     */
    public static BlockWorld readSceneFromFile(String name) {
        BlockWorld world = null;
        try ( FileReader fileReader = new FileReader(name) ) {
            BufferedReader bf = new BufferedReader(fileReader);
            String line = bf.readLine();

            String[] mapSize = line.split(" ");
            world = new BlockWorld(Integer.parseInt(mapSize[0]), Integer.parseInt(mapSize[1]));

            for ( int y = 0; y < world.getSize().getY(); y++ ) {
                for ( int x = 0; x < world.getSize().getX(); x++ ) {
                    line = bf.readLine();
                    if ( line != null ) {
                        String[] data = line.split(" ");
                        boolean isWall = Integer.parseInt(data[0]) == 1;
                        Block b;
                        b = BlockBuilder.buildFullBlock(new Vec2di());
                        for ( int j = 0; j < 12; j += 2 ) {
                            int textX = Integer.parseInt(data[j + 1 + 4]) / 32;
                            int textY = Integer.parseInt(data[j + 2 + 4]) / 32;
                            switch ( j / 2 ) {
                                case 0:
                                    b.setWall(CellSide.BOTTOM, new Vec2di(textX, textY));
                                    break;
                                case 1: // top
                                    b.setWall(CellSide.NORTH, new Vec2di(textX, textY));
                                    break;
                                case 2: // North
                                    b.setWall(CellSide.WEST, new Vec2di(textX, textY));
                                    break;
                                case 3:
                                    b.setWall(CellSide.SOUTH, new Vec2di(textX, textY));
                                    break;
                                case 4: // WEST
                                    b.setWall(CellSide.EAST, new Vec2di(textX, textY));
                                    break;
                                case 5: // EAST
                                    b.setWall(CellSide.TOP, new Vec2di(textX, textY));
                                    break;
                            }
                        }
                        b.setWall(isWall);
                        b.getPosition().setX(x);
                        b.getPosition().setY(y);
                        b.transform();
                        world.setBlock(x, y, b);
                    }
                }
            }

            world.setBlocks(Arrays.stream(world.getBlocks()).filter(Objects::nonNull).toArray(Block[]::new));

        } catch ( FileNotFoundException e) {
            System.out.println("File '" + name + "' not found");
            e.printStackTrace();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return world;
    }

    /**
     * This method writes a file which contains all the data of
     * the block world passed by parameter
     * @param name the name of the file to store the data of the block world
     * @param world the block world
     * @param tilesSize the size of the tiles of the image tiles
     */
    public static void writeSceneToFile(String name, BlockWorld world, Vec2di tilesSize) {
        try ( PrintWriter printWriter = new PrintWriter(name) ) {
            printWriter.println(world.getSize().getX() + " " + world.getSize().getY());
            for ( int y = 0; y < world.getSize().getY(); y++ ) {
                for ( int x = 0; x < world.getSize().getX(); x++ ) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(world.getBlock(x, y).isWall() ? 1 : 0);
                    stringBuilder.append("     ");
                    for ( int j = 0; j < 6; j++ ) {
                        int textureX = 0;
                        int textureY = 0;
                        CellSide side;
                        switch ( j ) {
                            case 0:
                            default: // Bottom
                                side = CellSide.BOTTOM;
                                break;
                            case 1: // North
                                side = CellSide.NORTH;
                                break;
                            case 2: // West
                                side = CellSide.WEST;
                                break;
                            case 3: // South
                                side = CellSide.SOUTH;
                                break;
                            case 4:
                                side = CellSide.EAST;
                                break;
                            case 5:
                                side = CellSide.TOP;
                                break;
                        }
                        if ( world.getBlock(x, y).getWalls().containsKey(side) ) {
                            textureX = world.getBlock(x, y).getWalls().get(side).getTexturePos().getX();
                            textureY = world.getBlock(x, y).getWalls().get(side).getTexturePos().getY();
                        }
                        stringBuilder.append(textureX * tilesSize.getX()).append(' ').append(textureY * tilesSize.getY()).append(' ');
                    }
                    printWriter.println(stringBuilder);
                }
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

}
