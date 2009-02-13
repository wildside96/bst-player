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
package com.bramosystems.gwt.player.client.ui.skin;

import com.bramosystems.gwt.player.client.*;
import com.bramosystems.gwt.player.client.ui.images.capsule.CapsuleImages;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;

/**
 * Sample custom sound player.
 *
 * <h3>Usage Example</h3>
 *
 * <p>
 * <code><pre>
 * SimplePanel panel = new SimplePanel();   // create panel to hold the player
 * Widget player = null;
 * try {
 *      // create the player
 *      player = new Capsule("www.example.com/mediafile.wma");
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
public class Capsule extends CustomPlayer {

    private CapsuleImages imgPack = GWT.create(CapsuleImages.class);
    private ProgressBar progress;
    private ToggleButton play,  stop;
    private HTML ta;
    private Timer playTimer;

    /**
     * Constructs <code>Capsule</code> player to automatically playback the
     * media located at {@code mediaURL}.
     *
     * @param mediaURL the URL of the media to playback
     *
     * @throws com.bramosystems.gwt.player.client.LoadException if an error occurs while
     * loading the media.
     * @throws com.bramosystems.gwt.player.client.PluginVersionException if the required
     * plugin version is not installed on the client.
     * @throws com.bramosystems.gwt.player.client.PluginNotFoundException if the
     * plugin is not installed on the client.
     */
    public Capsule(String mediaURL) throws PluginNotFoundException,
            PluginVersionException, LoadException {
        this(mediaURL, true);
    }

    /**
     * Constructs <code>Capsule</code> player to automatically playback the
     * media located at {@code mediaURL}, if {@code autoplay} is {@code true}.
     *
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     *
     * @throws com.bramosystems.gwt.player.client.LoadException if an error occurs while
     * loading the media.
     * @throws com.bramosystems.gwt.player.client.PluginVersionException if the required
     * plugin version is not installed on the client.
     * @throws com.bramosystems.gwt.player.client.PluginNotFoundException if the
     * plugin is not installed on the client.
     */
    public Capsule(String mediaURL, boolean autoplay) throws PluginNotFoundException,
            PluginVersionException, LoadException {
        this(Plugin.Auto, mediaURL, autoplay);
    }

    /**
     * Constructs <code>Capsule</code> player to automatically playback the
     * media located at {@code mediaURL}, if {@code autoplay} is {@code true} using
     * the specified {@code plugin}.
     *
     * @param plugin plugin to use for playback.
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     *
     * @throws com.bramosystems.gwt.player.client.LoadException if an error occurs while
     * loading the media.
     * @throws com.bramosystems.gwt.player.client.PluginVersionException if the required
     * plugin version is not installed on the client.
     * @throws com.bramosystems.gwt.player.client.PluginNotFoundException if the
     * plugin is not installed on the client.
     *
     * @see Plugin
     */
    public Capsule(Plugin plugin, String mediaURL, boolean autoplay) throws PluginNotFoundException,
            PluginVersionException, LoadException {
        super(plugin, mediaURL, autoplay);

        progress = new ProgressBar();
        progress.setWidth("95%");

        playTimer = new Timer() {

            @Override
            public void run() {
                progress.setTime(getPlayPosition());
            }
        };

        play = new ToggleButton(imgPack.play().createImage(), imgPack.pause().createImage(),
                new ClickListener() {

                    public void onClick(Widget sender) {
                        if (play.isDown()) {  // play media...
                            try {
                                playMedia();
                            } catch (PlayException ex) {
                                doReport(ex.getMessage());
                            }
                        } else { // pause media...
                            pauseMedia();
                            stop.setEnabled(true);
                        }
                    }
                });
        play.getDownDisabledFace().setImage(imgPack.pauseDisabled().createImage());
        play.getDownHoveringFace().setImage(imgPack.pauseHover().createImage());
        play.getUpDisabledFace().setImage(imgPack.playDisabled().createImage());
        play.getUpHoveringFace().setImage(imgPack.playHover().createImage());
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
        vc.addVolumeChangeListener(new VolumeChangeListener() {

            public void onVolumeChanged(double newValue) {
                setVolume(newValue);
            }
        });
        vc.setPopupStyle("background", "url(" +
                GWT.getModuleBaseURL() + "skin/capsule/background.png) repeat-x");
        vc.setPopupStyle("padding", "3px");

        addMediaStateListener(new MediaStateListener() {

            public void onError(String description) {
                onDebug(description);
            }

            public void onLoadingComplete() {
                progress.setInfo("Loaded 100%");
                progress.setLoadingProgress(1);
                progress.setDuration(getMediaDuration());
                progress.setTime(0);
                vc.setVolume(getVolume());
            }

            public void onPlayFinished() {
                stopMedia();
                progress.setFinishedState();
            }

            public void onDebug(String report) {
                doReport(report);
            }

            public void onLoadingProgress(double progrezz) {
                progress.setInfo("Loaded " + ((int) (progrezz * 100)) + "%");
                progress.setLoadingProgress(progrezz);
            }

            public void onPlayStarted() {
                progress.setDuration(getMediaDuration());
                playTimer.scheduleRepeating(1000);
                play.setDown(true);
                stop.setEnabled(true);
                vc.setVolume(getVolume());
            }

            public void onPlayerReady() {
                play.setEnabled(true);
                vc.setVolume(getVolume());
            }
        });

        // build the UI...
        DockPanel main = new DockPanel();
        main.setStyleName("");
        main.setSpacing(0);
        main.setVerticalAlignment(DockPanel.ALIGN_MIDDLE);
        main.setSize("100%", "64px");

        main.add(imgPack.lEdge().createImage(), DockPanel.WEST);
        main.add(play, DockPanel.WEST);
        main.add(stop, DockPanel.WEST);
        main.add(imgPack.rEdge().createImage(), DockPanel.EAST);
        main.add(vc, DockPanel.EAST);
        main.add(progress, DockPanel.CENTER);
        main.setCellWidth(progress, "100%");
        main.setCellHorizontalAlignment(progress, DockPanel.ALIGN_CENTER);

        DOM.setStyleAttribute(main.getElement(), "background", "url(" +
                GWT.getModuleBaseURL() + "skin/capsule/background.png) repeat-x");

        HorizontalPanel hp = new HorizontalPanel();
        hp.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
        hp.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
        hp.setWidth("100%");
        hp.add(main);

        setPlayerWidget(hp);
    }

    private void doReport(String report) {
        ta.setHTML(ta.getHTML() + "<br/>" + report);
    }

    @Override
    public void stopMedia() {
        super.stopMedia();
        playTimer.cancel();
        play.setDown(false);
        stop.setEnabled(false);
        progress.setTime(0);
    }

    /**
     * Overridden to release resources.
     */
    @Override
    protected void onUnload() {
        playTimer.cancel();
    }

    private class ProgressBar extends Composite {

        private MediaSeekBar seekBar;
        private Label timeLabel,  infoLabel;
        private long duration;
        private String durationString;

        public ProgressBar() {
            timeLabel = new Label("--:-- / --:--");
            timeLabel.setWordWrap(false);
            timeLabel.setHorizontalAlignment(Label.ALIGN_RIGHT);

            infoLabel = new Label();
            infoLabel.setWordWrap(false);

            DockPanel dp = new DockPanel();
            dp.setVerticalAlignment(DockPanel.ALIGN_MIDDLE);
            dp.setWidth("100%");
            DOM.setStyleAttribute(dp.getElement(), "font", "bold 8pt Arial,Helvetica,sans-serif");
            DOM.setStyleAttribute(dp.getElement(), "color", "white");

            dp.add(timeLabel, DockPanel.EAST);
            dp.add(infoLabel, DockPanel.CENTER);

            seekBar = new CSSSeekBar(5);
            seekBar.setWidth("100%");
            seekBar.addSeekChangeListener(new SeekChangeListener() {

                public void onSeekChanged(double newValue) {
                    double newTime = getMediaDuration() * newValue;
                    setPlayPosition(newTime);
                }
            });

            VerticalPanel main = new VerticalPanel();
            main.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
            main.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
            main.setSpacing(2);
            main.setWidth("80%");
            main.add(dp);
            main.add(seekBar);
            initWidget(main);
        }

        public void setTime(double timeInMS) {
            timeLabel.setText(PlayerUtil.formatMediaTime((long) timeInMS) + " / " + durationString);
            seekBar.setPlayingProgress(timeInMS / (double) duration);
        }

        public void setLoadingProgress(double progress) {
            seekBar.setLoadingProgress(progress);
        }

        public void setDuration(long duration) {
            this.duration = duration;
            durationString = PlayerUtil.formatMediaTime(duration);
        }

        public void setInfo(String info) {
            infoLabel.setText(info);
        }

        public void setFinishedState() {
            setTime(0);
            seekBar.setPlayingProgress(0);
        }
    }
}
