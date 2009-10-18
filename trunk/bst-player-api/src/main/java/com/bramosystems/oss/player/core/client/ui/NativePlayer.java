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
import com.bramosystems.oss.player.core.event.client.DebugEvent;
import com.bramosystems.oss.player.core.event.client.DebugHandler;
import com.bramosystems.oss.player.core.event.client.MediaInfoEvent;
import com.bramosystems.oss.player.core.event.client.MediaInfoHandler;
import com.bramosystems.oss.player.core.event.client.PlayStateEvent;
import com.bramosystems.oss.player.core.event.client.PlayerStateEvent;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;

/**
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems dot com>
 */
public class NativePlayer extends AbstractMediaPlayer implements HasMouseDownHandlers,
        HasMouseMoveHandlers, HasMouseUpHandlers, HasKeyDownHandlers, HasKeyUpHandlers, HasKeyPressHandlers {

    private NumberFormat volFmt = NumberFormat.getPercentFormat();
    private NativePlayerImpl impl;
    private String playerId,  _height,  _width;
    private DockPanel panel;
    private Widget playerWidget;
    private boolean adjustToVideoSize,  isEmbedded;
    private Logger logger;

    private NativePlayer() throws PluginNotFoundException {
        if (!PlayerUtil.isHTML5CompliantClient()) {
            throw new PluginNotFoundException();
        }

        playerId = DOM.createUniqueId().replace("-", "");
        adjustToVideoSize = false;
    }

    private void _init(String width, String height) {
        panel = new DockPanel();
        panel.setSize("100%", "100%");
        initWidget(panel);

        if ((width == null) || (height == null)) {
            _height = "0px";
            _width = "0px";
            isEmbedded = true;
        } else {
            _height = height;
            _width = width;

            logger = new Logger();
            panel.add(logger, DockPanel.SOUTH);

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
        }

        panel.add(playerWidget, DockPanel.CENTER);
        panel.setCellHeight(playerWidget, _height);
        setWidth(_width);
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
        playerWidget = new NativeWidget(playerId, mediaURL, autoplay);
        _init(width, height);
    }

    public NativePlayer(ArrayList<MediaItem> mediaSources, boolean autoplay, String height, String width)
            throws LoadException, PluginNotFoundException {
        this();
        playerWidget = new NativeWidget(playerId, mediaSources, autoplay);
        _init(width, height);
    }

    @Override
    protected void onLoad() {
        impl = NativePlayerImpl.getPlayer(playerId);
        impl.registerMediaStateHandlers(this);
        fireDebug("Native Browser Player");
        firePlayerStateEvent(PlayerStateEvent.State.Ready);
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

    /**
     * Sets the number of times the current media file should repeat playback before stopping.
     *
     * <p>If this player is not available on the panel, this method call is added
     * to the command-queue for later execution.
     */
    @Override
    public void setLoopCount(final int loop) {
        if (isPlayerOnPage(playerId)) {
            impl.setLooping(loop < 0);
        } else {
            addToPlayerReadyCommandQueue("loop", new Command() {

                public void execute() {
                    setLoopCount(loop);
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

        setWidth(_w);
        panel.setCellHeight(playerWidget, _h);

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

    private void checkAvailable() {
        if (!isPlayerOnPage(playerId)) {
            String message = "Player not available";
            fireDebug(message);
            throw new IllegalStateException(message);
        }
    }

    // TODO: check for progress probably using online content...
    @SuppressWarnings("unused")
    private void fireProgressChanged() {
//        fireLoadingProgress(0);
        NativePlayerImpl.TimeRange time = impl.getBuffered();
        String range = "Range: <br/>";
        for (int i = 0; i < time.getLength(); i++) {
            range += "Range " + i + ": " + time.getStart(i) + " - " + time.getEnd(i) + "<br/>";
        }
        logger.log(range, true);
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
                        fireError("ERROR: Media not supported!");
                        break;
                }
                break;
            case 13: // loading aborted
                fireDebug("Media loading aborted!");
                break;
        }
    }

    public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
        return addDomHandler(handler, MouseDownEvent.getType());
    }

    public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
        return addDomHandler(handler, MouseMoveEvent.getType());
    }

    public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
        return addDomHandler(handler, MouseUpEvent.getType());
    }

    public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
        return addDomHandler(handler, KeyDownEvent.getType());
    }

    public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
        return addDomHandler(handler, KeyUpEvent.getType());
    }

    public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
        return addDomHandler(handler, KeyPressEvent.getType());
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

    private class NativeWidget extends Widget {

        private Document _doc = Document.get();
        private Element e = _doc.createElement("video");

        public NativeWidget(String playerId, String mediaURL, boolean autoplay) {
            e.setId(playerId);
            e.setPropertyString("src", mediaURL);
            e.setPropertyBoolean("autoplay", autoplay);
            e.setPropertyBoolean("controls", true);
            setElement(e);
            setHeight("100%");
            setWidth("100%");
        }

        public NativeWidget(String playerId, ArrayList<MediaItem> sources, boolean autoplay) {
            e.setId(playerId);
            e.setPropertyBoolean("autoplay", autoplay);
            e.setPropertyBoolean("controls", true);

            for (MediaItem item : sources) {
                Element s = _doc.createElement("source");
                s.setAttribute("src", item.getSource());
                s.setAttribute("type", item.getType());
                e.appendChild(s);
            }
            setElement(e);
            setHeight("100%");
            setWidth("100%");
        }
    }
}
