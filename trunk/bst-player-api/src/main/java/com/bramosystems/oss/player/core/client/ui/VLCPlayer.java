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

import com.bramosystems.oss.player.core.event.client.*;
import com.bramosystems.oss.player.core.client.*;
import com.bramosystems.oss.player.core.client.MediaInfo.MediaInfoKey;
import com.bramosystems.oss.player.core.client.impl.VLCPlayerImpl;
import com.bramosystems.oss.player.core.client.skin.FlatCustomControl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Widget to embed VLC Player plugin.
 *
 * <h3>Usage Example</h3>
 *
 * <p>
 * <code><pre>
 * SimplePanel panel = new SimplePanel();   // create panel to hold the player
 * Widget player = null;
 * try {
 *      // create the player
 *      player = new VLCPlayer("www.example.com/mediafile.vob");
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
public final class VLCPlayer extends AbstractMediaPlayer implements PlaylistSupport {

    private static VLCPlayerImpl impl;
    private String playerId,  mediaUrl,  _width,  _height;
    private HTML playerDiv;
    private Logger logger;
    private boolean isEmbedded,  autoplay,  resizeToVideoSize;
    private HandlerRegistration initListHandler;
    private ArrayList<MRL> _playlistCache;
    private FlatCustomControl control;
    private DockPanel panel;
    private StateHandler stateHandler;

    VLCPlayer() throws PluginNotFoundException, PluginVersionException {
        PluginVersion req = Plugin.VLCPlayer.getVersion();
        PluginVersion v = PlayerUtil.getVLCPlayerPluginVersion();
        if (v.compareTo(req) < 0) {
            throw new PluginVersionException(req.toString(), v.toString());
        }

        if (impl == null) {
            impl = GWT.create(VLCPlayerImpl.class);
        }

        _playlistCache = new ArrayList<MRL>();
        playerId = DOM.createUniqueId().replace("-", "");
        stateHandler = new StateHandler();
    }

    /**
     * Constructs <code>VLCPlayer</code> with the specified {@code height} and
     * {@code width} to playback media located at {@code mediaURL}. Media playback
     * begins automatically if {@code autoplay} is {@code true}.
     *
     * <p> {@code height} and {@code width} are specified as CSS units. A value of {@code null}
     * for {@code height} or {@code width} puts the player in embedded mode.  When in embedded mode,
     * the player is made invisible on the page and media state events are propagated to registered 
     * listeners only.  This is desired especially when used with custom sound controls.  For custom
     * video control, specify valid CSS values for {@code height} and {@code width} but hide the
     * player controls with {@code setControllerVisible(false)}.
     *
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     * @param height the height of the player
     * @param width the width of the player.
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginVersionException if the required VLCPlayer plugin version is not installed on the client.
     * @throws PluginNotFoundException if the VLCPlayer plugin is not installed on the client.
     */
    public VLCPlayer(String mediaURL, boolean autoplay, String height, String width)
            throws LoadException, PluginVersionException, PluginNotFoundException {
        this();

        mediaUrl = mediaURL;
        this.autoplay = autoplay;
        _height = height;
        _width = width;

        panel = new DockPanel();
        panel.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
        panel.setStyleName("");
        panel.setWidth("100%");
        initWidget(panel);

        isEmbedded = (height == null) || (width == null);
        if (!isEmbedded) {
            logger = new Logger();
            logger.setVisible(false);
            panel.add(logger, DockPanel.SOUTH);

            control = new FlatCustomControl(this);
            panel.add(control, DockPanel.SOUTH);

            addDebugHandler(new DebugHandler() {

                public void onDebug(DebugEvent event) {
                    switch (event.getMessageType()) {
                        case Error:
                            Window.alert(event.getMessage());
                        case Info:
                            logger.log(event.getMessage(), false);
                    }
                }
            });
            addMediaInfoHandler(new MediaInfoHandler() {

                public void onMediaInfoAvailable(MediaInfoEvent event) {
                    MediaInfo info = event.getMediaInfo();
                    if (info.getAvailableItems().contains(MediaInfoKey.VideoHeight) ||
                            info.getAvailableItems().contains(MediaInfoKey.VideoWidth)) {
                        checkVideoSize(Integer.parseInt(info.getItem(MediaInfoKey.VideoHeight)) + 16,
                                Integer.parseInt(info.getItem(MediaInfoKey.VideoWidth)));
                    }
                    logger.log(event.getMediaInfo().asHTMLString(), true);
                }
            });
        } else {
            _height = "0px";
            _width = "0px";
        }

        playerDiv = new HTML();
        playerDiv.setStyleName("");
        playerDiv.setSize("100%", "100%");
        panel.add(playerDiv, DockPanel.CENTER);
        panel.setCellHeight(playerDiv, _height);
        panel.setCellWidth(playerDiv, _width);
        setWidth(_width);
    }

    /**
     * Constructs <code>VLCPlayer</code> to automatically playback media located at
     * {@code mediaURL} using the default height of 20px and width of 100%.
     *
     * @param mediaURL the URL of the media to playback
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginVersionException if the required VLCPlayer plugin version is not installed on the client.
     * @throws PluginNotFoundException if the VLCPlayer plugin is not installed on the client.
     *
     */
    public VLCPlayer(String mediaURL) throws LoadException, PluginVersionException,
            PluginNotFoundException {
        this(mediaURL, true, "0px", "100%");
    }

    /**
     * Constructs <code>VLCPlayer</code> to playback media located at {@code mediaURL}
     * using the default height of 20px and width of 100%. Media playback begins
     * automatically if {@code autoplay} is {@code true}.
     *
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginVersionException if the required VLCPlayer plugin version is not installed on the client.
     * @throws PluginNotFoundException if the VLCPlayer plugin is not installed on the client.
     */
    public VLCPlayer(String mediaURL, boolean autoplay) throws LoadException,
            PluginVersionException, PluginNotFoundException {
        this(mediaURL, autoplay, "0px", "100%");
    }

    /**
     * Overridden to register player for plugin DOM events
     */
    @Override
    protected final void onLoad() {
        Timer t = new Timer() {

            @Override
            public void run() {
                playerDiv.setHTML(impl.getPlayerScript(playerId,
                        playerDiv.getOffsetHeight(), playerDiv.getOffsetWidth()));
                stateHandler.init();
            }
        };
        t.schedule(500);            // IE workarround...
    }

    private void initComplete() {
        fireDebug("Plugin ready for media playback");
        impl.setLogLevel(playerId, MessageLevel.Info.ordinal());

        // load player ...
        stateHandler.addToPlaylist(mediaUrl, null);

        // fire player ready ...
        firePlayerStateEvent(PlayerStateEvent.State.Ready);

        // and play if required ...
        if (autoplay) {
            try {
                stateHandler.start();
            } catch (PlayException ex) {
            }
        }
    }

    /**
     * Subclasses that override this method should call <code>super.onUnload()</code>
     * to ensure the player is properly removed from the browser's DOM.
     *
     * Overridden to remove player from browsers' DOM.
     */
    @Override
    protected void onUnload() {
        close();
    }

    public void loadMedia(String mediaURL) throws LoadException {
        checkAvailable();
        stateHandler.clearPlaylist();
        stateHandler.addToPlaylist(mediaUrl, null);
    }

    public void playMedia() throws PlayException {
        checkAvailable();
        stateHandler.start();
    }

    public void play(int index) throws IndexOutOfBoundsException {
        checkAvailable();
        stateHandler.startAt(index);
    }

    public void playNext() throws PlayException {
        checkAvailable();
        stateHandler.next(true); // play next and start over if end-of-playlist
    }

    public void playPrevious() throws PlayException {
        checkAvailable();
        stateHandler.previous(true);
    }

    public void stopMedia() {
        checkAvailable();
        stateHandler.stop();
    }

    public void pauseMedia() {
        checkAvailable();
        impl.togglePause(playerId);
    }

    public void close() {
        stateHandler.close();
        playerDiv.getElement().setInnerText("");
    }

    public long getMediaDuration() {
        checkAvailable();
        return (long) impl.getDuration(playerId);
    }

    public double getPlayPosition() {
        checkAvailable();
        return impl.getTime(playerId);
    }

    public void setPlayPosition(double position) {
        checkAvailable();
        impl.setTime(playerId, position);
    }

    public double getVolume() {
        checkAvailable();
        return impl.getVolume(playerId);
    }

    public void setVolume(double volume) {
        checkAvailable();
        impl.setVolume(playerId, volume);
        fireDebug("Volume set to " + (volume * 100) + "%");
    }

    private void checkAvailable() {
        if (!impl.isPlayerAvailable(playerId)) {
            String message = "Player closed already, create another instance";
            fireDebug(message);
            throw new IllegalStateException(message);
        }
    }

    @Override
    public void showLogger(boolean enable) {
        if (!isEmbedded) {
            logger.setVisible(enable);
        }
    }

    @Override
    public void setControllerVisible(boolean show) {
        if (!isEmbedded) {
            control.setVisible(show);
        }
    }

    @Override
    public boolean isControllerVisible() {
        return control.isVisible();
    }

    @Override
    public int getLoopCount() {
        checkAvailable();
        return stateHandler.getLoopCount();
    }

    /**
     * Sets the number of times the current media file should repeat playback before stopping.
     *
     * <p>As of version 1.0, if this player is not available on the panel, this method
     * call is added to the command-queue for later execution.
     */
    @Override
    public void setLoopCount(final int loop) {
        if (impl.isPlayerAvailable(playerId)) {
            stateHandler.setLoopCount(loop);
        } else {
            addToPlayerReadyCommandQueue("loopcount", new Command() {

                public void execute() {
                    stateHandler.setLoopCount(loop);
                }
            });
        }
    }

    @Override
    public void addToPlaylist(String mediaURL) {
        if (impl.isPlayerAvailable(playerId)) {
            stateHandler.addToPlaylist(mediaURL, null);
        } else {
            if (initListHandler == null) {
                initListHandler = addPlayerStateHandler(new PlayerStateHandler() {

                    public void onPlayerStateChanged(PlayerStateEvent event) {
                        switch (event.getPlayerState()) {
                            case Ready:
                                for (MRL mrl : _playlistCache) {
                                    stateHandler.addToPlaylist(mrl.getUrl(), mrl.getOption());
                                }
                                break;
                        }
                        initListHandler.removeHandler();
                    }
                });
            }
            _playlistCache.add(new MRL(mediaURL, null));
        }
    }

    @Override
    public boolean isShuffleEnabled() {
        checkAvailable();
//        return impl.isShuffleEnabled(playerId);
        return false;
    }

    @Override
    public void setShuffleEnabled(final boolean enable) {
//        if (impl.isPlayerAvailable(playerId)) {
//            stateHandler.addToPlaylist(GWT.getModuleBaseURL() + "silence.mp3",
//                    enable ? ":loop" : ":no-loop");
//        } else {
//            _playlistCache.add(new MRL(GWT.getModuleBaseURL() + "silence.mp3",
//                    enable ? ":loop" : ":no-loop"));
//        }
    }

    @Override
    public void removeFromPlaylist(int index) {
        checkAvailable();
        stateHandler.removeFromPlaylist(index);
    }

    public void clearPlaylist() {
        checkAvailable();
        stateHandler.clearPlaylist();
    }

    public int getPlaylistSize() {
        checkAvailable();
        return impl.getPlaylistCount(playerId);
    }

    public void setAudioChannelMode(AudioChannelMode mode) {
        checkAvailable();
        impl.setAudioChannelMode(playerId, mode.ordinal());
    }

    public AudioChannelMode getAudioChannelMode() {
        checkAvailable();
        return AudioChannelMode.values()[impl.getAudioChannelMode(playerId)];
    }

    @Override
    public int getVideoHeight() {
        checkAvailable();
        return Integer.parseInt(impl.getVideoHeight(playerId));
    }

    @Override
    public int getVideoWidth() {
        checkAvailable();
        return Integer.parseInt(impl.getVideoWidth(playerId));
    }

    public void toggleFullScreen() {
        impl.toggleFullScreen(playerId);
    }
    /*
    public AspectRatio getAspectRatio() {
    checkAvailable();
    if (impl.hasVideo(playerId)) {
    return AspectRatio.parse(impl.getAspectRatio(playerId));
    } else {
    throw new IllegalStateException("No video input can be found");
    }
    }

    public void setAspectRatio(AspectRatio aspectRatio) {
    checkAvailable();
    if (impl.hasVideo(playerId)) {
    impl.setAspectRatio(playerId, aspectRatio.toString());
    } else {
    throw new IllegalStateException("No video input can be found");
    }
    }
     */

    @Override
    public void setResizeToVideoSize(boolean resize) {
        resizeToVideoSize = resize;
        if (impl.isPlayerAvailable(playerId)) {
            // if player is on panel now update its size, otherwise
            // allow it to be handled by the MediaInfoHandler...
            checkVideoSize(getVideoHeight(), getVideoWidth());
        }
    }

    @Override
    public boolean isResizeToVideoSize() {
        return resizeToVideoSize;
    }

    private void checkVideoSize(int vidHeight, int vidWidth) {
        String _h = _height, _w = _width;
        if (resizeToVideoSize) {
            if ((vidHeight > 0) && (vidWidth > 0)) {
                // adjust to video size ...
                fireDebug("Resizing Player : " + vidWidth + " x " + vidHeight);
                _h = vidHeight + "px";
                _w = vidWidth + "px";
            }
        }
        setWidth(_w);
        panel.setCellHeight(playerDiv, _h);
        panel.setCellWidth(playerDiv, _w);
        DOM.getElementById(playerId).setAttribute("width", _w);
        DOM.getElementById(playerId).setAttribute("height", _h);
    }

    private class MRL {

        private String url,  option;

        public MRL(String url, String option) {
            this.url = url;
            this.option = option;
        }

        public String getUrl() {
            return url;
        }

        public String getOption() {
            return option;
        }
    }

    private class StateHandler {

        // TODO: handle metadata firing for VLC
        private Timer statePooler;
        private final int poolerPeriod = 200;
        private int loopCount,  _loopCount,  previousState,  index;
        private boolean isBuffering,  stoppedByUser,  canDoMetadata;
        private ArrayList<Integer> playlistIndexCache;
        private PlayStateEvent.State currentState;

        public StateHandler() {
            loopCount = 1;
            _loopCount = 1;
            previousState = -10;
            stoppedByUser = false;
            canDoMetadata = true;

            statePooler = new Timer() {

                @Override
                public void run() {
                    checkPlayState();
                }
            };

            index = -1;
            playlistIndexCache = new ArrayList<Integer>();
            currentState = PlayStateEvent.State.Finished;
            addPlayStateHandler(new PlayStateHandler() {

                public void onPlayStateChanged(PlayStateEvent event) {
                    currentState = event.getPlayState();
                }
            });
        }

        public void init() {
            Timer t = new Timer() {

                @Override
                public void run() {
                    if (impl.isPlayerAvailable(playerId)) {
                        cancel();

                        // init complete ...
                        fireDebug("VLC Media Player plugin");
                        fireDebug("Version : " + impl.getPluginVersion(playerId));
                        statePooler.scheduleRepeating(poolerPeriod);
                        initComplete();
                    } else {
                        schedule(200);
                    }
                }
            };
            t.schedule(100);
        }

        /**
         * playback is finished, check if their is need to raise play-finished event
         */
        private void checkFinished() {
            try {
                next(false);    // move to next item in list
            } catch (PlayException ex) {
                try {
                    int _list = getPlaylistSize();
                    if (_loopCount > 1) {   // start over again ...
                        _loopCount--;
                        if (_list == 1) {
                            canDoMetadata = false;
                        }
                        next(true);
                    } else if (_loopCount < 0) {    // loop forever ...
                        if (_list == 1) {
                            canDoMetadata = false;
                        }
                        next(true);
                    } else {
                        firePlayStateEvent(PlayStateEvent.State.Finished, getCurrentIndex());
                        fireDebug("Media playback complete");
                    }
                } catch (PlayException ex1) {
                    logger.log(ex1.getMessage(), false);
                }
            }
        }

        private int checkPlayState() {
            int state = impl.getPlayerState(playerId);

            if (state == previousState) {
                return state;
            }

            switch (state) {
                case -1:   // no input yet...
                    break;
                case 0:    // idle/close
                      fireDebug("Idle ...");
                    break;
                case 6:    // finished
                    if (stoppedByUser) {
                        fireDebug("Media playback stopped");
                        firePlayStateEvent(PlayStateEvent.State.Stopped, getCurrentIndex());
                    } else {
                        fireDebug("Finished [6] ...");
                        checkFinished();
                    }
                    break;
                case 1:    // opening media
                    fireDebug("Opening media ...");
                    canDoMetadata = true;
                    break;
                case 2:    // buffering
                    fireDebug("Buffering started");
                    firePlayerStateEvent(PlayerStateEvent.State.BufferingStarted);
                    isBuffering = true;
                    break;
                case 3:    // playing
                    if (isBuffering) {
                        fireDebug("Buffering stopped");
                        firePlayerStateEvent(PlayerStateEvent.State.BufferingFinished);
                        isBuffering = false;
                    }

                    if (canDoMetadata) {
                        canDoMetadata = false;
                        MediaInfo info = new MediaInfo();
                        impl.fillMediaInfo(playerId, info);
                        fireMediaInfoAvailable(info);
                    }

//                    fireDebug("Current Track : " + getCurrentAudioTrack(id));
                    fireDebug("Media playback started");
                    stoppedByUser = false;
                    firePlayStateEvent(PlayStateEvent.State.Started,
                            getCurrentIndex());

//                    loadingComplete();
                    break;
                case 4:    // paused
                    fireDebug("Media playback paused");
                    firePlayStateEvent(PlayStateEvent.State.Paused,
                            getCurrentIndex());
                    break;
                case 5:    // stopping
                    fireDebug("Stopping [5] ...");
                    firePlayStateEvent(PlayStateEvent.State.Stopped,
                            getCurrentIndex());
                    break;
                case 7:    // error
                    break;
                case 8:    // playback complete
                    fireDebug("State [8] ...");
                    break;
            }
            previousState = state;
            dumpLog();
            return state;
        }

        private void loadingComplete() {
            fireLoadingProgress(1.0);
        }

        private void onError() {
            fireError("An error occured while loading media");
        }

        private void dumpLog() {
            Iterator<Message> logs = impl.getMessages(playerId).iterator();
            while (logs.hasNext()) {
                Message msg = logs.next();
                fireDebug("[VLC Log] - " + msg.getLevel() + " : " + msg.getModuleName() + " : " +
                        msg.getModuleType() + " : " + msg.getMessage());
            }
        }

        public int getLoopCount() {
            return loopCount;
        }

        public void setLoopCount(int loopCount) {
            this.loopCount = loopCount;
            _loopCount = loopCount;
            fireDebug("Loop Count set : " + loopCount);
        }

        public void close() {
            statePooler.cancel();
        }

        public void addToPlaylist(String mediaUrl, String options) {
            int _index = options == null ? impl.addToPlaylist(playerId, mediaUrl) : impl.addToPlaylist(playerId, mediaUrl, options);
            fireDebug("Added '" + mediaUrl + "' to playlist @ #" + _index +
                    (options == null ? "" : " with options [" + options + "]"));
            playlistIndexCache.add(_index);
            fireDebug("playlist size cache : " + playlistIndexCache.size());
            fireDebug("playlist size       : " + impl.getPlaylistCount(playerId));
        }

        public void removeFromPlaylist(int index) {
            impl.removeFromPlaylist(playerId, playlistIndexCache.get(index));
            playlistIndexCache.remove(index);
        }

        public void clearPlaylist() {
            impl.clearPlaylist(playerId);
            playlistIndexCache.clear();
        }

        public int getNextPlayIndex(boolean loop) throws PlayException {
            if (index >= (playlistIndexCache.size() - 1)) {
                index = -1;
                if (!loop) {
                    throw new PlayException("No more entries in playlist");
                }
            }
            return playlistIndexCache.get(++index);
        }

        public int getPreviousPlayIndex(boolean loop) throws PlayException {
            if (index < 0) {
                index = playlistIndexCache.size();
                if (!loop) {
                    throw new PlayException("Beginning of playlist reached");
                }
            }
            return playlistIndexCache.get(--index);
        }

        public int getPlayIndex(int _index) {
            return playlistIndexCache.get(_index);
        }

        public int getCurrentIndex() {
            return index;
        }

        public void start() throws PlayException {
            switch (currentState) {
                case Paused:
                    impl.togglePause(playerId);
                    break;
                case Finished:
                    impl.playMediaAt(playerId, getNextPlayIndex(false));
                    break;
                case Stopped:
                    impl.playMediaAt(playerId, getPlayIndex(index));
            }
        }

        public void startAt(int index) {
            switch (currentState) {
                case Started:
                case Paused:
                    stop();
                case Finished:
                case Stopped:
                    impl.playMediaAt(playerId, getPlayIndex(index));
            }
        }

        public void next(boolean canLoop) throws PlayException {
          impl.playMediaAt(playerId, getNextPlayIndex(canLoop));
        }

        public void previous(boolean canLoop) throws PlayException {
            impl.playMediaAt(playerId, getPreviousPlayIndex(canLoop));
        }

        public void stop() {
            stoppedByUser = true;
            impl.stop(playerId);
        }
    }

    public static enum AudioChannelMode {

        Disabled,
        Stereo,
        ReverseStereo,
        Left,
        Right,
        Dolby
    }

    public static class Message {

        private String moduleName,  moduleType,  message;
        private int severity;

        public Message() {
        }

        public String getMessage() {
            return message;
        }

        public String getModuleName() {
            return moduleName;
        }

        public String getModuleType() {
            return moduleType;
        }

        public MessageLevel getLevel() {
            return MessageLevel.values()[severity];
        }
    }

    public static enum MessageLevel {

        Info,
        Error,
        Warning,
        Debug
    }
}
