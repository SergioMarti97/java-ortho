package dungeonViewer;

import engine3d.PipeLine;
import olcPGEApproach.gfx.images.ImageTile;
import olcPGEApproach.vectors.points2d.Vec2di;
import orthographicViewer.CellSide;

import java.util.Arrays;

/**
 * This class represents a world
 * conformed by blocks
 */
public class BlockWorld {

    /**
     * one dimension array to store
     * all the blocks of the world
     */
    private Block[] blocks;

    /**
     * The size of the world, in block units
     */
    private Vec2di size;

    /**
     * The constructor
     * @param w the width in blocks of the world
     * @param h the hegith in blocks of the world
     */
    public BlockWorld(int w, int h) {
        size = new Vec2di(w, h);
        int s = size.getX() * size.getY();
        blocks = new Block[s];
        for (int i = 0; i < s; i++) {
            blocks[i] = new Block();
        }
    }

    /**
     * This method test if a x and y position is inside the world
     * @return true if the position is inside the world or false if not
     */
    private boolean isPositionInsideWorld(int x, int y) {
        return x >= 0 && x < size.getX() && y >= 0 && y < size.getY();
    }

    /**
     * This method draws a specified wall of the block
     * passed by parameter. The wall is specified also by
     * parameter
     * @param b the block
     * @param side the side to drawn on screen
     * @param p the pipeline object to render 3d graphics
     * @param img the image tile with  the assets
     */
    private void renderWall(Block b, CellSide side, PipeLine p, ImageTile img) {
        p.renderMesh(
                b.getWalls().get(side).getWall(),
                img.getTileImage(
                        b.getWalls().get(side).getTexturePos().getX(),
                        b.getWalls().get(side).getTexturePos().getY()
                )
        );
    }

    /**
     * This method draws on screen the floor of the block
     * @param b the block
     * @param p the pipeline object to render 3d graphics
     * @param img the image tile with the assets
     */
    private void renderFloorBlock(Block b, PipeLine p, ImageTile img) {
        renderWall(b, CellSide.BOTTOM, p, img);
    }

    /**
     * This method draws the walls of the block on screen
     * it does a optimization, to avoid drawing the walls
     * of blocks next to other, which can be seen
     * @param b the block to draw
     * @param p the pipeline object to render 3d graphics
     * @param img the image tile with the assets
     */
    private void renderWallsBlock(Block b, PipeLine p, ImageTile img) {
        if ( b.getWalls().containsKey(CellSide.TOP) ) {
            renderWall(b, CellSide.TOP, p, img);
        }

        for ( int i = 0; i < 4; i++ ) {
            int xToTest = b.getPosition().getX();
            int yToTest = b.getPosition().getY();
            CellSide side;
            switch ( i ) {
                case 0: default:
                    yToTest++;
                    side = CellSide.NORTH;
                    break;
                case 1:
                    yToTest--;
                    side = CellSide.SOUTH;
                    break;
                case 2:
                    xToTest++;
                    side = CellSide.EAST;
                    break;
                case 3:
                    xToTest--;
                    side = CellSide.WEST;
                    break;
            }
            if ( getSafeBlock(xToTest, yToTest) != null &&
                    !getSafeBlock(xToTest, yToTest).isWall() ||
                    xToTest < 0 || yToTest < 0 ||
                    xToTest >= size.getX() || yToTest >= size.getY() ) {
                renderWall(b, side, p, img);
            }
        }
    }

    /**
     * This method draws the block passed by parameter
     * on screen
     * @param b the block to get drawn on screen
     * @param p the pipeline object to render 3d graphics
     * @param imgTile the image tile which contains the assets
     */
    private void renderBlock(Block b, PipeLine p, ImageTile imgTile) {
        if ( b.isWall() ) {
            renderWallsBlock(b, p, imgTile);
        } else {
            renderFloorBlock(b, p, imgTile);
        }
    }

    /**
     * This method iterates for each block of
     * the world and draws it in screen
     * It needs the PipeLine object to render
     * the 3D graphics
     * @param p the object what contains all methods needed fo drawing 3D graphics
     * @param imageTile the image tile with all the assets of the world
     */
    public void drawWorld(PipeLine p, ImageTile imageTile) {
        for ( Block b : blocks ) {
            renderBlock(b, p, imageTile);
        }
    }

    /*
    * Getters and Setters
    */

    /**
     * Method to get a block of the world
     * @return the block located on x and y coordinates
     */
    public Block getBlock(int x, int y) {
        return blocks[y * size.getX() + x];
    }

    public Block getBlock(Vec2di pos) {
        return getBlock(pos.getX(), pos.getY());
    }

    /**
     * This method gets a block of the world,
     * testing if the x and y position are inside the
     * world. If it isn't, it returns null
     * @param x the x position where is the block
     * @param y the y position where is the block
     * @return the block located on x and y coordinate
     */
    public Block getSafeBlock(int x, int y) {
        if ( isPositionInsideWorld(x, y) ) {
            return getBlock(x, y);
        }
        return null;
    }

    public void setBlock(int x, int y, Block b) {
        blocks[y * size.getX() + x] = b;
    }

    public Block[] getBlocks() {
        return blocks;
    }

    public void setBlocks(Block[] blocks) {
        this.blocks = blocks;
    }

    public Vec2di getSize() {
        return size;
    }

    public void setSize(Vec2di size) {
        this.size = size;
        blocks = Arrays.copyOf(blocks, size.getX() * size.getY());
    }

}
