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

import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.skin.CustomPlayerControl;
import com.bramosystems.oss.player.common.client.Links;
import com.bramosystems.oss.player.core.client.PluginVersion;
import com.bramosystems.oss.player.core.client.ui.SWFWidget;
import com.bramosystems.oss.player.resources.sources.ResourceBundle;
import com.bramosystems.oss.player.youtube.client.ChromelessPlayer;
import com.bramosystems.oss.player.youtube.client.PlayerParameters;
import com.bramosystems.oss.player.youtube.client.YouTubePlayer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class MiscShowcase extends AbstractCase {
    public static AbstractCase instance = new MiscShowcase();

    private MiscShowcase() {
    }

    public String getSummary() {
        return "Miscellaneous media playback examples";
    }

    @Override
    public void initCase(Links link) {
        Widget w = null;
        switch (link) {
            case miscBasic:
                try {
                    AbstractMediaPlayer mp = PlayerUtil.getPlayer(GWT.getHostPageBaseURL() +
                            "media/applause.mp3", false, "50px", "100%");
                    mp.showLogger(true);
                    mp.setLoopCount(2);
                    w = mp;
                } catch (LoadException ex) {
                    Window.alert("Load Exp");
                } catch (PluginVersionException ex) {
                    w = PlayerUtil.getMissingPluginNotice(Plugin.Auto);
                } catch (PluginNotFoundException ex) {
                    w = PlayerUtil.getMissingPluginNotice(Plugin.Auto);
                }
                addCase("Playing with any supported plugin", null, w,
                        ResourceBundle.bundle.miscBasic());
                break;
            case miscFlash:
                try {
                    SWFWidget swf = new SWFWidget("http://www.youtube.com/v/IqnWs_j5MbM",
                            "100%", getCaseHeight(), PluginVersion.get(9, 0, 0));
                    swf.addProperty("bgcolor", "#000000");
                    w = swf;
                } catch (PluginVersionException ex) {
                    w = PlayerUtil.getMissingPluginNotice(Plugin.Auto);
                } catch (PluginNotFoundException ex) {
                    w = PlayerUtil.getMissingPluginNotice(Plugin.Auto);
                }
                addCase("Embed a generic Flash application", "YouTube Video (Asa - 'No one knows')", w,
                        ResourceBundle.bundle.miscFlash());
                break;
            case ytubeBasic:
                try {
                    PlayerParameters p = new PlayerParameters();
                    p.setLoadRelatedVideos(false);
                    p.setFullScreenEnabled(false);

                    YouTubePlayer u = new YouTubePlayer("http://www.youtube.com/v/QbwZL-EK6CY",
                            p, "100%", getCaseHeight());
                    u.showLogger(true);
                    w = u;
                } catch (PluginVersionException ex) {
                    w = PlayerUtil.getMissingPluginNotice(
                            ex.getPlugin(),ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    w = PlayerUtil.getMissingPluginNotice(ex.getPlugin());
                }
                addCase("Embedding YouTube video with YouTubePlayer widget", 
                        "Widget exposes player controls and events",
                        w, ResourceBundle.bundle.ytubeBasic());
                break;
            case ytubeChrome:
                try {
                    ChromelessPlayer cp = new ChromelessPlayer("http://www.youtube.com/v/QbwZL-EK6CY&hl=en&fs=1&",
                            "100%", getCaseHeight());
                    CustomPlayerControl cpc = new CustomPlayerControl(cp);

                    FlowPanel fp = new FlowPanel();
                    fp.add(cp);
                    fp.add(cpc);
                    w = fp;
                } catch (PluginVersionException ex) {
                    w = PlayerUtil.getMissingPluginNotice(
                            ex.getPlugin(),ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    w = PlayerUtil.getMissingPluginNotice(ex.getPlugin());
                }
                addCase("Custom YouTube video player with ChromelessPlayer widget",
                        null, w, ResourceBundle.bundle.ytubeChrome());
                break;
        }
    }
}
