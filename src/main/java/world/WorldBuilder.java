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

    public WorldBuilder makeMaze() {
        return makeTiles();
    }

    public static int[][] getMaze() { return maze; }
}
