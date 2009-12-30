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

import com.bramosystems.oss.player.core.event.client.PlayerStateHandler;
import com.bramosystems.oss.player.core.event.client.PlayerStateEvent;
import com.bramosystems.oss.player.core.event.client.MediaInfoEvent;
import com.bramosystems.oss.player.core.event.client.DebugEvent;
import com.bramosystems.oss.player.core.event.client.MediaInfoHandler;
import com.bramosystems.oss.player.core.event.client.DebugHandler;
import com.bramosystems.oss.player.core.client.*;
import com.bramosystems.oss.player.core.client.MediaInfo.MediaInfoKey;
import com.bramosystems.oss.player.core.client.impl.PlayerWidgetFactory;
import com.bramosystems.oss.player.core.client.impl.VLCPlayerImpl;
import com.bramosystems.oss.player.core.client.impl.VLCStateManager;
import com.bramosystems.oss.player.core.client.skin.CustomPlayerControl;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import java.util.ArrayList;

/**
 * Widget to embed VLC Media Player&trade; plugin.
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
public class VLCPlayer extends AbstractMediaPlayer implements PlaylistSupport {

    private VLCPlayerImpl impl;
    private Widget playerWidget;
    private VLCStateManager stateHandler;
    private String playerId,  mediaUrl,  _width,  _height;
    private Logger logger;
    private boolean isEmbedded,  autoplay,  resizeToVideoSize,  shuffleOn;
    private HandlerRegistration initListHandler;
    private ArrayList<MRL> _playlistCache;
    private CustomPlayerControl control;
    private DockPanel panel;

    VLCPlayer() throws PluginNotFoundException, PluginVersionException {
        PluginVersion req = Plugin.VLCPlayer.getVersion();
        PluginVersion v = PlayerUtil.getVLCPlayerPluginVersion();
        if (v.compareTo(req) < 0) {
            throw new PluginVersionException(req.toString(), v.toString());
        }

        _playlistCache = new ArrayList<MRL>();
        playerId = DOM.createUniqueId().replace("-", "");
        stateHandler = new VLCStateManager();
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

            control = new CustomPlayerControl(this);
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

        playerWidget = PlayerWidgetFactory.get().getPlayerWidget(Plugin.VLCPlayer, playerId, 
                null, false, null);
        panel.add(playerWidget, DockPanel.CENTER);
        panel.setCellHeight(playerWidget, _height);
        panel.setCellWidth(playerWidget, _width);
        setWidth(_width);
        DOM.setStyleAttribute(playerWidget.getElement(), "backgroundColor", "#000000");   // IE workaround
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
     * Overridden to register player for plugin events
     */
    @Override
    protected final void onLoad() {
        fixIEStyle(_height);
        impl = VLCPlayerImpl.getPlayer(playerId);

        fireDebug("VLC Media Player plugin");
        fireDebug("Version : " + impl.getPluginVersion());
        stateHandler.start(this, impl);   // start state pooling ...

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

    /**
     * @deprecated As of version 1.1. Remove widget from panel instead.
     */
    @Override
    public void close() {
        stateHandler.close();
        panel.remove(playerWidget);
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
        if (!isPlayerOnPage(playerId)) {
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
        if (isPlayerOnPage(playerId)) {
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
        if (isPlayerOnPage(playerId)) {
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
        checkAvailable();
        impl.toggleFullScreen();
    }

    public void setRate(double rate) {
        checkAvailable();
        impl.setRate(rate);
    }

    public double getRate() {
        checkAvailable();
        return impl.getRate();
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
        if (isPlayerOnPage(playerId)) {
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
        panel.setCellHeight(playerWidget, _h);
        fixIEStyle(_h);

        if (!_height.equals(_h) && !_width.equals(_w)) {
            firePlayerStateEvent(PlayerStateEvent.State.DimensionChangedOnVideo);
        }
    }

    private void fixIEStyle(String _h) {
        // IE workaround for style issues ...
        DOM.setStyleAttribute(playerWidget.getElement(), "height", _h);
//        DOM.setStyleAttribute(playerWidget.getElement(), "width", "100%");
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

    /**
     * An enum of Audio Channel modes for VLC Media Player&trade;
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

    public void doMouseEvents(MouseMoveHandler handler) {
        addDomHandler(handler, MouseMoveEvent.getType());
    }
}
