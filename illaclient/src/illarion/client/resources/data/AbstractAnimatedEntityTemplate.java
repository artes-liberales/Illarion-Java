/*
 * This file is part of the Illarion Client.
 *
 * Copyright © 2013 - Illarion e.V.
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
package illarion.client.resources.data;

import illarion.client.graphics.Sprite;
import org.newdawn.slick.Color;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;

/**
 * This template in general stores the data required to create any class that inherits
 * {@link illarion.client.graphics.AbstractEntity} and has additional support for animations.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
@Immutable
@ThreadSafe
public class AbstractAnimatedEntityTemplate extends AbstractMultiFrameEntityTemplate {
    /**
     * The animation speed.
     */
    private final int speed;

    /**
     * The constructor of this class.
     *
     * @param id           the identification number of the entity
     * @param sprite       the sprite used to render the entity
     * @param frames       the total amount of frames
     * @param stillFrame   the frame displayed in case no frame animation is running
     * @param speed        the animation speed of this entity
     * @param defaultColor the default color of the entity
     * @param shadowOffset the offset of the shadow
     */
    protected AbstractAnimatedEntityTemplate(final int id, @Nonnull final Sprite sprite,
                                             final int frames, final int stillFrame, final int speed,
                                             @Nullable final Color defaultColor, final int shadowOffset) {
        super(id, sprite, frames, stillFrame, defaultColor, shadowOffset);
        this.speed = speed;
    }

    public int getAnimationSpeed() {
        return speed;
    }
}
