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

    private static FlashMP3PlayerImpl impl = GWT.create(FlashMP3PlayerImpl.class);
    private String playerId,  mediaURL;
    private boolean autoplay;
    private VerticalPanel hp;

    /**
     * Constructs <code>FlashMP3Player</code> with the specified {@code height} and
     * {@code width} to playback media located at {@code mediaURL}. Media playback
     * begins automatically if {@code autoplay} is {@code true}.
     *
     * <p> {@code height} and {@code width} are specified as CSS units.
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

        PluginVersion v = PlayerUtil.getFlashPlayerVersion();
        if (v.compareTo(9, 0, 0) < 0) {
            throw new PluginVersionException("9.0.0", v.toString());
        }

        playerId = DOM.createUniqueId();
        impl.init(playerId, new MediaStateListener() {

            public void onPlayFinished() {
                firePlayFinished();
            }

            public void onLoadingComplete() {
                fireLoadingComplete();
            }

            public void onIOError() {
                fireIOError();
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
        }, false);
        this.mediaURL = mediaURL;
        this.autoplay = autoplay;

        String script = impl.getPlayerScript(playerId, false);
        hp = new VerticalPanel();
        if (height.equals("0") && width.equals("0")) {
            // placed to bypass UI setup in embedded cases especially
            // when used with CustomPlayer.
            hp.add(new HTML(script));
        } else {
            hp.add(new HTML(script));
            hp.add(new PlayerUI());
        }
        hp.setSize(width, height);
        initWidget(hp);
    }

    /**
     * Constructs <code>FlashMP3Player</code> to automatically playback media located at
     * {@code mediaURL} using the default height of 20px and width of 300px.
     *
     * <p> This is the same as calling {@code FlashMP3Player(mediaURL, true, "20", "300")}
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
        this(mediaURL, true, "20", "300");
    }

    /**
     * Constructs <code>FlashMP3Player</code> to playback media located at {@code mediaURL}
     * using the default height of 20px and width of 300px. Media playback begins
     * automatically if {@code autoplay} is {@code true}.
     *
     * <p> This is the same as calling {@code FlashMP3Player(mediaURL, autoplay, "20", "300")}
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
        this(mediaURL, autoplay, "20", "100%");
    }

    /**
     * Overridden to initiate loading of media when widget is attached to the DOM.
     */
    @Override
    protected final void onLoad() {
        try {
            loadMedia(mediaURL);
        } catch (LoadException ex) {
            //
        }
    }

    /**
     * Subclasses that override this method should call <code>super.onUnload()</code>
     * to ensure the player is properly removed from the browser's DOM.
     */
    @Override
    protected void onUnload() {
        hp.clear();
    }

    public final void playMedia() throws PlayException {
        impl.playMedia(playerId);
    }

    public final void stopMedia() {
        impl.stopMedia(playerId);
    }

    public final void pauseMedia() {
        impl.pauseMedia(playerId);
    }

    public final void ejectMedia() {
        impl.ejectMedia(playerId);
    }

    public final void close() {
        impl.closeMedia(playerId);
    }

    public final long getMediaDuration() {
        return (long) impl.getMediaDuration(playerId);
    }

    public final double getPlayPosition() {
        return impl.getPlayPosition(playerId);
    }

    public final void setPlayPosition(double position) {
        impl.setPlayPosition(playerId, position);
    }

    public double getVolume() {
        return impl.getVolume(playerId);
    }

    public void setVolume(double volume) {
        impl.setVolume(playerId, volume);
    }


    public final void loadMedia(final String mediaURL) throws LoadException {
        Timer loadingTimer = new Timer() {

            @Override
            public void run() {
                if (impl.isPlayerInit(playerId)) {
                    impl.loadMedia(playerId, mediaURL, autoplay);
                    cancel();
                } else {
                    schedule(200);
                    fireDebug(playerId + " - Player not init");
                }
            }
        };
        loadingTimer.run();
    }

    private class PlayerUI extends Composite {

        private FlashImages imgPack = GWT.create(FlashImages.class);
        private ToggleButton play, stop;
        private HTML ta;
        private Timer playTimer;
        private CSSSeekBar seekbar;
        private Label timeLabel;
        private String mediaDurationString;
        private long mediaDuration;
        private boolean onLoadOpsDone;

        public PlayerUI() {
            mediaDurationString = "";
            seekbar = new CSSSeekBar(5);
            seekbar.setPlayingStyle("background", "red");
            seekbar.setWidth("90%");
            seekbar.addSeekChangeListener(new SeekChangeListener() {

                public void onSeekChanged(double newValue) {
                    setPlayPosition(getPlayPosition() * newValue);
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
                                    toPlayState();
                                } catch (PlayException ex) {
                                    doReport(ex.getMessage());
                                }
                            } else {
                                doPause();
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
                    toStopState();
                }
            });
            stop.getUpDisabledFace().setImage(imgPack.stopDisabled().createImage());
            stop.getUpHoveringFace().setImage(imgPack.stopHover().createImage());
            stop.getDownDisabledFace().setImage(imgPack.stopDisabled().createImage());
            stop.getDownHoveringFace().setImage(imgPack.stopHover().createImage());
            stop.setEnabled(false);

            ta = new HTML();
            onLoadOpsDone = false;

            final VolumeControl vc = new VolumeControl(imgPack.spk().createImage(), 5);
            vc.setPopupStyle("background", "url(" + GWT.getModuleBaseURL() + "bg.png) repeat");
            vc.addVolumeChangeListener(new VolumeChangeListener() {

                public void onVolumeChanged(double newValue) {
                    setVolume(newValue);
                }
            });

            addMediaStateListener(new MediaStateListener() {

                public void onIOError() {
                    onDebug("An IO Error has occured");
                }

                public void onLoadingComplete() {
                    seekbar.setLoadingProgress(1.0);
                    vc.setVolume(getVolume());
                }

                public void onPlayFinished() {
                    toStopState();
                }

                public void onDebug(String report) {
                    doReport(report);
                }

                public void onLoadingProgress(double progress) {
                    if (!onLoadOpsDone) {
                        play.setEnabled(true);
                        onLoadOpsDone = true;
                    }
                    mediaDuration = getMediaDuration();
                    mediaDurationString = PlayerUtil.formatMediaTime(mediaDuration);
                    seekbar.setLoadingProgress(progress);
                }

                public void onPlayStarted() {
                    play.setEnabled(true);
                    play.setDown(true);
                    toPlayState();
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
            face.setSize("100%", "20");

            DOM.setStyleAttribute(timeLabel.getElement(), "font",
                    "bold 8pt Arial,Helvetica,sans-serif");
            DOM.setStyleAttribute(timeLabel.getElement(), "color", "white");
            DOM.setStyleAttribute(face.getElement(), "background",
                    "url(" + GWT.getModuleBaseURL() + "bg.png) repeat");
            initWidget(face);
        }

        private void doReport(String report) {
            ta.setHTML(ta.getHTML() + "<br/>" + report);
        }

        private void toPlayState() {
            playTimer.scheduleRepeating(1000);
            stop.setEnabled(true);
        }

        private void doPause() {
            // pause media...
            pauseMedia();
            playTimer.cancel();
        }

        private void toStopState() {
            playTimer.cancel();
            play.setDown(false);
            stop.setEnabled(false);
            seekbar.setPlayingProgress(0);
            setTime(0);
        }

        private void setTime(long time) {
            timeLabel.setText(PlayerUtil.formatMediaTime(time) + " / " + mediaDurationString);
        }

        @Override
        protected void onUnload() {
            playTimer.cancel();
        }
    }
}
