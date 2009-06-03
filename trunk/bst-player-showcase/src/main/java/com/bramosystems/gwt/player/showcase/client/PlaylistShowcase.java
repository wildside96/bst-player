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
import com.bramosystems.oss.player.core.client.ui.FlashPlayer;
import com.bramosystems.oss.player.core.client.ui.QuickTimePlayer;
import com.bramosystems.oss.player.core.client.ui.WinMediaPlayer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class PlaylistShowcase extends AbstractCase {

    public PlaylistShowcase() {
        Widget c3 = null;
        try {
            WinMediaPlayer mp = new WinMediaPlayer(GWT.getHostPageBaseURL() + "media/playlist.m3u", false);
            mp.showLogger(true);
            mp.setLoopCount(2);
            c3 = mp;
        } catch (LoadException ex) {
            Window.alert("Load exp");
        } catch (PluginVersionException ex) {
            c3 = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer, ex.getRequiredVersion());
        } catch (PluginNotFoundException ex) {
            c3 = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer);
        }
        addCase(c3, "sources/custom-audio/wmp.html");

        Widget c2 = null;
        try {
            FlashPlayer mp = new FlashPlayer(GWT.getHostPageBaseURL() + "media/funky.m3u", false);
            mp.showLogger(true);
            mp.addToPlaylist(GWT.getHostPageBaseURL() + "media/playlist.m3u");
            mp.setShuffleEnabled(true);
            c2 = mp;
        } catch (LoadException ex) {
            Window.alert("Load exp");
        } catch (PluginVersionException ex) {
            c2 = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer, ex.getRequiredVersion());
        } catch (PluginNotFoundException ex) {
            c2 = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer);
        }
        addCase(c2, "sources/custom-audio/wmp.html");

        Widget c = null;
        try {
            QuickTimePlayer mp = new QuickTimePlayer(GWT.getHostPageBaseURL() + "media/playlist.m3u", false);
            mp.showLogger(true);
            c = mp;
        } catch (LoadException ex) {
            Window.alert("Load exp");
        } catch (PluginVersionException ex) {
            c = PlayerUtil.getMissingPluginNotice(Plugin.QuickTimePlayer, ex.getRequiredVersion());
        } catch (PluginNotFoundException ex) {
            c = PlayerUtil.getMissingPluginNotice(Plugin.QuickTimePlayer);
        }
        addCase(c, "sources/custom-audio/wmp.html");

        Widget c4 = null;
        try {
            Capsule mp = new Capsule(GWT.getHostPageBaseURL() + "media/playlist.m3u", false);
            mp.showLogger(true);
            c4 = mp;
        } catch (LoadException ex) {
            Window.alert("Load exp");
        } catch (PluginVersionException ex) {
            c4 = PlayerUtil.getMissingPluginNotice(Plugin.Auto, ex.getRequiredVersion());
        } catch (PluginNotFoundException ex) {
            c4 = PlayerUtil.getMissingPluginNotice(Plugin.Auto);
        }
        addCase(c4, "sources/custom-audio/wmp.html");
    }

    public String getSummary() {
        return "Working with playlists";
    }
}
