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

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.awt.event.KeyEvent;

/**
 *
 * @author Aeranythe Echosong
 */
public class RestartScreen implements Screen {

    private ImageView[] huluView = new ImageView[7];

    public RestartScreen(ImageView[] huluView) {
        this.huluView = huluView;
    }

    @Override
    public Screen displayOutput(GridPane grid) {
        return this;
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                return null;
            default:
                return this;
        }
    }

}
