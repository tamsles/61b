package core;

import tileengine.TETile;
import tileengine.Tileset;
import utils.FileUtils;
import utils.RandomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {

    // build your own world!
    private static final int WIDTH = 80;
    private static final int HEIGHT = 40;

    private static final String SAVE_FILE = "save-file.txt";

    private final long seed;
    private final Random random;
    private final List<Room> rooms;

    private TETile[][] world;
    private int avatarX;
    private int avatarY;

    private int keyX;
    private int keyY;
    private boolean hasKey;
    private boolean keyCollected;

    private int doorX;
    private int doorY;
    private boolean gameWon;

    private int steps;

    private TETile tileUnderAvatar;

    public World(long seed) {
        this.seed = seed;
        this.random = new Random(seed);
        this.rooms = new ArrayList<>();
    }

    private static class Room {
        private final int x;
        private final int y;
        private final int width;
        private final int height;

        private Room(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        int centerX() {
            return x + (width / 2);
        }
        int centerY() {
            return y + (height / 2);
        }

        boolean overlaps(Room other) {
            return this.x < other.x + other.width
                    && this.x + this.width > other.x
                    && this.y < other.y + other.height
                    && this.y + this.height > other.y;
        }
    }

    public TETile[][] generate() {
        this.world = initializeWorld();

        addRandomRooms(this.world);
        connectRooms(this.world);
        addWalls(this.world);
        placeAvatar();

        placeDoor();
        placeKey();
        placeAvatar();

        this.hasKey = false;
        this.keyCollected = false;
        this.gameWon = false;
        this.steps = 0;

        tileUnderAvatar = Tileset.FLOOR;
        
        return this.world;
    }

    private void placeKey() {
        while (true) {
            int x = RandomUtils.uniform(random, 1, WIDTH - 1);
            int y = RandomUtils.uniform(random, 1, HEIGHT - 1);

            if(world[x][y] == Tileset.FLOOR) {
                keyX = x;
                keyY = y;
                world[x][y] = Tileset.FLOWER;
                return;
            }
        }
    }

    private void placeDoor() {
        while (true) {
            int x = RandomUtils.uniform(random, 1, WIDTH - 1);
            int y = RandomUtils.uniform(random, 1, HEIGHT - 1);

            if (world[x][y] == Tileset.FLOOR) {
                doorX = x;
                doorY = y;
                world[doorX][doorY] = Tileset.LOCKED_DOOR;
                return;
            }
        }
    }

    private void placeAvatar() {
        if(rooms.isEmpty()) {
            throw new RuntimeException("No rooms found!");
        }

        Room firstRoom = rooms.get(0);
        avatarX = firstRoom.centerX();
        avatarY = firstRoom.centerY();

        tileUnderAvatar = Tileset.FLOOR;
        world[avatarX][avatarY] = Tileset.AVATAR;
    }

    public TETile[][] getTiles() {
        return world;
    }

    public void moveAvatar(char key) {
        key = Character.toLowerCase(key);

        int nextX = avatarX;
        int nextY = avatarY;

        if (key == 'w') {
            nextY += 1;
        } else if (key == 'a') {
            nextX -= 1;
        } else if (key == 's') {
            nextY -= 1;
        } else if (key == 'd') {
            nextX += 1;
        } else {
            return;
        }
        
        if(canMoveTo(nextX, nextY)){
            TETile target = world[nextX][nextY];

            handleSpecialTile(nextX, nextY);

            world[avatarX][avatarY] = tileUnderAvatar;

            if (target == Tileset.FLOWER) {
                tileUnderAvatar = Tileset.FLOOR;
            } else if (target == Tileset.LOCKED_DOOR && hasKey) {
                tileUnderAvatar = Tileset.UNLOCKED_DOOR;
            } else {
                tileUnderAvatar = target;
            }

            avatarX = nextX;
            avatarY = nextY;
            world[avatarX][avatarY] = Tileset.AVATAR;

            steps++;
        }

    }

    private void handleSpecialTile(int x, int y) {
        TETile target = world[x][y];

        if(target == Tileset.FLOWER) {
            hasKey = true;
            keyCollected = true;
        }

        if(target == Tileset.LOCKED_DOOR &&  keyCollected) {
            gameWon = true;
            world[x][y] = Tileset.UNLOCKED_DOOR;
            keyCollected = false;
        }

        if (target == Tileset.UNLOCKED_DOOR) {
            gameWon = true;
        }
    }

    private boolean canMoveTo(int nextX, int nextY) {
        if (nextX < 0 || nextX >= WIDTH || nextY < 0 || nextY >= HEIGHT) {
            return false;
        }

        TETile target = world[nextX][nextY];

        if (target == Tileset.FLOOR || target == Tileset.FLOWER || target == Tileset.UNLOCKED_DOOR
        || (target == Tileset.LOCKED_DOOR && hasKey)) {
            return true;
        }

        return false;
    }

    private TETile[][] initializeWorld() {
        TETile[][] world = new TETile[WIDTH][HEIGHT];

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }

        return world;
    }

    private void addRandomRooms(TETile[][] world) {
        int targetRooms = RandomUtils.uniform(random, 15, 30);
        int attempts = 0;
        while (attempts < 1000 && rooms.size() < targetRooms) {
            attempts++;

            int width = RandomUtils.uniform(random, 4, 10);
            int height = RandomUtils.uniform(random, 4, 8);

            int x = RandomUtils.uniform(random, 1, WIDTH - width - 1);
            int y = RandomUtils.uniform(random, 1, HEIGHT - height - 1);

            Room room = new Room(x, y, width, height);

            if (!overlapsAny(room)) {
                drawRoom(world, room);
                rooms.add(room);
            }
        }
    }

    private boolean overlapsAny(Room room) {
        for (Room other : rooms) {
            if (other.overlaps(room)) {
                return true;
            }
        }

        return false;
    }

    private void drawRoom(TETile[][] world, Room room) {
        for (int x = room.x; x < room.x + room.width; x++) {
            for (int y = room.y; y < room.y + room.height; y++) {
                world[x][y] = Tileset.FLOOR;
            }
        }
    }

    private void connectRooms(TETile[][] world) {
        for (int i = 0; i < rooms.size() - 1; i++) {
            Room first = rooms.get(i);
            Room second = rooms.get(i + 1);

            int x1 = first.centerX();
            int y1 = first.centerY();
            int x2 = second.centerX();
            int y2 = second.centerY();

            if (random.nextBoolean()) {
                drawHorizontalHallway(world, x1, x2, y1);
                drawVerticalHallway(world, y1, y2, x2);
            } else {
                drawVerticalHallway(world, y1, y2, x1);
                drawHorizontalHallway(world, x1, x2, y2);
            }
        }
    }

    private void drawVerticalHallway(TETile[][] world, int y1, int y2, int x2) {
        int start = Math.min(y1, y2);
        int end = Math.max(y1, y2);

        for (int y = start; y <= end; y++) {
            world[x2][y] = Tileset.FLOOR;
        }
    }

    private void drawHorizontalHallway(TETile[][] world, int x1, int x2, int y1) {
        int start = Math.min(x1, x2);
        int end = Math.max(x1, x2);

        for(int i = start; i <= end; i++) {
            world[i][y1] = Tileset.FLOOR;
        }
    }

    private void addWalls(TETile[][] world) {
        for (int x = 1; x < WIDTH - 1; x++) {
            for (int y = 1; y < HEIGHT - 1; y++) {
                if (world[x][y] == Tileset.NOTHING && adjacentToFloor(world, x, y)) {
                    world[x][y] = Tileset.WALL;
                }
            }
        }
    }

    private boolean adjacentToFloor(TETile[][] world, int x, int y) {
        for(int dx = -1;  dx <= 1; dx++) {
            for(int dy = -1; dy <= 1; dy++) {
                int nx = x + dx;
                int ny = y + dy;

                if(world[nx][ny] == Tileset.FLOOR) {
                    return true;
                }
            }
        }

        return false;
    }

    public void save() {
        String contents = seed + "\n"
                + avatarX + "\n"
                + avatarY + "\n"
                + hasKey + "\n"
                + keyCollected + "\n"
                + keyX + "\n"
                + keyY + "\n"
                + doorX + "\n"
                + doorY + "\n"
                + gameWon + "\n"
                + steps;

        FileUtils.writeFile(SAVE_FILE, contents);
    }

    public static World load() {
        if (!FileUtils.fileExists(SAVE_FILE)) {
            return null;
        }

        String contents = FileUtils.readFile(SAVE_FILE);
        String[] lines = contents.split("\n");

        long seed = Long.parseLong(lines[0]);
        int savedX = Integer.parseInt(lines[1]);
        int savedY = Integer.parseInt(lines[2]);
        boolean savedHasKey = Boolean.parseBoolean(lines[3]);
        boolean savedKeyCollected = Boolean.parseBoolean(lines[4]);
        int savedKeyX = Integer.parseInt(lines[5]);
        int savedKeyY = Integer.parseInt(lines[6]);
        int savedDoorX = Integer.parseInt(lines[7]);
        int savedDoorY = Integer.parseInt(lines[8]);
        boolean savedGameWon = Boolean.parseBoolean(lines[9]);
        int savedSteps = Integer.parseInt(lines[10]);

        World loadedWorld = new World(seed);
        loadedWorld.generate();

        loadedWorld.hasKey = savedHasKey;
        loadedWorld.keyCollected = savedKeyCollected;
        loadedWorld.keyX = savedKeyX;
        loadedWorld.keyY = savedKeyY;
        loadedWorld.doorX = savedDoorX;
        loadedWorld.doorY = savedDoorY;
        loadedWorld.gameWon = savedGameWon;
        loadedWorld.steps = savedSteps;

        loadedWorld.restoreSpecialTiles();

        loadedWorld.setAvatarPosition(savedX, savedY);

        return loadedWorld;
    }

    private void restoreSpecialTiles() {
        if (!keyCollected) {
            world[keyX][keyY] = Tileset.FLOWER;
        } else {
            world[keyX][keyY] = Tileset.FLOOR;
        }

        if (gameWon) {
            world[doorX][doorY] = Tileset.UNLOCKED_DOOR;
        } else {
            world[doorX][doorY] = Tileset.LOCKED_DOOR;
        }
    }

    private void setAvatarPosition(int savedX, int savedY) {
        world[avatarX][avatarY] = tileUnderAvatar == null ? Tileset.FLOOR : tileUnderAvatar;

        avatarX = savedX;
        avatarY = savedY;

        tileUnderAvatar = world[avatarX][avatarY];

        if (tileUnderAvatar == Tileset.FLOWER) {
            tileUnderAvatar = Tileset.FLOOR;
        }

        world[avatarX][avatarY] = Tileset.AVATAR;
    }

    public String tileDescriptionAt(int x, int y) {
        if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT) {
            return "";
        }

        return world[x][y].description();
    }

    public boolean hasKey() {
        return hasKey;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public int getSteps() {
        return steps;
    }

    public TETile[][] getVisibleTiles() {
        TETile[][] visible = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                visible[x][y] = Tileset.NOTHING;
            }
        }

        int radius = 5;
        for (int x = avatarX - radius; x <= avatarX + radius; x++) {
            for (int y = avatarY - radius; y <= avatarY + radius; y++) {
                if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT){
                    int dx = x - avatarX;
                    int dy = y - avatarY;

                    if (dx * dx + dy * dy <= radius * radius) {
                        visible[x][y] = world[x][y];
                    }
                }
            }
        }

        return visible;
    }
}
