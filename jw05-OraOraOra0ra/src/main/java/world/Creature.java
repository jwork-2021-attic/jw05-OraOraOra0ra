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

import asciiPanel.AsciiPanel;
import screen.WinScreen;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Aeranythe Echosong
 */
public class Creature {

    private World world;

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

    private char glyph;

    public char glyph() {
        return this.glyph;
    }

    private Color color;

    public Color color() {
        return this.color;
    }

    private CreatureAI ai;

    int size;

    public void setAI(CreatureAI ai) {
        this.ai = ai;
    }

    public CreatureAI getAI() {
        return this.ai;
    }

    public int[][] maze = MazeBuilder.mazeArray();

    /*private int maxHP;

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
        }
    }

    private int attackValue;

    public int attackValue() {
        return this.attackValue;
    }

    private int defenseValue;

    public int defenseValue() {
        return this.defenseValue;
    }*/

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
        return true;
    }

    public void moveBy(int mx, int my) {
        Creature other = world.creature(x + mx, y + my);

        if (color == AsciiPanel.brightRed) {
            /*if ((world.tile(x+mx,y+my) == Tile.FLOORBR || world.tile(x+mx,y+my) == Tile.FLOORG) && other == null && legal(mx,my)) {
                ai.onEnter(x + mx, y + my, world.tile(x + mx, y + my));
                world.setTile(x, y, Tile.FLOORBR);
            }*/
            world.setTile(x, y, Tile.FLOORBR);
            mMoveBy(mx, my);
        } else if (color == AsciiPanel.brightBlue) {
            /*if ((world.tile(x+mx,y+my) == Tile.FLOORBB || world.tile(x+mx,y+my) == Tile.FLOORG) && other == null && legal(mx,my)) {
                ai.onEnter(x + mx, y + my, world.tile(x + mx, y + my));
                world.setTile(x, y, Tile.FLOORBB);
            }*/
            world.setTile(x, y, Tile.FLOORBB);
            mMoveBy(mx, my);
        } else if (color == AsciiPanel.brightGreen){
            world.setTile(x, y, Tile.FLOORBG);
            mMoveBy(mx, my);
        } else if (color == AsciiPanel.brightYellow) {
            world.setTile(x, y, Tile.FLOORBY);
            mMoveBy(mx, my);
        } else if (color == AsciiPanel.brightCyan) {
            world.setTile(x, y, Tile.FLOORBC);
            mMoveBy(mx, my);
        } else if (color == AsciiPanel.brightMagenta) {
            world.setTile(x, y, Tile.FLOORBM);
            mMoveBy(mx, my);
        } else if (color == AsciiPanel.brightWhite) {
            world.setTile(x, y, Tile.FLOORBW);
            mMoveBy(mx, my);
        }


        /*if (maze[x+mx][y+my]==0) {
            //world.setTile(x+mx,y+my,Tile.WALLBB);
            //ai.onEnter(0, 0, world.tile(0, 0));
        }
        else {
            world.setTile(x+mx,y+my,Tile.FLOORBR);
            ai.onEnter(x + mx, y + my, world.tile(x + mx, y + my));
        }*/
    }

    public void mMoveBy(int mx, int my) {
        int dest[][] = {{1,0},{-1,0},{0,1},{0,-1}};
        List<Integer> list = randomList(4);
        int flag=0;
        for (int i = 0; i < list.size(); i++) {
            int n = list.get(i);
            if (world.tile(x+dest[n][0],y+dest[n][1]) == Tile.FLOORG && world.creature(x+dest[n][0],y+dest[n][1])==null && legal(x+dest[n][0],y+dest[n][1])){
                ai.onEnter(x+dest[n][0],y+dest[n][1],world.tile(x+dest[n][0],y+dest[n][1]));
                size++;
                flag=1;
                break;
            }
        }

        if (color == AsciiPanel.brightGreen){
            if (flag==1) {
                world.setTile(x, y, Tile.FLOORBG);
                return;
            }
            if ((world.tile(x+mx,y+my) == Tile.FLOORBG) && world.creature(x + mx, y + my)==null && legal(mx,my)) {
                ai.onEnter(x + mx, y + my, world.tile(x + mx, y + my));
                world.setTile(x, y, Tile.FLOORBG);
            }
        } else if (color == AsciiPanel.brightYellow){
            if (flag==1) {
                world.setTile(x, y, Tile.FLOORBY);
                return;
            }
            if ((world.tile(x+mx,y+my) == Tile.FLOORBY) && world.creature(x + mx, y + my)==null) {
                ai.onEnter(x + mx, y + my, world.tile(x + mx, y + my));
                world.setTile(x, y, Tile.FLOORBY);
            }
        } else if (color == AsciiPanel.brightCyan){
            if (flag==1) {
                world.setTile(x, y, Tile.FLOORBC);
                return;
            }
            if ((world.tile(x+mx,y+my) == Tile.FLOORBC) && world.creature(x + mx, y + my)==null) {
                ai.onEnter(x + mx, y + my, world.tile(x + mx, y + my));
                world.setTile(x, y, Tile.FLOORBC);
            }
        } else if (color == AsciiPanel.brightMagenta){
            if (flag==1) {
                world.setTile(x, y, Tile.FLOORBM);
                return;
            }
            if ((world.tile(x+mx,y+my) == Tile.FLOORBM) && world.creature(x + mx, y + my)==null) {
                ai.onEnter(x + mx, y + my, world.tile(x + mx, y + my));
                world.setTile(x, y, Tile.FLOORBM);
            }
        } else if (color == AsciiPanel.brightWhite){
            if (flag==1) {
                world.setTile(x, y, Tile.FLOORBW);
                return;
            }
            if ((world.tile(x+mx,y+my) == Tile.FLOORBW) && world.creature(x + mx, y + my)==null) {
                ai.onEnter(x + mx, y + my, world.tile(x + mx, y + my));
                world.setTile(x, y, Tile.FLOORBW);
            }
        } else if (color == AsciiPanel.brightRed){
            if (flag==1) {
                world.setTile(x, y, Tile.FLOORBR);
                return;
            }
            if ((world.tile(x+mx,y+my) == Tile.FLOORBR) && world.creature(x + mx, y + my)==null) {
                ai.onEnter(x + mx, y + my, world.tile(x + mx, y + my));
                world.setTile(x, y, Tile.FLOORBR);
            }
        } else if (color == AsciiPanel.brightBlue){
            if (flag==1) {
                world.setTile(x, y, Tile.FLOORBB);
                return;
            }
            if ((world.tile(x+mx,y+my) == Tile.FLOORBB) && world.creature(x + mx, y + my)==null) {
                ai.onEnter(x + mx, y + my, world.tile(x + mx, y + my));
                world.setTile(x, y, Tile.FLOORBB);
            }
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

    public Creature(World world, char glyph, Color color, /*int HP, int attack, int defense,*/ int visionRadius) {
        this.world = world;
        this.glyph = glyph;
        this.color = color;
        this.visionRadius = visionRadius;
    }

    public List randomList (int size){
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < size; i++)
            list.add(new Integer(i));
        Collections.shuffle(list);
        return list;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

}
