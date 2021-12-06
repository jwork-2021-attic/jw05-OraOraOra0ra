/*
 * Copyright (C) 2015 Aeranythe Echosong
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package screen;

import start.PlotScene;
import start.StartScene;
import world.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Aeranythe Echosong
 */
public class PlayScreen implements Screen {

    private World world;

    private final ImageView snakeView = new ImageView(new Image("snake.png"));

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

    private String[] colorsName = {"red","orange","yellow","green","cyan","blue","purple"};

    private Color[] colors = {Color.RED,Color.ORANGE,Color.YELLOW,Color.GREEN,Color.CYAN,Color.BLUE,Color.PURPLE};

    private ImageView[] huluView = new ImageView[7];

    private Creature Athena;
    private int screenWidth;
    private int screenHeight;
    private List<String> messages;
    private List<String> oldMessages;
    public int[][] maze = MazeBuilder.mazeArray();

    public PlayScreen(ImageView[] huluView) {
        this.screenWidth = 25;
        this.screenHeight = 20;
        createWorld();
        this.messages = new ArrayList<String>();
        this.oldMessages = new ArrayList<String>();

        this.huluView = huluView;

        snakeView.setFitWidth(World.blockSize);
        snakeView.setFitHeight(World.blockSize);

        CreatureFactory creatureFactory = new CreatureFactory(this.world);
        createCreatures(creatureFactory);
    }

    private void createCreatures(CreatureFactory creatureFactory) {

        snake = creatureFactory.newHuluwa(1,"snake","grey", Color.GREY);

        for (int i = 0; i < 7; i++){
            this.hulus[i] = creatureFactory.newHuluwa(1,String.valueOf(i+1),colorsName[i],colors[i]);
        }

        Thread[] t = new Thread[7];
        for(int i = 0; i < 7; i++) {
            t[i] = new Thread(hulus[i].getAI());
            t[i].start();
        }

        Thread ts = new Thread(snake.getAI());
        ts.start();
    }

    private void createWorld() {
        world = new WorldBuilder(this.screenWidth, this.screenHeight).makeMaze().build();
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
                    flag++;
                } else if (world.tile(wx,wy)==Tile.WALLB) {
                    tmp = new ImageView(wall);
                } else if (world.tile(wx,wy)==Tile.FLOORR) {
                    tmp = new ImageView(floorR);
                } else if (world.tile(wx,wy)==Tile.FLOORO) {
                    tmp = new ImageView(floorO);
                } else if (world.tile(wx,wy)==Tile.FLOORY) {
                    tmp = new ImageView(floorY);
                } else if (world.tile(wx,wy)==Tile.FLOORG) {
                    tmp = new ImageView(floorG);
                } else if (world.tile(wx,wy)==Tile.FLOORC) {
                    tmp = new ImageView(floorC);
                } else if (world.tile(wx,wy)==Tile.FLOORB) {
                    tmp = new ImageView(floorB);
                } else if (world.tile(wx,wy)==Tile.FLOORP) {
                    tmp = new ImageView(floorP);
                } else if (world.tile(wx,wy)==Tile.FLOORS) {
                    tmp = new ImageView(floorS);
                }

                tmp.setFitWidth(World.blockSize);
                tmp.setFitHeight(World.blockSize);
                grid.add(tmp, x, y);

            }
        }

        world.creatureLock.lock();
        try {
            for (int i = 0; i < 7; i++) {
                grid.add(huluView[i], hulus[i].x(), hulus[i].y());
            }
            grid.add(snakeView,snake.x(),snake.y());
        } finally {
            world.creatureLock.unlock();
        }

        world.update();

        if (flag < 1)
            return 1;
        return 0;
    }

    public void displayMessages(Text msg) {

        /*int top = this.screenHeight;
        for (int i = 0; i < messages.size(); i++) {
            switch (i) {
                case 0:
                    terminal.write("Green:" + messages.get(i), 1, top + i + 1);
                    break;
                case 1:
                    terminal.write("Yellow:" + messages.get(i), 1, top + i + 1);
                    break;
                case 2:
                    terminal.write("Cyan:" + messages.get(i), 1, top + i + 1);
                    break;
                case 3:
                    terminal.write("Magenta:" + messages.get(i), 1, top + i + 1);
                    break;
                case 4:
                    terminal.write("White:" + messages.get(i), 1, top + i + 1);
                    break;
                case 5:
                    terminal.write("Red:" + messages.get(i), 1, top + i + 1);
                    break;
                case 6:
                    terminal.write("Blue:" + messages.get(i), 1, top + i + 1);
                    break;
            }
        }
        this.oldMessages.addAll(messages);
        messages.clear();*/
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

    @Override
    public Screen respondToUserInput(KeyEvent key) {
       /* switch (key.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                player1.moveBy(-1, 0);
                break;
            case KeyEvent.VK_RIGHT:
                player1.moveBy(1, 0);
                break;
            case KeyEvent.VK_UP:
                player1.moveBy(0, -1);
                break;
            case KeyEvent.VK_DOWN:
                player1.moveBy(0, 1);
                break;
            case KeyEvent.VK_A:
                player2.moveBy(-1, 0);
                break;
            case KeyEvent.VK_D:
                player2.moveBy(1, 0);
                break;
            case KeyEvent.VK_W:
                player2.moveBy(0, -1);
                break;
            case KeyEvent.VK_S:
                player2.moveBy(0, 1);
                break;
        }*/

        /*if (judgeEnd()==1)
            return new WinScreen("player1");
        else if (judgeEnd()==2)
            return new WinScreen("player2");*/

        return this;
    }

    public int getScrollX() {
        //return Math.max(0, Math.min(player1.x() - screenWidth / 2, world.width() - screenWidth));
        return Math.max(0, Math.min(hulus[0].x() - screenWidth / 2, world.width() - screenWidth));
    }

    public int getScrollY() {
        //return Math.max(0, Math.min(player1.y() - screenHeight / 2, world.height() - screenHeight));
        return Math.max(0, Math.min(hulus[0].y() - screenHeight / 2, world.height() - screenHeight));
    }

}
