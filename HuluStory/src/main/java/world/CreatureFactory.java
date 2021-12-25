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

import java.util.List;
import java.util.Random;

import javafx.scene.paint.Color;

/**
 *
 * @author Aeranythe Echosong
 */
public class CreatureFactory {

    private World world;

    Random r = new Random();

    public CreatureFactory(World world) {
        this.world = world;
    }

    public Creature newHuluwa(int No, int messages, String no, String colorName, Color color, int v, int maxHP, int attack, int defense) {
        Creature huluwa = new Creature(No, this.world, colorName, color, 100, maxHP, attack, defense);
        int rx = r.nextInt(world.width()-1);
        int ry = r.nextInt(world.height()-1);
        while(WorldBuilder.getMaze()[rx][ry]==0) {
            rx = r.nextInt(world.width()-1);
        }
        world.myLocation(huluwa,rx,ry);
        huluwa.setSize(messages);
        new MonsterAI(huluwa, messages, v);
        return huluwa;
    }

    public Creature newPlayer(int No, int messages, String no, String colorName, Color color, int maxHP, int attack, int defense) {
        Creature huluwa = new Creature(No, this.world, colorName, color, 100, maxHP, attack, defense);
        int rx = r.nextInt(world.width()-1);
        int ry = r.nextInt(world.height()-1);
        while(WorldBuilder.getMaze()[rx][ry]==0) {
            rx = r.nextInt(world.width()-1);
        }
        world.myLocation(huluwa,rx,ry);
        huluwa.setSize(messages);
        new PlayerAI(huluwa, messages);
        return huluwa;
    }
}
