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
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class SWFShowcase extends AbstractCase {

    public static String[] caseNames = {"Embed Flash plugin",
        "With Logger widget visible", "Embedding Video", "Using M3U playlists"};
    public static String[] caseLinks = {"swf/basic", "swf/logger", "swf/video", "swf/playlists"};

    public SWFShowcase() {
    }

    public String getSummary() {
        return "Media playback with Adobe Flash plugin";
    }

    @Override
    public void init(String token) {
        clearCases();
        Widget mp = null, mp2 = null;
        int index = getTokenLinkIndex(caseLinks, token);
        switch (index) {
            case 0:
                try {
                    mp = new FlashMediaPlayer(GWT.getHostPageBaseURL() + "media/applause.mp3");
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    mp = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    mp = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer);
                }
                addCase("Playing MP3 media automatically", null, mp, "sources/swf/auto.html");

                try {
                    mp2 = new FlashMediaPlayer(GWT.getHostPageBaseURL() + "media/thunder.mp3", false);
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    mp2 = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    mp2 = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer);
                }
                addCase("Playing media with autoplay set to false", null,
                        mp2, "sources/swf/no-auto.html");
                break;
            case 1:
                try {
                    FlashMediaPlayer p3 = new FlashMediaPlayer(GWT.getHostPageBaseURL() + "media/o-na-som.mp3", false);
                    p3.showLogger(true);
                    mp = p3;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    mp = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    mp = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer);
                }
                addCase("Playing sound with logger widget visible", null,
                        mp, "sources/swf/show-logger.html");
                break;
            case 2:
                try {
                    FlashMediaPlayer mmp = new FlashMediaPlayer(GWT.getHostPageBaseURL() + "media/traffic.flv",
                            false, "350px", "100%");
                    mmp.showLogger(true);
                    mp = mmp;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    mp = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    mp = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer);
                }
                addCase("Embedding video", null, mp, "sources/swf/video.html");

                try {
                    FlashMediaPlayer mmp = new FlashMediaPlayer(GWT.getHostPageBaseURL() + "media/traffic.flv",
                            false, "350px", "100%");
                    mmp.setResizeToVideoSize(true);
                    mp2 = mmp;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    mp2 = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    mp2 = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer);
                }
                addCase("Player adjusted to video size", null, mp2, "sources/swf/video-auto.html");
                break;
            case 3:
                try {
                    FlashMediaPlayer fp = new FlashMediaPlayer(GWT.getHostPageBaseURL() + "media/playlist.m3u", false);
                    fp.showLogger(true);
                    mp = fp;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    mp = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    mp = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer);
                }
                addCase("Working with M3U playlists", null, mp, "sources/swf/playlists.html");
                break;
        }
    }
}
