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
import com.bramosystems.gwt.player.client.ui.FlashMP3Player;
import com.bramosystems.gwt.player.client.ui.QuickTimePlayer;
import com.bramosystems.gwt.player.client.ui.WinMediaPlayer;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Abstract base class for HTML based custom players.
 * 
 * <p>The actual player plugin used to playback media files is wrapped by
 * this player and hidden on the browser.  This ensures that the player
 * is controlled via the HTML controls provided by implementation classes.
 *
 * @author Sikirulai Braheem
 */
public abstract class CustomPlayer extends AbstractMediaPlayer {

    private AbstractMediaPlayer engine;
    private SimplePanel container;

    /**
     * Constructs <code>CustomPlayer</code> with the specified {@code playerPlugin}
     * to playback media located at {@code mediaURL}. Media playback
     * begins automatically if {@code autoplay} is {@code true}.
     *
     * @param playerPlugin the plugin to use for playback.
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     *
     * @throws com.bramosystems.gwt.player.client.LoadException if an error occurs while loading the media.
     * @throws com.bramosystems.gwt.player.client.PluginVersionException if the required
     * player plugin version is not installed on the client.
     * @throws com.bramosystems.gwt.player.client.PluginNotFoundException if the player plugin is not
     * installed on the client.
     *
     * @see Plugin
     * @see QuickTimePlayer
     * @see WinMediaPlayer
     * @see FlashMP3Player
     */
    public CustomPlayer(Plugin playerPlugin, String mediaURL, boolean autoplay)
            throws PluginNotFoundException, PluginVersionException, LoadException {
        switch (playerPlugin) {
            case FlashMP3Player:
                engine = new FlashMP3Player(mediaURL, autoplay, "0", "0");
                break;
            case QuickTimePlayer:
                engine = new QuickTimePlayer(mediaURL, autoplay, "0", "0");
                break;
            case WinMediaPlayer:
                engine = new WinMediaPlayer(mediaURL, autoplay, "0", "0");
                break;
            case Auto:
                engine = PlayerUtil.getPlayer(mediaURL, autoplay, "0", "0");
                break;
        }
        engine.addMediaStateListener(new MediaStateListener() {

            public void onIOError() {
                fireIOError();
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
        });

        container = new SimplePanel();

        HorizontalPanel hp = new HorizontalPanel();
        hp.setSize("100%", "100%");
        hp.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
        hp.add(engine);
        hp.add(container);
        super.initWidget(hp);
    }

    /**
     * Overridden to prevent subclasses from changing the wrapped widget.
     * Subclass should call <code>setPlayerWidget</code> instead.
     *
     * @see #setPlayerWidget(com.google.gwt.user.client.ui.Widget)
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
    protected final void setPlayerWidget(Widget widget) {
        container.setWidget(widget);
    }

    public final void close() {
        engine.close();
    }

    public final void ejectMedia() {
        engine.ejectMedia();
    }

    public final long getMediaDuration() {
        return engine.getMediaDuration();
    }

    public final double getPlayPosition() {
        return engine.getPlayPosition();
    }

    public final void setPlayPosition(double position) {
        engine.setPlayPosition(position);
    }

    public final void loadMedia(String mediaURL) throws LoadException {
        engine.loadMedia(mediaURL);
    }

    public final void pauseMedia() {
        engine.pauseMedia();
    }

    public final void playMedia() throws PlayException {
        engine.playMedia();
    }

    public final void stopMedia() {
        engine.stopMedia();
    }

    public double getVolume() {
        return engine.getVolume();
    }

    public void setVolume(double volume) {
        engine.setVolume(volume);
    }
}
