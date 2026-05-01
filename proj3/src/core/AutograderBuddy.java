package core;

import tileengine.TETile;
import tileengine.Tileset;

public class AutograderBuddy {

    /**
     * Simulates a game, but doesn't render anything or call any StdDraw
     * methods. Instead, returns the world that would result if the input string
     * had been typed on the keyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quit and
     * save. To "quit" in this method, save the game to a file, then just return
     * the TETile[][]. Do not call System.exit(0) in this method.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public static TETile[][] getWorldFromInput(String input) {

        //throw new RuntimeException("Please fill out AutograderBuddy!");
//        long seed = parseSeed(input);
//        World world = new World(seed);
//        return world.generate();

        input = input.toLowerCase();

        World world;
        int index;

        if(input.charAt(0) == 'n') {
            int sIndex =  input.indexOf('s');
            long seed = Long.parseLong(input.substring(1, sIndex));

            world = new World(seed);
            world.generate();

            index = sIndex + 1;
        }else if(input.charAt(0) == 'l') {
            world = World.load();
            if (world == null) {
                return null;
            }

            index = 1;
        }else{
            throw new RuntimeException("Invalid input");
        }

        while(index < input.length()) {
            char c = input.charAt(index);

            if (c == ':') {
                if (index + 1 < input.length() && input.charAt(index + 1) == 'q') {
                    world.save();
                    return world.getTiles();
                }
            } else {
                world.moveAvatar(c);
            }

            index++;
        }
    return world.getTiles();
}

    private static long parseSeed(String input) {
        input = input.toLowerCase();

        int nIndex = input.indexOf("n");
        int sIndex = input.indexOf("s");

        String seedString = input.substring(nIndex + 1, sIndex);
        return Long.parseLong(seedString);
    }


    /**
     * Used to tell the autograder which tiles are the floor/ground (including
     * any lights/items resting on the ground). Change this
     * method if you add additional tiles.
     */
    public static boolean isGroundTile(TETile t) {
        return t.character() == Tileset.FLOOR.character()
                || t.character() == Tileset.AVATAR.character()
                || t.character() == Tileset.FLOWER.character();
    }

    /**
     * Used to tell the autograder while tiles are the walls/boundaries. Change
     * this method if you add additional tiles.
     */
    public static boolean isBoundaryTile(TETile t) {
        return t.character() == Tileset.WALL.character()
                || t.character() == Tileset.LOCKED_DOOR.character()
                || t.character() == Tileset.UNLOCKED_DOOR.character();
    }
}
