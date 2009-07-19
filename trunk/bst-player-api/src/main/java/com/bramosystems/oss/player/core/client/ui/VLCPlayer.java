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
import com.bramosystems.oss.player.core.client.impl.PlayerScriptUtil;
import com.bramosystems.oss.player.core.client.impl.VLCPlayerImpl;
import com.bramosystems.oss.player.core.client.skin.FlatCustomControl;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

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

    private VLCPlayerImpl impl;
    private String playerId,  mediaUrl,  _width,  _height;
    private HTML playerDiv;
    private Logger logger;
    private boolean isEmbedded,  autoplay,  resizeToVideoSize,  shuffleOn;
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

        _playlistCache = new ArrayList<MRL>();
        playerId = DOM.createUniqueId().replace("-", "");
        stateHandler = new StateHandler();
        shuffleOn = false;
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
     * @param autoplay {@code true} to play playing automatically, {@code false} otherwise
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
                        checkVideoSize(Integer.parseInt(info.getItem(MediaInfoKey.VideoHeight)),
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
     * @param autoplay {@code true} to play playing automatically, {@code false} otherwise
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
                playerDiv.setHTML(PlayerScriptUtil.getVLCPlayerScript(playerId,
                        playerDiv.getOffsetHeight(), playerDiv.getOffsetWidth()));
                Timer tt = new Timer() {

                    @Override
                    public void run() {
                        if (isPlayerAvailable(playerId)) {
                            cancel();
                            impl = VLCPlayerImpl.getPlayer(playerId);

                            fireDebug("VLC Media Player plugin");
                            fireDebug("Version : " + impl.getPluginVersion());
                            stateHandler.start();   // start state pooling ...

                            // load player ...
                            stateHandler.addToPlaylist(mediaUrl, null);

                            // fire player ready ...
                            firePlayerStateEvent(PlayerStateEvent.State.Ready);

                            // and play if required ...
                            if (autoplay) {
                                try {
                                    stateHandler.play();
                                } catch (PlayException ex) {
                                }
                            }
                        } else {
                            schedule(200);
                        }
                    }
                };
                tt.schedule(100);
            }
        };
        t.schedule(500);            // IE workarround...
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
        stateHandler.play();
    }

    public void play(int index) throws IndexOutOfBoundsException {
        checkAvailable();
        stateHandler.playItemAt(index);
    }

    public void playNext() throws PlayException {
        checkAvailable();
        stateHandler.next(true); // play next and play over if end-of-playlist
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
        impl.togglePause();
    }

    public void close() {
        stateHandler.close();
        playerDiv.getElement().setInnerText("");
    }

    public long getMediaDuration() {
        checkAvailable();
        return (long) impl.getDuration();
    }

    public double getPlayPosition() {
        checkAvailable();
        return impl.getTime();
    }

    public void setPlayPosition(double position) {
        checkAvailable();
        impl.setTime(position);
    }

    public double getVolume() {
        checkAvailable();
        return impl.getVolume();
    }

    public void setVolume(double volume) {
        checkAvailable();
        impl.setVolume(volume);
        fireDebug("Volume set to " + (volume * 100) + "%");
    }

    private void checkAvailable() {
        if (!isPlayerAvailable(playerId)) {
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
        if (isPlayerAvailable(playerId)) {
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
        if (isPlayerAvailable(playerId)) {
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
        return shuffleOn;
    }

    @Override
    public void setShuffleEnabled(boolean enable) {
        shuffleOn = enable;
        if (enable) {
            stateHandler.shuffle();
        }
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
        return impl.getPlaylistCount();
    }

    /**
     * Sets the audio channel mode of the player
     * 
     * <p>Use {@linkplain #getAudioChannelMode()} to check if setting of the audio channel
     * is succeessful
     *
     * @param mode the audio channel mode
     * @see #getAudioChannelMode()
     */
    public void setAudioChannelMode(AudioChannelMode mode) {
        checkAvailable();
        impl.setAudioChannelMode(mode.ordinal() + 1);
    }

    /**
     * Gets the current audio channel mode of the player
     *
     * @return the current mode of the audio channel
     * @see #setAudioChannelMode(AudioChannelMode)
     */
    public AudioChannelMode getAudioChannelMode() {
        checkAvailable();
        return AudioChannelMode.values()[impl.getAudioChannelMode() - 1];
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

    public void toggleFullScreen() {
        impl.toggleFullScreen();
    }

    /*
     * TODO:// check up aspect ration later...
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
        if (isPlayerAvailable(playerId)) {
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

        private Timer statePooler;
        private final int poolerPeriod = 200;
        private int loopCount,  _loopCount,  previousState,  index,  metaDataWaitCount;
        private boolean isBuffering,  stoppedByUser,  canDoMetadata;
        private ArrayList<Integer> playlistIndexCache;
        private ArrayList<Integer> shuffledIndexCache;
        private PlayStateEvent.State currentState;

        public StateHandler() {
            loopCount = 1;
            _loopCount = 1;
            previousState = -10;
            stoppedByUser = false;
            canDoMetadata = true;
            metaDataWaitCount = -1;

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

        public void start() {
            statePooler.scheduleRepeating(poolerPeriod);
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
                    if (_loopCount > 1) {   // play over again ...
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
                        firePlayStateEvent(PlayStateEvent.State.Finished, index);
                        fireDebug("Media playback complete");
                    }
                } catch (PlayException ex1) {
                    logger.log(ex1.getMessage(), false);
                }
            }
        }

        private void checkPlayState() {
            int state = impl.getPlayerState();

            if (state == previousState) {
                if (metaDataWaitCount >= 0) {
                    metaDataWaitCount--;
                    if (metaDataWaitCount < 0) {
                        MediaInfo info = new MediaInfo();
                        impl.fillMediaInfo(info);
                        fireMediaInfoAvailable(info);
                    }
                }
                return;
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
                        firePlayStateEvent(PlayStateEvent.State.Stopped, index);
                    } else {
                        fireDebug("Finished playlist item playback #" + index);
                        checkFinished();
                    }
                    break;
                case 1:    // opening media
                    fireDebug("Opening playlist item #" + index);
                    canDoMetadata = true;
                //                    break;
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
                        metaDataWaitCount = 4;  // implement some kind of delay, no extra timers...
                    }

//                    fireDebug("Current Track : " + getCurrentAudioTrack(id));
                    fireDebug("Media playback started");
                    stoppedByUser = false;
                    firePlayStateEvent(PlayStateEvent.State.Started, index);
                    //                    loadingComplete();
                    break;
                case 4:    // paused
                    fireDebug("Media playback paused");
                    firePlayStateEvent(PlayStateEvent.State.Paused, index);
                    break;
                case 5:    // stopping
                    firePlayStateEvent(PlayStateEvent.State.Stopped, index);
                    break;
                case 7:    // error
                    break;
                case 8:    // playback complete
                    break;
            }
            previousState = state;
        }

        private void loadingComplete() {
            fireLoadingProgress(1.0);
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

        public void shuffle() {
            Integer[] shuffled = playlistIndexCache.toArray(new Integer[0]);
            Arrays.sort(shuffled, new Comparator<Integer>() {

                public int compare(Integer o1, Integer o2) {
                    int pos = 0;
                    switch (Math.round((float) (Math.random() * 2.0))) {
                        case 0:
                            pos = -1;
                            break;
                        case 1:
                            pos = 0;
                            break;
                        case 2:
                            pos = 1;
                    }
                    return pos;
                }
            });
            shuffledIndexCache = new ArrayList<Integer>();
            for (Integer _index : shuffled) {
                shuffledIndexCache.add(_index);
            }
        }

        public void addToPlaylist(String mediaUrl, String options) {
            int _index = options == null ? impl.addToPlaylist(mediaUrl) : impl.addToPlaylist(mediaUrl, options);
            fireDebug("Added '" + mediaUrl + "' to playlist @ #" + _index +
                    (options == null ? "" : " with options [" + options + "]"));
            playlistIndexCache.add(_index);
            if (shuffleOn) {
                shuffle();
            }
        }

        public void removeFromPlaylist(int index) {
            impl.removeFromPlaylist(playlistIndexCache.get(index));
            playlistIndexCache.remove(index);
            if (shuffleOn) {
                shuffle();
            }
        }

        public void clearPlaylist() {
            impl.clearPlaylist();
            playlistIndexCache.clear();
        }

        private int getNextPlayIndex(boolean loop) throws PlayException {
            if (index >= (playlistIndexCache.size() - 1)) {
                index = -1;
                if (!loop) {
                    throw new PlayException("No more entries in playlist");
                }
            }
            return shuffleOn ? shuffledIndexCache.get(++index) : playlistIndexCache.get(++index);
        }

        private int getPreviousPlayIndex(boolean loop) throws PlayException {
            if (index < 0) {
                index = playlistIndexCache.size();
                if (!loop) {
                    throw new PlayException("Beginning of playlist reached");
                }
            }
            return shuffleOn ? shuffledIndexCache.get(--index) : playlistIndexCache.get(--index);
        }

        public void play() throws PlayException {
            switch (currentState) {
                case Paused:
                    impl.togglePause();
                    break;
                case Finished:
                    impl.playMediaAt(getNextPlayIndex(true));
                    break;
                case Stopped:
                    impl.playMediaAt(
                            shuffleOn ? shuffledIndexCache.get(index) : playlistIndexCache.get(index));
            }
        }

        public void playItemAt(int _index) {
            switch (currentState) {
                case Started:
                case Paused:
                    stop();
                case Finished:
                case Stopped:
                    impl.playMediaAt(playlistIndexCache.get(_index));
            }
        }

        public void next(boolean canLoop) throws PlayException {
            impl.playMediaAt(getNextPlayIndex(canLoop));
        }

        public void previous(boolean canLoop) throws PlayException {
            impl.playMediaAt(getPreviousPlayIndex(canLoop));
        }

        public void stop() {
            stoppedByUser = true;
            impl.stop();
        }
    }

    /**
     * An enum of Audio Channel modes for VLC Media Player
     */
    public static enum AudioChannelMode {

        /**
         * Stereo mode
         */
        Stereo,

        /**
         * Reverse Stereo mode
         */
        ReverseStereo,

        /**
         * Left only mode
         */
        Left,

        /**
         * Right only mode
         */
        Right,

        /**
         * Dolby mode
         */
        Dolby
    }
}
