package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;

public class Main {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 40;
    private static final int HUD_HEIGHT = 2;
    
    public static void main(String[] args) {

        // build your own world!
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT + HUD_HEIGHT);
        
        showMenu();

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toLowerCase(StdDraw.nextKeyTyped());

                if (c == 'n') {
                    String seedString = getSeedFromUser();
                    World world = new World(Long.parseLong(seedString));
                    world.generate();
                    runGameLoop(ter, world);
                    return;

                } else if (c == 'l') {
                    World world = World.load();

                    if (world == null) {
                        showMenu();
                        continue;
                    }

                    runGameLoop(ter, world);
                    return;

                } else if (c == 'q') {
                    return;
                }
            }
        }

//        TETile[][] world = AutograderBuddy.getWorldFromInput("N1245S");
//        ter.renderFrame(world);
    }

    private static void runGameLoop(TERenderer ter, World world) {
        ter.renderFrame(world.getTiles());

        boolean waitForQ = false;

        while (true) {
            drawHud(world);

            if(StdDraw.hasNextKeyTyped()){
                char c =  Character.toLowerCase(StdDraw.nextKeyTyped());

                if (waitForQ) {
                    if (c == 'q') {
                        world.save();
                        return;
                    }

                    waitForQ = false;
                }

                if(c == ':'){
                    waitForQ = true;
                }else {
                    world.moveAvatar(c);
                    ter.renderFrame(world.getVisibleTiles());
                }
            }
        }
    }

    private static void drawHud(World world) {
        int mouseX = (int) StdDraw.mouseX();
        int mouseY = (int) StdDraw.mouseY();

        String description = world.tileDescriptionAt(mouseX, mouseY);

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledRectangle(40, 41, 40, 1);

        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.textLeft(1, 41, "Tile: " + description);
        StdDraw.textLeft(1, 41, "Tile: " + description);
        StdDraw.textLeft(20, 41, "Has Key: " + world.hasKey());
        StdDraw.textLeft(40, 41, "Steps: " + world.getSteps());

        if (world.isGameWon()) {
            StdDraw.textLeft(55, 41, "You win!");
        }

        StdDraw.show();
    }

    private static String getSeedFromUser() {
        String seed = "";

        while (true) {
            StdDraw.clear();
            StdDraw.text(40, 28, "Enter seed:");
            StdDraw.text(40, 24, seed);
            StdDraw.text(40, 20, "Press S to start");
            StdDraw.show();

            if(StdDraw.hasNextKeyTyped()){
                char c = StdDraw.nextKeyTyped();

                if (Character.isDigit(c)) {
                    seed += c;
                } else if (Character.toLowerCase(c) == 's') {
                    return seed;
                }
            }
        }
    }

    private static void showMenu() {
        StdDraw.clear();
        StdDraw.text(40, 28, "CS61B: BYOW");
        StdDraw.text(40, 22, "New World (N)");
        StdDraw.text(40, 20, "Load World (L)");
        StdDraw.text(40, 18, "Quit (Q)");
        StdDraw.show();
    }
}
