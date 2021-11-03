package maze;

import olcPGEApproach.AbstractGame;
import olcPGEApproach.GameContainer;

public class MazeViewer implements AbstractGame {

    private Maze maze;

    @Override
    public void initialize(GameContainer gc) {
        MazeMaker.initialize();
        final int pathWidth = 10;
        final int wallWidth = 5;
        final int totalPathWidth = pathWidth + wallWidth;
        maze = new Maze(gc.getRenderer().getW() / totalPathWidth, gc.getRenderer().getH() / totalPathWidth, pathWidth, wallWidth);
        //MazeMaker.makeMaze(maze);
    }

    @Override
    public void update(GameContainer gc, float v) {
        MazeMaker.makeMaze1Iteration(maze);
    }

    @Override
    public void render(GameContainer gc) {
        MazeMaker.drawMaze(maze, gc.getRenderer());
    }
}
