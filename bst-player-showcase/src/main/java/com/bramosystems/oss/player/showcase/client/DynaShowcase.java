/*
 * Copyright 2009 Sikirulai Braheem
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.bramosystems.oss.player.showcase.client;

import com.bramosystems.oss.player.capsule.client.Capsule;
import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class DynaShowcase extends AbstractCase {

    public static String[] caseNames = {"Custom controls with Windows " +
            "Media Player plugin", "Custom controls with Flash plugin",
            "Custom controls with QuickTime plugin",
            "Custom controls with VLC Player Plugin",
            "Plugin determined at runtime"};
    public static String[] caseLinks = {"dyn/wmp", "dyn/swf", "dyn/qt", "dyn/vlc", "dyn/dyna"};

    public DynaShowcase() {
    }

    public String getSummary() {
        return "Using custom sound player controls.";
    }

    @Override
    public void init(String token) {
        clearCases();
        Widget v = null;
        int index = getTokenLinkIndex(caseLinks, token);
        switch (index) {
            case 0:
                try {
                    Capsule c = new Capsule(Plugin.WinMediaPlayer, GWT.getHostPageBaseURL() + "media/o-na-som.mp3", false);
                    c.showLogger(true);
                    v = c;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer);
                }
                addCase("Playing sound with Windows Media Player plugin", null,
                        v, "sources/custom-audio/wmp.html");
                break;
            case 1:
                try {
                    Capsule c = new Capsule(Plugin.FlashPlayer, GWT.getHostPageBaseURL() + "media/applause.mp3", false);
                    c.showLogger(true);
                    v = c;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer);
                }
                addCase("Playing with Flash Player plugin", null,
                        v, "sources/custom-audio/swf.html");
                break;
            case 2:
                try {
                    Capsule cv = new Capsule(Plugin.QuickTimePlayer, GWT.getHostPageBaseURL() + "media/thunder.mp3", false);
                    cv.showLogger(true);
                    v = cv;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.QuickTimePlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.QuickTimePlayer);
                }
                addCase("Playback with QuickTime Player plugin", null,
                        v, "sources/custom-audio/qt.html");
                break;
            case 3:
                try {
                    Capsule cv = new Capsule(Plugin.VLCPlayer, GWT.getHostPageBaseURL() + "media/thunder.mp3", false);
                    cv.showLogger(true);
                    v = cv;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.VLCPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.VLCPlayer);
                }
                addCase("Playback with VLC Media Player plugin", null,
                        v, "sources/custom-audio/vlc.html");
                break;
            case 4:
                try {
                    Capsule cap = new Capsule(GWT.getHostPageBaseURL() + "media/o-na-som.mp3", false);
                    cap.showLogger(true);
                    v = cap;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.Auto, "No Plugin",
                            "A compatible sound plugin could not be found", false);
                } catch (PluginNotFoundException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.Auto, "No Plugin",
                            "A compatible sound plugin could not be found", false);
                }
                addCase("Using a dynamically determined media plugin",
                        "Use the logger to check which plugin is in use",
                        v, "sources/custom-audio/dyn.html");
                break;
        }
    }
}
