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
import com.bramosystems.oss.player.core.client.ui.*;
import com.bramosystems.oss.player.flat.client.FlatVideoPlayer;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class Dev extends VerticalPanel implements EntryPoint {

    public Dev() {
        setSpacing(20);
        setWidth("80%");
    }

    @Override
    public void onModuleLoad() {
//        RootPanel.get().add(new ScrollPanel(this));
        RootPanel.get().add(this);
        addPlayer(Plugin.DivXPlayer);
//        addPlayer(Plugin.FlashPlayer);
//        addPlayer(Plugin.QuickTimePlayer);
//        addPlayer(Plugin.Native);
//        addPlayer(Plugin.VLCPlayer);

//        add(new MimeStuffs());
        
    }

    private void addPlayer(Plugin plugin) {
        switch (plugin) {
            case DivXPlayer:
                add(new Label("Testing DivX Web Player"));
                try {
                    final FlatVideoPlayer divx = new FlatVideoPlayer(Plugin.DivXPlayer,
//                    DivXPlayer divx = new DivXPlayer(//mediaURL,
                            "http://localhost:8080/local-video/gi-joe-trailer.mkv",
                            false, "350px", "100%");
                    divx.showLogger(true);
                    divx.setLoopCount(3);
//                    divx.setResizeToVideoSize(true);
//                    divx.setBannerEnabled(false);
//                    divx.setDisplayMode(DivXPlayer.DisplayMode.LARGE);
//                    divx.setAllowContextMenu(false);
                    add(divx);
                    add(new Button("Show", new ClickHandler() {

                public void onClick(ClickEvent event) {
                    divx.setControllerVisible(!divx.isControllerVisible());
                }
            }));
                } catch (LoadException ex) {
                    add(new Label("Load Exception"));
                } catch (PluginNotFoundException ex) {
                    add(PlayerUtil.getMissingPluginNotice(ex.getPlugin()));
                } catch (PluginVersionException ex) {
                    add(PlayerUtil.getMissingPluginNotice(ex.getPlugin(), ex.getRequiredVersion()));
                }
                break;
            case FlashPlayer:
                try {
                    FlashMediaPlayer mmp = new FlashMediaPlayer(
                            "http://localhost:8080/bst-media-server/stream?" +
                            "position=0&file=brandy-everything.mp3",
                            false);
                    mmp.showLogger(true);
//                    mmp.setControllerVisible(false);
                    add(mmp);
                } catch (LoadException ex) {
                    add(new Label("Load Exception"));
                } catch (PluginNotFoundException ex) {
                    add(PlayerUtil.getMissingPluginNotice(ex.getPlugin()));
                } catch (PluginVersionException ex) {
                    add(PlayerUtil.getMissingPluginNotice(ex.getPlugin(), ex.getRequiredVersion()));
                }
                break;
            case QuickTimePlayer:
                try {
                    QuickTimePlayer mmp = new QuickTimePlayer(
                            "http://localhost:8080/local-video/01_Al_Fatihah.m4a", false);
                    mmp.showLogger(true);
                    mmp.setLoopCount(2);
//                    mmp.setControllerVisible(false);
                    add(mmp);
                } catch (LoadException ex) {
                    add(new Label("Load Exception"));
                } catch (PluginNotFoundException ex) {
                    add(PlayerUtil.getMissingPluginNotice(ex.getPlugin()));
                } catch (PluginVersionException ex) {
                    add(PlayerUtil.getMissingPluginNotice(ex.getPlugin(), ex.getRequiredVersion()));
                }
                break;
            case VLCPlayer:
                try {
                    VLCPlayer mmp = new VLCPlayer(
                            "http://localhost:8080/local-video/01_Al_Fatihah.m4a", false);
                    mmp.showLogger(true);
                    mmp.setLoopCount(-2);
//                    mmp.setControllerVisible(false);
                    add(mmp);
                } catch (LoadException ex) {
                    add(new Label("Load Exception"));
                } catch (PluginNotFoundException ex) {
                    add(PlayerUtil.getMissingPluginNotice(ex.getPlugin()));
                } catch (PluginVersionException ex) {
                    add(PlayerUtil.getMissingPluginNotice(ex.getPlugin(), ex.getRequiredVersion()));
                }
                break;
            case Native:
                try {
                    NativePlayer mmp = new NativePlayer(
                            "http://localhost:8080/local-video/big-buck-bunny.ogv", false, "450px", "100%");
                    mmp.showLogger(true);
//                    mmp.setControllerVisible(false);
                    add(mmp);
                } catch (LoadException ex) {
                    add(new Label("Load Exception"));
                } catch (PluginNotFoundException ex) {
                    add(PlayerUtil.getMissingPluginNotice(ex.getPlugin()));
                }

        }
    }
}
