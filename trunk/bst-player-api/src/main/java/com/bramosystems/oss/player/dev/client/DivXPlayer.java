/*
 *  Copyright 2010 Sikiru Braheem
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.bramosystems.oss.player.dev.client;

import com.bramosystems.oss.player.core.client.*;
import com.bramosystems.oss.player.core.client.MediaInfo.MediaInfoKey;
import com.bramosystems.oss.player.core.client.impl.*;
import com.bramosystems.oss.player.core.client.ui.Logger;
import com.bramosystems.oss.player.core.event.client.*;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 *
 * @author Sikiru Braheem
 */
public class DivXPlayer extends AbstractMediaPlayer {

    private DivXStateManager manager;
    private DivXPlayerImpl impl;
    private PlayerWidget playerWidget;
    private boolean resizeToVideoSize, isEmbedded;
    private String playerId, _height, _width;
    private Logger logger;

    private DivXPlayer() throws PluginNotFoundException, PluginVersionException {
        PluginVersion req = Plugin.DivXPlayer.getVersion();
        PluginVersion v = PlayerUtil.getDivXPlayerPluginVersion();
        if (v.compareTo(req) < 0) {
            throw new PluginVersionException(req.toString(), v.toString());
        }

        playerId = DOM.createUniqueId().replace("-", "");
        manager = new DivXStateManager();
        manager.init(playerId, new DivXStateManager.StateCallback() {

            public void onStatusChanged(int statusId) {
                fireDebug("Status Changed : " + statusId);
                switch (statusId) {
                    case 0: // INIT_DONE
//                fireDebug("DivX Web Player plugin");
//                fireDebug("Version : " + impl.getPluginVersion());
//          impl = DivXPlayerImpl.getPlayer(playerId);
//        impl.setSize(getOffsetWidth(), getOffsetHeight());
                        firePlayerStateEvent(PlayerStateEvent.State.Ready);
                        break;
                    case 1: // OPEN_DONE - media info available
                        fireMediaInfoAvailable(manager.getFilledMediaInfo(impl.getMediaDuration(),
                                impl.getVideoWidth(), impl.getVideoHeight()));
                        break;
                    case 2: // VIDEO_END
                        firePlayStateEvent(PlayStateEvent.State.Finished, 0);
                        break;
                    case 10: // STATUS_PLAYING
                        firePlayStateEvent(PlayStateEvent.State.Started, 0);
                        break;
                    case 11: // STATUS_PAUSED
                        firePlayStateEvent(PlayStateEvent.State.Paused, 0);
                        break;
                    case 14: // STATUS_STOPPED
                        firePlayStateEvent(PlayStateEvent.State.Stopped, 0);
                        break;
                    case 15: // BUFFERING_START
                        firePlayerStateEvent(PlayerStateEvent.State.BufferingStarted);
                        break;
                    case 16: // BUFFERING_STOP
                        firePlayerStateEvent(PlayerStateEvent.State.BufferingFinished);
                        break;
                    case 17: // DOWNLOAD_START
                        fireLoadingProgress(0);
                        break;
                    case 19: // DOWNLOAD_DONE
                        fireLoadingProgress(1.0);
                        break;
                    case 3: // SHUT_DONE
                    case 4: // EMBEDDED_START
                    case 5: // EMBEDDED_END
                    case 6: // WINDOWED_START
                    case 7: // WINDOWED_END
                    case 8: // FULLSCREEN_START
                    case 9: // FULLSCREEN_END
                    case 12: // STATUS_FF
                    case 13: // STATUS_RW
                    case 18: // DOWNLOAD_FAILED
                    default:
                        fireDebug("Status Changed : " + statusId);
                }
            }

            public void onLoadingChanged(double current, double total) {
                fireLoadingProgress(current / total);
            }
        });
    }

    public DivXPlayer(String mediaURL, boolean autoplay, String height, String width)
            throws LoadException, PluginNotFoundException, PluginVersionException {
        this();

        _height = height;
        _width = width;

        isEmbedded = (height == null) || (width == null);
        if (isEmbedded) {
            _height = "0px";
            _width = "0px";
        }

        playerWidget = new PlayerWidget(Plugin.DivXPlayer, playerId, mediaURL,
                autoplay, new BeforeUnloadCallback() {

            public void onBeforeUnload() {
            }
        });
        playerWidget.addParam("statusCallback", "bstDivXStateChanged_" + playerId);
        playerWidget.addParam("downloadCallback", "bstDivXDownloadState_" + playerId);

        FlowPanel panel = new FlowPanel();
        panel.add(playerWidget);

        if (!isEmbedded) {
            logger = new Logger();
            logger.setVisible(false);
            panel.add(logger);

            addDebugHandler(new DebugHandler() {

                public void onDebug(DebugEvent event) {
                    logger.log(event.getMessage(), false);
                }
            });
            addMediaInfoHandler(new MediaInfoHandler() {

                public void onMediaInfoAvailable(MediaInfoEvent event) {
                    logger.log(event.getMediaInfo().asHTMLString(), true);
                    MediaInfo info = event.getMediaInfo();
                    if (info.getAvailableItems().contains(MediaInfoKey.VideoHeight)
                            || info.getAvailableItems().contains(MediaInfoKey.VideoWidth)) {
                        checkVideoSize(Integer.parseInt(info.getItem(MediaInfoKey.VideoHeight)),
                                Integer.parseInt(info.getItem(MediaInfoKey.VideoWidth)));
                    }
                }
            });
        }

        initWidget(panel);
    }

    private boolean isAvailable() {
        return isPlayerOnPage(playerId);// && stateManager.isPlayerStateManaged(playerId);
    }

    private void checkAvailable() {
        if (!isAvailable()) {
            String message = "Player not available, create an instance";
            fireDebug(message);
            throw new IllegalStateException(message);
        }
    }

    private void checkVideoSize(int vidHeight, int vidWidth) {
        String _h = _height, _w = _width;
        if (vidHeight == 0) {
            _h = "0px"; // suppress SWF app height for audio files ...
        }

        if (resizeToVideoSize) {
            if ((vidHeight > 0) && (vidWidth > 0)) {
                fireDebug("Resizing Player : " + vidWidth + " x " + vidHeight);
                _h = vidHeight + "px";
                _w = vidWidth + "px";
            }
        }

        playerWidget.setSize(_w, _h);
        impl.setSize(playerWidget.getOffsetWidth(), playerWidget.getOffsetHeight());
        setSize(_w, _h);

        if (!_height.equals(_h) && !_width.equals(_w)) {
            firePlayerStateEvent(PlayerStateEvent.State.DimensionChangedOnVideo);
        }
    }

    /**
     * Overridden to register player for plugin DOM events
     */
    @Override
    protected final void onLoad() {
        playerWidget.setSize(_width, _height);
        impl = DivXPlayerImpl.getPlayer(playerId);
//        impl.setSize(getOffsetWidth(), getOffsetHeight());
        fireDebug("DivX Web Player plugin");
        fireDebug("Version : " + impl.getPluginVersion());
        setWidth(_width);
        firePlayerStateEvent(PlayerStateEvent.State.Ready);
    }

    public void loadMedia(String mediaURL) throws LoadException {
        checkAvailable();
        impl.loadMedia(mediaURL);
    }

    public void playMedia() throws PlayException {
        checkAvailable();
        impl.playMedia();
    }

    public void stopMedia() {
        checkAvailable();
//        stateManager.stop(playerId);
        impl.stopMedia();
    }

    public void pauseMedia() {
        checkAvailable();
        impl.pauseMedia();
    }

    /**
     * @deprecated As of version 1.1, remove player from panel instead
     */
    @Override
    public void close() {
//        stateManager.close(playerId);
//        impl.close();
    }

    public long getMediaDuration() {
        checkAvailable();
        return (long) impl.getMediaDuration();
    }

    public double getPlayPosition() {
        checkAvailable();
        return 0;//impl.getCurrentPosition();
    }

    public void setPlayPosition(double position) {
        checkAvailable();
//        impl.setCurrentPosition(position);
    }

    public double getVolume() {
        checkAvailable();
        return 0; //impl.getVolume() / (double) 100;
    }

    public void setVolume(double volume) {
        checkAvailable();
        volume *= 100;
        impl.setVolume((int) volume);
        fireDebug("Volume set to " + ((int) volume) + "%");
    }

    @Override
    public void showLogger(boolean enable) {
        if (!isEmbedded) {
            logger.setVisible(enable);
        }
    }

    /**
     * Displays or hides the player controls.
     */
    @Override
    public void setControllerVisible(boolean show) {
    }

    /**
     * Checks whether the player controls are visible.
     */
    @Override
    public boolean isControllerVisible() {
        return false;
    }

    /**
     * Returns the number of times this player repeats playback before stopping.
     */
    @Override
    public int getLoopCount() {
        checkAvailable();
        return 0;//impl.getLoopCount();
    }

    /**
     * Sets the number of times the current media file should repeat playback before stopping.
     *
     * <p>As of version 1.0, if this player is not available on the panel, this method
     * call is added to the command-queue for later execution.
     */
    @Override
    public void setLoopCount(final int loop) {
        if (isAvailable()) {
//            impl.setLoopCount(loop);
        } else {
            addToPlayerReadyCommandQueue("loopcount", new Command() {

                public void execute() {
                    //                   impl.setLoopCount(loop);
                }
            });
        }
    }

    @Override
    public int getVideoHeight() {
        checkAvailable();
        return impl.getVideoHeight();
    }

    @Override
    public int getVideoWidth() {
        checkAvailable();
        return impl.getVideoWidth();
    }

    @Override
    public void setResizeToVideoSize(boolean resize) {
        resizeToVideoSize = resize;
    }

    @Override
    public boolean isResizeToVideoSize() {
        return resizeToVideoSize;
    }

    /**
     * Specifies whether the player should display the DivX advertisement banner
     * at the end of playback.
     *
     * <p>If this player is not available on the panel, this method
     * call is added to the command-queue for later execution.
     *
     * @param enable {@code true} to enable, {@code false} otherwise
     */
    public void setBannerEnabled(final boolean enable) {
        if (isAvailable()) {
            impl.setBannerEnabled(enable);
        } else {
            addToPlayerReadyCommandQueue("banner", new Command() {

                public void execute() {
                    impl.setBannerEnabled(enable);
                }
            });
        }
    }

    /**
     * Specify whether the player should display a contextual (right-click) menu
     * when the user presses the right mouse button or the menu buttons on the skin.
     *
     * <p>If this player is not available on the panel, this method
     * call is added to the command-queue for later execution.
     *
     * @param allow {@code true} to allow, {@code false} otherwise
     */
    public void setAllowContextMenu(final boolean allow) {
        if (isAvailable()) {
            impl.setAllowContextMenu(allow);
        } else {
            addToPlayerReadyCommandQueue("context", new Command() {

                public void execute() {
                    impl.setAllowContextMenu(allow);
                }
            });
        }
    }

    /**
     * Specify how the player should buffer downloaded data before attempting
     * to start playback.
     *
     * <p>If this player is not available on the panel, this method
     * call is added to the command-queue for later execution.
     *
     * @param mode the mode
     */
    public void setBufferingMode(final BufferingMode mode) {
        if (isAvailable()) {
            impl.setBufferingMode(mode.name().toLowerCase());
        } else {
            addToPlayerReadyCommandQueue("buffering", new Command() {

                public void execute() {
                    impl.setBufferingMode(mode.name().toLowerCase());
                }
            });
        }
    }

    /**
     * Specifies which skin mode the player should use to display playback controls.
     *
     * <p>If this player is not available on the panel, this method
     * call is added to the command-queue for later execution.
     *
     * @param mode the display mode
     */
    public void setDisplayMode(final DisplayMode mode) {
        if (isAvailable()) {
            impl.setMode(mode.name().toLowerCase());
        } else {
            addToPlayerReadyCommandQueue("displayMode", new Command() {

                public void execute() {
                    impl.setMode(mode.name().toLowerCase());
                }
            });
        }
    }

    public static enum SeekMethod {

        DOWN, UP, DRAG
    }

    /**
     * An enum of buffering modes.  The mode is used to specify how the DivX Web Player should
     * buffer downloaded data before attempting to start playback.
     */
    public static enum BufferingMode {

        /**
         * The player does only very minimal buffering and starts playing as soon as data is available.
         * This mode does not guarantee a very good user experience unless on a very fast internet connection.
         */
        NULL,
        /**
         * The player analyses the download speed and tries to buffer just enough so
         * that uninterrupted progressive playback can happen at the end of the buffer period.
         */
        AUTO,
        /**
         * The player will always buffer the full video file before starting playback.
         */
        FULL
    }

    /**
     * An enum of display modes.  The display mode is used to specify which skin mode
     * the plugin should use to display playback controls
     */
    public static enum DisplayMode {

        /**
         * The player shows absolutely no controls.
         */
        NULL,
        /**
         * The player only shows a small floating controls bar in the bottom left corner of
         * the allocated video area.
         */
        ZERO,
        /**
         * The player shows a more elaborate control bar at the bottom of the video area.
         */
        MINI,
        /**
         * The player shows a complete control bar at the bottom of the video area
         */
        LARGE,
        /**
         * The player displays the complete control bar at the bottom of the video area and
         * an additional control bar at the top of the video area
         */
        FULL
    }
}
