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
package illarion.client.gui.controller;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import illarion.client.IllaClient;
import illarion.client.Servers;
import illarion.client.graphics.DisplayModeSorter;
import illarion.common.bug.CrashReporter;
import illarion.common.config.Config;
import illarion.common.graphics.GraphicResolution;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.Display;
import org.newdawn.slick.GameContainer;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class OptionScreenController implements ScreenController {

    private Nifty nifty;
    private Screen screen;

    //private DropDown<String> charNameLength;
    //private CheckBox showCharId;
    private CheckBox runAutoAvoid;
    private DropDown<String> sendCrashReports;

    private DropDown<String> resolutions;
    private CheckBox fullscreen;

    private CheckBox soundOn;
    private Slider soundVolume;
    private CheckBox musicOn;
    private Slider musicVolume;

    private TextField serverAddress;
    private TextField serverPort;
    private TextField clientVersion;
    private CheckBox serverAccountLogin;
    private CheckBox serverResetSettings;

    @Override
    public void bind(final Nifty nifty, @Nonnull final Screen screen) {
        this.nifty = nifty;
        this.screen = screen;

        final Element tabRoot = screen.findElementByName("tabRoot");

        //charNameLength = screen.findNiftyControl("charNameLength", DropDown.class);
        //charNameLength.addItem("${options-bundle.charNameDisplay.short}");
        //charNameLength.addItem("${options-bundle.charNameDisplay.long}");

        //showCharId = screen.findNiftyControl("showCharId", CheckBox.class);

        runAutoAvoid = tabRoot.findNiftyControl("runAutoAvoid", CheckBox.class);

        sendCrashReports = tabRoot.findNiftyControl("sendCrashReports", DropDown.class);
        sendCrashReports.addItem("${options-bundle.report.ask}");
        sendCrashReports.addItem("${options-bundle.report.always}");
        sendCrashReports.addItem("${options-bundle.report.never}");

        resolutions = tabRoot.findNiftyControl("resolutions", DropDown.class);
        resolutions.addAllItems(getResolutionList());

        fullscreen = tabRoot.findNiftyControl("fullscreen", CheckBox.class);

        soundOn = tabRoot.findNiftyControl("soundOn", CheckBox.class);
        soundVolume = tabRoot.findNiftyControl("soundVolume", Slider.class);
        musicOn = tabRoot.findNiftyControl("musicOn", CheckBox.class);
        musicVolume = tabRoot.findNiftyControl("musicVolume", Slider.class);

        final Element serverTab = tabRoot.findElementByName("#serverTab");
        if (serverTab == null) {
            return;
        }
        if (IllaClient.DEFAULT_SERVER == Servers.realserver) {
            tabRoot.getNiftyControl(TabGroup.class).removeTab(serverTab);
        } else {
            serverAddress = serverTab.findNiftyControl("serverAddress", TextField.class);
            serverPort = serverTab.findNiftyControl("serverPort", TextField.class);
            clientVersion = serverTab.findNiftyControl("clientVersion", TextField.class);
            serverAccountLogin = serverTab.findNiftyControl("serverAccountLogin", CheckBox.class);
            serverResetSettings = serverTab.findNiftyControl("resetServerSettings", CheckBox.class);
        }
    }

    @Override
    public void onStartScreen() {
        //charNameLength.selectItemByIndex(IllaClient.getCfg().getInteger(People.CFG_NAMEMODE_KEY) - 1);
        //showCharId.setChecked(IllaClient.getCfg().getBoolean(People.CFG_SHOWID_KEY));
        runAutoAvoid.setChecked(IllaClient.getCfg().getBoolean("runAutoAvoid"));
        sendCrashReports.selectItemByIndex(IllaClient.getCfg().getInteger(CrashReporter.CFG_KEY));
        resolutions.selectItem(IllaClient.getCfg().getString(IllaClient.CFG_RESOLUTION));
        fullscreen.setChecked(IllaClient.getCfg().getBoolean(IllaClient.CFG_FULLSCREEN));

        soundOn.setChecked(IllaClient.getCfg().getBoolean("soundOn"));
        soundVolume.setValue(IllaClient.getCfg().getFloat("soundVolume"));
        musicOn.setChecked(IllaClient.getCfg().getBoolean("musicOn"));
        musicVolume.setValue(IllaClient.getCfg().getFloat("musicVolume"));

        if (serverAddress != null) {
            serverAddress.setText(IllaClient.getCfg().getString("serverAddress"));
            serverPort.setText(Integer.toString(IllaClient.getCfg().getInteger("serverPort")));
            clientVersion.setText(Integer.toString(IllaClient.getCfg().getInteger("clientVersion")));
            serverAccountLogin.setChecked(IllaClient.getCfg().getBoolean("serverAccountLogin"));
            serverResetSettings.setChecked(false);
        }
    }

    @NiftyEventSubscriber(id = "saveButton")
    public void onSaveButtonClickedEvent(final String topic, final ButtonClickedEvent event) {
        nifty.gotoScreen("login");
        final Config configSystem = IllaClient.getCfg();

        //configSystem.set(People.CFG_NAMEMODE_KEY, charNameLength.getSelectedIndex() + 1);
        //configSystem.set(People.CFG_SHOWID_KEY, showCharId.isChecked());
        configSystem.set("runAutoAvoid", runAutoAvoid.isChecked());
        configSystem.set(CrashReporter.CFG_KEY, sendCrashReports.getSelectedIndex());
        configSystem.set(IllaClient.CFG_RESOLUTION, resolutions.getSelection());
        configSystem.set(IllaClient.CFG_FULLSCREEN, fullscreen.isChecked());

        configSystem.set("soundOn", soundOn.isChecked());
        configSystem.set("soundVolume", soundVolume.getValue());
        configSystem.set("musicOn", musicOn.isChecked());
        configSystem.set("musicVolume", musicVolume.getValue());

        if (serverAddress != null) {
            if (serverResetSettings.isChecked()) {
                configSystem.set("serverAddress", Servers.testserver.getServerHost());
                configSystem.set("serverPort", Servers.testserver.getServerPort());
                configSystem.set("clientVersion", Servers.testserver.getClientVersion());
                configSystem.set("serverAccountLogin", true);
            } else {
                configSystem.set("serverAddress", serverAddress.getRealText());
                configSystem.set("serverPort", Integer.parseInt(serverPort.getRealText()));
                configSystem.set("clientVersion", Integer.parseInt(clientVersion.getRealText()));
                configSystem.set("serverAccountLogin", serverAccountLogin.isChecked());
            }
        }

        configSystem.save();
    }

    @NiftyEventSubscriber(id = "cancelButton")
    public void onCancelButtonClickedEvent(final String topic, final ButtonClickedEvent event) {
        nifty.gotoScreen("login");
    }

    @Override
    public void onEndScreen() {
    }

    @Nonnull
    public static List<String> getResolutionList() {
        final GameContainer container = IllaClient.getInstance().getContainer();

        DisplayMode[] displayModes;
        try {
            displayModes = Display.getAvailableDisplayModes(800, 600, container.getScreenWidth(),
                    container.getScreenHeight(), 24, 32, 40, 120);
        } catch (LWJGLException exc) {
            displayModes = new DisplayMode[1];
            displayModes[0] = new DisplayMode(800, 600);
        }

        Arrays.sort(displayModes, new DisplayModeSorter());

        final List<String> resList = new ArrayList<String>();

        for (final DisplayMode mode : displayModes) {
            resList.add(new GraphicResolution(mode.getWidth(), mode.getHeight(), mode.getBitsPerPixel(),
                    mode.getFrequency()).toString());
        }

        return resList;
    }
}
