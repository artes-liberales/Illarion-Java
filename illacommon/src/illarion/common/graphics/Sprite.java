/*
 * This file is part of the Illarion Graphics Engine.
 *
 * Copyright © 2011 - Illarion e.V.
 *
 * The Illarion Graphics Engine is free software: you can redistribute i and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * The Illarion Graphics Engine is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * the Illarion Graphics Interface. If not, see <http://www.gnu.org/licenses/>.
 */
package illarion.common.graphics;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * LWJGL implementation of the sprite interface that uses LWJGL to render the
 * sprite on the screen.
 * 
 * @author Martin Karing
 * @version 2.00
 * @since 2.00
 */
public final class Sprite {
    public enum VAlign {
        top, middle, bottom

    }

    public enum HAlign {
        left, center, right

    }

    /**
     * The count of textures drawn.
     */
    private static int drawnTextures = 0;

    /**
     * The last fetched value of drawn objects.
     */
    private static int lastDrawnTex;

    /**
     * Get the count of drawn textures.
     * 
     * @return the count of drawn textures
     */
    public static int getDrawnObjects() {
        return lastDrawnTex;
    }

    /**
     * This function is supposed to be called, when ever a texture is drawn. Its
     * used to generate the statistics.
     */
    protected static void reportDrawTexture() {
        drawnTextures++;
    }

    /**
     * Reset the count of drawn objects and update the readable value.
     */
    public static void resetDrawCount() {
        lastDrawnTex = drawnTextures;
        drawnTextures = 0;
    }

    /**
     * The horizontal align that is used at rendering this sprite.
     */
    private HAlign hAlignUsed;

    /**
     * Flag for horizontal mirroring. Activating this flag results in a mirrored
     * display for this texture.
     */
    private boolean mirror;

    /**
     * The x offset of the sprite, so the amount of pixels the origin of the
     * sprite is moved horizontal.
     */
    private int offsetX;

    /**
     * The y offset of the sprite, so the amount of pixels the origin of the
     * sprite is moved vertical.
     */
    private int offsetY;

    /**
     * The rotation degree of the rendered sprite. Value in degree.
     */
    private float rotation;

    /**
     * The textures that are loaded to this Sprite and can be rendered.
     */
    private final Image[] textures;

    /**
     * The amount of textures that are currently not set.
     */
    private int unsetTextures;

    /**
     * The vertical align that is used at rendering this sprite.
     */
    private VAlign vAlignUsed;

    /**
     * The default constructor of the LWJGL Sprite. This one does nothing on its
     * own.
     * 
     * @param frames the amount of frames stored in this sprite
     */
    public Sprite(final int frames) {
        hAlignUsed = HAlign.left;
        vAlignUsed = VAlign.top;
        textures = new Image[frames];
        unsetTextures = frames;
        offsetX = 0;
        offsetY = 0;
        mirror = false;
    }
    
    /**
     * Copy constructor creates a new sprite that is at the time of coping
     * identical to the original sprite. How ever its possible to change the
     * sprite later.
     * 
     * @param org the original sprite
     */
    public Sprite(final Sprite org) {
        hAlignUsed = org.hAlignUsed;
        vAlignUsed = org.vAlignUsed;
        textures = org.textures.clone();
        unsetTextures = org.unsetTextures;
        offsetX = org.offsetX;
        offsetY = org.offsetY;
        mirror = org.mirror;
    }

    /**
     * Add a texture to the sprite. This texture needs to by a LWJGL
     * implementation of a texture.
     * 
     * @param newTexture the instance of the texture that is added to the
     *            texture storage of this sprite.
     */
    @SuppressWarnings("nls")
    public void addImage(final Image newTexture) {
        if (newTexture == null) {
            throw new IllegalArgumentException("Added NULL Texture");
        }

        if ((textures.length - unsetTextures) > 0) {
            final Image firstImg = textures[0];
            
            if ((firstImg.getHeight() != newTexture.getHeight())
                || (firstImg.getWidth() != newTexture.getWidth())) {
                throw new IllegalArgumentException(
                    "Texture size does not fit.");
            }
        }

        textures[textures.length - unsetTextures] = newTexture;
        unsetTextures--;
    }

    /**
     * Get the additional offset that is caused by the align of the sprite. This
     * function returns the x share of the offset caused by the align.
     * 
     * @return the x share of the offset caused by the sprite align
     */
    protected final int getAlignOffsetX() {
        if (hAlignUsed == HAlign.center) {
            return -(getWidth() >> 1);
        } else if (hAlignUsed == HAlign.right) {
            return -getWidth();
        }
        return 0;
    }

    /**
     * Get the additional offset that is caused by the align of the sprite. This
     * function returns the y share of the offset caused by the align.
     * 
     * @return the y share of the offset caused by the sprite align
     */
    protected final int getAlignOffsetY() {
        if (vAlignUsed == VAlign.middle) {
            return -(getHeight() >> 1);
        } else if (vAlignUsed == VAlign.bottom) {
            return -getHeight();
        }
        return 0;
    }

    /**
     * The the default light instance of this sprite implementation.
     * 
     * @return the default light object
     */
    public final Color getDefaultLight() {
        return Color.white;
    }

    /**
     * Get the amount of textures that are set to this sprite.
     * 
     * @return the amount of textures that are set to this sprite
     */
    public final int getFrames() {
        return textures.length;
    }

    /**
     * Get the height of the sprite textures.
     * 
     * @return the height of the sprite textures and -1 in case there are no
     *         textures set yet
     */
    public final int getHeight() {
        final Image texture = textures[0];
        if (texture == null) {
            return -1;
        }
        return texture.getHeight();
    }

    /**
     * Get the offset in X direction of the sprite.
     * 
     * @return the offset in pixel that is added to the drawing position
     */
    public final int getOffsetX() {
        return offsetX;
    }

    /**
     * Get the offset in Y direction of the sprite.
     * 
     * @return the offset in pixel that is added to the drawing position
     */
    public final int getOffsetY() {
        return offsetY;
    }

    /**
     * Get the rotation that is applied to this sprite.
     * 
     * @return the rotation applied to this sprite
     */
    protected final float getRotation() {
        return rotation;
    }

    /**
     * Get the offset in X direction of the sprite in case its rendered with a
     * set scaling value.
     * 
     * @param scale the scaling value that is assumed to be used
     * @return the offset in pixel that is added to the drawing position
     */
    public final int getScaledOffsetX(final float scale) {
        int retOffset = (int) (offsetX * scale);

        if (hAlignUsed == HAlign.center) {
            retOffset += (getWidth() * scale) / 2.f;
        } else if (hAlignUsed == HAlign.left) {
            retOffset += getWidth() * scale;
        }
        return retOffset;
    }

    /**
     * Get the offset in Y direction of the sprite in case its rendered with a
     * set scaling value.
     * 
     * @param scale the scaling value that is assumed to be used
     * @return the offset in pixel that is added to the drawing position
     */
    public final int getScaledOffsetY(final float scale) {
        int retOffset = (int) (offsetY * scale);

        if (vAlignUsed == VAlign.middle) {
            retOffset -= (getHeight() * scale) / 2;
        } else if (vAlignUsed == VAlign.bottom) {
            retOffset -= getHeight() * scale;
        }
        return retOffset;
    }

    /**
     * Get the texture at a specified index.
     * 
     * @param index the index of the texture
     * @return the texture at the index
     */
    public final Image getTexture(final int index) {
        return textures[index];
    }

    /**
     * Get the width of the sprite textures.
     * 
     * @return the width of the sprite textures and -1 in case there are no
     *         textures set yet
     */
    public final int getWidth() {
        final Image texture = textures[0];
        if (texture == null) {
            return -1;
        }
        return texture.getWidth();
    }

    /**
     * Get if the texture on the sprite is supposed to be displayed mirrored.
     * 
     * @return <code>true</code> in case the texture needs to be displayed
     *         mirrored
     */
    protected final boolean isMirrored() {
        return mirror;
    }

    /**
     * This function cleans up all textures of the sprite. So all textures get
     * removed from the sprite after this call. This should only be called in
     * case the sprite is not used anymore for sure.
     */
    public final void remove() {
        for (final Image texture : textures) {
            try {
                texture.destroy();
            } catch (SlickException e) {
                // destroying the texture failed.
            }
            unsetTextures++;
        }
    }

    /**
     * Set the align that shall be used at rendering the sprite.
     * 
     * @param horzAlign the new horizontal align that shall be used for
     *            rendering the sprite
     * @param vertAlign the new vertical align that shall be used for rendering
     *            the sprite
     */
    public final void setAlign(final HAlign horzAlign, final VAlign vertAlign) {
        hAlignUsed = horzAlign;
        vAlignUsed = vertAlign;
    }

    /**
     * Change the mirror flag. Activating this flag results in a mirrored
     * display for the texture.
     * 
     * @param newMirror new value for the mirror flag
     */
    public final void setMirror(final boolean newMirror) {
        mirror = newMirror;
    }

    /**
     * Set the offset values for this sprite. The origin of the texture will be
     * moved and so the position relative to the location where the sprite shall
     * be drawn.
     * 
     * @param xOffset the new value of the horizontal offset
     * @param yOffset the new value of the vertical offset
     */
    public final void setOffset(final int xOffset, final int yOffset) {
        offsetX = xOffset;
        offsetY = yOffset;
    }

    /**
     * Set the rotation that is applied to the rendered texture. Default is 0
     * degree.
     * 
     * @param degree the rotation degree that is supposed to be applied to the
     *            texture
     */
    public final void setRotation(final float degree) {
        rotation = degree;
    }

    /**
     * Simple drawing function. Draw the first texture of the LWJGL sprite to a
     * location.
     * 
     * @param x the x coordinate of the location the texture shall been drawn at
     * @param y the y coordinate of the location the texture shall been drawn at
     */
    public void draw(final Graphics g, final int x, final int y) {
        draw(g, x, y, null, 0, 1.f);
    }

    /**
     * Simple drawing function. Draw a frame texture of the LWJGL sprite to a
     * location that is enlighten with the default color. The texture is scaled
     * to the width and height set in this function. The first texture is
     * rendered.
     * 
     * @param x the x coordinate of the location the texture shall been drawn at
     * @param y the y coordinate of the location the texture shall been drawn at
     * @param w the width the width of the sprite shall be scaled to
     * @param h the height the height of the sprite shall be scaled to
     */
    public void draw(final Graphics g, final int x, final int y, final int w, final int h) {
        draw(g, x, y, w, h, null, 0);
    }

    /**
     * Simple drawing function. Draw a frame texture of the LWJGL sprite to a
     * location that is enlighten with the color that is set. The texture is
     * scaled to the width and height set in this function. The first texture is
     * rendered.
     * 
     * @param x the x coordinate of the location the texture shall been drawn at
     * @param y the y coordinate of the location the texture shall been drawn at
     * @param w the width the width of the sprite shall be scaled to
     * @param h the height the height of the sprite shall be scaled to
     * @param color the color that is used to render the sprite
     */
    public void draw(final Graphics g, final int x, final int y, final int w, final int h,
        final Color color) {
        draw(g, x, y, w, h, color, 0);
    }

    /**
     * Drawing function. Draw a frame texture of the LWJGL sprite to a location
     * that is enlighten with the color that is set. The texture is scaled to
     * the width and height set in this function.
     * 
     * @param x the x coordinate of the location the texture shall been drawn at
     * @param y the y coordinate of the location the texture shall been drawn at
     * @param w the width the width of the sprite shall be scaled to
     * @param h the height the height of the sprite shall be scaled to
     * @param color the color that is used to render the sprite
     * @param frame the frame that shall be rendered
     */
    @SuppressWarnings("nls")
    public void draw(final Graphics g, final int x, final int y, final int w, final int h,
        final Color color, final int frame) {

        final Image texture = getTexture(frame);
        if (texture == null) {
            throw new IllegalArgumentException("Failed to get proper texture.");
        }
        
        g.pushTransform();
        if (isMirrored()) {
            g.scale(-(w / getWidth()), h / getHeight());
        } else {
            g.scale(w / getWidth(), h / getHeight());
        }
        
        g.rotate(x, y, getRotation());
        
        g.drawImage(texture, x + getAlignOffsetX(), y + getAlignOffsetY(), getColor(color));
        
        g.popTransform();

        reportDrawTexture();
    }

    /**
     * Simple drawing function. Draw the first texture of the JOGL sprite to a
     * location that is enlighten with the color that is set. Also the alpha
     * value of this color is taken into account for transparency effects.
     * 
     * @param x the x coordinate of the location the texture shall been drawn at
     * @param y the y coordinate of the location the texture shall been drawn at
     * @param color the color the texture of the sprite is rendered with
     */
    public void draw(final Graphics g, final int x, final int y, final Color color) {
        draw(g, x, y, color, 0);
    }

    /**
     * Simple drawing function. Draw the first texture of the JOGL sprite to a
     * location that is enlighten with the color that is set. Also the alpha
     * value of this color is taken into account for transparency effects.
     * 
     * @param x the x coordinate of the location the texture shall been drawn at
     * @param y the y coordinate of the location the texture shall been drawn at
     * @param color the color the texture of the sprite is rendered with
     * @param frame the frame that is rendered
     */
    @SuppressWarnings("nls")
    public void draw(final Graphics g, final int x, final int y, final Color color,
        final int frame) {
        
        final Image texture = getTexture(frame);
        if (texture == null) {
            throw new IllegalArgumentException("Failed to get proper texture.");
        }
        
        g.pushTransform();
        
        if (isMirrored()) {
            g.scale(-1.f, 1.f);
        }

        texture.setRotation(getRotation());
        texture.setCenterOfRotation(getAlignOffsetX(), getAlignOffsetY());
        
        g.drawImage(texture, x + getAlignOffsetX() + getOffsetX(), y + getAlignOffsetY() + getOffsetY(), getColor(color));
        
        g.popTransform();
        reportDrawTexture();
    }
    
    /**
     * This function returns the color that is set as parameter or in case its
     * null it returns the default color.
     * 
     * @param color the color to check
     * @return the parameter color or in case the parameter is null the white
     * color is returned
     */
    private Color getColor(final Color color) {
        if (color == null) {
            return Color.white;
        }
        return color;
    }

    /**
     * Drawing function. Draw a frame texture of the LWJGL sprite to a location
     * that is enlighten with the color that is set. Also the alpha value of
     * this color is taken into account for transparency effects. The texture is
     * scaled by the value set.
     * 
     * @param x the x coordinate of the location the texture shall been drawn at
     * @param y the y coordinate of the location the texture shall been drawn at
     * @param color the color the texture of the sprite is rendered with
     * @param frame the frame that is rendered
     * @param scale the scaling value the height and the width is reduced with
     */
    @SuppressWarnings("nls")
    public void draw(final Graphics g, final int x, final int y, final Color color,
        final int frame, final float scale) {
        final Image texture = getTexture(frame);
        if (texture == null) {
            throw new IllegalArgumentException("Failed to get proper texture.");
        }
        
        g.pushTransform();
        
        if (isMirrored()) {
            g.scale(-scale, scale);
        } else {
            g.scale(scale, scale);
        }
        g.rotate(x, y, getRotation());
        g.drawImage(texture, x + getAlignOffsetX(), y + getAlignOffsetY(), getColor(color));
        
        g.popTransform();
    }
}
