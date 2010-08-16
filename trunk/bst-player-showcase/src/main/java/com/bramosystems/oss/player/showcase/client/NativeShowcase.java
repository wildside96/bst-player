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
import com.bramosystems.oss.player.common.client.Links;
import com.bramosystems.oss.player.resources.sources.ResourceBundle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import java.util.ArrayList;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class NativeShowcase extends AbstractCase {

    public static AbstractCase instance = new NativeShowcase();

    private NativeShowcase() {
    }

    public String getSummary() {
        return "Media playback with HTML 5 media handlers";
    }

    @Override
    public void initCase(Links link) {
        Widget mp = null, mp2 = null;
        switch (link) {
            case ntiveBasic:
                try {
                    mp = new NativePlayer(GWT.getHostPageBaseURL() + "media/applause.mp3");
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginNotFoundException ex) {
                    mp = PlayerUtil.getMissingPluginNotice(Plugin.Native);
                }
                addCase("Playing MP3 media automatically", null, mp,
                        ResourceBundle.bundle.nativeBasic());
//            case ntiveLogger:
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
                        mp, ResourceBundle.bundle.nativeLogger());
                break;
            case ntiveVideo:
                ArrayList<String> items = new ArrayList<String>();
                items.add(GWT.getHostPageBaseURL() + "media/big-buck-bunny.mp4");
                items.add(GWT.getHostPageBaseURL() + "media/big-buck-bunny.ogv");

                try {
                    NativePlayer mmp = new NativePlayer(items, false, getCaseHeight(), "100%");
                    mmp.showLogger(true);
                    mmp.setLoopCount(2);
                    mp = mmp;
                } catch (LoadException ex) {
                    Window.alert("Load exception");
                } catch (PluginNotFoundException ex) {
                    mp = PlayerUtil.getMissingPluginNotice(Plugin.Native);
                }
                addCase("Embedding video", null, mp, ResourceBundle.bundle.nativeVideo());

                try {
                    NativePlayer mmp = new NativePlayer(items, false, getCaseHeight(), "100%");
                    mmp.setResizeToVideoSize(true);
                    mmp.setLoopCount(-1);
                    mp2 = mmp;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginNotFoundException ex) {
                    mp2 = PlayerUtil.getMissingPluginNotice(Plugin.Native);
                }
                addCase("Player adjusted to video size", null, mp2, ResourceBundle.bundle.nativeVideoAuto());
                break;
        }
    }
}
