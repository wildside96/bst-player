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

import com.bramosystems.oss.player.core.client.ConfigParameter;
import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.ui.WinMediaPlayer;
import com.bramosystems.oss.player.core.event.client.PlayStateEvent;
import com.bramosystems.oss.player.core.event.client.PlayStateHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class WMPShowcase extends AbstractCase {

    public static String[] caseNames = {"Embed Windows Media Player", "With Logger widget visible",
        "Embedding Video", "Windows Media Playlist", "WMP UI Mode"};
    public static String[] caseLinks = {"wmp/basic", "wmp/logger", "wmp/video", "wmp/playlist", "wmp/uimode"};

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
                    final WinMediaPlayer p = new WinMediaPlayer(GWT.getHostPageBaseURL()
                            + "media/o-na-som.mp3", false);
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

                try {
                    final Label lbl = new Label();
                    WinMediaPlayer p = new WinMediaPlayer(GWT.getHostPageBaseURL()
                            + "media/o-na-som.mp3", false);
                    p.showLogger(true);
                    p.addMouseMoveHandler(new MouseMoveHandler() {

                        public void onMouseMove(MouseMoveEvent event) {
                            lbl.setText("x:y = " + event.getX() + ":" + event.getY()
                                    + ", cx:cy = " + event.getClientX() + ":" + event.getClientY()
                                    + ", sx:xy = " + event.getScreenX() + ":" + event.getScreenY());
                        }
                    });
                    p.addKeyPressHandler(new KeyPressHandler() {

                        public void onKeyPress(KeyPressEvent event) {
                            lbl.setText("[KeyPress] Xter Code = " + event.getCharCode());
                        }
                    });
                    VerticalPanel vp = new VerticalPanel();
                    vp.setWidth("100%");
                    vp.add(p);
                    vp.add(lbl);
                    wmp1 = vp;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    wmp1 = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    wmp1 = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer);
                }
                addCase("Playing sound with logger widget visible", null,
                        wmp1, "sources/wmp/show-logger.html");
                break;
            case 2:
                try {
                    WinMediaPlayer p = new WinMediaPlayer("http://bst-player.googlecode.com/svn/"
                            + "tags/showcase/media/islamic-jihad.wmv",
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
                    WinMediaPlayer p = new WinMediaPlayer("http://bst-player.googlecode.com/svn/"
                            + "tags/showcase/media/islamic-jihad.wmv",
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
                    WinMediaPlayer p = new WinMediaPlayer(GWT.getHostPageBaseURL()
                            + "media/playlist.wpl", false);
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
            case 4:
                String wmv = "http://localhost/xplorer/g:/Islamics%20VIDEOS/Qadian2008-Arrival-in-India.wmv";

                try {
                    final PopupPanel pp = new DialogBox(false, false);
                    pp.setWidth("300px");
                    pp.setHeight("400px");
                    pp.setWidget(new Image(GWT.getHostPageBaseURL() + "images/loading.gif"));
                    DOM.setStyleAttribute(pp.getElement(), "backgroundColor", "blue");

                    final WinMediaPlayer p = new WinMediaPlayer(wmv, false, "350px", "100%");
                    p.setConfigParameter(ConfigParameter.WMPUIMode, WinMediaPlayer.UIMode.MINI);
                    p.showLogger(true);
                    p.addPlayStateHandler(new PlayStateHandler() {

                        public void onPlayStateChanged(PlayStateEvent event) {
                            pp.center();
                        }
                    });
                    wmp = p;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    wmp = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    wmp = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer);
                }
                addCase("Embedding video", "UI MODE FULL",
                        wmp, "sources/wmp/video.html");

                try {
                    final WinMediaPlayer p = new WinMediaPlayer(wmv, false, "350px", "100%");
                    p.setUIMode(WinMediaPlayer.UIMode.NONE);
                    p.showLogger(true);
                    wmp1 = p;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    wmp1 = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    wmp1 = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer);
                }
                addCase("Auto-adjust to video size", "NONE",
                        wmp1, "sources/wmp/video.html");
                break;
        }
    }
}
