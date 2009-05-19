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

import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.ui.QuickTimePlayer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class QTShowcase extends AbstractCase {

    public QTShowcase() {
        Widget qt = null;
        try {
            qt = new QuickTimePlayer(GWT.getHostPageBaseURL() + "media/thunder.mp3");
        } catch (LoadException ex) {
            Window.alert("Load exp");
        } catch (PluginVersionException ex) {
            qt = PlayerUtil.getMissingPluginNotice(Plugin.QuickTimePlayer, ex.getRequiredVersion());
        } catch (PluginNotFoundException ex) {
            qt = PlayerUtil.getMissingPluginNotice(Plugin.QuickTimePlayer);
        }
        addCase(qt, "sources/qt/auto.html");

        Widget qt2 = null;
        try {
            qt2 = new QuickTimePlayer(GWT.getHostPageBaseURL() + "media/thunder.mp3", false);
        } catch (LoadException ex) {
            Window.alert("Load exp");
        } catch (PluginVersionException ex) {
            qt2 = PlayerUtil.getMissingPluginNotice(Plugin.QuickTimePlayer, ex.getRequiredVersion());
        } catch (PluginNotFoundException ex) {
            qt2 = PlayerUtil.getMissingPluginNotice(Plugin.QuickTimePlayer);
        }
        addCase(qt2, "sources/qt/no-auto.html");

        Widget qt3 = null;
        try {
            QuickTimePlayer p = new QuickTimePlayer(GWT.getHostPageBaseURL() + "media/o-na-som.mp3",
                    false);
            p.showLogger(true);
            qt3 = p;
        } catch (LoadException ex) {
            Window.alert("Load exp");
        } catch (PluginVersionException ex) {
            qt3 = PlayerUtil.getMissingPluginNotice(Plugin.QuickTimePlayer, ex.getRequiredVersion());
        } catch (PluginNotFoundException ex) {
            qt3 = PlayerUtil.getMissingPluginNotice(Plugin.QuickTimePlayer);
        }
        addCase(qt3, "sources/qt/show-logger.html");

        Widget qt4 = null;
        try {
            QuickTimePlayer p = new QuickTimePlayer("http://movies.apple.com/movies/" +
                    "paramount/star_trek/startrek-tlr3_h.480.mov", false, "220px", "100%");
            p.showLogger(true);
            qt4 = p;
        } catch (LoadException ex) {
            Window.alert("Load exp");
        } catch (PluginVersionException ex) {
            qt4 = PlayerUtil.getMissingPluginNotice(Plugin.QuickTimePlayer, ex.getRequiredVersion());
        } catch (PluginNotFoundException ex) {
            qt4 = PlayerUtil.getMissingPluginNotice(Plugin.QuickTimePlayer);
        }
        addCase(qt4, "sources/qt/video.html");
    }

    public String getSummary() {
        return "Using QuickTime Player plugin";
    }
}
