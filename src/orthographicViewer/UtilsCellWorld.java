package orthographicViewer;

import olcPGEApproach.vectors.points2d.Vec2di;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
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
public class UtilsCellWorld {

    public static World readWorldFromFile(String name) {
        World w = null;
        try (FileReader fileReader = new FileReader(name); BufferedReader bf = new BufferedReader(fileReader)) {
            String line = bf.readLine();
            String[] data = line.split(" ");
            w = new World(Integer.parseInt(data[0]), Integer.parseInt(data[1]));

            for (int y = 0; y < w.getSize().getY(); y++) {
                for (int x = 0; x < w.getSize().getX(); x++) {
                    line = bf.readLine();
                    if (line != null) {
                        data = line.split(" ");
                        boolean isWall = Integer.parseInt(data[0]) == 1;
                        Cell c = new Cell(isWall);
                        for (int j = 0; j < 12; j += 2) {
                            int textX = Integer.parseInt(data[j + 1 + 4]) / 32;
                            int textY = Integer.parseInt(data[j + 2 + 4]) / 32;
                            switch ( j / 2 ) {
                                case 0:
                                    c.getFaces().put(CellSide.BOTTOM, new Vec2di(textX, textY));
                                    break;
                                case 1: // top
                                    c.getFaces().put(CellSide.NORTH, new Vec2di(textX, textY));
                                    break;
                                case 2: // North
                                    c.getFaces().put(CellSide.WEST, new Vec2di(textX, textY));
                                    break;
                                case 3:
                                    c.getFaces().put(CellSide.SOUTH, new Vec2di(textX, textY));
                                    break;
                                case 4: // WEST
                                    c.getFaces().put(CellSide.EAST, new Vec2di(textX, textY));
                                    break;
                                case 5: // EAST
                                    c.getFaces().put(CellSide.TOP, new Vec2di(textX, textY));
                                    break;
                            }
                        }
                        w.setCell(x, y, c);
                    }
                }
            }

            w.getCells().removeIf(Objects::isNull);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return w;
    }

    public static void writeWorldToFile(String path, World w, int tileSizeW, int tileSizeH) {
        try (PrintWriter printWriter = new PrintWriter(path)) {
            printWriter.println(w.getSize().getX() + " " + w.getSize().getY());
            for (int y = 0; y < w.getSize().getY(); y++) {
                for (int x = 0; x < w.getSize().getX(); x++) {
                    StringBuilder strBuilder = new StringBuilder();
                    strBuilder.append(w.getCell(x, y).isWall() ? 1 : 0);
                    strBuilder.append("     ");
                    for (int j = 0; j < 6; j++) {
                        int textX = 0;
                        int textY = 0;
                        CellSide side;
                        switch (j) {
                            case 0: default:
                                side = CellSide.BOTTOM;
                                break;
                            case 1:
                                side = CellSide.NORTH;
                                break;
                            case 2:
                                side = CellSide.WEST;
                                break;
                            case 3:
                                side = CellSide.SOUTH;
                                break;
                            case 4:
                                side = CellSide.EAST;
                                break;
                            case 5:
                                side = CellSide.TOP;
                                break;
                        }
                        if (w.getCell(x, y).getFaces().containsKey(side)) {
                            textX = w.getCell(x, y).getFaces().get(side).getX();
                            textY = w.getCell(x, y).getFaces().get(side).getY();
                        }
                        strBuilder.append(textX * tileSizeW).append(' ').append(textY * tileSizeH).append(' ');
                    }
                    printWriter.println(strBuilder);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeWorldToFile(String path, World w, Vec2di tileSize) {
        writeWorldToFile(path, w, tileSize.getX(), tileSize.getY());
    }

}
