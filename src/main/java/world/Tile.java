package world;

import javafx.scene.paint.Color;
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

    FLOORW(Color.WHITE),

    FLOORR(Color.RED),
    FLOORO(Color.ORANGE),
    FLOORY(Color.YELLOW),
    FLOORG(Color.GREEN),
    FLOORC(Color.CYAN),
    FLOORB(Color.BLUE),
    FLOORP(Color.PURPLE),
    FLOORS(Color.GREY),

    WALLB(Color.BLACK),

    BOUNDS(Color.MAGENTA);


    private Color color;

    public Color color() {
        return color;
    }

    public boolean isDiggable() {
        return this != Tile.WALLB;
    }

    public boolean isGround() {
        return this != Tile.WALLB && this != Tile.BOUNDS;
    }

    Tile(Color color) {
        this.color = color;
    }
    
    public Color getColor() { return color; }
}
