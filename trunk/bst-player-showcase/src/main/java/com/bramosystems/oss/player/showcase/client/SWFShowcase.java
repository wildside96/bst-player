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
import com.bramosystems.oss.player.resources.sources.ResourceBundle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class SWFShowcase extends AbstractCase {

    public static AbstractCase instance = new SWFShowcase();

    private SWFShowcase() {
    }

    public String getSummary() {
        return "Media playback with Adobe Flash plugin";
    }

    @Override
    public void initCase(Links link) {
        Widget mp = null, mp2 = null;
        switch (link) {
            case swfBasic:
                try {
                    mp = new FlashMediaPlayer(GWT.getHostPageBaseURL() + "media/applause.mp3");
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    mp = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    mp = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer);
                }
                addCase("Playing MP3 media automatically", null, mp,
                        ResourceBundle.bundle.swfBasic());
                break;
            case swfLogger:
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
                        mp, ResourceBundle.bundle.swfLogger());
                break;
            case swfVideo:
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
                addCase("Embedding video", null, mp, ResourceBundle.bundle.swfVideo());

                try {
                    final Label lbl = new Label();
                    FlashMediaPlayer mmp = new FlashMediaPlayer(GWT.getHostPageBaseURL() + "media/traffic.flv",
                            false, "350px", "100%");
                    mmp.setResizeToVideoSize(true);
                    mmp.showLogger(true);
                    mmp.addKeyDownHandler(new KeyDownHandler() {

                        public void onKeyDown(KeyDownEvent event) {
                            lbl.setText("KeyDown = " + event.getNativeKeyCode());
                        }
                    });
                    mmp.addKeyUpHandler(new KeyUpHandler() {

                        public void onKeyUp(KeyUpEvent event) {
                            lbl.setText("KeyDown = " + event.getNativeKeyCode());
                        }
                    });
                    mmp.addKeyPressHandler(new KeyPressHandler() {

                        public void onKeyPress(KeyPressEvent event) {
                            lbl.setText("KeyPress = " + event.getCharCode());
                        }
                    });

                    VerticalPanel vp = new VerticalPanel();
                    vp.setWidth("100%");
                    vp.add(mmp);
                    vp.add(lbl);

                    mp2 = vp;// mmp;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    mp2 = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    mp2 = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer);
                }
                addCase("Player adjusted to video size", null, mp2, ResourceBundle.bundle.swfVideoAuto());
                break;
            case swfPlaylist:
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
                addCase("Working with M3U playlists", null, mp,
                        ResourceBundle.bundle.swfPlaylist());
                break;
        }
    }
}
