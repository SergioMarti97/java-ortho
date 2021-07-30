package dungeonViewer.builders;

import olcPGEApproach.gfx.images.Image;
import olcPGEApproach.gfx.images.ImageTile;

/**
 * This class contains all
 * the wall textures for the
 * dungeon viewer
 */
public class WallTextures {

    private static final ImageTile imageTile = new ImageTile("/dungeon/dg_features32.png", 32, 32);

    public static final Image GRAY = imageTile.getTileImage(0, 0);

    public static final Image DARK_GRAY = imageTile.getTileImage(1, 0);

    public static final Image VERY_DARK_GRAY = imageTile.getTileImage(2, 0);

    public static final Image[] GRAYS = new Image[] {GRAY, DARK_GRAY, VERY_DARK_GRAY};

    public static final Image DARK_BRICKS_1 = imageTile.getTileImage(3, 0);

    public static final Image DARK_BRICKS_2 = imageTile.getTileImage(4, 0);

    public static final Image DARK_BRICKS_3 = imageTile.getTileImage(5, 0);

    public static final Image[] DARK_BRICKS = new Image[] {DARK_BRICKS_1, DARK_BRICKS_2, DARK_BRICKS_3};

    public static final Image MOSSY_1 = imageTile.getTileImage(6, 0);

    public static final Image MOSSY_2 = imageTile.getTileImage(7, 0);

    public static final Image MOSSY_3 = imageTile.getTileImage(8, 0);

    public static final Image[] MOSSY = new Image[] {MOSSY_1, MOSSY_2, MOSSY_3};

    public static final Image MOSSY_GOLD_1 = imageTile.getTileImage(0, 1);

    public static final Image MOSSY_GOLD_2 = imageTile.getTileImage(1, 1);

    public static final Image MOSSY_GOLD_3 = imageTile.getTileImage(2, 1);

    public static final Image[] MOSSY_GOLD = new Image[] {MOSSY_GOLD_1, MOSSY_GOLD_2, MOSSY_GOLD_3};

    public static final Image FLINT_1 = imageTile.getTileImage(3, 1);

    public static final Image FLINT_2 = imageTile.getTileImage(4, 1);

    public static final Image FLINT_3 = imageTile.getTileImage(5, 1);

    public static final Image[] FLINT = new Image[] {FLINT_1, FLINT_2, FLINT_3};

    public static final Image FLINT_GOLD_1 = imageTile.getTileImage(6, 1);

    public static final Image FLINT_GOLD_2 = imageTile.getTileImage(7, 1);

    public static final Image FLINT_GOLD_3 = imageTile.getTileImage(8, 1);

    public static final Image[] FLINT_GOLD = new Image[] {FLINT_GOLD_1, FLINT_GOLD_2, FLINT_GOLD_3};

    public static final Image JAIL_DOOR_CLOSED = imageTile.getTileImage(0, 2);

    public static final Image JAIL_DOOR_OPEN = imageTile.getTileImage(1, 2);

    public static final Image JAIL_DOOR_BROKEN = imageTile.getTileImage(2, 2);

    public static final Image[] JAIL_DOOR = new Image[] {JAIL_DOOR_CLOSED, JAIL_DOOR_OPEN, JAIL_DOOR_BROKEN};

    public static final Image BROWN_STONES_1 = imageTile.getTileImage(3, 3);

    public static final Image BROWN_STONES_2 = imageTile.getTileImage(4, 3);

    public static final Image BROWN_STONES_3 = imageTile.getTileImage(5, 3);

    public static final Image[] BROWN_STONES = new Image[] {BROWN_STONES_1, BROWN_STONES_2, BROWN_STONES_3};

    public static final Image WHITE_STONES_1 = imageTile.getTileImage(6, 3);

    public static final Image WHITE_STONES_2 = imageTile.getTileImage(7, 3);

    public static final Image WHITE_STONES_3 = imageTile.getTileImage(8, 3);

    public static final Image[] WHITE_STONES = new Image[] {WHITE_STONES_1, WHITE_STONES_2, WHITE_STONES_3};

    public static final Image GRASS_1 = imageTile.getTileImage(0, 5);

    public static final Image GRASS_2 = imageTile.getTileImage(1, 5);

    public static final Image GRASS_3 = imageTile.getTileImage(2, 5);

    public static final Image[] GRASS = new Image[] {GRASS_1, GRASS_2, GRASS_3};

    public static final Image SEA_1 = imageTile.getTileImage(3, 5);

    public static final Image SEA_2 = imageTile.getTileImage(4, 5);

    public static final Image SEA_3 = imageTile.getTileImage(5, 5);

    public static final Image[] SEA = new Image[] {SEA_1, SEA_2, SEA_3};

    public static final Image OCEAN_1 = imageTile.getTileImage(6, 5);

    public static final Image OCEAN_2 = imageTile.getTileImage(7, 5);

    public static final Image OCEAN_3 = imageTile.getTileImage(8, 5);

    public static final Image[] OCEAN = new Image[] {OCEAN_1, OCEAN_2, OCEAN_3};

    public static final Image MAGMA_1 = imageTile.getTileImage(0, 6);

    public static final Image MAGMA_2 = imageTile.getTileImage(1, 6);

    public static final Image MAGMA_3 = imageTile.getTileImage(2, 6);

    public static final Image[] MAGMA = new Image[] {MAGMA_1, MAGMA_2, MAGMA_3};

    public static final Image LAVA_1 = imageTile.getTileImage(3, 6);

    public static final Image LAVA_2 = imageTile.getTileImage(4, 6);

    public static final Image LAVA_3 = imageTile.getTileImage(5, 6);

    public static final Image[] LAVA = new Image[] {LAVA_1, LAVA_2, LAVA_3};

    public static final Image LIGHT_BRICKS_1 = imageTile.getTileImage(6, 6);

    public static final Image LIGHT_BRICKS_2 = imageTile.getTileImage(7, 6);

    public static final Image LIGHT_BRICKS_3 = imageTile.getTileImage(8, 6);

    public static final Image[] LIGHT_BRICKS = new Image[] {LIGHT_BRICKS_1, LIGHT_BRICKS_2, LIGHT_BRICKS_3};

    public static final Image WOOD_DOOR_CLOSED = imageTile.getTileImage(0, 7);

    public static final Image WOOD_DOOR_OPEN = imageTile.getTileImage(1, 7);

    public static final Image WOOD_DOOR_BROKEN = imageTile.getTileImage(2, 7);

    public static final Image[] WOOD_DOOR = new Image[] {WOOD_DOOR_CLOSED, WOOD_DOOR_OPEN, WOOD_DOOR_BROKEN};

    public static final Image STAIRS_UP = imageTile.getTileImage(2,9);

    public static final Image STAIRS_DOWN = imageTile.getTileImage(3,9);

    public static final Image[] STAIRS = new Image[] {STAIRS_UP, STAIRS_DOWN};

    public static final Image NOISE_WALL_GRAY = imageTile.getTileImage(4, 9);

    public static final Image NOISE_WALL_WHITE = imageTile.getTileImage(5, 9);

    public static final Image[] NOISE_WALL = new Image[] {NOISE_WALL_GRAY, NOISE_WALL_WHITE};

    public static final Image MOUNTAIN = imageTile.getTileImage(6, 9);

    public static final Image PLANE_GRAY_WALL_1 = imageTile.getTileImage(7, 9);

    public static final Image PLANE_GRAY_WALL_2 = imageTile.getTileImage(8, 9);

    public static final Image PLANE_GRAY_WALL_3 = imageTile.getTileImage(0, 10);

    public static final Image PLANE_GRAY_WALL_4 = imageTile.getTileImage(1, 10);

    public static final Image PLANE_GRAY_WALL_5 = imageTile.getTileImage(2, 10);

    public static final Image[] PLANE_GRAY_WALL = new Image[] {PLANE_GRAY_WALL_1, PLANE_GRAY_WALL_2, PLANE_GRAY_WALL_3, PLANE_GRAY_WALL_4, PLANE_GRAY_WALL_5};

    public static final Image BLOCKS_WALL_1 = imageTile.getTileImage(3, 10);

    public static final Image BLOCKS_WALL_2 = imageTile.getTileImage(4, 10);

    public static final Image BLOCKS_WALL_3 = imageTile.getTileImage(5, 10);

    public static final Image[] BLOCKS_WALL = new Image[] {BLOCKS_WALL_1, BLOCKS_WALL_2, BLOCKS_WALL_3};

    public static final Image SMALL_WOOD_DOOR_CLOSED = imageTile.getTileImage(6, 10);

    public static final Image SMALL_WOOD_DOOR_OPEN = imageTile.getTileImage(7, 10);

    public static final Image SMALL_WOOD_DOOR_BROKEN = imageTile.getTileImage(8, 10);

    public static final Image[] SMALL_WOOD_DOOR = new Image[] {SMALL_WOOD_DOOR_CLOSED, SMALL_WOOD_DOOR_OPEN, SMALL_WOOD_DOOR_BROKEN};

    public static final Image FANCY_STONE_DOOR_CLOSED = imageTile.getTileImage(0, 11);

    public static final Image FANCY_STONE_DOOR_OPEN = imageTile.getTileImage(1, 11);

    public static final Image FANCY_STONE_DOOR_BROKEN = imageTile.getTileImage(3, 11);

    public static final Image[] FANCY_STONE_DOOR = new Image[] {FANCY_STONE_DOOR_CLOSED, FANCY_STONE_DOOR_OPEN, FANCY_STONE_DOOR_BROKEN};

}
