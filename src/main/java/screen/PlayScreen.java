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
import javafx.scene.input.KeyEvent;
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

import static world.World.blockSize;

/**
 *
 * @author Aeranythe Echosong
 */
public class PlayScreen implements Screen {

    private World world;

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

    private Creature[] creas = new Creature[8];

    private int[][] creasLoc = new int[8][2];

    private int[] score = new int[8];

    private String[] colorsName = {"red","orange","yellow","green","cyan","blue","purple"};

    private Color[] colors = {Color.RED,Color.ORANGE,Color.YELLOW,Color.GREEN,Color.CYAN,Color.BLUE,Color.PURPLE};

    private ImageView[] huluView = new ImageView[7];

    private final ImageView snakeView = new ImageView(new Image("snake.png"));

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

        snakeView.setFitWidth(blockSize);
        snakeView.setFitHeight(blockSize);

        CreatureFactory creatureFactory = new CreatureFactory(this.world);
        createCreatures(creatureFactory);

        initColorTiles();
    }

    public PlayScreen(ImageView[] huluView, World world, int[][] creasLoc, int[] score) {
        this.creasLoc = creasLoc;
        this.screenWidth = 25;
        this.screenHeight = 20;
        this.world = world;
        this.messages = new ArrayList<String>();
        this.oldMessages = new ArrayList<String>();

        this.huluView = huluView;

        snakeView.setFitWidth(blockSize);
        snakeView.setFitHeight(blockSize);

        CreatureFactory creatureFactory = new CreatureFactory(this.world);
        createCreatures(creatureFactory, creasLoc, score);

        initColorTiles();
    }

    private void createCreatures(CreatureFactory creatureFactory) {

        snake = creatureFactory.newHuluwa(7, 1,"snake","grey", Color.GREY, 50, 100, 0, 0);

        for (int i = 0; i < 7; i++){
            this.hulus[i] = creatureFactory.newHuluwa(i, 1,String.valueOf(i+1),colorsName[i],colors[i], 50, 100, 0, 0);
        }

        Thread[] t = new Thread[7];
        for(int i = 0; i < 7; i++) {
            t[i] = new Thread(hulus[i].getAI());
            t[i].start();
        }

        Thread ts = new Thread(snake.getAI());
        ts.start();
    }

    private void createCreatures(CreatureFactory creatureFactory, int[][] creasLoc, int[] score) {
        snake = creatureFactory.newHuluwa(7, creasLoc[7][0],creasLoc[7][1],score[7],"snake","grey", Color.GREY, 50, 100, 0, 0);

        for (int i = 0; i < 7; i++){
            this.hulus[i] = creatureFactory.newHuluwa(i, creasLoc[i][0],creasLoc[i][1], score[i],String.valueOf(i+1),colorsName[i],colors[i],50, 100, 0, 0);
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

    private void initColorTiles() {
        world.setTile(hulus[0].x(),hulus[0].y(),Tile.FLOORR);
        world.setTile(hulus[1].x(),hulus[1].y(),Tile.FLOORO);
        world.setTile(hulus[2].x(),hulus[2].y(),Tile.FLOORY);
        world.setTile(hulus[3].x(),hulus[3].y(),Tile.FLOORG);
        world.setTile(hulus[4].x(),hulus[4].y(),Tile.FLOORC);
        world.setTile(hulus[5].x(),hulus[5].y(),Tile.FLOORB);
        world.setTile(hulus[6].x(),hulus[6].y(),Tile.FLOORP);
        world.setTile(snake.x(),snake.y(),Tile.FLOORS);
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
                } else if (world.tile(wx,wy)==Tile.FLOORO) {
                    tmp = new ImageView(floorO);
                    world.setMap(wx, wy, 2);
                } else if (world.tile(wx,wy)==Tile.FLOORY) {
                    tmp = new ImageView(floorY);
                    world.setMap(wx, wy, 3);
                } else if (world.tile(wx,wy)==Tile.FLOORG) {
                    tmp = new ImageView(floorG);
                    world.setMap(wx, wy, 4);
                } else if (world.tile(wx,wy)==Tile.FLOORC) {
                    tmp = new ImageView(floorC);
                    world.setMap(wx, wy, 5);
                } else if (world.tile(wx,wy)==Tile.FLOORB) {
                    tmp = new ImageView(floorB);
                    world.setMap(wx, wy, 6);
                } else if (world.tile(wx,wy)==Tile.FLOORP) {
                    tmp = new ImageView(floorP);
                    world.setMap(wx, wy, 7);
                } else if (world.tile(wx,wy)==Tile.FLOORS) {
                    tmp = new ImageView(floorS);
                    world.setMap(wx, wy, 8);
                }

                tmp.setFitWidth(blockSize);
                tmp.setFitHeight(blockSize);
                grid.add(tmp, x, y);

            }
        }

        world.creatureLock.lock();
        try {
            int j = screenHeight+1;
            for (int i = 0; i < 7; i++) {
                if (world.creature(hulus[i].x(), hulus[i].y())==hulus[i])
                    grid.add(huluView[i], hulus[i].x(), hulus[i].y());
            }
            grid.add(snakeView,snake.x(),snake.y());
        } finally {
            world.creatureLock.unlock();
        }

        world.update();

        for(int i = 0; i < 7; i++) {
            creasLoc[i][0] = hulus[i].x();
            creasLoc[i][1] = hulus[i].y();
            score[i] = hulus[i].getSize();
        }
        creasLoc[7][0] = snake.x();
        creasLoc[7][1] = snake.y();
        score[7] = snake.getSize();

        if (flag < 20) {
            for(int i = 0; i < 7; i++) {
                hulus[i].close();
                creas[i] = hulus[i];
            }
            snake.close();
            creas[7] = snake;
            return 1;
        }
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

    public void displaySize(Text msg) {
        String text = "";
        for(int i = 0; i < 7; i++) {
            text += hulus[i].getSize();
            if (i % 2 == 0)
                text += "                                                      \t";
            else
                text += "\n\n";
        }
        text += snake.getSize();
        text += "\n";
        msg.setText(text);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
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

    public World getWorld() {
        return this.world;
    }

    public Creature[] getCreatures() {
        return creas;
    }

    public int[][] getCreaturesLocation() {
        return creasLoc;
    }

    public int[] getCreaturesScore() {
        return score;
    }
}
