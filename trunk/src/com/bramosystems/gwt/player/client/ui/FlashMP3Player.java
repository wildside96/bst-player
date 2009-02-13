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
package com.bramosystems.gwt.player.client.ui;

import com.bramosystems.gwt.player.client.*;
import com.bramosystems.gwt.player.client.impl.FlashMP3PlayerImpl;
import com.bramosystems.gwt.player.client.ui.images.flash.FlashImages;
import com.bramosystems.gwt.player.client.ui.skin.CSSSeekBar;
import com.bramosystems.gwt.player.client.ui.skin.SeekChangeListener;
import com.bramosystems.gwt.player.client.ui.skin.VolumeChangeListener;
import com.bramosystems.gwt.player.client.ui.skin.VolumeControl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;

/**
 * Widget to embed MP3 media using Flash Plugin
 *
 * <h3>Usage Example</h3>
 *
 * <p>
 * <code><pre>
 * SimplePanel panel = new SimplePanel();   // create panel to hold the player
 * Widget player = null;
 * try {
 *      // create the player
 *      player = new FlashMP3Player("www.example.com/mediafile.mp3");
 * } catch(LoadException e) {
 *      // catch loading exception and alert user
 *      Window.alert("An error occured while loading");
 * } catch(PluginVersionException e) {
 *      // catch plugin version exception and alert user, possibly providing a link
 *      // to the plugin download page.
 *      player = new HTML(".. some nice message telling the user to download plugin first ..");
 * } catch(PluginNotFoundException e) {
 *      // catch PluginNotFoundException and tell user to download plugin, possibly providing
 *      // a link to the plugin download page.
 *      player = new HTML(".. another kind of message telling the user to download plugin..");
 * }
 *
 * panel.setWidget(player); // add player to panel.
 * </pre></code>
 *
 * @author Sikirulai Braheem
 */
public class FlashMP3Player extends AbstractMediaPlayer {

    private static FlashMP3PlayerImpl impl;
    private FlashImages imgPack = GWT.create(FlashImages.class);
    private String playerId, playerDivId, mediaDurationString, mediaURL;
    private boolean isEmbedded, autoplay;
    private SimplePanel playerDiv;
    private ToggleButton play,  stop;
    private HTML ta;
    private Timer playTimer;
    private CSSSeekBar seekbar;
    private Label timeLabel;
    private long mediaDuration;

    FlashMP3Player() {
        if(impl == null) {
            impl = GWT.create(FlashMP3PlayerImpl.class);
        }
    }

    /**
     * Constructs <code>FlashMP3Player</code> with the specified {@code height} and
     * {@code width} to playback media located at {@code mediaURL}. Media playback
     * begins automatically if {@code autoplay} is {@code true}.
     *
     * <p> {@code height} and {@code width} are specified as CSS units. A value of {@code null}
     * for {@code height} or {@code width} renders the player invisible on the page.  This is
     * desired especially when used with custom controls.
     *
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     * @param height the height of the player
     * @param width the width of the player.
     *
     * @throws com.bramosystems.gwt.player.client.LoadException if an error occurs while loading the media.
     * @throws com.bramosystems.gwt.player.client.PluginVersionException if the required
     * Flash plugin version is not installed on the client.
     * @throws com.bramosystems.gwt.player.client.PluginNotFoundException if the Flash plugin is not
     * installed on the client.
     */
    public FlashMP3Player(String mediaURL, boolean autoplay, String height, String width)
            throws PluginNotFoundException, PluginVersionException, LoadException {
        this();

        PluginVersion v = PlayerUtil.getFlashPlayerVersion();
        if (v.compareTo(9, 0, 0) < 0) {
            throw new PluginVersionException("9.0.0", v.toString());
        }

        this.autoplay = autoplay;
        this.mediaURL = mediaURL;
        playerId = DOM.createUniqueId().replace("-", "");
        isEmbedded = (height == null) || (width == null);
        impl.init(playerId, new MediaStateListener() {

            public void onPlayFinished() {
                firePlayFinished();
            }

            public void onLoadingComplete() {
                fireLoadingComplete();
            }

            public void onError(String description) {
                fireError(description);
            }

            public void onDebug(String report) {
                fireDebug(report);
            }

            public void onLoadingProgress(double progress) {
                fireLoadingProgress(progress);
            }

            public void onPlayStarted() {
                firePlayStarted();
            }

            public void onPlayerReady() {
                firePlayerReady();
            }
        });

        playerDivId = playerId + "_div";
        playerDiv = new SimplePanel();
        playerDiv.getElement().setId(playerDivId);

        VerticalPanel hp = new VerticalPanel();
        hp.add(playerDiv);
        if (!isEmbedded) {
            // placed to bypass UI setup in embedded cases especially
            // when used with CustomPlayer.
            hp.add(getPlayerWidget());
        }
        hp.setSize(isEmbedded ? "0px" : width, isEmbedded ? "0px" : height);
        initWidget(hp);
    }

    /**
     * Constructs <code>FlashMP3Player</code> to automatically playback media located at
     * {@code mediaURL} using the default height of 20px and width of 300px.
     *
     * <p> This is the same as calling {@code FlashMP3Player(mediaURL, true, "20px", "100%")}
     *
     * @param mediaURL the URL of the media to playback
     *
     * @throws com.bramosystems.gwt.player.client.LoadException if an error occurs while loading the media.
     * @throws com.bramosystems.gwt.player.client.PluginVersionException if the required
     * Flash plugin version is not installed on the client.
     * @throws com.bramosystems.gwt.player.client.PluginNotFoundException if the Flash plugin is not
     * installed on the client.
     *
     */
    public FlashMP3Player(String mediaURL) throws PluginNotFoundException,
            PluginVersionException, LoadException {
        this(mediaURL, true, "20px", "100%");
    }

    /**
     * Constructs <code>FlashMP3Player</code> to playback media located at {@code mediaURL}
     * using the default height of 20px and width of 300px. Media playback begins
     * automatically if {@code autoplay} is {@code true}.
     *
     * <p> This is the same as calling {@code FlashMP3Player(mediaURL, autoplay, "20px", "100%")}
     *
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     *
     * @throws com.bramosystems.gwt.player.client.LoadException if an error occurs while loading the media.
     * @throws com.bramosystems.gwt.player.client.PluginVersionException if the required
     * Flash plugin version is not installed on the client.
     * @throws com.bramosystems.gwt.player.client.PluginNotFoundException if the Flash plugin is not
     * installed on the client.
     */
    public FlashMP3Player(String mediaURL, boolean autoplay) throws PluginNotFoundException,
            PluginVersionException, LoadException {
        this(mediaURL, autoplay, "20px", "100%");
    }

    private Widget getPlayerWidget() {
        mediaDurationString = "";
        seekbar = new CSSSeekBar(5);
        seekbar.setPlayingStyle("background", "red");
        seekbar.setWidth("90%");
        seekbar.addSeekChangeListener(new SeekChangeListener() {

            public void onSeekChanged(double newValue) {
                setPlayPosition(getMediaDuration() * newValue);
            }
        });

        playTimer = new Timer() {

            @Override
            public void run() {
                double pos = getPlayPosition();
                setTime((long) pos);
                seekbar.setPlayingProgress(pos / (double) mediaDuration);
            }
        };

        play = new ToggleButton(imgPack.play().createImage(), imgPack.pause().createImage(),
                new ClickListener() {

                    public void onClick(Widget sender) {
                        if (play.isDown()) {
                            try {
                                // play media...
                                playMedia();
                            } catch (PlayException ex) {
                                doReport(ex.getMessage());
                            }
                        } else {
                            pauseMedia();
                        }
                    }
                });
        play.getUpDisabledFace().setImage(imgPack.playDisabled().createImage());
        play.getUpHoveringFace().setImage(imgPack.playHover().createImage());
        play.getDownHoveringFace().setImage(imgPack.pauseHover().createImage());
        play.setEnabled(false);

        stop = new ToggleButton(imgPack.stop().createImage(), imgPack.stop().createImage(),
                new ClickListener() {

                    public void onClick(Widget sender) {
                        stopMedia();
                    }
                });
        stop.getUpDisabledFace().setImage(imgPack.stopDisabled().createImage());
        stop.getUpHoveringFace().setImage(imgPack.stopHover().createImage());
        stop.getDownDisabledFace().setImage(imgPack.stopDisabled().createImage());
        stop.getDownHoveringFace().setImage(imgPack.stopHover().createImage());
        stop.setEnabled(false);

        ta = new HTML();

        final VolumeControl vc = new VolumeControl(imgPack.spk().createImage(), 5);
        vc.setPopupStyle("background", "url(" + GWT.getModuleBaseURL() + "bg.png) repeat");
        vc.addVolumeChangeListener(new VolumeChangeListener() {

            public void onVolumeChanged(double newValue) {
                setVolume(newValue);
            }
        });

        addMediaStateListener(new MediaStateListener() {

            public void onError(String description) {
                onDebug(description);
            }

            public void onLoadingComplete() {
                mediaDuration = getMediaDuration();
                mediaDurationString = PlayerUtil.formatMediaTime(mediaDuration);
                seekbar.setLoadingProgress(1.0);
                vc.setVolume(getVolume());
            }

            public void onPlayFinished() {
                playTimer.cancel();
                play.setDown(false);
                stop.setEnabled(false);
                seekbar.setPlayingProgress(0);
                setTime(0);
            }

            public void onDebug(String report) {
                doReport(report);
            }

            public void onLoadingProgress(double progress) {
                mediaDuration = getMediaDuration();
                mediaDurationString = PlayerUtil.formatMediaTime(mediaDuration);
                seekbar.setLoadingProgress(progress);
            }

            public void onPlayStarted() {
                play.setEnabled(true);
                play.setDown(true);
                playTimer.scheduleRepeating(1000);
                stop.setEnabled(true);
                vc.setVolume(getVolume());
            }

            public void onPlayerReady() {
                play.setEnabled(true);
                setTime(0);
            }
        });

        // Time label..
        timeLabel = new Label("--:-- / --:--");
        timeLabel.setWordWrap(false);
        timeLabel.setHorizontalAlignment(Label.ALIGN_CENTER);

        // build the UI...
        DockPanel face = new DockPanel();
        face.setVerticalAlignment(DockPanel.ALIGN_MIDDLE);
        face.setSpacing(3);
        face.add(vc, DockPanel.WEST);
        face.add(play, DockPanel.WEST);
        face.add(stop, DockPanel.WEST);
        face.add(timeLabel, DockPanel.EAST);
        face.add(seekbar, DockPanel.CENTER);

        face.setCellWidth(seekbar, "100%");
        face.setCellHorizontalAlignment(seekbar, DockPanel.ALIGN_CENTER);
        face.setSize("100%", "20px");

        DOM.setStyleAttribute(timeLabel.getElement(), "font",
                "bold 8pt Arial,Helvetica,sans-serif");
        DOM.setStyleAttribute(timeLabel.getElement(), "color", "white");
        DOM.setStyleAttribute(face.getElement(), "background",
                "url(" + GWT.getModuleBaseURL() + "bg.png) repeat");
        return face;
    }

    private void doReport(String report) {
        ta.setHTML(ta.getHTML() + "<br/>" + report);
    }

    private void setTime(long time) {
        timeLabel.setText(PlayerUtil.formatMediaTime(time) + " / " + mediaDurationString);
    }

    @Override
    protected void onLoad() {
       impl.injectScript(playerDivId, playerId, mediaURL, autoplay, false);
    }

    /**
     * Subclasses that override this method should call <code>super.onUnload()</code>
     * to ensure the player is properly removed from the browser's DOM.
     */
    @Override
    protected void onUnload() {
        stopMedia();
        impl.closeMedia(playerId);
        playerDiv.clear();
    }

    public void close() {
        impl.closeMedia(playerId);
    }

    private void checkAvailable() {
        if(!impl.isPlayerAvailable(playerId))
            throw new IllegalStateException("Player closed already, create" +
                    " another instance.");
    }

    public void ejectMedia() {
        checkAvailable();
        impl.ejectMedia(playerId);
    }

    public long getMediaDuration() {
        checkAvailable();
        return (long) impl.getMediaDuration(playerId);
    }

    public double getPlayPosition() {
        checkAvailable();
        return impl.getPlayPosition(playerId);
    }

    public double getVolume() {
        checkAvailable();
        return impl.getVolume(playerId);
    }

    public void loadMedia(String mediaURL) throws LoadException {
        checkAvailable();
        impl.loadMedia(playerId, mediaURL);
    }

    public void pauseMedia() {
        checkAvailable();
        impl.pauseMedia(playerId);
        if (!isEmbedded) {
            playTimer.cancel();
        }
    }

    public void playMedia() throws PlayException {
        checkAvailable();
        impl.playMedia(playerId);
    }

    public void setPlayPosition(double position) {
        checkAvailable();
        impl.setPlayPosition(playerId, position);
    }

    public void setVolume(double volume) {
        checkAvailable();
        impl.setVolume(playerId, volume);
    }

    public void stopMedia() {
        checkAvailable();
        impl.stopMedia(playerId);
        if (!isEmbedded) {
            playTimer.cancel();
            play.setDown(false);
            stop.setEnabled(false);
            seekbar.setPlayingProgress(0);
            setTime(0);
        }
    }
}
