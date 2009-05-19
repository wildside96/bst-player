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

package com.bramosystems.gwt.player.showcase.client;

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

    public DynaShowcase() {
        Widget c = null;
        try {
            c = new Capsule(Plugin.WinMediaPlayer, GWT.getHostPageBaseURL() + "media/o-na-som.mp3", false);
        } catch (LoadException ex) {
            Window.alert("Load exp");
        } catch (PluginVersionException ex) {
            c = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer, ex.getRequiredVersion());
        } catch (PluginNotFoundException ex) {
            c = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer);
        }
        addCase(c, "sources/custom-audio/wmp.html");

        Widget c2 = null;
        try {
            c2 = new Capsule(Plugin.FlashMP3Player, GWT.getHostPageBaseURL() + "media/applause.mp3", false);
        } catch (LoadException ex) {
            Window.alert("Load exp");
        } catch (PluginVersionException ex) {
            c2 = PlayerUtil.getMissingPluginNotice(Plugin.FlashMP3Player, ex.getRequiredVersion());
        } catch (PluginNotFoundException ex) {
            c2 = PlayerUtil.getMissingPluginNotice(Plugin.FlashMP3Player);
        }
        addCase(c2, "sources/custom-audio/swf.html");

        Widget c3 = null;
        try {
            c3 = new Capsule(Plugin.QuickTimePlayer, GWT.getHostPageBaseURL() + "media/thunder.mp3", false);
        } catch (LoadException ex) {
            Window.alert("Load exp");
        } catch (PluginVersionException ex) {
            c3 = PlayerUtil.getMissingPluginNotice(Plugin.QuickTimePlayer, ex.getRequiredVersion());
        } catch (PluginNotFoundException ex) {
            c3 = PlayerUtil.getMissingPluginNotice(Plugin.QuickTimePlayer);
        }
            addCase(c3, "sources/custom-audio/qt.html");

        Widget c4 = null;
        try {
            Capsule cap = new Capsule(GWT.getHostPageBaseURL() + "media/o-na-som.mp3", false);
            cap.showLogger(true);
            c4 = cap;
        } catch (LoadException ex) {
            Window.alert("Load exp");
        } catch (PluginVersionException ex) {
            c4 = PlayerUtil.getMissingPluginNotice(Plugin.Auto, "No Plugin",
                    "A compatible sound plugin could not be found", false);
        } catch (PluginNotFoundException ex) {
            c4 = PlayerUtil.getMissingPluginNotice(Plugin.Auto, "No Plugin",
                    "A compatible sound plugin could not be found", false);
        }
        addCase(c4, "sources/custom-audio/dyn.html");
    }

    public String getSummary() {
        return "Using custom sound player controls.";
    }
}
