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
import com.bramosystems.oss.player.core.client.playlist.MRL;
import com.bramosystems.oss.player.capsule.client.Capsule;
import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.ConfigParameter;
import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.PlaylistSupport;
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.ui.DivXPlayer;
import com.bramosystems.oss.player.core.client.ui.FlashMediaPlayer;
import com.bramosystems.oss.player.core.client.ui.NativePlayer;
import com.bramosystems.oss.player.core.client.ui.QuickTimePlayer;
import com.bramosystems.oss.player.core.client.ui.VLCPlayer;
import com.bramosystems.oss.player.core.client.ui.WinMediaPlayer;
import com.bramosystems.oss.player.core.event.client.PlayStateEvent;
import com.bramosystems.oss.player.core.event.client.PlayStateEvent.State;
import com.bramosystems.oss.player.core.event.client.PlayStateHandler;
//import com.bramosystems.oss.player.flat.client.FlatVideoPlayer;
import com.bramosystems.oss.player.playlist.client.asx.ASXEntry;
import com.bramosystems.oss.player.playlist.client.asx.ASXPlaylist;
import com.bramosystems.oss.player.playlist.client.spf.SPFPlaylist;
import com.bramosystems.oss.player.playlist.client.spf.Track;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.Iterator;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class Dev extends FlowPanel implements EntryPoint {

    private final String HEIGHT = "350px", WIDTH = "90%";
    private AbstractMediaPlayer mmp;
    private MRL mrl;

    public Dev() {
//        setSpacing(20);
        setWidth("80%");

        GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {

            @Override
            public void onUncaughtException(Throwable e) {
                GWT.log(e.getMessage(), e);
                Window.alert("Dev Uncaught : " + e.getMessage());
            }
        });

        mrl = new MRL();
        //       mrl.addURL(GWT.getModuleBaseURL() + "applause.mp3");
//        mrl.addURL(GWT.getModuleBaseURL() + "DSCF1780.AVI");
//        mrl.addURL(GWT.getModuleBaseURL() + "traffic.avi");
        mrl.addURL(GWT.getModuleBaseURL() + "big-buck-bunny.mp4");
        mrl.addURL(GWT.getModuleBaseURL() + "sample_mpeg4.mp4");
//        mrl.addURL("applause.mp3");
    }

//TODO: test resizeToVideoSize feature for plugins ...
    @Override
    public void onModuleLoad() {
        //        RootPanel.get().add(new ScrollPanel(this));
        RootPanel.get().add(this);
//                addPlayer(Plugin.QuickTimePlayer);
        
//                addPlayer(Plugin.QuickTimePlayer);

//                    add(new MimeStuffs());
        //        addUTube();
        //               issueDialog();
         add(pb.createAndBindUi(this)); //TODO: requires GWT 2.1
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

    private void showPlaylist(SPFPlaylist pl) {
        add(new Label("<<<<<<<<<<<<<<<<<<<<<<<<<< Playlist >>>>>>>>>>>>>>>>>>>>>>>>>>>>>"));
        add(new Label(pl.getTitle()));
        add(new Label(pl.getInfo()));
        add(new Label(pl.getCreator()));

        JsArray<Track> ts = pl.getTracks();
        for (int i = 0; i < ts.length(); i++) {
            Track t = ts.get(i);
            add(new Label("<<<<<<<<<<<<<<<<<<< Track " + i + " >>>>>>>>>>>>>>>>>"));
            add(new Label(t.getTitle()));
            add(new Label(t.getCreator()));
            add(new Label(t.getAlbum()));
        }
    }

    private void showPlaylist(ASXPlaylist pl) {
        GWT.log("<<< Entries : " + pl.getEntries().size() + " >>>");
        
        add(new Label("<<<<<<<<<<<<<<<<<<<<<<<<<< Playlist >>>>>>>>>>>>>>>>>>>>>>>>>>>>>"));
        add(new Label(pl.getTitle()));
        add(new Label(pl.getAbstract()));
        add(new Label(pl.getAuthor()));

        Iterator<ASXEntry> ts = pl.getEntries().iterator();
        int i = 0;
        while (ts.hasNext()) {
            ASXEntry t = ts.next();
            add(new Label("<<<<<<<<<<<<<<<<<<< Track " + i++ + " >>>>>>>>>>>>>>>>>"));
            add(new Label(t.getTitle()));
            add(new Label(t.getAuthor()));
            add(new Label(t.getRefs().toString()));
        }
    }

    private void addPlayer(Plugin plugin) {
        /*
        add(new Button("Show", new ClickHandler() {
        
        @Override
        public void onClick(ClickEvent event) {
        mmp.setControllerVisible(!mmp.isControllerVisible());
        }
        }));
         */
        try {
            switch (plugin) {
                case DivXPlayer:
                    mmp = new DivXPlayer(mrl.getNextResource(true), false, "350px", "100%");
//                    divx.setDisplayMode(DivXPlayer.DisplayMode.LARGE);
//                    divx.setAllowContextMenu(false);
                    break;
                case FlashPlayer:
                    mmp = new FlashMediaPlayer(mrl.getNextResource(true), true, HEIGHT, WIDTH);
                    break;
                case QuickTimePlayer:
                    mmp = new QuickTimePlayer(mrl.getNextResource(true), false, HEIGHT, WIDTH);
                    break;
                case VLCPlayer:
                    mmp = new VLCPlayer(mrl.getNextResource(true), false, HEIGHT, WIDTH);
                    break;
                case WinMediaPlayer:
                    mmp = new WinMediaPlayer(mrl.getNextResource(true), false, HEIGHT, WIDTH, WinMediaPlayer.EmbedMode.EMBED_ONLY);
                    break;
                case Native:
                    mmp = new NativePlayer(mrl.getNextResource(true), true, HEIGHT, WIDTH);
                    break;
                case PlaylistSupport:
                    mmp = new Capsule(Plugin.FlashPlayer, mrl.getNextResource(true), false);
                    break;
                case Auto:
                    mmp = PlayerUtil.getPlayer(mrl.getNextResource(true), false, "350px", "100%");
                //                    mmp = new FlatVideoPlayer(Plugin.FlashPlayer, mrl.getNextResource(true),  false, "350px", "100%");
            }
            mmp.addPlayStateHandler(new PlayStateHandler() {

                @Override
                public void onPlayStateChanged(PlayStateEvent event) {
                    GWT.log("Index : " + event.getItemIndex() + " = " + event.getPlayState());
                }
            });
            mmp.showLogger(true);
//            mmp.setConfigParameter(ConfigParameter.QTScale, QuickTimePlayer.Scale.Aspect);
            mmp.setConfigParameter(ConfigParameter.BackgroundColor, "#ffffff");
//                      ((PlaylistSupport) mmp).addToPlaylist(mrl.getNextResource(false));
//            mmp.setResizeToVideoSize(true);
//            mmp.setLoopCount(2);
//            mmp.setRepeatMode(RepeatMode.REPEAT_ALL);
            ((PlaylistSupport) mmp).setShuffleEnabled(false);
            mmp.setControllerVisible(true);
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

    private void addUTube() {
        final PlayStateHandler psh = new PlayStateHandler() {

            @Override
            public void onPlayStateChanged(PlayStateEvent event) {

                if (event.getPlayState() == State.Finished) {
                    GWT.log("finished");
                }
                if (event.getPlayState() == State.Stopped) {
                    GWT.log("stopped");
                }
                if (event.getPlayState() == State.Paused) {
                    GWT.log("paused");
                }
                if (event.getPlayState() == State.Started) {
                    GWT.log("started");
                }
            }
        };

        add(new Button("[1] click to create player", new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                try {
                    NativePlayer np = new NativePlayer(GWT.getModuleBaseURL() + "big-buck-bunny.mp4", false, "260px", "80%");
                    np.showLogger(true);
                    np.addPlayStateHandler(psh);
                    add(np);
                } catch (LoadException ex) {
                } catch (PluginNotFoundException ex) {
                    add(PlayerUtil.getMissingPluginNotice(ex.getPlugin()));
                }
            }
        }));

        try {
            npp = new NativePlayer(null, false);
            npp.setResizeToVideoSize(true);
            npp.showLogger(true);
            npp.addPlayStateHandler(psh);
            npp.setVisible(false);
            add(npp);
        } catch (LoadException ex) {
        } catch (PluginNotFoundException ex) {
        }

        add(new Button("[2] click to set player url", new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                try {
                    npp.setVisible(true);
                    npp.loadMedia(GWT.getModuleBaseURL() + "big-buck-bunny.mp4");
                } catch (LoadException ex) {
                }
            }
        }));
    }
    NativePlayer npp = null;

    private String getURL(String path) {
        return Location.createUrlBuilder().setPort(8080).setPath(path).buildString();
    }
    
    @UiTemplate("Player.ui.xml")
    interface PlayerBinder extends UiBinder<Widget, Dev>{}
    PlayerBinder pb = GWT.create(PlayerBinder.class);

    public void issue32() {
        // GWT.getModuleBaseURL() + "applause.mp3";
        final String fileUrl = GWT.getModuleBaseURL() + "big-buck-bunny.mp4";
        AbstractMediaPlayer player;
        Widget mp = null;
        try {
            player = new FlashMediaPlayer(fileUrl, true, "464px", "620px");
            player.setResizeToVideoSize(true);
            player.showLogger(true);
            mp = player;
        } catch (PluginNotFoundException e) {
            mp = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer, e.getMessage());
        } catch (PluginVersionException e) {
            mp = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer, e.getRequiredVersion());
        } catch (LoadException e) {
            mp = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer);
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
        } catch (LoadException e) {
            mp = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer);
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
