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
import com.bramosystems.oss.player.common.client.Links;
import com.bramosystems.oss.player.resources.sources.ResourceBundle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class WMPShowcase extends AbstractCase {

    public static AbstractCase instance = new WMPShowcase();

    public WMPShowcase() {
    }

    public String getSummary() {
        return "Embedding Windows Media Player plugin";
    }

    @Override
    public void initCase(Links link) {
        Widget wmp = null, wmp1 = null;
        switch (link) {
            case wmpBasic:
                try {
                    wmp = new WinMediaPlayer(GWT.getHostPageBaseURL() + "media/applause.mp3");
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    wmp = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    wmp = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer);
                }
                addCase("Playing sound automatically", null, wmp,
                        ResourceBundle.bundle.wmpBasic());
//                break;
//            case wmpLogger:
                try {
                    final WinMediaPlayer p = new WinMediaPlayer(GWT.getHostPageBaseURL()
                            + "media/o-na-som.mp3", false);
                    p.showLogger(true);
                    p.setLoopCount(20);
                    wmp = p;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    wmp = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    wmp = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer);
                }
                addCase("Playing sound with logger widget visible", null,
                        wmp, ResourceBundle.bundle.wmpLogger());

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
                        wmp1, ResourceBundle.bundle.wmpLogger());
                break;
            case wmpVideo:
                String url = "http://bst-player.googlecode.com/svn/tags/showcase/media/islamic-jihad.wmv";
                try {
                    WinMediaPlayer p = new WinMediaPlayer(url, false, getCaseHeight(), "100%");
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
                        wmp, ResourceBundle.bundle.wmpVideo());

                try {
                    WinMediaPlayer p = new WinMediaPlayer(url, false, getCaseHeight(), "100%");
                    p.setResizeToVideoSize(true);
                    p.showLogger(true);
                    wmp1 = p;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    wmp1 = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    wmp1 = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer);
                }
                addCase("Auto-adjust to video size", "Islamic Jihad",
                        wmp1, ResourceBundle.bundle.wmpVideoAuto());
                break;
            case wmpPlaylist:
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
                        wmp, ResourceBundle.bundle.wmpPlaylist());
                break;
        }
    }
}
