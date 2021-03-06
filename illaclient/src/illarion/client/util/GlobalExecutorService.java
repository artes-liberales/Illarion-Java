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
package illarion.client.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class stores the reference to the global executor service.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class GlobalExecutorService {
    /**
     * The executor service instance.
     */
    private static final ExecutorService SERVICE = Executors.newCachedThreadPool();

    /**
     * Get the service instance.
     *
     * @return the service instance
     */
    public static ExecutorService getService() {
        return SERVICE;
    }

    /**
     * Shut the service down. This class will be rendered unusable after calling this function.
     */
    public static void shutdown() {
        SERVICE.shutdown();
    }

    /**
     * Blocking any instance of this class.
     */
    private GlobalExecutorService() {
        // nothing
    }
}
