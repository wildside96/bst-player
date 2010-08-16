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
import com.bramosystems.oss.player.common.client.Links;
import com.bramosystems.oss.player.core.client.ui.VLCPlayer;
import com.bramosystems.oss.player.resources.sources.ResourceBundle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class VLCShowcase extends AbstractCase {

    public static AbstractCase instance = new VLCShowcase();

    public VLCShowcase() {
    }

    public String getSummary() {
        return "Media playback with VLC Media Player plugin";
    }

    @Override
    public void initCase(Links link) {
        Widget v = null, v2 = null;
        switch (link) {
            case vlcBasic:
                try {
                    v = new VLCPlayer(GWT.getHostPageBaseURL() + "media/thunder.mp3");
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.VLCPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.VLCPlayer);
                }
                addCase("Playing sound automatically", null, v,
                        ResourceBundle.bundle.vlcBasic());
//              break;
//            case vlcLogger:
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
                addCase("Debugging with the Logger widget", null, v,
                        ResourceBundle.bundle.vlcLogger());
                break;
            case vlcVideo:
                try {
                    VLCPlayer v1 = new VLCPlayer(GWT.getHostPageBaseURL() +
                            "media/traffic.flv", false, getCaseHeight(), "100%");
                    v1.showLogger(true);
                    v = v1;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.VLCPlayer, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.VLCPlayer);
                }
                addCase("Embedding Video", null, v, ResourceBundle.bundle.vlcVideo());

                try {
                    VLCPlayer v1V = new VLCPlayer(GWT.getHostPageBaseURL() +
                            "media/traffic.flv", false, getCaseHeight(), "100%");
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
                addCase("Auto-resize embedded video", null, v2, ResourceBundle.bundle.vlcVideoAuto());
                break;
        }
    }
}
