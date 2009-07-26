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
import com.bramosystems.oss.player.flat.client.FlatVideoPlayer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class DynaVideoShowcase extends AbstractCase {

    public static String[] caseNames = {"Custom controls with WMP",
        "Custom controls with Flash", "Custom controls with QuickTime plugin",
        "Custom controls with VLC Media Player", "Plugin determined at runtime"};
    public static String[] caseLinks = {"dynvd/wmp", "dynvd/swf", "dynvd/qt", "dynvd/vlc", "dynvd/dyna"};

    public DynaVideoShowcase() {
    }

    public String getSummary() {
        return "Working with custom video player controls.";
    }

    @Override
    public void init(String token) {
        clearCases();
        Widget v = null;
        int index = getTokenLinkIndex(caseLinks, token);
        switch (index) {
            case 0:
                try {
                    FlatVideoPlayer fv = new FlatVideoPlayer(Plugin.WinMediaPlayer,
                            "http://bst-player.googlecode.com/svn/tags/showcase/media/teaching-of-islam.wmv",
                            false, "350px", "100%");
                    fv.showLogger(true);
                    v = fv;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer);
                }
                addCase("Custom video player with Windows Media Player plugin", 
                        "Teaching of Islam", v, "sources/custom-video/wmp.html");
                break;
            case 1:
                try {
                    FlatVideoPlayer vp = new FlatVideoPlayer(Plugin.FlashPlayer,
                            GWT.getHostPageBaseURL() + "media/traffic.flv",
                            false, "350px", "100%");
                    vp.showLogger(true);
                    vp.setResizeToVideoSize(true);
                    v = vp;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer);
                }
                addCase("Custom video player with Flash plugin", null,
                        v, "sources/custom-video/swf.html");
                break;
            case 2:
                try {
                    FlatVideoPlayer vp = new FlatVideoPlayer(Plugin.QuickTimePlayer,
                            GWT.getHostPageBaseURL() + "media/traffic.mp4",
                            false, "250px", "100%");
                    vp.showLogger(true);
                    v = vp;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.QuickTimePlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.QuickTimePlayer);
                }
                addCase("Custom video player with QuickTime Player plugin", null,
                        v, "sources/custom-video/qt.html");
                break;
            case 3:
                try {
                    FlatVideoPlayer vp = new FlatVideoPlayer(Plugin.VLCPlayer,
                            GWT.getHostPageBaseURL() + "media/traffic.mp4",
                            false, "350px", "100%");
                    vp.showLogger(true);
                    v = vp;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.VLCPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.VLCPlayer);
                }
                addCase("Custom video playback with VLC Media Player", null,
                        v, "sources/custom-video/vlc.html");
                break;
            case 4:
                try {
                    FlatVideoPlayer p = new FlatVideoPlayer(GWT.getHostPageBaseURL() +
                            "media/traffic.mp4", false, "350px", "100%");
                    p.showLogger(true);
                    v = p;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.Auto, "Missing Plugin",
                            "No compatible video player plugin could be found", false);
                } catch (PluginNotFoundException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.Auto, "Missing Plugin",
                            "No compatible video player plugin could be found", false);
                }
                addCase("Embedding video with any suitable plugin", null, v, "sources/custom-video/dyn.html");
                break;
        }
    }
}
