package world;

import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

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
/**
 *
 * @author Aeranythe Echosong
 */
public class World implements Serializable {

    public ReentrantLock changeLock = new ReentrantLock();
    public ReentrantLock creatureLock = new ReentrantLock();

    private int[][] map;
    private Tile[][] tiles;
    private int width;
    private int height;
    private List<Creature> creatures;

    private boolean[] isAlive = new boolean[8];

    public final static int blockHeightCount = 20;
    public final static int blockWidthCount = 25;
    public final static int blockSize = 25;

    public static final int TILE_TYPES = 2;

    public World(Tile[][] tiles) {
        this.tiles = tiles;
        this.width = tiles.length;
        this.height = tiles[0].length;
        this.creatures = new ArrayList<>();
        map = new int[blockWidthCount][blockHeightCount];
        initState();
    }

    public Tile tile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return Tile.BOUNDS;
        } else {
            return tiles[x][y];
        }
    }

    public void initState() {
        for (int i = 0; i < 8; i++)
            isAlive[i] = true;
    }

    public Color color(int x, int y) {
        return tiles[x][y].color();
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public void dig(int x, int y) {
        if (tile(x, y).isDiggable()) {
            tiles[x][y] = Tile.FLOORW;
        }
    }

    public void addAtEmptyLocation(Creature creature) {
        int x;
        int y;

        do {
            x = (int) (Math.random() * this.width);
            y = (int) (Math.random() * this.height);
        } while (!tile(x, y).isGround() || this.creature(x, y) != null);

        creature.setX(x);
        creature.setY(y);

        this.creatures.add(creature);
    }

    public void myLocation(Creature creature, int x, int y) {
        creature.setX(x);
        creature.setY(y);

        this.creatures.add(creature);
    }

    public synchronized Creature creature(int x, int y) {
        for (Creature c : this.creatures) {
            if (c.x() == x && c.y() == y) {
                return c;
            }
        }
        return null;
    }

    public List<Creature> getCreatures() {
        return this.creatures;
    }

    public void remove(Creature target) {
        this.creatures.remove(target);
        isAlive[target.getNo()] = false;
    }

    public void update() {
        ArrayList<Creature> toUpdate = new ArrayList<>(this.creatures);

        for (Creature creature : toUpdate) {
            creature.update();
        }
    }

    public synchronized void setTile(int x,int y,Tile tile){
        tiles[x][y] = tile;
    }

    public void loadTile(Tile[][] tiles) { this.tiles = tiles; }

    public static int getBlockWidthCount() {
        return blockWidthCount;
    }

    public static int getBlockHeightCount() {
        return blockHeightCount;
    }

    public void deleteCreatures() {
        this.creatures.clear();
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void setMap(int x, int y, int no) { map[x][y] = no; }

    public int[][] getMap() { return map; }

    public boolean boolAlive(int i) { return isAlive[i]; }

    public void changeState(boolean[] state) { isAlive = state; }

    public boolean[] getState() { return isAlive; }
}
