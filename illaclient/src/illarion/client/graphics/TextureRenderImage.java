/*
 * This file is part of the Illarion Client.
 *
 * Copyright © 2012 - Illarion e.V.
 *
 * The Illarion Client is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Illarion Client is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the Illarion Client.  If not, see <http://www.gnu.org/licenses/>.
 */
package illarion.client.graphics;

import de.lessvoid.nifty.slick2d.render.image.ImageSlickRenderImage;
import org.newdawn.slick.Image;

/**
 * This class allows accessing the textures from the texture loader by nifty.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class TextureRenderImage extends ImageSlickRenderImage {
    /**
     * Default constructor.
     *
     * @param image the image that is encapsulated into this class
     */
    public TextureRenderImage(final Image image) {
        super(image);
    }

    /**
     * Overwrite the destroy method to avoid that our images get ripped apart.
     */
    @Override
    public void dispose() {
        // do nothing
    }
}
