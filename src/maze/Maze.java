package maze;

import olcPGEApproach.vectors.points2d.Vec2di;

import java.util.Arrays;

public class Maze {

    private Vec2di size;

    private int[] maze;

    private int pathWidth;

    private int gap;

    public Maze(int width, int height, int pathWidth, int gap) {
        this.size = new Vec2di(width, height);
        maze = new int[width * height];
        Arrays.fill(maze, 0x00);
        this.pathWidth = pathWidth;
        this.gap = gap;
    }

    // Getters and Setters

    public int getCell(int x, int y) {
        return maze[x + size.getX() * y];
    }

    public int getNumCells() {
        return size.getX() * size.getY();
    }

    public int getFullPathWidth() {
        return pathWidth + gap;
    }

    public Vec2di getSize() {
        return size;
    }

    public int[] getMaze() {
        return maze;
    }

    public int getPathWidth() {
        return pathWidth;
    }

    public int getGap() {
        return gap;
    }

}
