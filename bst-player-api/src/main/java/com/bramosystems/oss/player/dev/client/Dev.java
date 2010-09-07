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
import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.PlaylistSupport;
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.ui.*;
import com.bramosystems.oss.player.youtube.client.PlayerParameters;
import com.bramosystems.oss.player.youtube.client.YouTubePlayer;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import java.util.ArrayList;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class Dev extends VerticalPanel implements EntryPoint {

    public Dev() {
        setSpacing(20);
        setWidth("80%");

        GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {

            @Override
            public void onUncaughtException(Throwable e) {
                GWT.log(e.getMessage(), e);
                Window.alert("Dev Uncaught : " + e.getMessage());
            }
        });
    }

//TODO: test resizeToVideoSize feature for plugins ...
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
//        addUTube();
    }

    private void addPlayer(Plugin plugin) {
        AbstractMediaPlayer mmp = null;
        try {
            switch (plugin) {
                case DivXPlayer:
//                    mmp = new FlatVideoPlayer(Plugin.DivXPlayer,
                    mmp = new DivXPlayer(
                            getURL("/local-video/divx7_postinstall.divx"),
                            false, "350px", "100%");
//                    divx.setBannerEnabled(false);
//                    divx.setDisplayMode(DivXPlayer.DisplayMode.LARGE);
//                    divx.setAllowContextMenu(false);
//                    ((DivXPlayer) mmp).addToPlaylist(getURL("/local-video/divx7_postinstall.divx"));
                    ((DivXPlayer) mmp).addToPlaylist(getURL("/local-video/gi-joe-trailer.mkv"));
                    add(new Button("Show", new ClickHandler() {

                        @Override
                        public void onClick(ClickEvent event) {
                            //                           mmp.setControllerVisible(!mmp.isControllerVisible());
                        }
                    }));
                    break;
                case FlashPlayer:
                    mmp = new FlashMediaPlayer(getURL("/local-video/big-buck-bunny.mp4"), true, "350px", "100%");
                    ((PlaylistSupport)mmp).addToPlaylist(getURL("/local-video/brandy-everything.mp3"));
                    ((PlaylistSupport)mmp).addToPlaylist(getURL("/local-video/traffic.flv"));
                    break;
                case QuickTimePlayer:
                    mmp = new QuickTimePlayer(getURL("/local-video/Sample.mov"), false, "250px", "100%");
//                    ((PlaylistSupport) mmp).addToPlaylist(getURL("/local-video/01_Al_Fatihah.m4a"));
                    ((PlaylistSupport) mmp).addToPlaylist(getURL("/local-video/big-buck-bunny.mp4"));
//            mmp.setConfigParameter(ConfigParameter.QTScale, QuickTimePlayer.Scale.ToFit);
                    break;
                case VLCPlayer:
                    mmp = new VLCPlayer(getURL("/local-video/divx7_postinstall.divx"), true, "250px", "100%");
//                    mmp.setVolume(0.2);
                    ((PlaylistSupport) mmp).addToPlaylist(getURL("/local-video/fireflies.flv"));
//                    mmp = new VLCPlayer(getURL("/local-video/big-buck-bunny.mp4"), true, "250px", "100%");
//                    ((PlaylistSupport) mmp).addToPlaylist(getURL("/local-video/divx7_postinstall.divx"));
  //                  ((PlaylistSupport) mmp).addToPlaylist(getURL("/local-video/traffic.flv"));
                    ((PlaylistSupport) mmp).addToPlaylist(getURL("/local-video/Sample.mov"));
                    break;
                case WinMediaPlayer:
                    mmp = new WinMediaPlayer(getURL("/local-video/home-video.wmv"), false, "200px", "100%");
                    ((PlaylistSupport) mmp).addToPlaylist(getURL("/local-video/applause.mp3"));
//                    ((PlaylistSupport) mmp).addToPlaylist(getURL("/local-video/o-na-som.mp3"));
//                    mmp = new Capsule(Plugin.FlashPlayer, getURL("/local-video/applause.mp3"), false);
                    ((PlaylistSupport) mmp).addToPlaylist(getURL("/local-video/o-na-som.mp3"));
                    break;
                case Native:
                    ArrayList<String> urls = new ArrayList<String>();
                    urls.add(getURL("/local-video/big-buck-bunny.ogv"));
                    urls.add(getURL("/local-video/big-buck-bunny.mp4"));

                    mmp = new NativePlayer(urls, true, "450px", "100%");
//                    ((NativePlayer) mmp).addToPlaylist(getURL("/local-video/big-buck-bunny.mp4"));
            }
            mmp.showLogger(true);
            mmp.setResizeToVideoSize(true);
//            mmp.setLoopCount(2);
//            mmp.setRepeatMode(RepeatMode.REPEAT_ALL);
//            ((PlaylistSupport) mmp).setShuffleEnabled(true);
//            mmp.setControllerVisible(false);
            add(mmp);

            final Label lbl = new Label("MM - ");
            add(lbl);

            mmp.addMouseMoveHandler(new MouseMoveHandler() {

                @Override
                public void onMouseMove(MouseMoveEvent event) {
                    lbl.setText("MM - " + event.getX() + ", " + event.getY());
                }
            });
        } catch (LoadException ex) {
            add(new Label("Load Exception"));
        } catch (PluginNotFoundException ex) {
            add(PlayerUtil.getMissingPluginNotice(ex.getPlugin()));
        } catch (PluginVersionException ex) {
            add(PlayerUtil.getMissingPluginNotice(ex.getPlugin(), ex.getRequiredVersion()));
        }
    }

    private void addUTube() {
        try {
            PlayerParameters p = new PlayerParameters();
            p.setLoadRelatedVideos(false);
            p.setFullScreenEnabled(false);

            final YouTubePlayer u = new YouTubePlayer("http://www.youtube.com/v/QbwZL-EK6CY", "100%", "350px");
            u.showLogger(true);
            add(u);

            final Label label = new Label();
            add(label);

            Timer timer = new Timer() {

                @Override
                public void run() {
                    try {
                        double position = u.getPlayPosition();
                        double duration = u.getMediaDuration();
                        label.setText("" + position + "/" + duration);
                    } catch (IllegalStateException e) {
                        label.setText("?");
                    }
                }
            };
            timer.scheduleRepeating(5000);
        } catch (PluginNotFoundException ex) {
            add(PlayerUtil.getMissingPluginNotice(ex.getPlugin()));
        } catch (PluginVersionException ex) {
            add(PlayerUtil.getMissingPluginNotice(ex.getPlugin()));
        }
    }

    private String getURL(String path) {
        return Location.createUrlBuilder().setPort(8080).setPath(path).buildString();
    }
}
