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

import javafx.scene.input.KeyCode;
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

    private Creature snake;

    private Creature old;

    private Creature[] hulus = new Creature[7];

    private Creature[] creas = new Creature[9];

    private int[][] creasLoc = new int[9][2];

    private int[] score = new int[9];

    private String[] colorsName = {"red","orange","yellow","green","cyan","blue","purple"};

    private Color[] colors = {Color.RED,Color.ORANGE,Color.YELLOW,Color.GREEN,Color.CYAN,Color.BLUE,Color.PURPLE};

    private Boolean isStart = false;

    private int screenWidth;
    private int screenHeight;

    public int[][] maze = MazeBuilder.mazeArray();

    public PlayScreen() {
        this.screenWidth = 25;
        this.screenHeight = 20;
        createWorld();

        CreatureFactory creatureFactory = new CreatureFactory(this.world);
        createCreatures(creatureFactory);

        initColorTiles();
    }

    private void createCreatures(CreatureFactory creatureFactory) {

        snake = creatureFactory.newPlayer(7, 1,"snake","grey", Color.GREY, 100, 0, 0);

        old = creatureFactory.newPlayer(8, 1,"old","black", Color.BLACK, 100, 0, 0);

        for (int i = 0; i < 7; i++){
            this.hulus[i] = creatureFactory.newHuluwa(i, 1,String.valueOf(i+1),colorsName[i],colors[i], 200, 100, 0, 0);
        }

        //Thread ts = new Thread(snake.getAI());
        //ts.start();
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
        world.setTile(old.x(),old.y(),Tile.FLOORF);
    }

    public int displayTiles() {
        // Show terrain
        int flag = 0;
        for (int x = 0; x < screenWidth; x++) {
            for (int y = 0; y < screenHeight; y++) {
                if (world.tile(x,y)==Tile.FLOORW) {
                    world.setMap(x, y, 0);
                    flag++;
                } else if (world.tile(x,y)==Tile.WALLB) {
                    world.setMap(x, y, 10);
                } else if (world.tile(x,y)==Tile.FLOORR) {
                    world.setMap(x, y, 1);
                } else if (world.tile(x,y)==Tile.FLOORO) {
                    world.setMap(x, y, 2);
                } else if (world.tile(x,y)==Tile.FLOORY) {
                    world.setMap(x, y, 3);
                } else if (world.tile(x,y)==Tile.FLOORG) {
                    world.setMap(x, y, 4);
                } else if (world.tile(x,y)==Tile.FLOORC) {
                    world.setMap(x, y, 5);
                } else if (world.tile(x,y)==Tile.FLOORB) {
                    world.setMap(x, y, 6);
                } else if (world.tile(x,y)==Tile.FLOORP) {
                    world.setMap(x, y, 7);
                } else if (world.tile(x,y)==Tile.FLOORS) {
                    world.setMap(x, y, 8);
                }  else if (world.tile(x,y)==Tile.FLOORF) {
                    world.setMap(x, y, 9);
                }
            }
        }

        world.update();

        for(int i = 0; i < 7; i++) {
            creasLoc[i][0] = hulus[i].x();
            creasLoc[i][1] = hulus[i].y();
            score[i] = hulus[i].getSize();
        }
        creasLoc[7][0] = snake.x();
        creasLoc[7][1] = snake.y();
        creasLoc[8][0] = old.x();
        creasLoc[8][1] = old.y();
        score[7] = snake.getSize();
        score[8] = old.getSize();

        if (flag < 1) {

            for(int i = 0; i < 7; i++) {
                hulus[i].close();
                creas[i] = hulus[i];
            }
            snake.close();
            old.close();
            creas[7] = snake;
            creas[8] = old;
            return 1;
        }
        return 0;
    }

    @Override
    public Screen displayOutput(GridPane grid) {
        // Terrain and creatures
        displayTiles();

        return this;
    }

    public int display(GridPane grid) {
        // Terrain and creatures
        return displayTiles();
    }

    @Override
    public Screen respondToUserInput(KeyCode key, int playerNo) {
        Creature player = null;
        if (playerNo == 0)
            player = snake;
        else if (playerNo == 1)
            player = old;
        switch (key) {
            case LEFT:
                player.moveBy(-1, 0);
                break;
            case RIGHT:
                player.moveBy(1, 0);
                break;
            case UP:
                player.moveBy(0, -1);
                break;
            case DOWN:
                player.moveBy(0, 1);
                break;
        }

        if (isStart == false) {
            Thread[] t = new Thread[7];
            for (int i = 0; i < 7; i++) {
                t[i] = new Thread(hulus[i].getAI());
                t[i].start();
            }
            isStart = true;
        }

        return this;
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
