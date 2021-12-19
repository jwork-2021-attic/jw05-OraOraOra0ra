package world;


public class WorldBuilder {

    private int width;
    private int height;
    private static Tile[][] tiles;
    private static int[][] maze;

    public WorldBuilder(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new Tile[width][height];
        MazeBuilder mazeBuilder = new MazeBuilder(width,height);
        mazeBuilder.MazeBuilder();
        maze = mazeBuilder.mazeArray();
    }

    public WorldBuilder(int[][] map) {
        this.width = map.length;
        this.height = map[0].length;
        this.tiles = new Tile[width][height];
    }

    public World build() {
        return new World(tiles);
    }

    private WorldBuilder makeTiles() {
        for (int width = 0; width < this.width; width++) {
            for (int height = 0; height < this.height; height++) {
                switch (maze[width][height]) {
                    case 1:
                        tiles[width][height] = Tile.FLOORW;
                        break;
                    case 0:
                        tiles[width][height] = Tile.WALLB;
                        break;
                }
            }
        }
        return this;
    }

    private WorldBuilder makeTiles(int[][] map) {
        for (int width = 0; width < this.width; width++) {
            for (int height = 0; height < this.height; height++) {
                switch (map[width][height]) {
                    case 0:
                        tiles[width][height] = Tile.FLOORW;
                        break;
                    case 1:
                        tiles[width][height] = Tile.FLOORR;
                        break;
                    case 2:
                        tiles[width][height] = Tile.FLOORO;
                        break;
                    case 3:
                        tiles[width][height] = Tile.FLOORY;
                        break;
                    case 4:
                        tiles[width][height] = Tile.FLOORG;
                        break;
                    case 5:
                        tiles[width][height] = Tile.FLOORC;
                        break;
                    case 6:
                        tiles[width][height] = Tile.FLOORB;
                        break;
                    case 7:
                        tiles[width][height] = Tile.FLOORP;
                        break;
                    case 8:
                        tiles[width][height] = Tile.FLOORS;
                        break;
                    case 9:
                        tiles[width][height] = Tile.WALLB;
                        break;
                }
            }
        }
        return this;
    }

    public WorldBuilder makeMaze() {
        return makeTiles();
    }

    public WorldBuilder makeMaze(int[][] map) {
        return makeTiles(map);
    }

    public static int[][] getMaze() { return maze; }
}
