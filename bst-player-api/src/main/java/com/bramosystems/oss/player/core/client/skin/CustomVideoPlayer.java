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

import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.PlayException;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.PlaylistSupport;
import com.bramosystems.oss.player.core.client.ui.FlashMediaPlayer;
import com.bramosystems.oss.player.core.client.ui.NativePlayer;
import com.bramosystems.oss.player.core.client.ui.QuickTimePlayer;
import com.bramosystems.oss.player.core.client.ui.VLCPlayer;
import com.bramosystems.oss.player.core.client.ui.WinMediaPlayer;
import com.bramosystems.oss.player.core.event.client.DebugEvent;
import com.bramosystems.oss.player.core.event.client.DebugHandler;
import com.bramosystems.oss.player.core.event.client.LoadingProgressEvent;
import com.bramosystems.oss.player.core.event.client.LoadingProgressHandler;
import com.bramosystems.oss.player.core.event.client.MediaInfoEvent;
import com.bramosystems.oss.player.core.event.client.MediaInfoHandler;
import com.bramosystems.oss.player.core.event.client.PlayStateEvent;
import com.bramosystems.oss.player.core.event.client.PlayStateHandler;
import com.bramosystems.oss.player.core.event.client.PlayerStateEvent;
import com.bramosystems.oss.player.core.event.client.PlayerStateHandler;
import com.google.gwt.user.client.ui.DockPanel;
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
public abstract class CustomVideoPlayer extends AbstractMediaPlayer implements PlaylistSupport {

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
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginVersionException if the required player plugin version is not installed on the client.
     * @throws PluginNotFoundException if the player plugin is not installed on the client.
     * @throws NullPointerException if {@code height} or {@code width} is {@code null}
     *
     * @see Plugin
     * @see QuickTimePlayer
     * @see WinMediaPlayer
     * @see FlashMediaPlayer
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
            case VLCPlayer:
                engine = new VLCPlayer(mediaURL, autoplay, height, "100%");
                break;
            case FlashPlayer:
                engine = new FlashMediaPlayer(mediaURL, autoplay, height, "100%");
                break;
            case QuickTimePlayer:
                engine = new QuickTimePlayer(mediaURL, autoplay, height, "100%");
                break;
            case WinMediaPlayer:
                engine = new WinMediaPlayer(mediaURL, autoplay, height, "100%");
                break;
            case Native:
                engine = new NativePlayer(mediaURL, autoplay, height, "100%");
                break;
            default:
                engine = PlayerUtil.getPlayer(playerPlugin, mediaURL, autoplay, height, "100%");
                break;
        }

        engine.addDebugHandler(new DebugHandler() {

            public void onDebug(DebugEvent event) {
                fireEvent(event);
            }
        });
        engine.addLoadingProgressHandler(new LoadingProgressHandler() {

            public void onLoadingProgress(LoadingProgressEvent event) {
                fireEvent(event);
            }
        });
        engine.addMediaInfoHandler(new MediaInfoHandler() {

            public void onMediaInfoAvailable(MediaInfoEvent event) {
                fireEvent(event);
            }
        });
        engine.addPlayStateHandler(new PlayStateHandler() {

            public void onPlayStateChanged(PlayStateEvent event) {
                fireEvent(event);
            }
        });
        engine.addPlayerStateHandler(new PlayerStateHandler() {

            public void onPlayerStateChanged(PlayerStateEvent event) {
                switch (event.getPlayerState()) {
                    case DimensionChangedOnVideo:
                        onVideoDimensionChanged(engine.getOffsetWidth(),
                                engine.getOffsetHeight());
                        break;
                    default:
                        fireEvent(event);
                }
            }
        });

        engine.setControllerVisible(false);
        engine.showLogger(false);

        container = new SimplePanel();
        container.setWidth("100%");

        DockPanel hp = new DockPanel();
        hp.setSpacing(0);
        hp.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
        hp.add(container, DockPanel.SOUTH);
        hp.add(engine, DockPanel.CENTER);

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

    public void addToPlaylist(String mediaURL) {
        if (engine instanceof PlaylistSupport) {
            ((PlaylistSupport) engine).addToPlaylist(mediaURL);
        }
    }

    public boolean isShuffleEnabled() {
        if (engine instanceof PlaylistSupport) {
            return ((PlaylistSupport) engine).isShuffleEnabled();
        }
        return false;
    }

    public void removeFromPlaylist(int index) {
        if (engine instanceof PlaylistSupport) {
            ((PlaylistSupport) engine).removeFromPlaylist(index);
        }
    }

    public void setShuffleEnabled(boolean enable) {
        if (engine instanceof PlaylistSupport) {
            ((PlaylistSupport) engine).setShuffleEnabled(enable);
        }
    }

    public void clearPlaylist() {
        if (engine instanceof PlaylistSupport) {
            ((PlaylistSupport) engine).clearPlaylist();
        }
    }

    public int getPlaylistSize() {
        if (engine instanceof PlaylistSupport) {
            return ((PlaylistSupport) engine).getPlaylistSize();
        }
        return 1;
    }

    public void play(int index) throws IndexOutOfBoundsException {
        if (engine instanceof PlaylistSupport) {
            ((PlaylistSupport) engine).play(index);
        }
    }

    public void playNext() throws PlayException {
        if (engine instanceof PlaylistSupport) {
            ((PlaylistSupport) engine).playNext();
        }
    }

    public void playPrevious() throws PlayException {
        if (engine instanceof PlaylistSupport) {
            ((PlaylistSupport) engine).playPrevious();
        }
    }

    @Override
    public int getVideoHeight() {
        return engine.getVideoHeight();
    }

    @Override
    public int getVideoWidth() {
        return engine.getVideoWidth();
    }

    @Override
    public boolean isControllerVisible() {
        return engine.isControllerVisible();
    }

    @Override
    public boolean isResizeToVideoSize() {
        return engine.isResizeToVideoSize();
    }

    @Override
    public void setControllerVisible(boolean show) {
        engine.setControllerVisible(show);
    }

    @Override
    public void setResizeToVideoSize(boolean resize) {
        engine.setResizeToVideoSize(resize);
    }

    @Override
    public void showLogger(boolean show) {
        engine.showLogger(show);
    }

    /**
     * Called when the size of the embedded player changes to match the dimension of
     * the media (especially video)
     *
     * <p>This method is called whenever the {@link PlayerStateEvent} event is fired
     * with <code>State.DimensionChangedOnVideo</code> state.
     *
     * @param width the width of the media (in pixels)
     * @param height the height of the media (in pixels)
     */
    protected abstract void onVideoDimensionChanged(int width, int height);
}
