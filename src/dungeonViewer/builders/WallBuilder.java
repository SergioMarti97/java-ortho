package dungeonViewer.builders;

import dungeonViewer.Wall;
import engine3d.mesh.Triangle;
import engine3d.vectors.Vec3df;
import engine3d.vectors.Vec4df;
import olcPGEApproach.vectors.points2d.Vec2di;
import orthographicViewer.CellSide;

import java.util.ArrayList;

/**
 * This class builds walls
 */
public class WallBuilder {

    private static Wall buildTopWall(Vec2di texturePos) {
        ArrayList<Triangle> triangles = new ArrayList<>();
        // TOP
        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(0.0f, 1.0f, 0.0f),
                        new Vec4df(0.0f, 1.0f, 1.0f),
                        new Vec4df(1.0f, 1.0f, 1.0f)
                },
                new Vec3df[] {
                        new Vec3df(0.0f, 1.0f),
                        new Vec3df(0.0f, 0.0f),
                        new Vec3df(1.0f, 0.0f)
                }));
        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(0.0f, 1.0f, 0.0f),
                        new Vec4df(1.0f, 1.0f, 1.0f),
                        new Vec4df(1.0f, 1.0f, 0.0f)
                },
                new Vec3df[] {
                        new Vec3df(0.0f, 1.0f),
                        new Vec3df(1.0f, 0.0f),
                        new Vec3df(1.0f, 1.0f)
                }));
        return new Wall(triangles, texturePos);
    }

    private static Wall buildBottomWall(Vec2di texturePos) {
        ArrayList<Triangle> triangles = new ArrayList<>();
        // BOTTOM
        /*triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(1.0f, 0.0f, 1.0f),
                        new Vec4df(0.0f, 0.0f, 1.0f),
                        new Vec4df(0.0f, 0.0f, 0.0f)
                },
                new Vec3df[] {
                        new Vec3df(0.0f, 1.0f),
                        new Vec3df(0.0f, 0.0f),
                        new Vec3df(1.0f, 0.0f)
                }));
        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(1.0f, 0.0f, 1.0f),
                        new Vec4df(0.0f, 0.0f, 0.0f),
                        new Vec4df(1.0f, 0.0f, 0.0f)
                },
                new Vec3df[] {
                        new Vec3df(0.0f, 1.0f),
                        new Vec3df(1.0f, 0.0f),
                        new Vec3df(1.0f, 1.0f)
                }));*/
        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(0.0f, 0.0f, 0.0f),
                        new Vec4df(0.0f, 0.0f, 1.0f),
                        new Vec4df(1.0f, 0.0f, 1.0f)
                },
                new Vec3df[] {
                        new Vec3df(1.0f, 0.0f),
                        new Vec3df(0.0f, 0.0f),
                        new Vec3df(0.0f, 1.0f)
                }));
        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(1.0f, 0.0f, 0.0f),
                        new Vec4df(0.0f, 0.0f, 0.0f),
                        new Vec4df(1.0f, 0.0f, 1.0f)
                },
                new Vec3df[] {
                        new Vec3df(1.0f, 1.0f),
                        new Vec3df(1.0f, 0.0f),
                        new Vec3df(0.0f, 1.0f)

                }));
        return new Wall(triangles, texturePos);
    }

    private static Wall buildWestWall(Vec2di texturePos) {
        ArrayList<Triangle> triangles = new ArrayList<>();
        // WEST
        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(0.0f, 0.0f, 1.0f),
                        new Vec4df(0.0f, 1.0f, 1.0f),
                        new Vec4df(0.0f, 1.0f, 0.0f)
                },
                new Vec3df[] {
                        new Vec3df(0.0f, 1.0f),
                        new Vec3df(0.0f, 0.0f),
                        new Vec3df(1.0f, 0.0f)
                }));
        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(0.0f, 0.0f, 1.0f),
                        new Vec4df(0.0f, 1.0f, 0.0f),
                        new Vec4df(0.0f, 0.0f, 0.0f)
                },
                new Vec3df[] {
                        new Vec3df(0.0f, 1.0f),
                        new Vec3df(1.0f, 0.0f),
                        new Vec3df(1.0f, 1.0f)
                }));
        return new Wall(triangles, texturePos);
    }

    private static Wall buildEastWall(Vec2di texturePos) {
        ArrayList<Triangle> triangles = new ArrayList<>();
        // EAST
        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(1.0f, 0.0f, 0.0f),
                        new Vec4df(1.0f, 1.0f, 0.0f),
                        new Vec4df(1.0f, 1.0f, 1.0f)
                },
                new Vec3df[] {
                        new Vec3df(0.0f, 1.0f),
                        new Vec3df(0.0f, 0.0f),
                        new Vec3df(1.0f, 0.0f)
                }));
        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(1.0f, 0.0f, 0.0f),
                        new Vec4df(1.0f, 1.0f, 1.0f),
                        new Vec4df(1.0f, 0.0f, 1.0f)},
                new Vec3df[] {
                        new Vec3df(0.0f, 1.0f),
                        new Vec3df(1.0f, 0.0f),
                        new Vec3df(1.0f, 1.0f)
                }));
        return new Wall(triangles, texturePos);
    }

    private static Wall buildSouthWall(Vec2di texturePos) {
        ArrayList<Triangle> triangles = new ArrayList<>();
        // SOUTH
        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(0.0f, 0.0f, 0.0f),
                        new Vec4df(0.0f, 1.0f, 0.0f),
                        new Vec4df(1.0f, 1.0f, 0.0f)
                },
                new Vec3df[] {
                        new Vec3df(0.0f, 1.0f),
                        new Vec3df(0.0f, 0.0f),
                        new Vec3df(1.0f, 0.0f)
                }));
        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(0.0f, 0.0f, 0.0f),
                        new Vec4df(1.0f, 1.0f, 0.0f),
                        new Vec4df(1.0f, 0.0f, 0.0f)
                },
                new Vec3df[] {
                        new Vec3df(0.0f, 1.0f),
                        new Vec3df(1.0f, 0.0f),
                        new Vec3df(1.0f, 1.0f)
                }));
        return new Wall(triangles, texturePos);
    }

    private static Wall buildNorthWall(Vec2di texturePos) {
        ArrayList<Triangle> triangles = new ArrayList<>();
        // NORTH
        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(1.0f, 0.0f, 1.0f),
                        new Vec4df(1.0f, 1.0f, 1.0f),
                        new Vec4df(0.0f, 1.0f, 1.0f)
                },
                new Vec3df[] {
                        new Vec3df(0.0f, 1.0f),
                        new Vec3df(0.0f, 0.0f),
                        new Vec3df(1.0f, 0.0f)
                }));
        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(1.0f, 0.0f, 1.0f),
                        new Vec4df(0.0f, 1.0f, 1.0f),
                        new Vec4df(0.0f, 0.0f, 1.0f)
                },
                new Vec3df[] {
                        new Vec3df(0.0f, 1.0f),
                        new Vec3df(1.0f, 0.0f),
                        new Vec3df(1.0f, 1.0f)
                }));
        return new Wall(triangles, texturePos);
    }

    public static Wall buildWall(CellSide side, Vec2di texturePos) {
        switch ( side ) {
            case BOTTOM: default:
                return buildBottomWall(texturePos);
            case TOP:
                return buildTopWall(texturePos);
            case WEST:
                return buildWestWall(texturePos);
            case EAST:
                return buildEastWall(texturePos);
            case SOUTH:
                return buildSouthWall(texturePos);
            case NORTH:
                return buildNorthWall(texturePos);
        }
    }

}
