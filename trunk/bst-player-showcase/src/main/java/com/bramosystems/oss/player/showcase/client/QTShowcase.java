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
import com.bramosystems.oss.player.core.client.ui.QuickTimePlayer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class QTShowcase extends AbstractCase {

    public static String[] caseNames = {"Embed QuickTime Plugin",
        "With Logger widget visible", "Embedding QuickTime Video"};
    public static String[] caseLinks = {"qt/basic", "qt/logger", "qt/video"};

    public QTShowcase() {
    }

    public String getSummary() {
        return "Using QuickTime Player plugin";
    }

    @Override
    public void init(String token) {
        clearCases();
        Widget qt = null, qt2 = null;

        int index = getTokenLinkIndex(caseLinks, token);
        switch (index) {
            case 0:
                try {
                    qt = new QuickTimePlayer(GWT.getHostPageBaseURL() + "media/thunder.mp3");
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    qt = PlayerUtil.getMissingPluginNotice(Plugin.QuickTimePlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    qt = PlayerUtil.getMissingPluginNotice(Plugin.QuickTimePlayer);
                }
                addCase("Playing sound automatically", null, qt, "sources/qt/auto.html");

                try {
                    qt2 = new QuickTimePlayer(GWT.getHostPageBaseURL() + "media/thunder.mp3", false);
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    qt2 = PlayerUtil.getMissingPluginNotice(Plugin.QuickTimePlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    qt2 = PlayerUtil.getMissingPluginNotice(Plugin.QuickTimePlayer);
                }
                addCase("Playing sound with autoplay set to false", null,
                        qt2, "sources/qt/no-auto.html");
                break;
            case 1:
                try {
                    QuickTimePlayer p = new QuickTimePlayer(GWT.getHostPageBaseURL() + "media/o-na-som.mp3",
                            false);
                    p.showLogger(true);
                    qt = p;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    qt = PlayerUtil.getMissingPluginNotice(Plugin.QuickTimePlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    qt = PlayerUtil.getMissingPluginNotice(Plugin.QuickTimePlayer);
                }
                addCase("Playing sound with logger widget visible", null,
                        qt, "sources/qt/show-logger.html");
                break;
            case 2:
                try {
                    final Label lbl = new Label();
                    final QuickTimePlayer p = new QuickTimePlayer(GWT.getHostPageBaseURL() + "media/traffic.mp4",
                            false, "300px", "100%");
                    p.showLogger(true);
                    p.addMouseMoveHandler(new MouseMoveHandler() {

                        public void onMouseMove(MouseMoveEvent event) {
                            lbl.setText("X:Y = " + event.getX() + ":" + event.getY());
                        }
                    });


                    VerticalPanel vp = new VerticalPanel();
                    vp.setWidth("100%");
                    vp.add(p);
                    vp.add(lbl);
                    qt = vp;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    qt = PlayerUtil.getMissingPluginNotice(Plugin.QuickTimePlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    qt = PlayerUtil.getMissingPluginNotice(Plugin.QuickTimePlayer);
                }
                addCase("Embedding video", null, qt, "sources/qt/video.html");

                try {
                    QuickTimePlayer p = new QuickTimePlayer(GWT.getHostPageBaseURL() + "media/traffic.mp4",
                            false, "300px", "100%");
                    p.setResizeToVideoSize(true);
                    qt2 = p;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    qt2 = PlayerUtil.getMissingPluginNotice(Plugin.QuickTimePlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    qt2 = PlayerUtil.getMissingPluginNotice(Plugin.QuickTimePlayer);
                }
                addCase("Auto-resize player to video dimensions", null, qt2, "sources/qt/video-auto.html");
                break;
        }

    }
}
