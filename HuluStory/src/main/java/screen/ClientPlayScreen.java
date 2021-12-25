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

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

/**
 *
 * @author Aeranythe Echosong
 */
public class ClientPlayScreen {

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
    private final Image floorF = new Image("floorF.png");

    private ImageView[] huluView = new ImageView[7];

    private final ImageView snakeView = new ImageView(new Image("snake.png"));
    private final ImageView oldView = new ImageView(new Image("grandfather.png"));

    private int screenWidth;
    private int screenHeight;

    public ClientPlayScreen() {
        this.screenWidth = 25;
        this.screenHeight = 20;
        for (int i = 0; i < 7; i++) {
            huluView[i] = new ImageView(new Image((i + 1) + ".png"));
        }
    }

    public void display(GridPane grid, int[][] map, int[][] creasLoc) {
        // Terrain and creatures
        for (int i = 0; i < screenWidth; i++) {
            for (int j = 0; j < screenHeight; j++) {
                ImageView tmp = new ImageView(floorW);
                switch (map[i][j]) {
                    case 0:
                        break;
                    case 1:
                        tmp = new ImageView(floorR);
                        break;
                    case 2:
                        tmp = new ImageView(floorO);
                        break;
                    case 3:
                        tmp = new ImageView(floorY);
                        break;
                    case 4:
                        tmp = new ImageView(floorG);
                        break;
                    case 5:
                        tmp = new ImageView(floorC);
                        break;
                    case 6:
                        tmp = new ImageView(floorB);
                        break;
                    case 7:
                        tmp = new ImageView(floorP);
                        break;
                    case 8:
                        tmp = new ImageView(floorS);
                        break;
                    case 9:
                        tmp = new ImageView(floorF);
                        break;
                    case 10:
                        tmp = new ImageView(wall);
                        break;
                }
                tmp.setFitHeight(25);
                tmp.setFitWidth(25);
                grid.add(tmp,i,j);
            }
        }

        for(int i = 0; i < 7; i++) {
            huluView[i].setFitWidth(25);
            huluView[i].setFitHeight(25);
            grid.add(huluView[i],creasLoc[i][0],creasLoc[i][1]);
        }
        snakeView.setFitWidth(25);
        snakeView.setFitHeight(25);
        grid.add(snakeView,creasLoc[7][0],creasLoc[7][1]);
        oldView.setFitWidth(25);
        oldView.setFitHeight(25);
        grid.add(oldView,creasLoc[8][0],creasLoc[8][1]);
    }

    public void displaySize(Text msg, int[] score) {
        String text = "";
        for(int i = 0; i < 7; i++) {
            text += score[i];
            if (i % 2 == 0)
                text += "                                                      \t";
            else
                text += "\n\n";
        }
        text += "\n\n";
        text += score[7];
        text += "                                                      \t";
        text += score[8];
        msg.setText(text);
    }

}
