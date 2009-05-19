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

package com.bramosystems.gwt.player.showcase.client;

import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.ui.FlashMP3Player;
import com.bramosystems.oss.player.core.client.ui.FlashVideoPlayer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class SWFShowcase extends AbstractCase {

    public SWFShowcase() {
        Widget mp = null;
        try {
            mp = new FlashMP3Player(GWT.getHostPageBaseURL() + "media/applause.mp3");
        } catch (LoadException ex) {
            Window.alert("Load exp");
        } catch (PluginVersionException ex) {
            mp = PlayerUtil.getMissingPluginNotice(Plugin.FlashMP3Player, ex.getRequiredVersion());
        } catch (PluginNotFoundException ex) {
            mp = PlayerUtil.getMissingPluginNotice(Plugin.FlashMP3Player);
        }
        addCase(mp, "sources/swf/auto.html");

        Widget mp2 = null;
        try {
            mp2 = new FlashMP3Player(GWT.getHostPageBaseURL() + "media/thunder.mp3", false);
        } catch (LoadException ex) {
            Window.alert("Load exp");
        } catch (PluginVersionException ex) {
            mp2 = PlayerUtil.getMissingPluginNotice(Plugin.FlashMP3Player, ex.getRequiredVersion());
        } catch (PluginNotFoundException ex) {
            mp2 = PlayerUtil.getMissingPluginNotice(Plugin.FlashMP3Player);
        }
        addCase(mp2, "sources/swf/no-auto.html");

        Widget mp3 = null;
        try {
            FlashMP3Player p3 = new FlashMP3Player(GWT.getHostPageBaseURL() + "media/o-na-som.mp3", false);
            p3.showLogger(true);
            mp3 = p3;
        } catch (LoadException ex) {
            Window.alert("Load exp");
        } catch (PluginVersionException ex) {
            mp3 = PlayerUtil.getMissingPluginNotice(Plugin.FlashMP3Player, ex.getRequiredVersion());
        } catch (PluginNotFoundException ex) {
            mp3 = PlayerUtil.getMissingPluginNotice(Plugin.FlashMP3Player);
        }
        addCase(mp3, "sources/swf/show-logger.html");

        Widget mp4 = null;
        try {
            FlashVideoPlayer v1 = new FlashVideoPlayer("http://vixy.net/flv/" +
                    "vixy_net_podcaster_demo_director.flv",
                    false, "350px", "100%");
            v1.showLogger(true);
            mp4 = v1;
        } catch (LoadException ex) {
            Window.alert("Load exp");
        } catch (PluginVersionException ex) {
            mp4 = PlayerUtil.getMissingPluginNotice(Plugin.FlashVideoPlayer, ex.getRequiredVersion());
        } catch (PluginNotFoundException ex) {
            mp4 = PlayerUtil.getMissingPluginNotice(Plugin.FlashVideoPlayer);
        }
        addCase(mp4, "sources/swf/video.html");
    }

    public String getSummary() {
        return "Media playback with Adobe Flash plugin";
    }
}
