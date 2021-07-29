package carcrimegame.city;

import engine3d.PipeLine;
import engine3d.matrix.Mat4x4;
import engine3d.matrix.MatrixMath;
import engine3d.mesh.Mesh;
import engine3d.mesh.MeshFactory;
import olcPGEApproach.gfx.images.Image;
import olcPGEApproach.vectors.points2d.Vec2di;

/**
 * This represent a cell from the carcrimegame.city
 * @author Sergio Martí Torregrosa. sMartiTo
 * @version 0.0.01. pre-alpha
 * @date 20/08/2020
 */
public class CityCell {

    /**
     * The flat mesh of the city cell
     */
    private Mesh flat;

    /**
     * The walls out of the city cell
     */
    private Mesh wallsOut;

    /**
     * The position of the cell on the world
     */
    private Vec2di position;

    /**
     * The height of the building
     */
    private int height;

    /**
     * The road index for the road image
     */
    private int imageIndex;

    /**
     * Flag if the cell is a road
     */
    private boolean isRoad;

    /**
     * Flag if the cell is a building
     */
    private boolean isBuilding;

    /**
     * Constructor
     * @param height the height of the building of the cell
     * @param worldX the X position on the city
     * @param worldY the Y position on the city
     * @param isRoad if it's a road
     * @param isBuilding if it's a building
     */
    public CityCell(int height, int worldX, int worldY, boolean isRoad, boolean isBuilding) {
        flat = MeshFactory.getFlat();
        wallsOut = MeshFactory.getWallsOut();
        position = new Vec2di(worldX, worldY);
        this.height = height;
        imageIndex = 2;
        this.isRoad = isRoad;
        this.isBuilding = isBuilding;
    }

    /**
     * This method renders the city cell on screen
     * @param pipeLine the pipeLine object
     * @param images the images
     */
    public void render(PipeLine pipeLine, Image[] images) {
        // todo: hay un fallo a la hora de renderizar los bloques de edificios, ya que se dibujan por debajo del suelo
        Mat4x4 matTranslation = MatrixMath.matrixMakeTranslation(position.getX(), position.getY(), 0.0f);
        pipeLine.setTransform(matTranslation);

        if ( isRoad ) {
            pipeLine.renderMesh(MeshFactory.getFlat(), images[imageIndex]);
        } else {
            if ( height == 0 ) {
                pipeLine.renderMesh(flat, images[imageIndex]);
            } else if ( height > 0 ) {
                int h;
                for ( h = 0; h < height; h++ ) {
                    matTranslation = MatrixMath.matrixMakeTranslation(
                            position.getX(),
                            position.getY(),
                            -(h + 1) * 0.2f);
                    pipeLine.setTransform(matTranslation);
                    pipeLine.renderMesh(wallsOut, images[7]);
                }
                matTranslation = MatrixMath.matrixMakeTranslation(
                        position.getX(),
                        position.getY(),
                        -(h) * 0.2f);
                pipeLine.setTransform(matTranslation);
                pipeLine.renderMesh(MeshFactory.getFlat(), images[3]);
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////

    public boolean isRoad() {
        return isRoad;
    }

    public boolean isBuilding() {
        return isBuilding;
    }

    public int getHeight() {
        return height;
    }

    public Vec2di getPosition() {
        return position;
    }

    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }

    public void setHeight(int height) {
        this.height = height;
        if ( height > 0 ) {
            flat = MeshFactory.getWallsOut();
        }
        if ( height == 0 ) {
            flat = MeshFactory.getFlat();
        }
    }

    public void setBuilding(boolean building) {
        isBuilding = building;
    }

    public void setRoad(boolean road) {
        isRoad = road;
    }

    public void setPosition(Vec2di position) {
        this.position = position;
    }

}
