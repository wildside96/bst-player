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
package com.bramosystems.oss.player.provider.vimeo.client.dev;

import com.bramosystems.oss.player.core.client.*;
import com.bramosystems.oss.player.core.client.playlist.MRL;
import com.bramosystems.oss.player.core.client.skin.CustomPlayerControl;
import com.bramosystems.oss.player.core.client.ui.Logger;
import com.bramosystems.oss.player.core.event.client.DebugEvent;
import com.bramosystems.oss.player.core.event.client.DebugHandler;
import com.bramosystems.oss.player.core.event.client.PlayStateEvent;
import com.bramosystems.oss.player.core.event.client.PlayStateHandler;
import com.bramosystems.oss.player.provider.vimeo.client.VimeoConfigParameters;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import java.util.ArrayList;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 * 
 * TODO: 2.x - Remove mediaURL from constructor methods, use with playlist
 * TODO: 2.x - Remove LoadException from constructor methods
 * TODO: Fix WMP display on Firefox
 */
public class Dev extends FlowPanel implements EntryPoint {

    private final String HEIGHT = "350px", WIDTH = "80%";
    private AbstractMediaPlayer mmp;
    private ArrayList<MRL> mrls;

    public Dev() {
//        setSpacing(20);
        setWidth("80%");

        GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {

            @Override
            public void onUncaughtException(Throwable e) {
                GWT.log(e.getMessage(), e);
//                Window.alert("Dev Uncaught : " + e.getMessage());
            }
        });

        mrls = new ArrayList<MRL>();
//        mrls.add(new MRL(GWT.getModuleBaseURL() + "applause.mp3"));
        
//        mrls.add(new MRL("7047863"));
        
//        mrl.addURL(GWT.getModuleBaseURL() + "DSCF1780.AVI");
        mrls.add(new MRL(GWT.getModuleBaseURL() + "big-buck-bunny.mp4"));
//        mrls.add(new MRL(GWT.getModuleBaseURL() + "u2intro.mp4"));
//        mrls.add(new MRL(GWT.getModuleBaseURL() + "traffic.flv"));
//        mrls.add(new MRL(GWT.getModuleBaseURL() + "traffic.avi"));
//        mrl.addURL("applause.mp3");
    }

//TODO: test resizeToVideoSize feature for plugins ...
    @Override
    public void onModuleLoad() {
        //        RootPanel.get().add(new ScrollPanel(this));
        RootPanel.get().add(this);
//        addPlayer("bst.vimeo", "UniversalPlayer");
        addPlayer("core", "Native");
//        addPlayer(Plugin.WinMediaPlayer);

//                    add(new MimeStuffs());
//                addUTube();
        //               issueDialog();
//         add(pb.createAndBindUi(this));
    }


    private void addPlayer(String prov, String player) {
        /*
        add(new Button("Show", new ClickHandler() {
        
        @Override
        public void onClick(ClickEvent event) {
        mmp.setControllerVisible(!mmp.isControllerVisible());
        }
        }));
         */
        try {
            PlayerInfo pi = PlayerUtil.getPlayerInfo(prov, player);
            mmp = PlayerUtil.getPlayer(pi, mrls.get(0).getNextResource(true), false, HEIGHT, WIDTH);
 //           mmp = new WinMediaPlayer(mrl.getNextResource(true), false, HEIGHT, WIDTH, WinMediaPlayer.EmbedMode.EMBED_ONLY);
 //                   mmp = new Capsule(Plugin.FlashPlayer, mrl.getNextResource(true), false);
            mmp.addPlayStateHandler(new PlayStateHandler() {

                @Override
                public void onPlayStateChanged(PlayStateEvent event) {
                    GWT.log("Index : " + event.getItemIndex() + " = " + event.getPlayState());
                }
            });
            mmp.showLogger(true);
//            mmp.setConfigParameter(ConfigParameter.QTScale, QuickTimePlayer.Scale.Aspect);
//            mmp.setConfigParameter(DefaultConfigParameter.BackgroundColor, "#ffdddf");
            mmp.setConfigParameter(VimeoConfigParameters.ShowTitle, true);
//            ((PlaylistSupport) mmp).addToPlaylist(mrls);
//            mmp.setResizeToVideoSize(true);
//            mmp.setLoopCount(2);
//            mmp.setRepeatMode(RepeatMode.REPEAT_ALL);
//            ((PlaylistSupport) mmp).setShuffleEnabled(false);
//            mmp.setControllerVisible(true);
            add(mmp);
            /*
            final Label lbl = new Label("MM - ");
            add(lbl);
            mmp.addMouseMoveHandler(new MouseMoveHandler() {
            
            @Override
            public void onMouseMove(MouseMoveEvent event) {
            lbl.setText("MM - " + event.getX() + ", " + event.getY());
            }
            });
             */
            CustomPlayerControl cpc = new CustomPlayerControl(mmp);
            add(cpc);
            
            final Logger l = new Logger();
            add(l);
            mmp.addDebugHandler(new DebugHandler() {

                @Override
                public void onDebug(DebugEvent event) {
                    l.log(event.getMessage(), true);
                }
            });
        } catch (LoadException ex) {
            add(new Label("Load Exception"));
        } catch (PluginNotFoundException ex) {
            add(PlayerUtil.getMissingPluginNotice(ex.getPlugin()));
            GWT.log("Missing plugin >>>>", ex);
        } catch (PluginVersionException ex) {
            add(PlayerUtil.getMissingPluginNotice(ex.getPlugin(), ex.getRequiredVersion()));
            GWT.log("Missing Plugin Version >>>>>", ex);
        }
    }

  }
