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
package world;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import screen.WinScreen;

import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Aeranythe Echosong
 */
public class Creature implements Serializable {

    private World world;

    private int No;

    private int x;

    public void setX(int x) {
        this.x = x;
    }

    public int x() {
        return x;
    }

    private int y;

    public void setY(int y) {
        this.y = y;
    }

    public int y() {
        return y;
    }

    private Color color;

    public Color color() {
        return this.color;
    }

    private CreatureAI ai;

    private String colorName;

    private ImageView imageView;

    int size;

    public void setAI(CreatureAI ai) {
        this.ai = ai;
    }

    public CreatureAI getAI() {
        return this.ai;
    }

    public int[][] maze = MazeBuilder.mazeArray();

    private int maxHP;

    private int attack;

    private int defense;

    public int maxHP() {
        return this.maxHP;
    }

    private int hp;

    public int hp() {
        return this.hp;
    }

    public void modifyHP(int amount) {
        this.hp += amount;

        if (this.hp < 1) {
            world.remove(this);
            close();
        }
    }


    public int attackValue() {
        return this.attack;
    }

    public int defenseValue() {
        return this.defense;
    }

    public void attack(Creature other) {
        if (this.attack-other.defenseValue()>0)
            other.modifyHP(other.defenseValue()-this.attack);
        if (this.defense-other.attackValue()<0)
            modifyHP(this.defense-other.attackValue());
    }

    private int visionRadius;

    public int visionRadius() {
        return this.visionRadius;
    }

    /*public boolean canSee(int wx, int wy) {
        return ai.canSee(wx, wy);
    }*/

    public Tile tile(int wx, int wy) {
        return world.tile(wx, wy);
    }

    public void dig(int wx, int wy) {
        world.dig(wx, wy);
    }

    public Boolean legal(int mx, int my) {
        if ((x+mx) < 0 && mx == -1) //| (x+mx) > world.width() | (y+my) < 0 | (y+mx) > world.height() ) {
            return false;
        else if ((x+mx) >= world.width() && mx == 1)
            return false;
        else if ((y+my) < 0 && my == -1)
            return false;
        else if ((y+my) >= world.height() && my == 1)
            return false;
        else if (world.tile(x+mx,y+my)==Tile.WALLB)
            return false;
        return true;
    }

    public void moveBy(int mx, int my, int[] dest) {
        world.changeLock.lock();
        try {
            Creature other = world.creature(x + mx, y + my);
            if (legal(mx,my) && other==null) {
                ai.onEnter(x() + mx, y() + my, world.tile(x() + mx, y() + my));
                if (color == Color.GRAY)
                    world.setTile(x,y,Tile.FLOORS);
            } else if (other!=null) {
                attack(other);
                saveAttackMsg(other);
            }
            Creature another = world.creature(x+dest[0],y+dest[1]);
            if (another != null) {
                attack(another);
                saveAttackMsg(another);
            }
        } finally {
            world.changeLock.unlock();
        }
    }

    public void mMoveBy() {
        int dest[][] = {{1,0},{-1,0},{0,1},{0,-1}};
        List<Integer> list = randomList(4);
        int flag=0;
        world.changeLock.lock();
        try {
            int n = 0;
            for (int i = 0; i < list.size(); i++) {
                n = list.get(i);
                if (world.tile(x + dest[n][0], y + dest[n][1]) == Tile.FLOORW && world.creature(x + dest[n][0], y + dest[n][1]) == null && legal(dest[n][0], dest[n][1])) {
                    ai.onEnter(x + dest[n][0], y + dest[n][1], world.tile(x + dest[n][0], y + dest[n][1]));
                    flag = 1;
                    size++;
                    break;
                }
            }

            Tile[][] tiles = world.getTiles();
            if (flag==1) {
                Tile tile = tiles[x-dest[n][0]][y-dest[n][1]];
                world.setTile(x, y, tile);
                return;
            }
            Tile tile = tiles[x][y];
            for (int i = 0; i < list.size(); i++) {
                int m = list.get(i);
                if ((world.tile(x + dest[m][0], y +dest[m][1]) == tile) && world.creature(x + dest[m][0], y + dest[m][1]) == null && legal(dest[m][0], dest[m][1])) {
                    ai.onEnter(x + dest[m][0], y + dest[m][1], world.tile(x + dest[m][0], y + dest[m][1]));
                    break;
                }
            }
        } finally {
            world.changeLock.unlock();
        }

    }

    public void update() {
        this.ai.onUpdate();
    }

    public boolean canEnter(int x, int y) {
        return world.tile(x, y).isGround();
    }

    public void notify(String message, Object... params) {
        ai.onNotify(String.format(message, params));
    }

    public Creature(int No, World world, ImageView imageView, String colorName, Color color, int visionRadius, int maxHP, int attack, int defense) {
        this.No = No;
        this.world = world;
        imageView.setFitHeight(World.blockSize);
        imageView.setFitWidth(World.blockSize);
        this.imageView = imageView;
        this.colorName = colorName;
        this.color = color;
        this.visionRadius = visionRadius;
        this.maxHP = maxHP;
        this.hp = maxHP;
        this.attack = attack;
        this.defense = defense;
    }

    public List randomList (int size){
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < size; i++)
            list.add(i);
        Collections.shuffle(list);
        return list;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void close() {
        ai.close();
    }

    private String enemy;
    private int enemyDefense;
    private int enemyAttack;

    public void saveAttackMsg(Creature other) {
        enemy = other.colorName;
        enemyDefense = other.defense;
        enemyAttack = other.attack;
    }

    public String getMsg() {
        int damage = 0;
        int hurt = 0;
        if (attack - enemyDefense > 0)
            damage = attack - enemyDefense;
        if (enemyAttack - defense > 0)
            hurt = enemyAttack - defense;
        if (enemy!=null)
            return colorName + " attacked " + enemy + " of " + damage + " hp, and get hurt by " + enemy + " of " + hurt + " hp";
        else
            return "";
    }

    public int getNo() { return No; }

}
