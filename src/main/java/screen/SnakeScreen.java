package screen;
import javafx.scene.input.KeyEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import world.*;

import java.util.ArrayList;
import java.util.List;

public class SnakeScreen implements Screen {

    private World world;

    private final ImageView snakeView = new ImageView(new Image("snake.png"));

    private final ImageView swordView = new ImageView(new Image("sword.png"));

    private final Image wall = new Image("wall.png");
    private final Image floorW = new Image("floorW.png");
    private final Image floorR = new Image("floorR.png");
    private final Image floorO = new Image("floorO.png");
    private final Image floorY = new Image("floorY.png");
    private final Image floorG = new Image("floorG.png");
    private final Image floorC = new Image("floorC.png");
    private final Image floorB = new Image("floorB.png");
    private final Image floorP = new Image("floorP.png");
    private final Image floorS = new Image("floorS.png");

    private Creature snake;

    private Creature[] hulus = new Creature[7];

    private int[][] creasLoc = new int[8][2];

    private String[] colorsName = {"red","orange","yellow","green","cyan","blue","purple"};

    private Color[] colors = {Color.RED,Color.ORANGE,Color.YELLOW,Color.GREEN,Color.CYAN,Color.BLUE,Color.PURPLE};

    private ImageView[] huluView = new ImageView[7];

    private int screenWidth;
    private int screenHeight;
    private List<String> messages;
    private List<String> oldMessages;
    public int[][] maze = MazeBuilder.mazeArray();

    private int dest[] = new int[2];

    public SnakeScreen(ImageView[] huluView, World world, int[][] creasLoc, boolean[] state) {
        this.creasLoc=creasLoc;
        this.world = world;
        world.changeState(state);
        this.world.deleteCreatures();
        this.screenWidth = 25;
        this.screenHeight = 20;
        this.messages = new ArrayList<String>();
        this.oldMessages = new ArrayList<String>();

        this.huluView = huluView;

        snakeView.setFitWidth(World.blockSize);
        snakeView.setFitHeight(World.blockSize);

        swordView.setFitWidth(World.blockSize);
        swordView.setFitHeight(World.blockSize);
        dest[0]=1;
        dest[1]=0;

        CreatureFactory creatureFactory = new CreatureFactory(this.world);
        createCreatures(creatureFactory);
    }

    private void createCreatures(CreatureFactory creatureFactory) {
        snake = creatureFactory.newPlayer(7, creasLoc[7][0],creasLoc[7][1],1,"snake","grey", Color.GREY, 100, 50, 0);

        for (int i = 0; i < 7; i++){
            if (world.boolAlive(i))
                this.hulus[i] = creatureFactory.newHuluwa(i, creasLoc[i][0],creasLoc[i][1], 1,String.valueOf(i+1),colorsName[i],colors[i],1000, 100, 0, 0);
        }

        Thread[] t = new Thread[7];
        for(int i = 0; i < 7; i++) {
            if (world.boolAlive(i)) {
                t[i] = new Thread(hulus[i].getAI());
                t[i].start();
            }
        }
    }

    private int displayTiles(GridPane grid, int left, int top) {
        // Show terrain
        int flag = 0;
        for (int x = 0; x < screenWidth; x++) {
            for (int y = 0; y < screenHeight; y++) {
                int wx = x + left;
                int wy = y + top;
                ImageView tmp = new ImageView(floorW);
                if (world.tile(wx,wy)==Tile.FLOORW) {
                    tmp = new ImageView(floorW);
                    world.setMap(wx, wy, 0);
                    flag++;
                } else if (world.tile(wx,wy)==Tile.WALLB) {
                    tmp = new ImageView(wall);
                    world.setMap(wx, wy, 9);
                } else if (world.tile(wx,wy)==Tile.FLOORR) {
                    tmp = new ImageView(floorR);
                    world.setMap(wx, wy, 1);
                    flag++;
                } else if (world.tile(wx,wy)==Tile.FLOORO) {
                    tmp = new ImageView(floorO);
                    world.setMap(wx, wy, 2);
                    flag++;
                } else if (world.tile(wx,wy)==Tile.FLOORY) {
                    tmp = new ImageView(floorY);
                    world.setMap(wx, wy, 3);
                    flag++;
                } else if (world.tile(wx,wy)==Tile.FLOORG) {
                    tmp = new ImageView(floorG);
                    world.setMap(wx, wy, 4);
                    flag++;
                } else if (world.tile(wx,wy)==Tile.FLOORC) {
                    tmp = new ImageView(floorC);
                    world.setMap(wx, wy, 5);
                    flag++;
                } else if (world.tile(wx,wy)==Tile.FLOORB) {
                    tmp = new ImageView(floorB);
                    world.setMap(wx, wy, 6);
                    flag++;
                } else if (world.tile(wx,wy)==Tile.FLOORP) {
                    tmp = new ImageView(floorP);
                    world.setMap(wx, wy, 7);
                    flag++;
                } else if (world.tile(wx,wy)==Tile.FLOORS) {
                    tmp = new ImageView(floorS);
                    world.setMap(wx, wy, 8);
                }

                tmp.setFitWidth(World.blockSize);
                tmp.setFitHeight(World.blockSize);
                grid.add(tmp, x, y);

            }
        }

        world.creatureLock.lock();
        try {
            for (int i = 0; i < 7; i++) {
                if(world.boolAlive(i))
                    if (world.creature(hulus[i].x(), hulus[i].y())==hulus[i])
                        grid.add(huluView[i], hulus[i].x(), hulus[i].y());
            }
            if(world.boolAlive(7)) {
                grid.add(snakeView, snake.x(), snake.y());
                if (snake.legal(dest[0], dest[1]))
                    grid.add(swordView, snake.x() + dest[0], snake.y() + dest[1]);
            }
        } finally {
            world.creatureLock.unlock();
        }

        world.update();

        for(int i = 0; i < 7; i++) {
            if (world.boolAlive(i)) {
                creasLoc[i][0] = hulus[i].x();
                creasLoc[i][1] = hulus[i].y();
            }
        }
        if (world.boolAlive(7)) {
            creasLoc[7][0] = snake.x();
            creasLoc[7][1] = snake.y();
        }

        if (flag == 0)
            return 1;
        return 0;
    }

    @Override
    public Screen displayOutput(GridPane grid) {
        // Terrain and creatures
        displayTiles(grid, getScrollX(), getScrollY());

        return this;
    }

    public int display(GridPane grid) {
        // Terrain and creatures
        return displayTiles(grid, getScrollX(), getScrollY());
    }

    public void displayMsg(Text msg) {
        msg.setText(snake.getMsg());
    }

    @Override
    public SnakeScreen respondToUserInput(KeyEvent key) {
        switch (key.getCode()) {
            case LEFT:
                dest[0]=-1;
                dest[1]=0;
                snake.moveBy(-1, 0, dest);
                break;
            case RIGHT:
                dest[0]=1;
                dest[1]=0;
                snake.moveBy(1, 0, dest);
                break;
            case UP:
                dest[0]=0;
                dest[1]=-1;
                snake.moveBy(0, -1, dest);
                break;
            case DOWN:
                dest[0]=0;
                dest[1]=1;
                snake.moveBy(0, 1, dest);
                break;
        }

        return this;
    }

    public int getScrollX() {
        //return Math.max(0, Math.min(player1.x() - screenWidth / 2, world.width() - screenWidth));
        return Math.max(0, Math.min(snake.x() - screenWidth / 2, world.width() - screenWidth));
    }

    public int getScrollY() {
        //return Math.max(0, Math.min(player1.y() - screenHeight / 2, world.height() - screenHeight));
        return Math.max(0, Math.min(snake.y() - screenHeight / 2, world.height() - screenHeight));
    }

    public World getWorld() {
        return this.world;
    }

    public int[][] getCreaturesLocation() {
        return creasLoc;
    }
}
