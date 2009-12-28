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
import com.bramosystems.oss.player.core.client.ui.NativePlayer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import java.util.ArrayList;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class NativeShowcase extends AbstractCase {

    public static String[] caseNames = {"Embed Native player",
        "With Logger widget visible", "Embedding Video"};
    public static String[] caseLinks = {"_native/basic", "_native/logger", "_native/video"};

    public NativeShowcase() {
    }

    public String getSummary() {
        return "Media playback with HTML 5 media handlers";
    }

    @Override
    public void init(String token) {
        clearCases();
        Widget mp = null, mp2 = null;
        int index = getTokenLinkIndex(caseLinks, token);
        switch (index) {
            case 0:
                try {
                    mp = new NativePlayer(GWT.getHostPageBaseURL() + "media/applause.mp3");
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginNotFoundException ex) {
                    mp = PlayerUtil.getMissingPluginNotice(Plugin.Native);
                }
                addCase("Playing MP3 media automatically", null, mp, "sources/swf/auto.html");

                try {
                    mp2 = new NativePlayer(GWT.getHostPageBaseURL() + "media/thunder.mp3", false);
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginNotFoundException ex) {
                    mp2 = PlayerUtil.getMissingPluginNotice(Plugin.Native);
                }
                addCase("Playing media with autoplay set to false", null,
                        mp2, "sources/swf/no-auto.html");
                break;
            case 1:
                try {
                    NativePlayer p3 = new NativePlayer(GWT.getHostPageBaseURL() + "media/o-na-som.mp3", false);
                    p3.showLogger(true);
                    mp = p3;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginNotFoundException ex) {
                    mp = PlayerUtil.getMissingPluginNotice(Plugin.Native);
                }
                addCase("Playing sound with logger widget visible", null,
                        mp, "sources/swf/show-logger.html");
                break;
            case 2:
                try {
                    final Label lbl = new Label();

                    ArrayList<String> items = new ArrayList<String>();
                    items.add(new String(GWT.getHostPageBaseURL() + "media/big-buck-bunny.mp4"));
                    items.add(new String(GWT.getHostPageBaseURL() + "media/big-buck-bunny.ogv"));

                    NativePlayer mmp = new NativePlayer(items, false, "350px", "100%");
                    mmp.setResizeToVideoSize(true);
                    mmp.showLogger(true);

                    mmp.addMouseMoveHandler(new MouseMoveHandler() {

                        public void onMouseMove(MouseMoveEvent event) {
                            lbl.setText("X:Y = " + event.getX() + ":" + event.getY());
                        }
                    });
                    VerticalPanel vp = new VerticalPanel();
                    vp.setWidth("100%");
                    vp.add(mmp);
                    vp.add(lbl);
                    mp = vp; //mmp;
                } catch (LoadException ex) {
                    Window.alert("Load exception");
                } catch (PluginNotFoundException ex) {
                    mp = PlayerUtil.getMissingPluginNotice(Plugin.Native);
                }
                addCase("Embedding video", null, mp, "sources/swf/video.html");

                try {
                    NativePlayer mmp = new NativePlayer(GWT.getHostPageBaseURL() + "media/traffic.mp4",
                            false, "350px", "100%");
                    mmp.setResizeToVideoSize(true);
                    mp2 = mmp;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginNotFoundException ex) {
                    mp2 = PlayerUtil.getMissingPluginNotice(Plugin.Native);
                }
                addCase("Player adjusted to video size", null, mp2, "sources/swf/video-auto.html");
                break;
        }
    }
}
