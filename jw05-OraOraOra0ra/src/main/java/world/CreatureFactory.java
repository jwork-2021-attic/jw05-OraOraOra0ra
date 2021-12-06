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

import asciiPanel.AsciiPanel;

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

    public Creature newPlayer1(List<String> messages) {
        Creature player1 = new Creature(this.world, (char)2, AsciiPanel.brightRed, 100);

        int rx = r.nextInt(world.width()-1);
        int ry = r.nextInt(world.height()-1);
        while(WorldBuilder.getMaze()[rx][ry]==0) {
            rx = r.nextInt(world.width()-1);
        }
        world.myLocation(player1,rx,ry);
        new PlayerAI(player1, messages);
        return player1;
    }

    public Creature newPlayer2(List<String> messages) {
        Creature player2= new Creature(this.world, (char)2, AsciiPanel.brightBlue, 100);
        int rx = r.nextInt(world.width()-1);
        int ry = r.nextInt(world.height()-1);
        while(WorldBuilder.getMaze()[rx][ry]==0) {
            rx = r.nextInt(world.width()-1);
        }
        world.myLocation(player2,rx,ry);
        new PlayerAI(player2, messages);
        return player2;
    }

    public Creature newMonster1(int messages) {
        Creature monster1= new Creature(this.world, (char)2, AsciiPanel.brightGreen, 100);
        int rx = r.nextInt(world.width()-1);
        int ry = r.nextInt(world.height()-1);
        while(WorldBuilder.getMaze()[rx][ry]==0) {
            rx = r.nextInt(world.width()-1);
        }
        world.myLocation(monster1,rx,ry);
        monster1.setSize(messages);
        new MonsterAI(monster1, messages);
        return monster1;
    }

    public Creature newMonster2(int messages) {
        Creature monster2= new Creature(this.world, (char)2, AsciiPanel.brightYellow, 100);
        int rx = r.nextInt(world.width()-1);
        int ry = r.nextInt(world.height()-1);
        while(WorldBuilder.getMaze()[rx][ry]==0) {
            rx = r.nextInt(world.width()-1);
        }
        world.myLocation(monster2,rx,ry);
        monster2.setSize(messages);
        new MonsterAI(monster2,messages);
        return monster2;
    }
    public Creature newMonster3(int messages) {
        Creature monster3= new Creature(this.world, (char)2, AsciiPanel.brightCyan, 100);
        int rx = r.nextInt(world.width()-1);
        int ry = r.nextInt(world.height()-1);
        while(WorldBuilder.getMaze()[rx][ry]==0) {
            rx = r.nextInt(world.width()-1);
        }
        world.myLocation(monster3,rx,ry);
        monster3.setSize(messages);
        new MonsterAI(monster3, messages);
        return monster3;
    }
    public Creature newMonster4(int messages) {
        Creature monster4= new Creature(this.world, (char)2, AsciiPanel.brightMagenta, 100);
        int rx = r.nextInt(world.width()-1);
        int ry = r.nextInt(world.height()-1);
        while(WorldBuilder.getMaze()[rx][ry]==0) {
            rx = r.nextInt(world.width()-1);
        }
        world.myLocation(monster4,rx,ry);
        monster4.setSize(messages);
        new MonsterAI(monster4, messages);
        return monster4;
    }

    public Creature newMonster5(int messages) {
        Creature monster5= new Creature(this.world, (char)2, AsciiPanel.brightWhite, 100);
        int rx = r.nextInt(world.width()-1);
        int ry = r.nextInt(world.height()-1);
        while(WorldBuilder.getMaze()[rx][ry]==0) {
            rx = r.nextInt(world.width()-1);
        }
        world.myLocation(monster5,rx,ry);
        monster5.setSize(messages);
        new MonsterAI(monster5, messages);
        return monster5;
    }

    public Creature newMonster6(int messages) {
        Creature monster6= new Creature(this.world, (char)2, AsciiPanel.brightRed, 100);
        int rx = r.nextInt(world.width()-1);
        int ry = r.nextInt(world.height()-1);
        while(WorldBuilder.getMaze()[rx][ry]==0) {
            rx = r.nextInt(world.width()-1);
        }
        world.myLocation(monster6,rx,ry);
        monster6.setSize(messages);
        new MonsterAI(monster6, messages);
        return monster6;
    }

    public Creature newMonster7(int messages) {
        Creature monster7= new Creature(this.world, (char)2, AsciiPanel.brightBlue, 100);
        int rx = r.nextInt(world.width()-1);
        int ry = r.nextInt(world.height()-1);
        while(WorldBuilder.getMaze()[rx][ry]==0) {
            rx = r.nextInt(world.width()-1);
        }
        world.myLocation(monster7,rx,ry);
        monster7.setSize(messages);
        new MonsterAI(monster7, messages);
        return monster7;
    }

    public Creature newFungus() {
        Creature fungus = new Creature(this.world, (char)3, AsciiPanel.green, 10);
        world.addAtEmptyLocation(fungus);
        new FungusAI(fungus, this);
        return fungus;
    }

    public Creature Athena() {
        Creature Athena = new Creature(this.world, (char)12, AsciiPanel.brightYellow, 100);
        world.myLocation(Athena,this.world.width()-1,this.world.height()-1);
        new CreatureAI(Athena);
        return Athena;
    }

}
