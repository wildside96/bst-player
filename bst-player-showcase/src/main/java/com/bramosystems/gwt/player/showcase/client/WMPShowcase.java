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
import com.bramosystems.oss.player.core.client.ui.WinMediaPlayer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class WMPShowcase extends AbstractCase {

    public WMPShowcase() {
        Widget wmp = null;
        try {
            wmp = new WinMediaPlayer(GWT.getHostPageBaseURL() + "media/applause.mp3", false);
        } catch (LoadException ex) {
            Window.alert("Load exp");
        } catch (PluginVersionException ex) {
            wmp = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer, ex.getRequiredVersion());
        } catch (PluginNotFoundException ex) {
            wmp = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer);
        }
        addCase(wmp, "sources/wmp/auto.html");

        Widget wmp2 = null;
        try {
            final WinMediaPlayer wmp2p = new WinMediaPlayer(GWT.getHostPageBaseURL() + "media/applause.mp3", false);
            wmp2p.addToPlaylist(GWT.getHostPageBaseURL() + "media/o-na-som.mp3");
            wmp2p.setLoopCount(2);
            wmp2 = wmp2p;
        } catch (LoadException ex) {
            Window.alert("Load exp");
        } catch (PluginVersionException ex) {
            wmp2 = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer, ex.getRequiredVersion());
        } catch (PluginNotFoundException ex) {
            wmp2 = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer);
        }
        addCase(wmp2, "sources/wmp/no-auto.html");

        Widget wmp3 = null;
        try {
            WinMediaPlayer p = new WinMediaPlayer(GWT.getHostPageBaseURL() + "media/o-na-som.mp3", false);
            p.showLogger(true);
            wmp3 = p;
        } catch (LoadException ex) {
            Window.alert("Load exp");
        } catch (PluginVersionException ex) {
            wmp3 = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer, ex.getRequiredVersion());
        } catch (PluginNotFoundException ex) {
            wmp3 = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer);
        }
        addCase(wmp3, "sources/wmp/show-logger.html");

        Widget wmp4 = null;
        try {
            WinMediaPlayer p = new WinMediaPlayer("http://bst-player.googlecode.com/svn/" +
                    "tags/showcase/media/islamic-jihad.wmv",
                    false, "350px", "100%");
            p.showLogger(true);
            wmp4 = p;
        } catch (LoadException ex) {
            Window.alert("Load exp");
        } catch (PluginVersionException ex) {
            wmp4 = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer, ex.getRequiredVersion());
        } catch (PluginNotFoundException ex) {
            wmp4 = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer);
        }
        addCase(wmp4, "sources/wmp/video.html");
}

    public String getSummary() {
        return "Embedding Windows Media Player plugin";
    }
}
