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

import world.*;
import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Aeranythe Echosong
 */
public class PlayScreen implements Screen {

    private World world;
    private Creature player1;
    private Creature player2;
    private Creature monster1;
    private Creature monster2;
    private Creature monster3;
    private Creature monster4;
    private Creature monster5;
    private Creature monster6;
    private Creature monster7;
    private Creature Athena;
    private int screenWidth;
    private int screenHeight;
    private List<String> messages;
    private List<String> oldMessages;
    public int[][] maze = MazeBuilder.mazeArray();

    public PlayScreen() {
        this.screenWidth = 25;
        this.screenHeight = 25;
        createWorld();
        this.messages = new ArrayList<String>();
        this.oldMessages = new ArrayList<String>();

        CreatureFactory creatureFactory = new CreatureFactory(this.world);
        createCreatures(creatureFactory);
    }

    private void createCreatures(CreatureFactory creatureFactory) {
        //this.player1 = creatureFactory.newPlayer1(this.messages);
        //this.player2 = creatureFactory.newPlayer2(this.messages);
        this.monster1 = creatureFactory.newMonster1(1);
        this.monster2 = creatureFactory.newMonster2(1);
        this.monster3 = creatureFactory.newMonster3(1);
        this.monster4 = creatureFactory.newMonster4(1);
        this.monster5 = creatureFactory.newMonster5(1);
        this.monster6 = creatureFactory.newMonster6(1);
        this.monster7 = creatureFactory.newMonster7(1);

        //world.setTile(player1.x(), player1.y(), Tile.FLOORBR);
        //world.setTile(player2.x(), player2.y(), Tile.FLOORBB);
        world.setTile(monster1.x(), monster1.y(), Tile.FLOORBG);
        world.setTile(monster2.x(), monster2.y(), Tile.FLOORBY);
        world.setTile(monster3.x(), monster3.y(), Tile.FLOORBC);
        world.setTile(monster4.x(), monster4.y(), Tile.FLOORBM);
        world.setTile(monster5.x(), monster5.y(), Tile.FLOORBW);
        world.setTile(monster6.x(), monster6.y(), Tile.FLOORBR);
        world.setTile(monster7.x(), monster7.y(), Tile.FLOORBB);

        Thread t1 = new Thread(monster1.getAI());
        Thread t2 = new Thread(monster2.getAI());
        Thread t3 = new Thread(monster3.getAI());
        Thread t4 = new Thread(monster4.getAI());
        Thread t5 = new Thread(monster5.getAI());
        Thread t6 = new Thread(monster6.getAI());
        Thread t7 = new Thread(monster7.getAI());
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        t7.start();
        //this.Athena = creatureFactory.Athena();
        /*for (int i = 0; i < 8; i++) {
            Creature creature = creatureFactory.newFungus();
            //Thread t = new Thread(creature.getAI());
            //t.start();
        }*/
    }

    private void createWorld() {
        world = new WorldBuilder(25, 25).makeMaze().build();
    }

    private void displayTiles(AsciiPanel terminal, int left, int top) {
        // Show terrain
        for (int x = 0; x < screenWidth; x++) {
            for (int y = 0; y < screenHeight; y++) {
                int wx = x + left;
                int wy = y + top;

                terminal.write(world.glyph(wx, wy), x, y, world.color(wx, wy));

                /*if (player.canSee(wx, wy)) {
                    terminal.write(world.glyph(wx, wy), x, y, world.color(wx, wy));
                } else {
                    terminal.write(world.glyph(wx, wy), x, y, Color.DARK_GRAY);
                }*/
            }
        }
        // Show creatures
        /*for (Creature creature : world.getCreatures()) {
            if (creature.x() >= left && creature.x() < left + screenWidth && creature.y() >= top
                    && creature.y() < top + screenHeight) {
                if (player.canSee(creature.x(), creature.y())) {
                    terminal.write(creature.glyph(), creature.x() - left, creature.y() - top, creature.color());
                }
            }
        }*/
        // Creatures can choose their next action now
        world.update();
    }

    private void displayMessages(AsciiPanel terminal, List<String> messages) {
        int top = this.screenHeight;
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
        messages.clear();
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        // Terrain and creatures
        displayTiles(terminal, getScrollX(), getScrollY());
        // Player
        //terminal.write(player1.glyph(), player1.x() - getScrollX(), player1.y() - getScrollY(), player1.color());
        //terminal.write(player2.glyph(), player2.x() - getScrollX(), player2.y() - getScrollY(), player2.color());
        terminal.write(monster1.glyph(), monster1.x() - getScrollX(), monster1.y() - getScrollY(), monster1.color());
        terminal.write(monster2.glyph(), monster2.x() - getScrollX(), monster2.y() - getScrollY(), monster2.color());
        terminal.write(monster3.glyph(), monster3.x() - getScrollX(), monster3.y() - getScrollY(), monster3.color());
        terminal.write(monster4.glyph(), monster4.x() - getScrollX(), monster4.y() - getScrollY(), monster4.color());
        terminal.write(monster5.glyph(), monster5.x() - getScrollX(), monster5.y() - getScrollY(), monster5.color());
        terminal.write(monster6.glyph(), monster6.x() - getScrollX(), monster6.y() - getScrollY(), monster6.color());
        terminal.write(monster7.glyph(), monster7.x() - getScrollX(), monster7.y() - getScrollY(), monster7.color());
        //terminal.write(Athena.glyph(), Athena.x() - getScrollX(), Athena.y() - getScrollY(), Athena.color());
        // Messages
        this.messages.add(String.valueOf(monster1.getSize()));
        this.messages.add(String.valueOf(monster2.getSize()));
        this.messages.add(String.valueOf(monster3.getSize()));
        this.messages.add(String.valueOf(monster4.getSize()));
        this.messages.add(String.valueOf(monster5.getSize()));
        this.messages.add(String.valueOf(monster6.getSize()));
        this.messages.add(String.valueOf(monster7.getSize()));
        displayMessages(terminal, this.messages);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()) {
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
        }

        /*if (judgeEnd()==1)
            return new WinScreen("player1");
        else if (judgeEnd()==2)
            return new WinScreen("player2");*/

        return this;
    }

    public int judgeEnd(){
        if (player1.x()==world.width()-1 && player1.y()==world.height()-1)
            return 1;
        else if (player2.x()==world.width()-1 && player2.y()==world.height()-1)
            return 2;
        return 0;
    }

    public int getScrollX() {
        //return Math.max(0, Math.min(player1.x() - screenWidth / 2, world.width() - screenWidth));
        return Math.max(0, Math.min(monster1.x() - screenWidth / 2, world.width() - screenWidth));
    }

    public int getScrollY() {
        //return Math.max(0, Math.min(player1.y() - screenHeight / 2, world.height() - screenHeight));
        return Math.max(0, Math.min(monster1.y() - screenHeight / 2, world.height() - screenHeight));
    }

}
