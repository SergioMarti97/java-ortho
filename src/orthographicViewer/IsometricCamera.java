package orthographicViewer;

import olcPGEApproach.vectors.points2d.Vec2df;
import olcPGEApproach.vectors.points3d.Vec3df;

/**
 * Contains all data relative to the
 * camera to do the transformations
 * needed to the isometric view
 */
public class IsometricCamera {

    private Vec2df pos;

    private final Vec3df pos3d;

    private float angle;

    private float pitch;

    private float zoom;

    /**
     * Constructor
     * @param pos position 2d
     * @param angle angle to see the world axis Y
     * @param pitch angle to see the world axis x
     * @param zoom zoom to see the world
     */
    public IsometricCamera(Vec2df pos, float angle, float pitch, float zoom) {
        this.pos = pos;
        this.angle = angle;
        this.pitch = pitch;
        this.zoom = zoom;
        pos3d = new Vec3df();
    }

    public void updatePos3d() {
        pos3d.setX(pos.getX());
        pos3d.setY(0);
        pos3d.setZ(pos.getY());
    }

    // Getters

    public Vec2df getPos() {
        return pos;
    }

    public Vec3df getPos3d() {
        return pos3d;
    }

    public float getAngle() {
        return angle;
    }

    public float getPitch() {
        return pitch;
    }

    public float getZoom() {
        return zoom;
    }

    // Setters

    public IsometricCamera setPos(Vec2df pos) {
        this.pos = pos;
        return this;
    }

    public IsometricCamera setAngle(float angle) {
        this.angle = angle;
        return this;
    }

    public IsometricCamera setPitch(float pitch) {
        this.pitch = pitch;
        return this;
    }

    public IsometricCamera setZoom(float zoom) {
        this.zoom = zoom;
        return this;
    }

    // Modifiers

    public IsometricCamera addToAngle(float amount) {
        angle += amount;
        return this;
    }

    public IsometricCamera addToPitch(float amount) {
        pitch += amount;
        return this;
    }

    public IsometricCamera addToZoom(float amount) {
        zoom += amount;
        return this;
    }

}
