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
package com.bramosystems.oss.player.core.client.skin;

import com.bramosystems.oss.player.core.client.MediaInfo;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.PlayException;
import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.MediaStateListener;
import com.bramosystems.oss.player.core.client.PlaylistSupport;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

/**
 * Custom player control widget.
 *
 * <h4>CSS Styles</h4>
 * <code><pre>
 * .player-FlatCustomControl { the player control }
 * .player-FlatCustomControl-seekbar { the seekbar of the control }
 * .player-FlatCustomControl-seekbar .loading { the seekbars' loading progress indicator }
 * .player-FlatCustomControl-seekbar .playing { the seekbars' playing progress indicator }
 * .player-FlatCustomControl-volumeControl { the volume controls' slider widget }
 * .player-FlatCustomControl-volumeControl .volume { the volume level indicator }
 * .player-FlatCustomControl-volumeControl .track  { the volume sliders' track indicator }
 * </pre></code>
 *
 * @author Sikirulai Braheem
 * @since 0.6
 */
public class FlatCustomControl extends Composite {

    private final String STYLE_NAME = "player-FlatCustomControl";
    private ImagePack imgPack;
    private PushButton play,  stop,  prev,  next;
    private Timer playTimer;
    private CSSSeekBar seekbar;
    private Label timeLabel;
    private AbstractMediaPlayer player;
    private PlayState playState;
    private VolumeControl vc;

    /**
     * Contructs FlatCustomControl object.
     *
     * @param player the player object to control
     */
    public FlatCustomControl(AbstractMediaPlayer player) {
        this(player, (ImagePack) GWT.create(ImagePack.class));
    }

    /**
     * Constructs FlatCustomControl to control the specified player using the
     * specified image icons for the control buttons.
     *
     * @param player the player object to control
     * @param imagePack the control button icons
     * @since 1.0
     */
    public FlatCustomControl(AbstractMediaPlayer player, ImagePack imagePack) {
        this.player = player;
        this.imgPack = imagePack;
        playState = PlayState.Stop;

        initWidget(getPlayerWidget());
        setSize("100%", "20px");
        setStylePrimaryName(STYLE_NAME);
    }

    /**
     * Sets the primary CSS style name of this widget as well as the seekbar and
     * the volume controls' slider widgets.
     *
     * @param style the CSS style name
     */
    @Override
    public void setStylePrimaryName(String style) {
        super.setStylePrimaryName(style);
        seekbar.setStylePrimaryName(style);
        vc.setPopupStyleName(style + "-volumeControl");
    }

    /**
     * Sets the CSS style name of this widget as well as the seekbar and
     * the volume controls' slider widgets.
     *
     * @param style the CSS style name
     */
    @Override
    public void setStyleName(String style) {
        super.setStyleName(style);
        seekbar.setStylePrimaryName(style);
        vc.setPopupStyleName(style + "-volumeControl");
    }

    /**
     * Overriden to release resources.
     */
    @Override
    protected void onUnload() {
        playTimer.cancel();
    }

    private Widget getPlayerWidget() {
        seekbar = new CSSSeekBar(5);
        seekbar.setStylePrimaryName(STYLE_NAME);
        seekbar.addStyleDependentName("seekbar");
        seekbar.setWidth("95%");
        seekbar.addSeekChangeListener(new SeekChangeListener() {

            public void onSeekChanged(double newValue) {
                player.setPlayPosition(player.getMediaDuration() * newValue);
            }
        });

        playTimer = new Timer() {

            @Override
            public void run() {
                updateSeekState();
            }
        };

        play = new PushButton(imgPack.play().createImage(), new ClickHandler() {

            public void onClick(ClickEvent event) {
                switch (playState) {
                    case Stop:
                    case Pause:
                        try { // play media...
                            player.playMedia();
                        } catch (PlayException ex) {
                            player.fireError(ex.getMessage());
                        }
                        break;
                    case Playing:
                        player.pauseMedia();
                        toPlayState(PlayState.Pause);
                }
            }
        });
        play.getUpDisabledFace().setImage(imgPack.playDisabled().createImage());
        play.setEnabled(false);

        stop = new PushButton(imgPack.stop().createImage(), new ClickHandler() {

            public void onClick(ClickEvent event) {
                player.stopMedia();
                toPlayState(PlayState.Stop);
            }
        });
        stop.getUpDisabledFace().setImage(imgPack.stopDisabled().createImage());
        stop.getUpHoveringFace().setImage(imgPack.stopHover().createImage());
        stop.setEnabled(false);

        prev = new PushButton(imgPack.prev().createImage(), new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (player instanceof PlaylistSupport) {
                    try {
                        ((PlaylistSupport) player).playPrevious();
                    } catch (PlayException ex) {
                        next.setEnabled(false);
                        player.fireDebug(ex.getMessage());
                    }
                }
            }
        });
        prev.getUpDisabledFace().setImage(imgPack.prevDisabled().createImage());
        prev.getUpHoveringFace().setImage(imgPack.prevHover().createImage());
        prev.setEnabled(false);

        next = new PushButton(imgPack.next().createImage(), new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (player instanceof PlaylistSupport) {
                    try {
                        ((PlaylistSupport) player).playNext();
                    } catch (PlayException ex) {
                        next.setEnabled(false);
                        player.fireDebug(ex.getMessage());
                    }
                }
            }
        });
        next.getUpDisabledFace().setImage(imgPack.nextDisabled().createImage());
        next.getUpHoveringFace().setImage(imgPack.nextHover().createImage());
        next.setEnabled(false);

        vc = new VolumeControl(imgPack.spk().createImage(), 5);
        vc.setPopupStyleName(STYLE_NAME + "-volumeControl");
        vc.addVolumeChangeListener(new VolumeChangeListener() {

            public void onVolumeChanged(double newValue) {
                player.setVolume(newValue);
            }
        });

        player.addMediaStateListener(new MediaStateListener() {

            @Override
            public void onError(String description) {
                Window.alert(description);
                onDebug(description);
            }

            @Override
            public void onLoadingComplete() {
                seekbar.setLoadingProgress(1.0);
                vc.setVolume(player.getVolume());
                updateSeekState();
            }

            @Override
            public void onPlayFinished() {
                toPlayState(PlayState.Stop);
            }

            @Override
            public void onLoadingProgress(double progress) {
                seekbar.setLoadingProgress(progress);
                updateSeekState();
            }

            @Override
            public void onPlayStarted() {
                toPlayState(PlayState.Playing);
            }

            @Override
            public void onPlayerReady() {
                play.setEnabled(true);
                vc.setVolume(player.getVolume());
            }

            public void onDebug(String message) {
            }

            public void onMediaInfoAvailable(MediaInfo info) {
            }
            
            public void onBuffering(boolean buffering) {
            }

            public void onPlayStarted(int index) {
                toPlayState(PlayState.Playing);
                next.setEnabled(index < (((PlaylistSupport) player).getPlaylistSize() - 1));
                prev.setEnabled(index > 0);
            }

            public void onPlayFinished(int index) {
                toPlayState(PlayState.Stop);
                next.setEnabled(index >= 1);
                prev.setEnabled(index >= 1);
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
        face.add(prev, DockPanel.WEST);
        face.add(next, DockPanel.WEST);
        face.add(timeLabel, DockPanel.EAST);
        face.add(seekbar, DockPanel.CENTER);

        face.setCellWidth(seekbar, "100%");
        face.setCellHorizontalAlignment(seekbar, DockPanel.ALIGN_CENTER);
        return face;
    }

    private void setTime(long time, long duration) {
        timeLabel.setText(PlayerUtil.formatMediaTime(time) +
                " / " + PlayerUtil.formatMediaTime(duration));
    }

    private void toPlayState(PlayState state) {
        switch (state) {
            case Playing:
                play.setEnabled(true);
                playTimer.scheduleRepeating(1000);
                stop.setEnabled(true);
                vc.setVolume(player.getVolume());

                play.getUpFace().setImage(imgPack.pause().createImage());
                play.getUpHoveringFace().setImage(imgPack.pauseHover().createImage());
                break;
            case Stop:
                stop.setEnabled(false);
                seekbar.setPlayingProgress(0);
                setTime(0, player.getMediaDuration());
                playTimer.cancel();
                next.setEnabled(false);
                prev.setEnabled(false);
            case Pause:
                play.getUpFace().setImage(imgPack.play().createImage());
                play.getUpHoveringFace().setImage(imgPack.playHover().createImage());
                break;
        }
        playState = state;
    }

    private void updateSeekState() {
        double pos = player.getPlayPosition();
        long duration = player.getMediaDuration();
        setTime((long) pos, duration);
        seekbar.setPlayingProgress(pos / duration);
    }

    private enum PlayState {

        Playing, Pause, Stop;
    }

    /**
     * ImageBundle definition for the FlatCustomControl class
     */
    public interface ImagePack extends ImageBundle {

        public AbstractImagePrototype pause();

        public AbstractImagePrototype pauseHover();

        public AbstractImagePrototype play();

        public AbstractImagePrototype playHover();

        public AbstractImagePrototype playDisabled();

        public AbstractImagePrototype stop();

        public AbstractImagePrototype stopDisabled();

        public AbstractImagePrototype stopHover();

        public AbstractImagePrototype spk();

        public AbstractImagePrototype prev();

        public AbstractImagePrototype prevDisabled();

        public AbstractImagePrototype prevHover();

        public AbstractImagePrototype next();

        public AbstractImagePrototype nextDisabled();

        public AbstractImagePrototype nextHover();
    }
}
