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
import com.bramosystems.gwt.player.client.ui.FlashVideoPlayer;
import com.bramosystems.gwt.player.client.ui.FlashMP3Player;
import com.bramosystems.gwt.player.client.ui.QuickTimePlayer;
import com.bramosystems.gwt.player.client.ui.WinMediaPlayer;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Abstract base class for HTML based custom video players.
 * 
 * <p>The actual player plugin used to playback media files is wrapped by
 * this player and hidden on the browser.  This ensures that the player
 * is controlled via the HTML controls provided by implementation classes.
 *
 * @author Sikirulai Braheem
 */
public abstract class CustomVideoPlayer extends AbstractMediaPlayer {

    private AbstractMediaPlayer engine;
    private SimplePanel container;

    /**
     * Constructs <code>CustomVideoPlayer</code> with the specified {@code height} and
     * {@code width} using the specified {@code playerPlugin} to playback media located
     * at {@code mediaURL}. Media playback begins automatically if {@code autoplay} is {@code true}.
     *
     * @param playerPlugin the plugin to use for playback.
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     * @param height the height of the player
     * @param width the width of the player.
     *
     * @throws com.bramosystems.gwt.player.client.LoadException if an error occurs while loading the media.
     * @throws com.bramosystems.gwt.player.client.PluginVersionException if the required
     * player plugin version is not installed on the client.
     * @throws com.bramosystems.gwt.player.client.PluginNotFoundException if the player plugin is not
     * installed on the client.
     * @throws NullPointerException if {@code height} or {@code width} is {@code null}
     *
     * @see Plugin
     * @see QuickTimePlayer
     * @see WinMediaPlayer
     * @see FlashMP3Player
     */
    public CustomVideoPlayer(Plugin playerPlugin, String mediaURL, boolean autoplay,
            String height, String width)
            throws PluginNotFoundException, PluginVersionException, LoadException {
        if (height == null) {
            throw new NullPointerException("height cannot be null");
        }
        if (width == null) {
            throw new NullPointerException("width cannot be null");
        }

        switch (playerPlugin) {
            case FlashVideoPlayer:
                engine = new FlashVideoPlayer(mediaURL, autoplay, height, "100%");
                break;
            case QuickTimePlayer:
                engine = new QuickTimePlayer(mediaURL, autoplay, height, "100%");
                break;
            case WinMediaPlayer:
                engine = new WinMediaPlayer(mediaURL, autoplay, height, "100%");
                break;
            default:
                engine = PlayerUtil.getPlayer(mediaURL, autoplay, height, "100%");
                break;
        }

        engine.addMediaStateListener(new MediaStateListener() {

            public void onError(String description) {
                fireError(description);
            }

            public void onLoadingComplete() {
                fireLoadingComplete();
            }

            public void onPlayFinished() {
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
                firePlayerReady();
            }

            public void onMediaInfoAvailable(MediaInfo info) {
                fireMediaInfoAvailable(info);
            }
        });
        engine.setControllerVisible(false);
        engine.showLogger(false);

        container = new SimplePanel();
        container.setWidth("100%");

        VerticalPanel hp = new VerticalPanel();
        hp.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
        hp.add(engine);
        hp.add(container);
        super.initWidget(hp);
        setSize(width, height);
    }

    /**
     * Overridden to prevent subclasses from changing the wrapped widget.
     * Subclass should call <code>setPlayerControlWidget</code> instead.
     *
     * @see #setPlayerControlWidget(com.google.gwt.user.client.ui.Widget)
     */
    @Override
    protected final void initWidget(Widget widget) {
    }

    /**
     * Sets the widget that will be used to control the player plugin.
     * <p>Subclasses should call this method before calling any method that
     * targets this widget.
     *
     * @param widget the player control widget
     */
    protected final void setPlayerControlWidget(Widget widget) {
        container.setWidget(widget);
    }

    public void close() {
        engine.close();
    }

    public void ejectMedia() {
        engine.ejectMedia();
    }

    public long getMediaDuration() {
        return engine.getMediaDuration();
    }

    public double getPlayPosition() {
        return engine.getPlayPosition();
    }

    public void setPlayPosition(double position) {
        engine.setPlayPosition(position);
    }

    public void loadMedia(String mediaURL) throws LoadException {
        engine.loadMedia(mediaURL);
    }

    public void pauseMedia() {
        engine.pauseMedia();
    }

    public void playMedia() throws PlayException {
        engine.playMedia();
    }

    public void stopMedia() {
        engine.stopMedia();
    }

    public double getVolume() {
        return engine.getVolume();
    }

    public void setVolume(double volume) {
        engine.setVolume(volume);
    }

    /**
     * Returns the remaining number of times this player loops playback before stopping.
     */
    @Override
    public int getLoopCount() {
        return engine.getLoopCount();
    }

    /**
     * Sets the number of times the current media file should loop playback before stopping.
     */
    @Override
    public void setLoopCount(int loop) {
        engine.setLoopCount(loop);
    }

}
