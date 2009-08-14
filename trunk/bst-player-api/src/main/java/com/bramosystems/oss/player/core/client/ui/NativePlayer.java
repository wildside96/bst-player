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

import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.MediaInfo;
import com.bramosystems.oss.player.core.client.MediaInfo.MediaInfoKey;
import com.bramosystems.oss.player.core.client.PlayException;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.impl.NativePlayerImpl;
import com.bramosystems.oss.player.core.client.impl.PlayerScriptUtil;
import com.bramosystems.oss.player.core.event.client.DebugEvent;
import com.bramosystems.oss.player.core.event.client.DebugHandler;
import com.bramosystems.oss.player.core.event.client.MediaInfoEvent;
import com.bramosystems.oss.player.core.event.client.MediaInfoHandler;
import com.bramosystems.oss.player.core.event.client.PlayStateEvent;
import com.bramosystems.oss.player.core.event.client.PlayerStateEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import java.util.ArrayList;

/**
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems dot com>
 */
public class NativePlayer extends AbstractMediaPlayer {

    private NativePlayerImpl impl;
    private String playerId,  mediaURL,  _height,  _width;
    private DockPanel panel;
    private SimplePanel playerPanel;
    private boolean autoplay,  adjustToVideoSize;
    private ArrayList<MediaItem> mediaItems;
    private Logger logger;
    private Timer initTimer;

    private NativePlayer() throws PluginNotFoundException {
        if (!PlayerUtil.isHTML5CompliantClient()) {
            throw new PluginNotFoundException();
        }

        playerId = DOM.createUniqueId().replace("-", "");
        playerPanel = new SimplePanel();
        playerPanel.setSize("100%", "100%");
        adjustToVideoSize = false;
    }

    private void _init(String width, String height) {
        _height = height;
        _width = width;

        panel = new DockPanel();
        panel.setSize("100%", "100%");
        initWidget(panel);
        setWidth(width);

        logger = new Logger();
        panel.add(logger, DockPanel.SOUTH);
        panel.add(playerPanel, DockPanel.CENTER);
        panel.setCellHeight(playerPanel, height);

        addDebugHandler(new DebugHandler() {

            public void onDebug(DebugEvent event) {
                logger.log(event.getMessage(), false);
            }
        });
        addMediaInfoHandler(new MediaInfoHandler() {

            public void onMediaInfoAvailable(MediaInfoEvent event) {
                MediaInfo info = event.getMediaInfo();
                if (info.getAvailableItems().contains(MediaInfoKey.VideoHeight) ||
                        info.getAvailableItems().contains(MediaInfoKey.VideoWidth)) {
                    checkVideoSize(Integer.parseInt(info.getItem(MediaInfoKey.VideoHeight)),
                            Integer.parseInt(info.getItem(MediaInfoKey.VideoWidth)));
                }
                logger.log(info.asHTMLString(), true);
            }
        });

        initTimer = new Timer() {

            @Override
            public void run() {
                if (isPlayerOnPage(playerId)) {
                    impl = NativePlayerImpl.getPlayer(playerId);
                    impl.registerMediaStateHandlers(NativePlayer.this);
                    fireDebug("Native Browser Player");
                    firePlayerStateEvent(PlayerStateEvent.State.Ready);
                } else {
                    schedule(200);
                }
            }
        };
    }

    public NativePlayer(String mediaURL) throws LoadException, PluginNotFoundException {
        this(mediaURL, true, "20px", "100%");
    }

    public NativePlayer(String mediaURL, boolean autoplay)
            throws LoadException, PluginNotFoundException {
        this(mediaURL, autoplay, "20px", "100%");
    }

    public NativePlayer(String mediaURL, boolean autoplay, String height, String width)
            throws LoadException, PluginNotFoundException {
        this();
        this.mediaURL = mediaURL;
        this.autoplay = autoplay;
        _init(width, height);
    }

    public NativePlayer(ArrayList<MediaItem> mediaSources, boolean autoplay, String height, String width)
            throws LoadException, PluginNotFoundException {
        this();
        this.mediaItems = mediaSources;
        this.autoplay = autoplay;

        _init(width, height);
    }

    @Override
    protected void onLoad() {
        int h = playerPanel.getOffsetHeight();
        int w = playerPanel.getOffsetWidth();
        playerPanel.setWidget(new HTML(mediaItems == null ? PlayerScriptUtil.getNativePlayerScript(playerId,
                mediaURL, autoplay, h, w) : PlayerScriptUtil.getNativePlayerScript(playerId, mediaItems, autoplay,
                h, w)));
        initTimer.run();
    }

    @Override
    public void loadMedia(String mediaURL) throws LoadException {
        checkAvailable();
        impl.setMediaURL(mediaURL);
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
    public void close() {
    }

    @Override
    public long getMediaDuration() {
        checkAvailable();
        return (long) impl.getDuration() * 1000;
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
        return 0;
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

    @Override
    public void setControllerVisible(boolean show) {
        checkAvailable();
        impl.setControlsVisible(show);
    }

    @Override
    public void setLoopCount(int loop) {
        if (isPlayerOnPage(playerId)) {
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

        setWidth(_w);
        panel.setCellHeight(playerPanel, _h);
        DOM.getElementById(playerId).setAttribute("width", _w);
        DOM.getElementById(playerId).setAttribute("height", _h);

        if (!_height.equals(_h) && !_width.equals(_w)) {
            firePlayerStateEvent(PlayerStateEvent.State.DimensionChangedOnVideo);
        }
    }

    @Override
    public void showLogger(boolean show) {
        logger.setVisible(show);
    }

    private void checkAvailable() {
        if (!isPlayerOnPage(playerId)) {
            String message = "Player not available";
            fireDebug(message);
            throw new IllegalStateException(message);
        }
    }

    @SuppressWarnings("unused")
    private void fireProgressChanged() {
        fireLoadingProgress(0);
        fireMediaInfoAvailable(null);
        fireDebug(playerId);
        fireError(playerId);
//        firePlayerStateEvent(state);
    }

    @SuppressWarnings("unused")
    private void fireStateChanged(int code) {
        switch (code) {
            case 1: // play started
                fireDebug("Play started");
                firePlayStateEvent(PlayStateEvent.State.Started, 0);
                break;
            case 2: // pause
                fireDebug("Play paused");
                firePlayStateEvent(PlayStateEvent.State.Paused, 0);
                break;
            case 3: // finished
                fireDebug("Play finished");
                firePlayStateEvent(PlayStateEvent.State.Finished, 0);
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
                    fireDebug("Volume changed : " + (impl.getVolume() * 100));
                }
                break;
            case 8: // error
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
                        String url = mediaURL != null ? mediaURL : mediaItems.get(0).getSource();
                        fireError("ERROR: Media not supported - " + url);
                        break;
                }
                break;
            case 9: // loading aborted
                fireDebug("Media loading aborted!");
                break;
        }
    }

    public static class MediaItem {

        private String source,  type;

        public MediaItem(String source, String type) {
            this.source = source;
            this.type = type;
        }

        public String getSource() {
            return source;
        }

        public String getType() {
            return type;
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
