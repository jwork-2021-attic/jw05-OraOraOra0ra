package world;

import asciiPanel.AsciiPanel;
import java.awt.Color;
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
public enum Tile {

    FLOORB((char) 250, AsciiPanel.black),

    WALLG((char) 250, AsciiPanel.green),

    FLOORG((char) 250, AsciiPanel.green),

    FLOORBR((char) 177, AsciiPanel.brightRed),

    FLOORBB((char) 177, AsciiPanel.brightBlue),

    FLOORBG((char) 177, AsciiPanel.brightGreen),

    FLOORBY((char) 177, AsciiPanel.brightYellow),

    FLOORBC((char) 177, AsciiPanel.brightCyan),

    FLOORBM((char) 177, AsciiPanel.brightMagenta),

    FLOORBW((char) 177, AsciiPanel.brightWhite),

    WALLB((char) 177, AsciiPanel.black),

    WALLBB((char) 177, AsciiPanel.brightBlack),

    BOUNDS('x', AsciiPanel.magenta);

    private char glyph;

    public char glyph() {
        return glyph;
    }

    private Color color;

    public Color color() {
        return color;
    }

    public void drawColor(int i) {
        if (i == 0)
            color = AsciiPanel.brightBlack;
        else if (i == 1)
            color = AsciiPanel.brightGreen;
    }

    public boolean isDiggable() {
        return this != Tile.WALLBB && this != Tile.WALLB;
    }

    public boolean isGround() {
        return this != Tile.WALLBB && this != Tile.WALLB && this != Tile.BOUNDS;
    }

    Tile(char glyph, Color color) {
        this.glyph = glyph;
        this.color = color;
    }
    
    public Color getColor() { return color; }
}
