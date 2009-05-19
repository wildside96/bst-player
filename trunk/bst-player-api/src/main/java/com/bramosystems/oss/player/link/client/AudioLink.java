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
package com.bramosystems.oss.player.link.client;

import com.bramosystems.oss.player.core.client.*;
import com.bramosystems.oss.player.core.client.ui.*;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 */
public class AudioLink extends AbstractMediaPlayer {

    private AbstractMediaPlayer engine;
    private State playState;
    private boolean isRealized;

    public AudioLink(String mediaURL, String text, boolean preload)
            throws PluginNotFoundException, PluginVersionException, LoadException {
        this(Plugin.Auto, mediaURL, text, preload);
    }

    public AudioLink(Plugin playerPlugin, String mediaURL, String text, final boolean preload)
            throws PluginNotFoundException, PluginVersionException, LoadException {
        switch (playerPlugin) {
            case FlashMP3Player:
                engine = new FlashMP3Player(mediaURL, false, null, null);
                break;
            case QuickTimePlayer:
                engine = new QuickTimePlayer(mediaURL, false, null, null);
                break;
            case WinMediaPlayer:
                engine = new WinMediaPlayer(mediaURL, false, null, null);
                break;
            case FlashVideoPlayer:
                engine = new FlashVideoPlayer(mediaURL, false, null, null);
                break;
            case Auto:
                engine = PlayerUtil.getPlayer(mediaURL, false, null, null);
                break;
        }

        final HorizontalPanel hp = new HorizontalPanel();
        initWidget(hp);

        engine.addMediaStateListener(new MediaStateListener() {

            public void onError(String description) {
                fireError(description);
            }

            public void onLoadingComplete() {
                fireLoadingComplete();
            }

            public void onPlayFinished() {
                toPlayState(State.stop);
                firePlayFinished();
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
                if (!isRealized) {
                    isRealized = true;
                    if (!preload) {
                        doPlay();
                    }
                }
                firePlayerReady();
            }

            public void onMediaInfoAvailable(MediaInfo info) {
                fireMediaInfoAvailable(info);
            }
        });

        HTML link = new HTML("<a href='" + mediaURL + "'>" + text + "</a>");
        link.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (!event.getNativeEvent().getCtrlKey()) {
                    event.preventDefault();
                    if (!isRealized) {
                        hp.add(engine);
                    } else {
                        switch (playState) {
                            case paused:
                            case stop:
                                doPlay();
                                break;
                            case playing:
                                pauseMedia();
                        }
                    }
                }
            }
        });
        hp.add(link);
        if (preload) {
            hp.add(engine);
        }

        setStyleName("player-AudioLink");
        setTitle("Ctrl + Click to follow link");
        playState = State.stop;
        isRealized = false;
    }

    private void doPlay() {
        try {
            playMedia();
        } catch (PlayException ex) {
            fireError(ex.getMessage());
        }
    }

    private void toPlayState(State state) {
        removeStyleDependentName(playState.name());
        playState = state;
        if (!playState.equals(State.stop)) {
            addStyleDependentName(playState.name());
        }
    }

    @Override
    public void loadMedia(String mediaURL) throws LoadException {
        engine.loadMedia(mediaURL);
    }

    @Override
    public void playMedia() throws PlayException {
        engine.playMedia();
        toPlayState(State.playing);
    }

    @Override
    public void stopMedia() {
        engine.stopMedia();
        toPlayState(State.stop);
    }

    @Override
    public void pauseMedia() {
        engine.pauseMedia();
        toPlayState(State.paused);
    }

    @Override
    public void close() {
        engine.close();
    }

    @Override
    public long getMediaDuration() {
        return engine.getMediaDuration();
    }

    @Override
    public double getPlayPosition() {
        return engine.getPlayPosition();
    }

    @Override
    public void setPlayPosition(double position) {
        engine.setPlayPosition(position);
    }

    @Override
    public double getVolume() {
        return engine.getVolume();
    }

    @Override
    public void setVolume(double volume) {
        engine.setVolume(volume);
    }

    private enum State {

        playing, paused, stop
    }
}
