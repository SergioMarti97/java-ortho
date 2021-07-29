package carcrimegame.city;

import engine3d.PipeLine;
import engine3d.matrix.MatrixMath;
import engine3d.mesh.Mesh;
import engine3d.mesh.MeshFactory;
import olcPGEApproach.gfx.images.Image;
import olcPGEApproach.gfx.images.ImageTile;
import olcPGEApproach.vectors.points2d.Vec2di;

/**
 * This class encapsulates all the city related methods
 * from the TopDownCityBasedCarCrimeGame.
 *
 * @author Sergio Martí Torregrosa. sMartiTo
 * @date 24/08/2020
 */
public class City {

    /**
     * The mesh which is a flat plane flat
     */
    private Mesh flat;

    /**
     * The mesh which is the out walls of a building
     */
    private Mesh wallsOut;

    /**
     * The image tile with all the resources
     */
    private ImageTile resources;

    /**
     * All the images what the image tile contains
     */
    private Image[] images;

    /**
     * The array of cells what compose the city
     */
    private CityCell[] cells;

    /**
     * The dimensions of the city
     */
    private Vec2di dimensions;

    /**
     * Constructor
     * @param cityWidth the width (on cells) of the city
     * @param cityHeight the height (on cells) of the city
     * @param resources the image tile which contains all the images needed for renderer
     *                  the city
     */
    public City(int cityWidth, int cityHeight, ImageTile resources) {
        dimensions = new Vec2di(cityWidth, cityHeight);
        initializeMap();
        this.resources = resources;
        images = getImages();
        flat = MeshFactory.getFlat();
        wallsOut = MeshFactory.getWallsOut();
        MatrixMath.transformMesh(MatrixMath.matrixMakeScale(1.0f, 1.0f, 0.2f), wallsOut);
    }

    /**
     * This method initialises the map
     */
    public void initializeMap() {
        cells = new CityCell[dimensions.getX() * dimensions.getY()];

        for ( int x = 0; x < dimensions.getX(); x++ ) {
            for ( int y = 0; y < dimensions.getY(); y++ ) {
                cells[y * dimensions.getX() + x] = new CityCell(0, x, y, false, false);
            }
        }
    }

    /**
     * @return an array of images which contains chopped roads from the original image tile
     */
    private Image[] getImages() {
        Image[] roads = new Image[20];
        for ( int x = 0; x < resources.getW() / resources.getTileW(); x++ ) {
            for ( int y = 0; y < resources.getH() / resources.getTileH(); y++ ) {
                roads[y * 4 + x] = resources.getTileImage(x, y);
            }
        }
        return roads;
    }

    /**
     * This method return if the cell at X = positionX + i and
     * Y = positionY + j, is a road or not
     * @param positionX the x for cell where you are
     * @param positionY the y for cell there you are
     * @param i the number of cells in x axis where have to move for the cell is asking for
     * @param j the number of cells in y axis where have to move for the cell is asking for
     * @return if the cell next i axis x j axis y displaced from the cell which is at the position
     * positionX and positionY is a road or not
     */
    private boolean isRoad(int positionX, int positionY, int i, int j) {
        int finalX = positionX + i;
        int finalY = positionY + j;
        if ( finalX < dimensions.getX() && finalY < dimensions.getY() && finalX >= 0 && finalY >= 0 ) {
            return getCell(finalX, finalY).isRoad();
        } else {
            return false;
        }
    }

    /**
     * This method is needed for the method "getRoadIndex"
     * What this does is tell if the cell next to the cell
     * passed on the parameter at the relative position specified
     * with "i an "j"
     * @param cityCell the city cell
     * @param i the number of cells next in x axis
     * @param j the number of cells next in y axis
     * @return if the next cell to the cell is a road
     */
    private boolean isRoad(CityCell cityCell, int i, int j) {
        return isRoad(cityCell.getPosition().getX(), cityCell.getPosition().getY(), i, j);
    }

    /**
     * This method calculates the correct road index for the
     * cell which is a road, based on the neighbors cells
     * @param cityCell the cell
     * @return the correct road index
     */
    private int getRoadIndex(CityCell cityCell) {
        if ( isRoad(cityCell, 0, +1) && isRoad(cityCell, 0, -1) &&
                !isRoad(cityCell, +1, 0) && !isRoad(cityCell, -1, 0) ) {
            return  0;
        }
        if ( !isRoad(cityCell, 0, +1) && !isRoad(cityCell, 0, -1) &&
                isRoad(cityCell, +1, 0) && isRoad(cityCell, -1, 0) ) {
            return 1;
        }

        if ( !isRoad(cityCell, 0, +1) && isRoad(cityCell, 0, -1) &&
                !isRoad(cityCell, +1, 0) && isRoad(cityCell, -1, 0) ) {
            return 6; // 4
        }
        if ( !isRoad(cityCell, 0, +1) && isRoad(cityCell, 0, -1) &&
                isRoad(cityCell, +1, 0) && isRoad(cityCell, -1, 0) ) {
            return 5;
        }
        if ( !isRoad(cityCell, 0, +1) && isRoad(cityCell, 0, -1) &&
                isRoad(cityCell, +1, 0) && !isRoad(cityCell, -1, 0) ) {
            return 4; // 6
        }

        if ( isRoad(cityCell, 0, +1) && isRoad(cityCell, 0, -1) &&
                !isRoad(cityCell, +1, 0) && isRoad(cityCell, -1, 0) ) {
            return 10; // 8
        }
        if ( isRoad(cityCell, 0, +1) && isRoad(cityCell, 0, -1) &&
                isRoad(cityCell, +1, 0) && isRoad(cityCell, -1, 0) ) {
            return 9;
        }
        if ( isRoad(cityCell, 0, +1) && isRoad(cityCell, 0, -1) &&
                isRoad(cityCell, +1, 0) && !isRoad(cityCell, -1, 0) ) {
            return 8; // 10
        }

        if ( isRoad(cityCell, 0, +1) && !isRoad(cityCell, 0, -1) &&
                !isRoad(cityCell, +1, 0) && isRoad(cityCell, -1, 0) ) {
            return 14; // 12
        }
        if ( isRoad(cityCell, 0, +1) && !isRoad(cityCell, 0, -1) &&
                isRoad(cityCell, +1, 0) && isRoad(cityCell, -1, 0) ) {
            return 13;
        }
        if ( isRoad(cityCell, 0, +1) && !isRoad(cityCell, 0, -1) &&
                isRoad(cityCell, +1, 0) && !isRoad(cityCell, -1, 0) ) {
            return 12; // 14
        }

        return 0;
    }

    /**
     * When a cell is road flag is changed, the cells
     * the index of the images and their neighbors has
     * to change
     */
    public void updatesCellsIndexImage() {
        for ( CityCell cityCell : cells ) {
            if ( cityCell.isRoad() ) {
                cityCell.setImageIndex(getRoadIndex(cityCell));
            } else {
                cityCell.setImageIndex(2);
            }
        }
    }

    /**
     * This method renders on screen the city
     * @param pipeLine the pipeLine object which has all method sto render 3D objects on screen
     */
    public void render(PipeLine pipeLine) {
        for ( CityCell cityCell : cells ) {
            cityCell.render(pipeLine, images);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////

    public CityCell[] getCells() {
        return cells;
    }

    public CityCell getCell(int x, int y) {
        return cells[y * dimensions.getX() + x];
    }

    public Vec2di getDimensions() {
        return dimensions;
    }

    public void setDimensions(Vec2di dimensions) {
        this.dimensions = dimensions;
        initializeMap();
    }

}
