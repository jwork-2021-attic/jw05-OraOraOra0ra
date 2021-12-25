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

import javafx.scene.layout.GridPane;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Aeranythe Echosong
 */
public class MonsterAI extends CreatureAI implements Serializable {

    private int sizeMessage;

    private int v;

    private boolean flag = true;

    public MonsterAI(Creature creature, int sizeMessage, int v) {
        super(creature);
        this.v = v;
        this.sizeMessage = sizeMessage;
    }

    public void onEnter(int x, int y, Tile tile) {
        if (tile.isGround()) {
            creature.setX(x);
            creature.setY(y);
        }
    }

    @Override
    public void run(){
        while(flag){
            try {
                Thread.sleep(v);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.creature.mMoveBy();
        }
    }

    public void onNotify(int sizeMessage) {
        this.sizeMessage++;
    }

    public void close() {
        flag =false;
    }
}
