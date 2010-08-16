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

import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.ui.FlashMediaPlayer;
import com.bramosystems.oss.player.common.client.Links;
import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.PlaylistSupport;
import com.bramosystems.oss.player.core.client.ui.VLCPlayer;
import com.bramosystems.oss.player.resources.sources.ResourceBundle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class PlaylistShowcase extends AbstractCase {

    public static AbstractCase instance = new PlaylistShowcase();

    private PlaylistShowcase() {
    }

    public String getSummary() {
        return "Using client-side playlists";
    }

    @Override
    public void initCase(Links link) {
        Widget v = null;
        switch (link) {
            case listSwf:
                try {
                    FlashMediaPlayer mp = new FlashMediaPlayer(GWT.getHostPageBaseURL() + "media/o-na-som.mp3", false);
                    mp.addToPlaylist(GWT.getHostPageBaseURL() + "media/thunder.mp3");
                    mp.addToPlaylist(GWT.getHostPageBaseURL() + "media/applause.mp3");
                    v = mp;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer);
                }
                addCase("Using playlist with Adobe Flash", null, v,
                        ResourceBundle.bundle.pllSwf());
                break;
            case listVlc:
                try {
                    VLCPlayer mp = new VLCPlayer(GWT.getHostPageBaseURL() + "media/thunder.mp3", false);
                    mp.setShuffleEnabled(true);
                    mp.addToPlaylist(GWT.getHostPageBaseURL() + "media/applause.mp3");
                    mp.addToPlaylist(GWT.getHostPageBaseURL() + "media/o-na-som.mp3");
                    mp.showLogger(true);
                    v = mp;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer);
                }
                addCase("Using playlist with VLC Media Player", null,
                        v, ResourceBundle.bundle.pllVlc());
                break;
            case listAuto:
                try {
                    AbstractMediaPlayer mp = PlayerUtil.getPlayer(Plugin.PlaylistSupport,
                            GWT.getHostPageBaseURL() + "media/o-na-som.mp3", false);
                    ((PlaylistSupport) mp).addToPlaylist(GWT.getHostPageBaseURL() + "media/thunder.mp3");
                    ((PlaylistSupport) mp).addToPlaylist(GWT.getHostPageBaseURL() + "media/applause.mp3");
                    ((PlaylistSupport) mp).setShuffleEnabled(true);
                    v = mp;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.PlaylistSupport, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.PlaylistSupport);
                }
                addCase("Playlist handling using any suitable player plugin with shuffling enabled", null,
                        v, ResourceBundle.bundle.pllAuto());
                break;
        }
    }
}
