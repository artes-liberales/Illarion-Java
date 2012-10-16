/*
 * This file is part of the Illarion Mapeditor.
 *
 * Copyright © 2012 - Illarion e.V.
 *
 * The Illarion Mapeditor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Illarion Mapeditor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the Illarion Mapeditor.  If not, see <http://www.gnu.org/licenses/>.
 */
package illarion.mapedit.gui;

import illarion.mapedit.Lang;
import illarion.mapedit.MapEditor;
import illarion.mapedit.resource.loaders.ImageLoader;
import org.apache.log4j.Logger;
import org.pushingpixels.flamingo.api.ribbon.JRibbonFrame;
import org.pushingpixels.flamingo.api.ribbon.RibbonTask;

import javax.swing.*;
import java.awt.*;

/**
 * This class represents the whole gui.
 *
 * @author Tim
 */
public class MainFrame extends JRibbonFrame {
    private static final Dimension WINDOW_SIZE = new Dimension(900, 700);
    private static final Logger LOGGER = Logger.getLogger(MainFrame.class);
    private static MainFrame instance;


    private final MapPanel map;
    private final ObjectSelector selector;

    private MainFrame() {
        instance = this;
        addWindowListener(new WindowEventListener());
        setTitle(Lang.getMsg("application.Name") + MapEditor.getVersion());
        setSize(WINDOW_SIZE);
        getRibbon().setApplicationMenu(new MainMenu(this));

        map = MapPanel.getInstance();
        selector = new ObjectSelector();
        final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(selector);
        add(map, BorderLayout.CENTER);
        add(splitPane, BorderLayout.EAST);
        final RibbonTask task = new RibbonTask(Lang.getMsg("gui.mainframe.ribbon"),
                new ClipboardBand(), new ZoomBand(), new ViewBand(), new ToolBand());


        getRibbon().addTask(task);
        setApplicationIcon(ImageLoader.getResizableIcon("mapedit64"));
        new ObjectSelector();
    }

    public static MainFrame getInstance() {
        if (instance == null) {
            return new MainFrame();
        }
        return instance;

    }

    public MapPanel getMapPanel() {
        return map;
    }

    public void exit() {
        dispose();
    }
}