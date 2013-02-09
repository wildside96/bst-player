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

//import com.bramosystems.oss.player.capsule.client.Capsule;
import com.bramosystems.oss.player.core.client.*;
import com.bramosystems.oss.player.core.client.playlist.MRL;
import com.bramosystems.oss.player.core.client.ui.CoreConfigParameter;
import com.bramosystems.oss.player.core.client.ui.FlashMediaPlayer;
import com.bramosystems.oss.player.core.client.ui.QuickTimePlayer;
import com.bramosystems.oss.player.core.client.ui.VLCPlayer;
import com.bramosystems.oss.player.core.client.ui.WinMediaPlayer;
import com.bramosystems.oss.player.core.event.client.MediaInfoEvent;
import com.bramosystems.oss.player.core.event.client.MediaInfoHandler;
import com.bramosystems.oss.player.core.event.client.PlayStateEvent;
import com.bramosystems.oss.player.core.event.client.PlayStateHandler;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.*;
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
        mrls.add(new MRL(GWT.getModuleBaseURL() + "applause.mp3"));
        
//        mrls.add(new MRL("7047863"));
        
//        mrl.addURL(GWT.getModuleBaseURL() + "DSCF1780.AVI");
        mrls.add(new MRL(GWT.getModuleBaseURL() + "big-buck-bunny.mp4"));
        mrls.add(new MRL("file:///E:/LIB/MMX/Videos/New%20Movies/Contraband.2012.DVDRip.XViD-NYDIC.avi"));
//        mrls.add(new MRL(GWT.getModuleBaseURL() + "u2intro.mp4"));
//        mrls.add(new MRL(GWT.getModuleBaseURL() + "traffic.flv"));
//        mrls.add(new MRL(GWT.getModuleBaseURL() + "traffic.avi"));
//        mrl.addURL("applause.mp3");
    }

//TODO: test resizeToVideoSize feature for plugins ...
    @Override
    public void onModuleLoad() {
        RootPanel.get().add(this);
        addPlayer("core", "VLCPlayer");
//        addPlayer(Plugin.WinMediaPlayer);
        
/*
        try {
        RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, GWT.getModuleBaseURL() + "jspf.json");
        //            RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, GWT.getModuleBaseURL() + "xspf.xml");
        //           RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, GWT.getModuleBaseURL() + "asx.xml");
        rb.sendRequest(null, new RequestCallback() {
        
        @Override
        public void onResponseReceived(Request request, Response response) {
        showPlaylist(PlaylistFactory.parseJspfPlaylist(response.getText()));
        //                    showPlaylist(SPFParser.parseAsxPlaylist(response.getText()));
        //                    showPlaylist(SPFParser.parseXspfPlaylist(response.getText()));               
        }
        
        @Override
        public void onError(Request request, Throwable exception) {
        }
        });
        } catch (RequestException ex) {
        GWT.log("error ", ex);
        }
         */
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
            mmp.addPlayStateHandler(new PlayStateHandler() {

                @Override
                public void onPlayStateChanged(PlayStateEvent event) {
                    GWT.log("Index : " + event.getItemIndex() + " = " + event.getPlayState());
                }
            });
            mmp.setConfigParameter(CoreConfigParameter.QTScale, QuickTimePlayer.Scale.Aspect);
//            mmp.setConfigParameter(DefaultConfigParameter.BackgroundColor, "#ffdddf");
            mrls.remove(0);
            ((PlaylistSupport) mmp).addToPlaylist(mrls);
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
            CustomPlayerControl cpc = new CustomPlayerControl(mmp);
            add(cpc);
            
             */
 //           add(Logger.getLogger(mmp));
            mmp.addMediaInfoHandler(new MediaInfoHandler() {

                @Override
                public void onMediaInfoAvailable(MediaInfoEvent event) {
                    GWT.log(event.getMediaInfo().toString());
                }
            });
            add(new Button("Toggle Screen", new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    ((VLCPlayer)mmp).toggleFullScreen();
                }
            }));
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

    @UiTemplate("Player.ui.xml")
    interface PlayerBinder extends UiBinder<Widget, Dev> {
    }
    PlayerBinder pb = GWT.create(PlayerBinder.class);

    public void issue32() {
        // GWT.getModuleBaseURL() + "applause.mp3";
        final String fileUrl = GWT.getModuleBaseURL() + "big-buck-bunny.mp4";
        AbstractMediaPlayer player;
        Widget mp = null;
        try {
            player = new FlashMediaPlayer(fileUrl, true, "464px", "620px");
            player.setResizeToVideoSize(true);
            mp = player;
        } catch (PluginNotFoundException e) {
            mp = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer, e.getMessage());
        } catch (PluginVersionException e) {
            mp = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer, e.getRequiredVersion());
         }
        add(mp);
    }

    public void issueDialog() {
        final DialogBox panel = new DialogBox(false, true);
        panel.setSize("700px", "500px");
        AbstractMediaPlayer player;
        Widget mp = null;
        try {
            player = new WinMediaPlayer("", true, "464px", "620px");
            //           player.setResizeToVideoSize(false);
            mp = player;
        } catch (PluginNotFoundException e) {
            mp = PlayerUtil.getMissingPluginNotice(e.getPlugin(), e.getMessage());
        } catch (PluginVersionException e) {
            mp = PlayerUtil.getMissingPluginNotice(e.getPlugin(), e.getRequiredVersion());
        }

        FlowPanel fp = new FlowPanel();
        fp.add(new Button("close", new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                panel.hide();
            }
        }));
        fp.add(mp);
        panel.setWidget(fp);

        add(new Button("Show", new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                panel.center();
            }
        }));
    }
}
