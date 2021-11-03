package maze;

import javafx.util.Pair;
import olcPGEApproach.gfx.HexColors;
import olcPGEApproach.gfx.Renderer;
import orthographicViewer.Cell;

import java.util.*;

public class MazeMaker {

    private static final int N = 0x01;

    private static final int E = 0x02;

    private static final int S = 0x04;

    private static final int W = 0x08;

    private static final int VISITED = 0x10;

    private static int numCellsVisited = 0;

    private static Stack<Pair<Integer, Integer>> stack;

    private static ArrayList<Integer> neighbours;

    private static Random rnd;

    public static void initialize() {
        rnd = new Random();
        stack = new Stack<>();
        stack.push(new Pair<>(0, 0));
        neighbours = new ArrayList<>();
        numCellsVisited = 1;
    }

    private static int offset(Maze maze, int x, int y) {
        return (stack.peek().getValue() + y) * maze.getSize().getX() + (stack.peek().getKey() + x);
    }

    private static void makeMazeIteration(Maze maze) {
        neighbours.clear();
        if (stack.peek().getValue() > 0 && (maze.getMaze()[offset(maze, 0, -1)] & VISITED) == 0) {
            neighbours.add(0);
        }
        if (stack.peek().getKey() < maze.getSize().getX() - 1 && (maze.getMaze()[offset(maze,1, 0)] & VISITED) == 0) {
            neighbours.add(1);
        }
        if (stack.peek().getValue() < maze.getSize().getY() - 1 && (maze.getMaze()[offset(maze,0, 1)] & VISITED) == 0) {
            neighbours.add(2);
        }
        if (stack.peek().getKey() > 0 && (maze.getMaze()[offset(maze,-1, 0)] & VISITED) == 0) {
            neighbours.add(3);
        }

        if (!neighbours.isEmpty()) {
            int nextCellDirection = neighbours.get(rnd.nextInt(neighbours.size()));

            switch (nextCellDirection) {
                case 0: default: // north
                    maze.getMaze()[offset(maze,0, -1)] |= VISITED | S;
                    maze.getMaze()[offset(maze,0, 0)] |= N;
                    stack.push(new Pair<>(stack.peek().getKey(), stack.peek().getValue() - 1));
                    break;
                case 1: // east
                    maze.getMaze()[offset(maze,+1, 0)] |= VISITED | W;
                    maze.getMaze()[offset(maze,0, 0)] |= E;
                    stack.push(new Pair<>(stack.peek().getKey() + 1, stack.peek().getValue()));
                    break;
                case 2: // south
                    maze.getMaze()[offset(maze,0, +1)] |= VISITED | N;
                    maze.getMaze()[offset(maze,0, 0)] |= S;
                    stack.push(new Pair<>(stack.peek().getKey(), stack.peek().getValue() + 1));
                    break;
                case 3: // west
                    maze.getMaze()[offset(maze,-1, 0)] |= VISITED | E;
                    maze.getMaze()[offset(maze,0, 0)] |= W;
                    stack.push(new Pair<>(stack.peek().getKey() - 1, stack.peek().getValue()));
                    break;
            }
            numCellsVisited++;
        } else {
            stack.pop();
        }
    }

    public static void makeMaze(Maze m) {
        while (numCellsVisited < m.getNumCells()) {
            makeMazeIteration(m);
        }
        m.getMaze()[offset(m, 0, 0)] |= VISITED;
    }

    public static void makeMaze1Iteration(Maze m) {
        if (numCellsVisited < m.getNumCells()) {
            makeMazeIteration(m);
        }
    }

    public static <T> void translateMaze(Maze m, List<T> l, int w, T visited, T noVisited, T wall) {
        for (int i = 0; i < l.size(); i++) {
            l.set(i, wall);
        }

        for (int y = 0; y < m.getSize().getY(); y++) {
            for (int x = 0; x < m.getSize().getX(); x++) {
                // Each cell is inflated by pathWidth, so fill it in
                for (int py = 0; py < m.getPathWidth(); py++) {
                    for (int px = 0; px < m.getPathWidth(); px++) {
                        int posX = x * m.getFullPathWidth() + px;
                        int posY = y * m.getFullPathWidth() + py;
                        int index = posX + w * posY;
                        if ((m.getCell(x, y) & VISITED) == VISITED) {
                            l.set(index, visited);
                        } else {
                            l.set(index, noVisited);
                        }
                    }
                }

                // passageways between cells
                for (int p = 0; p < m.getPathWidth(); p++) {
                    if ((m.getCell(x, y) & S) == S) {
                        int posX = x * m.getFullPathWidth() + p;
                        int posY = y * m.getFullPathWidth() + m.getPathWidth();
                        int index = posX + w * posY;
                        l.set(index, visited);
                    }
                    if ((m.getCell(x, y) & E) == E) {
                        int posX = x * m.getFullPathWidth() + m.getPathWidth();
                        int posY = y * m.getFullPathWidth() + p;
                        int index = posX + w * posY;
                        l.set(index, visited);
                    }
                }

            }
        }
    }

    public static void translateMaze(Maze m, List<Cell> l, int w, Cell visited, Cell noVisited, Cell wall) {
        for (int i = 0; i < l.size(); i++) {
            l.set(i, new Cell(wall));
        }

        for (int y = 0; y < m.getSize().getY(); y++) {
            for (int x = 0; x < m.getSize().getX(); x++) {
                // Each cell is inflated by pathWidth, so fill it in
                for (int py = 0; py < m.getPathWidth(); py++) {
                    for (int px = 0; px < m.getPathWidth(); px++) {
                        int posX = x * m.getFullPathWidth() + px;
                        int posY = y * m.getFullPathWidth() + py;
                        int index = posX + w * posY;
                        if ((m.getCell(x, y) & VISITED) == VISITED) {
                            l.set(index, new Cell(visited));
                        } else {
                            l.set(index, new Cell(noVisited));
                        }
                    }
                }

                // passageways between cells
                for (int p = 0; p < m.getPathWidth(); p++) {
                    if ((m.getCell(x, y) & S) == S) {
                        int posX = x * m.getFullPathWidth() + p;
                        int posY = y * m.getFullPathWidth() + m.getPathWidth();
                        int index = posX + w * posY;
                        l.set(index, new Cell(visited));
                    }
                    if ((m.getCell(x, y) & E) == E) {
                        int posX = x * m.getFullPathWidth() + m.getPathWidth();
                        int posY = y * m.getFullPathWidth() + p;
                        int index = posX + w * posY;
                        l.set(index, new Cell(visited));
                    }
                }

            }
        }
    }
    
    public static void drawMaze(Maze maze, Renderer r) {
        r.clear(HexColors.BLACK);

        for (int y = 0; y < maze.getSize().getY(); y++) {
            for (int x = 0; x < maze.getSize().getX(); x++) {

                if ((maze.getCell(x, y) & VISITED) == VISITED) {
                    r.drawFillRectangle(x * (maze.getFullPathWidth()), y * (maze.getFullPathWidth()), maze.getPathWidth(), maze.getPathWidth(), HexColors.WHITE);
                } else {
                    r.drawFillRectangle(x * (maze.getFullPathWidth()), y * (maze.getFullPathWidth()), maze.getPathWidth(), maze.getPathWidth(), HexColors.BLUE);
                }

                if ((maze.getCell(x, y) & S) == S) {
                    r.drawFillRectangle(x * (maze.getFullPathWidth()), y * (maze.getFullPathWidth()) + maze.getPathWidth(), maze.getPathWidth(), maze.getGap(), HexColors.WHITE);
                }
                if ((maze.getCell(x, y) & E) == E) {
                    r.drawFillRectangle(x * (maze.getFullPathWidth()) + maze.getPathWidth(), y * (maze.getFullPathWidth()), maze.getGap(), maze.getPathWidth(), HexColors.WHITE);
                }
            }
        }
    }

    public static Stack<Pair<Integer, Integer>> getStack() {
        return stack;
    }
    
}
