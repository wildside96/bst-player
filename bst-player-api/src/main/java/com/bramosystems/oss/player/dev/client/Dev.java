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

import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.ConfigParameter;
import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.ui.*;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class Dev extends VerticalPanel implements EntryPoint {

    private AbstractMediaPlayer mmp;

    public Dev() {
        setSpacing(20);
        setWidth("80%");

        GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {

            @Override
            public void onUncaughtException(Throwable e) {
                Window.alert("Dev Uncaught : " + e.getMessage());
            }
        });
    }

    @Override
    public void onModuleLoad() {
//        RootPanel.get().add(new ScrollPanel(this));
        RootPanel.get().add(this);
//        addPlayer(Plugin.WinMediaPlayer);
//        addPlayer(Plugin.DivXPlayer);
//        addPlayer(Plugin.FlashPlayer);
//        addPlayer(Plugin.QuickTimePlayer);
//        addPlayer(Plugin.Native);
        addPlayer(Plugin.VLCPlayer);

//        add(new MimeStuffs());

    }

    private void addPlayer(Plugin plugin) {
        try {
            switch (plugin) {
                case DivXPlayer:
                    add(new Label("Testing DivX Web Player"));
//                    mmp = new FlatVideoPlayer(Plugin.DivXPlayer,
                    mmp = new DivXPlayer(
                            getURL("/local-video/divx7_postinstall.divx"),
                            false, "350px", "100%");
//                    divx.setResizeToVideoSize(true);
//                    divx.setBannerEnabled(false);
//                    divx.setDisplayMode(DivXPlayer.DisplayMode.LARGE);
//                    divx.setAllowContextMenu(false);
                    add(new Button("Show", new ClickHandler() {

                        @Override
                        public void onClick(ClickEvent event) {
                            mmp.setControllerVisible(!mmp.isControllerVisible());
                        }
                    }));
                    break;
                case FlashPlayer:
                    mmp = new FlashMediaPlayer(
                            getURL("/local-video/big-buck-bunny.mp4"),
                            //                            "http://localhost:8080/bst-media-server/stream?file=brandy-everything.mp3",
                            false, "350px", "100%");
                    break;
                case QuickTimePlayer:
//                    mmp = new QuickTimePlayer(getURL("/local-video/01_Al_Fatihah.m4a"), false);
                    mmp = new QuickTimePlayer(getURL("/local-video/Sample.mov"), false, "555px", "720px");
                    mmp.setConfigParameter(ConfigParameter.QTScale, QuickTimePlayer.Scale.ToFit);
                    break;
                case VLCPlayer:
                    mmp = new VLCPlayer(getURL("/local-video/big-buck-bunny.mp4"), true, "250px", "100%");
                    ((VLCPlayer) mmp).addToPlaylist(getURL("/local-video/fireflies.flv"));
                    ((VLCPlayer) mmp).addToPlaylist(getURL("/local-video/divx7_postinstall.divx"));
                    ((VLCPlayer) mmp).addToPlaylist(getURL("/local-video/traffic.flv"));
                    ((VLCPlayer) mmp).addToPlaylist(getURL("/local-video/Sample.mov"));
                    ((VLCPlayer) mmp).setShuffleEnabled(true);

//                    mmp = new VLCPlayer1(getURL("/local-video/big-buck-bunny.mp4"), true, "50px", "100%");
                    break;
                case WinMediaPlayer:
                    mmp = new WinMediaPlayer(
                            //                    mmp = new FlatVideoPlayer(Plugin.WinMediaPlayer,
                            getURL("/local-video/home-video.wmv"), true, "200px", "100%");
//                    mmp = new WinMediaPlayer(getURL("/local-video/applause.mp3"), true);
//                    mmp = new WinMediaPlayer(getURL("/local-video/playlist.m3u"), true);
//                    mmp = new WinMediaPlayer(getURL("/local-video/home-video.wmv"), true, "200px", "100%");
                    //                   mmp.setResizeToVideoSize(true);
                    break;
                case Native:
                    mmp = new NativePlayer(getURL("/local-video/big-buck-bunny.ogv"),
                            false, "450px", "100%");
            }
            mmp.showLogger(true);
            mmp.setLoopCount(-1);
//            mmp.setControllerVisible(false);
            add(mmp);
        } catch (LoadException ex) {
            add(new Label("Load Exception"));
        } catch (PluginNotFoundException ex) {
            add(PlayerUtil.getMissingPluginNotice(ex.getPlugin()));
        } catch (PluginVersionException ex) {
            add(PlayerUtil.getMissingPluginNotice(ex.getPlugin(), ex.getRequiredVersion()));
        }
    }

    private String getURL(String path) {
        return Location.createUrlBuilder().setPort(8080).setPath(path).buildString();

    }
}
