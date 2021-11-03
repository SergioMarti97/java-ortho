package dungeonViewer;

import engine3d.mesh.Mesh;
import engine3d.mesh.Triangle;
import olcPGEApproach.vectors.points2d.Vec2di;

import java.util.ArrayList;

/**
 * This class represents a wall
 * A wall is conformed by a mesh
 * and a image what is the texture
 */
public class Wall {

    /**
     * This is the mesh of the wall
     * Usually, only two triangles
     */
    private Mesh wall;

    /**
     * The texture of this mesh
     */
    private Vec2di texturePos;

    /**
     * Constructor
     */
    public Wall(ArrayList<Triangle> tris, Vec2di texturePos) {
        wall = new Mesh(tris);
        this.texturePos = texturePos;
    }

    public Wall(Vec2di texturePos) {
        wall = new Mesh();
        this.texturePos = texturePos;
    }

    public Mesh getWall() {
        return wall;
    }


    public void setWall(Mesh wall) {
        this.wall = wall;
    }

    public Vec2di getTexturePos() {
        return texturePos;
    }

    public void setTexturePos(Vec2di texturePos) {
        this.texturePos = texturePos;
    }

}
