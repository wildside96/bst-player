/*
 * Copyright 2009 Sikirulai Braheem
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a footer of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.bramosystems.oss.player.dev.client;

import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class Dev extends VerticalPanel implements EntryPoint {
    private String mediaURL = GWT.getModuleBaseURL() + "applause.mp3";

    public Dev() {
        setSpacing(20);
        setWidth("80%");
    }

    public void onModuleLoad() {
        RootLayoutPanel.get().add(new ScrollPanel(this));

        History.fireCurrentHistoryState();

        addPlayer(Plugin.DivXPlayer);
    }

    private void addPlayer(Plugin plugin) {
        switch (plugin) {
            case DivXPlayer:
                add(new Label("Testing DivX Web Player"));
                try {
                    DivXPlayer divx = new DivXPlayer(//mediaURL,
                            "http://localhost:8080/local-video/gi-joe-trailer.mkv",
                            false, "350px", "100%");
                    divx.showLogger(true);
//                    divx.setResizeToVideoSize(true);
                    divx.setBannerEnabled(false);
                    divx.setDisplayMode(DivXPlayer.DisplayMode.LARGE);
                    divx.setAllowContextMenu(false);
                    add(divx);
                } catch (LoadException ex) {
                    add(new Label("Load Exception"));
                } catch (PluginNotFoundException ex) {
                    add(PlayerUtil.getMissingPluginNotice(ex.getPlugin()));
                } catch (PluginVersionException ex) {
                    add(PlayerUtil.getMissingPluginNotice(ex.getPlugin(), ex.getRequiredVersion()));
                }
                break;
        }
    }
}
