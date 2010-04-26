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
package com.bramosystems.oss.player.core.client.ui;

import java.util.ArrayList;

import com.bramosystems.oss.player.core.client.AbstractMediaPlayerWithPlaylist;
import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.MediaInfo;
import com.bramosystems.oss.player.core.client.MediaInfo.MediaInfoKey;
import com.bramosystems.oss.player.core.client.PlayException;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.impl.LoopManager;
import com.bramosystems.oss.player.core.client.impl.NativePlayerImpl;
import com.bramosystems.oss.player.core.client.impl.NativePlayerUtil;
import com.bramosystems.oss.player.core.client.impl.PlayerWidget;
import com.bramosystems.oss.player.core.event.client.DebugEvent;
import com.bramosystems.oss.player.core.event.client.DebugHandler;
import com.bramosystems.oss.player.core.event.client.MediaInfoEvent;
import com.bramosystems.oss.player.core.event.client.MediaInfoHandler;
import com.bramosystems.oss.player.core.event.client.PlayStateEvent;
import com.bramosystems.oss.player.core.event.client.PlayerStateEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * Widget to embed media files with HTML 5 video elements in compliant browsers.
 *
 * <h3>Usage Example</h3>
 *
 * <p>
 * <code><pre>
 * SimplePanel panel = new SimplePanel();   // create panel to hold the player
 * Widget player = null;
 * try {
 *      // create the player
 *      player = new NativePlayer("www.example.com/mediafile.ogg");
 * } catch(LoadException e) {
 *      // catch loading exception and alert user
 *      Window.alert("An error occured while loading");
 * } catch(PluginNotFoundException e) {
 *      // PluginNotFoundException thrown if browser does not support HTML 5 specs.
 *      player = PlayerUtil.getMissingPluginNotice(e.getPlugin());
 * }
 *
 * panel.setWidget(player); // add player to panel.
 * </pre></code>
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems dot com>
 */
public class NativePlayer extends AbstractMediaPlayerWithPlaylist {

    private NumberFormat volFmt = NumberFormat.getPercentFormat();
    private NativePlayerImpl impl;
    private String playerId, _height, _width;
    private PlayerWidget playerWidget;
    private boolean adjustToVideoSize, isEmbedded;
    private Logger logger;
    private LoopManager loopManager;

    private NativePlayer() throws PluginNotFoundException {
        if (!PlayerUtil.isHTML5CompliantClient()) {
            throw new PluginNotFoundException(Plugin.Native);
        }

        playerId = DOM.createUniqueId().replace("-", "");
        adjustToVideoSize = false;
    }

    private void _init(String width, String height) {
        FlowPanel panel = new FlowPanel();
        panel.add(playerWidget);
        initWidget(panel);

        if ((width == null) || (height == null)) {
            _height = "0px";
            _width = "0px";
            isEmbedded = true;
        } else {
            _height = height;
            _width = width;

            logger = new Logger();
            logger.setVisible(false);
            panel.add(logger);

            addDebugHandler(new DebugHandler() {

                @Override
                public void onDebug(DebugEvent event) {
                    logger.log(event.getMessage(), false);
                }
            });
            addMediaInfoHandler(new MediaInfoHandler() {

                @Override
                public void onMediaInfoAvailable(MediaInfoEvent event) {
                    MediaInfo info = event.getMediaInfo();
                    if (info.getAvailableItems().contains(MediaInfoKey.VideoHeight)
                            || info.getAvailableItems().contains(MediaInfoKey.VideoWidth)) {
                        checkVideoSize(Integer.parseInt(info.getItem(MediaInfoKey.VideoHeight)),
                                Integer.parseInt(info.getItem(MediaInfoKey.VideoWidth)));
                    }
                    logger.log(info.asHTMLString(), true);
                }
            });
        }

        loopManager = new LoopManager(!NativePlayerUtil.get.isLoopingSupported(),
                new LoopManager.LoopCallback() {

            @Override
            public void onLoopFinished() {
                fireDebug("Play finished");
                firePlayStateEvent(PlayStateEvent.State.Finished, 0);
            }

            @Override
            public void loopForever(boolean loop) {
                impl.setLooping(loop);
            }

            @Override
            public void playNextLoop() {
                impl.play();
            }
        });
    }

    /**
     * Constructs <code>NativePlayer</code> to playback media located at {@code mediaURL}.
     * Media playback begins automatically.
     *
     * <p>This is the same as calling {@code NativePlayer(mediaURL, true, "20px", "100%")}</p>
     *
     * @param mediaURL the URL of the media to playback
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginNotFoundException if browser does not support the HTML 5 specification.
     */
    public NativePlayer(String mediaURL) throws LoadException, PluginNotFoundException {
        this(mediaURL, true, NativePlayerUtil.get.getPlayerHeight(), "100%");
    }

    /**
     * Constructs <code>NativePlayer</code> to playback media located at {@code mediaURL}.
     * Media playback begins automatically if {@code autoplay} is {@code true}.
     *
     * <p>This is the same as calling {@code NativePlayer(mediaURL, autoplay, "20px", "100%")}</p>
     *
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginNotFoundException if browser does not support the HTML 5 specification.
     */
    public NativePlayer(String mediaURL, boolean autoplay)
            throws LoadException, PluginNotFoundException {
        this(mediaURL, autoplay, NativePlayerUtil.get.getPlayerHeight(), "100%");
    }

    // TODO: if player does not support URL throw LoadException...
    /**
     * Constructs <code>NativePlayer</code> with the specified {@code height} and
     * {@code width} to playback media located at {@code mediaURL}. Media playback
     * begins automatically if {@code autoplay} is {@code true}.
     *
     * <p> {@code height} and {@code width} are specified as CSS units.</p>
     *
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     * @param height the height of the player
     * @param width the width of the player.
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginNotFoundException if browser does not support the HTML 5 specification.
     */
    public NativePlayer(String mediaURL, boolean autoplay, String height, String width)
            throws LoadException, PluginNotFoundException {
        this();
        playerWidget = new PlayerWidget(Plugin.Native, playerId, mediaURL, autoplay, null);
        _init(width, height);
    }

    /**
     * Constructs <code>NativePlayer</code> with the specified {@code height} and
     * {@code width} to playback media located at any of the {@code mediaSources}.
     * Playback begins automatically if {@code autoplay} is {@code true}.
     *
     * <p>As per the HTML 5 specification, the browser chooses any of the {@code mediaSources}
     * it supports</p>
     *
     * <p> {@code height} and {@code width} are specified as CSS units.</p>
     *
     * @param mediaSources a list of media URLs
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     * @param height the height of the player
     * @param width the width of the player.
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginNotFoundException if browser does not support the HTML 5 specification.
     */
    public NativePlayer(ArrayList<String> mediaSources, boolean autoplay, String height, String width)
            throws LoadException, PluginNotFoundException {
        this();
        playerWidget = new PlayerWidget(Plugin.Native, playerId, mediaSources, autoplay, null);
        _init(width, height);
    }

    /**
     * Overriden to register player for DOM events.
     */
    @Override
    protected void onLoad() {
        playerWidget.setSize(_width, _height);
        setWidth(_width);

        impl = NativePlayerImpl.getPlayer(playerId);
        impl.registerMediaStateHandlers(this);
        fireDebug("Browsers' Native Player");
        firePlayerStateEvent(PlayerStateEvent.State.Ready);
    }

    @Override
    public void loadMedia(String mediaURL) throws LoadException {
        checkAvailable();
        impl.setMediaURL(mediaURL);
        impl.load();
    }

    @Override
    public void playMedia() throws PlayException {
        checkAvailable();
        impl.play();
    }

    @Override
    public void stopMedia() {
        checkAvailable();
        impl.pause();
        impl.setTime(0);
        firePlayStateEvent(PlayStateEvent.State.Stopped, 0);
    }

    @Override
    public void pauseMedia() {
        checkAvailable();
        impl.pause();
    }

    @Override
    public long getMediaDuration() {
        checkAvailable();
        return (long) impl.getDuration();
    }

    @Override
    public double getPlayPosition() {
        checkAvailable();
        return impl.getTime();
    }

    @Override
    public void setPlayPosition(double position) {
        checkAvailable();
        impl.setTime(position);
    }

    @Override
    public double getVolume() {
        checkAvailable();
        return impl.getVolume();
    }

    @Override
    public void setVolume(double volume) {
        checkAvailable();
        impl.setVolume(volume);
    }

    @Override
    public int getLoopCount() {
        checkAvailable();
        return loopManager.getLoopCount();
    }

    @Override
    public int getVideoHeight() {
        checkAvailable();
        return Integer.parseInt(impl.getVideoHeight());
    }

    @Override
    public int getVideoWidth() {
        checkAvailable();
        return Integer.parseInt(impl.getVideoWidth());
    }

    @Override
    public boolean isControllerVisible() {
        checkAvailable();
        return impl.isControlsVisible();
    }

    @Override
    public boolean isResizeToVideoSize() {
        return adjustToVideoSize;
    }

    /**
     * Displays or hides the player controls.
     *
     * <p>If this player is not available on the panel, this method
     * call is added to the command-queue for later execution.
     */
    @Override
    public void setControllerVisible(final boolean show) {
        if (isPlayerOnPage(playerId)) {
            impl.setControlsVisible(show);
        } else {
            addToPlayerReadyCommandQueue("controller", new Command() {

                @Override
                public void execute() {
                    impl.setControlsVisible(show);
                }
            });
        }
    }

    /**
     * Sets the number of times the current media file should repeat playback before stopping.
     *
     * <p>If this player is not available on the panel, this method call is added
     * to the command-queue for later execution.
     */
    @Override
    public void setLoopCount(final int loop) {
        if (isPlayerOnPage(playerId)) {
            loopManager.setLoopCount(loop);
        } else {
            addToPlayerReadyCommandQueue("loop", new Command() {

                @Override
                public void execute() {
                    loopManager.setLoopCount(loop);
                }
            });
        }
    }

    @Override
    public void setResizeToVideoSize(boolean resize) {
        adjustToVideoSize = resize;
        if (isPlayerOnPage(playerId)) {
            // if player is on panel now update its size, otherwise
            // allow it to be handled by the MediaInfoHandler...
            checkVideoSize(getVideoHeight(), getVideoWidth());
        }
    }

    private void checkVideoSize(int vidHeight, int vidWidth) {
        String _h = _height, _w = _width;
        if (adjustToVideoSize) {
            if ((vidHeight > 0) && (vidWidth > 0)) {
                // adjust to video size ...
                fireDebug("Resizing Player : " + vidWidth + " x " + vidHeight);
                _w = vidWidth + "px";
                _h = vidHeight + "px";
            }
        }

        playerWidget.setSize(_w, _h);
        setWidth(_w);

        if (!_height.equals(_h) && !_width.equals(_w)) {
            firePlayerStateEvent(PlayerStateEvent.State.DimensionChangedOnVideo);
        }
    }

    @Override
    public void showLogger(boolean show) {
        if (!isEmbedded) {
            logger.setVisible(show);
        }
    }

    @Override
    public void setRate(final double rate) {
        if (isPlayerOnPage(playerId)) {
            impl.setRate(rate);
        } else {
            addToPlayerReadyCommandQueue("rate", new Command() {

                @Override
                public void execute() {
                    impl.setRate(rate);
                }
            });
        }
    }

    @Override
    public double getRate() {
        checkAvailable();
        return impl.getRate();
    }

    private void checkAvailable() {
        if (!isPlayerOnPage(playerId)) {
            String message = "Player not available, create an instance";
            fireDebug(message);
            throw new IllegalStateException(message);
        }
    }

    @SuppressWarnings("unused")
    private void fireProgressChanged() {
        NativePlayerImpl.TimeRange time = impl.getBuffered();
        if (time != null) {
            double i = time.getLength();
            fireLoadingProgress((time.getEnd(i - 1) - time.getStart(0)) * 1000 / impl.getDuration());
        }
    }

    @SuppressWarnings("unused")
    private void fireStateChanged(int code) {
        switch (code) {
            case 1: // play started
                fireDebug("Play started");
                firePlayStateEvent(PlayStateEvent.State.Started, 0);
                fireDebug("Playing media at '" + impl.getMediaURL() + "'");
                break;
            case 2: // pause
                fireDebug("Play paused");
                firePlayStateEvent(PlayStateEvent.State.Paused, 0);
                break;
            case 3: // finished
                // notify loop manager, it handles play finished event ...
                loopManager.notifyPlayFinished();
                break;
            case 4: // buffering
                fireDebug("Buffering started");
                firePlayerStateEvent(PlayerStateEvent.State.BufferingStarted);
                break;
            case 5: // playing again, buffering stopped
                fireDebug("Buffering stopped");
                firePlayerStateEvent(PlayerStateEvent.State.BufferingFinished);
                break;
            case 6: // process metadata
                fireDebug("Media Metadata available");
                MediaInfo info = new MediaInfo();
                impl.fillMediaInfo(info);
                fireMediaInfoAvailable(info);
                break;
            case 7: // volume changed
                if (impl.isMute()) {
                    fireDebug("Volume muted");
                } else {
                    fireDebug("Volume changed : " + volFmt.format(impl.getVolume()));
                }
                break;
            case 10: // loading started
                fireDebug("Loading started");
                fireLoadingProgress(0);
                break;
            case 11: // loading finished
                fireDebug("Loading completed");
                fireLoadingProgress(1.0);
                break;
            case 12: // error
                switch (MediaError.values()[impl.getErrorState()]) {
                    case Aborted:
                        fireDebug("Loading aborted!");
                        break;
                    case DecodeError:
                        fireError("ERROR: Decoding error");
                        break;
                    case NetworkError:
                        fireError("ERROR: Network error");
                        break;
                    case UnsupportedMedia:
//                        String url = mediaURL != null ? mediaURL : mediaItems.get(0).getSource();
//                        fireError("ERROR: Media not supported - " + url);
                        fireError("ERROR: Media not supported! '" + impl.getMediaURL() + "'");
                        break;
                }
                break;
            case 13: // loading aborted
                fireDebug("Media loading aborted!");
                break;
        }
    }

    private enum NetworkState {

        Empty, Idle, Loading, Loaded, NoSource
    }

    private enum MediaError {

        NoError, Aborted, NetworkError, DecodeError, UnsupportedMedia
    }

    private enum ReadyState {

        HaveNothing, HaveMetadata, CurrentData, FutureData, EnoughData
    }
}
