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
import com.bramosystems.oss.player.core.client.ui.VLCPlayer;
import com.bramosystems.oss.player.core.event.client.PlayStateEvent;
import com.bramosystems.oss.player.core.event.client.PlayStateHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class VLCShowcase extends AbstractCase {

    public static String[] caseNames = {"Embedding VLC Player",
        "Embedding with Logger widget visible", "Embedding Video"};
    public static String[] caseLinks = {"vlc/basic", "vlc/logger", "vlc/video"};

    public VLCShowcase() {
    }

    public String getSummary() {
        return "Media playback with VLC Media Player plugin";
    }

    @Override
    public void init(String token) {
        clearCases();
        Widget v = null, v2 = null;
        int index = getTokenLinkIndex(caseLinks, token);
        switch (index) {
            case 0:
                try {
                    v = new VLCPlayer(GWT.getHostPageBaseURL() + "media/thunder.mp3");
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.VLCPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.VLCPlayer);
                }
                addCase("Playing sound automatically", null, v, "sources/vlc/auto.html");

                try {
                    VLCPlayer vv2 = new VLCPlayer(GWT.getHostPageBaseURL() + "media/applause.mp3", false);
                    vv2.showLogger(true);
                    v2 = vv2;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    v2 = PlayerUtil.getMissingPluginNotice(Plugin.VLCPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    v2 = PlayerUtil.getMissingPluginNotice(Plugin.VLCPlayer);
                }
                addCase("Playing sound with autoplay set to false", null,
                        v2, "sources/vlc/no-auto.html");
                break;
            case 1:
                try {
                    VLCPlayer vlc = new VLCPlayer(GWT.getHostPageBaseURL() + "media/o-na-som.mp3", false);
                    vlc.showLogger(true);
                    v = vlc;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.VLCPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.VLCPlayer);
                }
                addCase("Debugging with the Logger widget", null, v, "sources/vlc/show-logger.html");
                break;
            case 2:
                try {
                    final Label lbl = new Label();
                    final PopupPanel pp = new DialogBox(false, false);
                    pp.setWidth("300px");
                    pp.setHeight("400px");
                    pp.setWidget(new Image(GWT.getHostPageBaseURL() + "images/loading.gif"));
                    DOM.setStyleAttribute(pp.getElement(), "backgroundColor", "blue");

                    VLCPlayer v1 = new VLCPlayer(GWT.getHostPageBaseURL() +
                            "media/traffic.flv", false, "350px", "100%");
                    v1.showLogger(true);
                    v1.doMouseEvents(new MouseMoveHandler() {

                        public void onMouseMove(MouseMoveEvent event) {
                            lbl.setText("X:Y = " + event.getX() + ":" + event.getY());
                        }
                    });
//                    mmp.setConfigParameter(ConfigParameter.WMPUIMode, WinMediaPlayer.UIMode.MINI);
                    v1.addPlayStateHandler(new PlayStateHandler() {

                        public void onPlayStateChanged(PlayStateEvent event) {
                            pp.center();
                        }
                    });
                    VerticalPanel vp = new VerticalPanel();
                    vp.setWidth("100%");
                    vp.add(v1);
                    vp.add(lbl);
                    v = vp;
//                    v = v1;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.VLCPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.VLCPlayer);
                }
                addCase("Embedding Video", null, v, "sources/vlc/video.html");

                try {
                    VLCPlayer v1V = new VLCPlayer(GWT.getHostPageBaseURL() +
                            "media/traffic.flv", false, "350px", "100%");
                    v1V.setResizeToVideoSize(true);
                    v1V.showLogger(true);
                    v2 = v1V;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    v2 = PlayerUtil.getMissingPluginNotice(Plugin.VLCPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    v2 = PlayerUtil.getMissingPluginNotice(Plugin.VLCPlayer);
                }
                addCase("Auto-resize embedded video", null, v2, "sources/vlc/video-auto.html");
                break;
        }
    }
}
