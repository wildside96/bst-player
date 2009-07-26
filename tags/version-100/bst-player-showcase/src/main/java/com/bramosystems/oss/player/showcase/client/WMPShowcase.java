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
import com.bramosystems.oss.player.core.client.ui.WinMediaPlayer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class WMPShowcase extends AbstractCase {

    public static String[] caseNames = {"Embed Windows Media Player",
        "With Logger widget visible", "Embedding Video", "Windows Media Playlist"};
    public static String[] caseLinks = {"wmp/basic", "wmp/logger", "wmp/video", "wmp/playlist"};

    public WMPShowcase() {
    }

    public String getSummary() {
        return "Embedding Windows Media Player plugin";
    }

    @Override
    public void init(String token) {
        clearCases();
        Widget wmp = null, wmp1 = null;
        int index = getTokenLinkIndex(caseLinks, token);
        switch (index) {
            case 0:
                try {
                    wmp = new WinMediaPlayer(GWT.getHostPageBaseURL() + "media/applause.mp3");
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    wmp = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    wmp = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer);
                }
                addCase("Playing sound automatically", null, wmp, "sources/wmp/auto.html");

                try {
                    wmp1 = new WinMediaPlayer(GWT.getHostPageBaseURL() + "media/applause.mp3", false);
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    wmp1 = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    wmp1 = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer);
                }
                addCase("Playing sound with autoplay set to false", null,
                        wmp1, "sources/wmp/no-auto.html");
                break;
            case 1:
                try {
                    WinMediaPlayer p = new WinMediaPlayer(GWT.getHostPageBaseURL() +
                            "media/o-na-som.mp3", false);
                    p.showLogger(true);
                    wmp = p;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    wmp = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    wmp = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer);
                }
                addCase("Playing sound with logger widget visible", null,
                        wmp, "sources/wmp/show-logger.html");
                break;
            case 2:
                try {
                    WinMediaPlayer p = new WinMediaPlayer("http://bst-player.googlecode.com/svn/" +
                            "tags/showcase/media/islamic-jihad.wmv",
                            false, "350px", "100%");
                    p.showLogger(true);
                    wmp = p;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    wmp = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    wmp = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer);
                }
                addCase("Embedding video", "Islamic Jihad",
                        wmp, "sources/wmp/video.html");

                try {
                    WinMediaPlayer p = new WinMediaPlayer("http://bst-player.googlecode.com/svn/" +
                            "tags/showcase/media/islamic-jihad.wmv",
                            false, "350px", "100%");
                    p.setResizeToVideoSize(true);
                    wmp1 = p;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    wmp1 = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    wmp1 = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer);
                }
                addCase("Auto-adjust to video size", "Islamic Jihad",
                        wmp1, "sources/wmp/video.html");
                break;
            case 3:
                try {
                    WinMediaPlayer p = new WinMediaPlayer(GWT.getHostPageBaseURL() +
                            "media/playlist.wpl", false);
                    p.showLogger(true);
                    wmp = p;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    wmp = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    wmp = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer);
                }
                addCase("Using playlists", "(Windows Media playlist)",
                        wmp, "sources/wmp/playlist.html");
                break;
        }
    }
}
