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

import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PluginVersion;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.MediaInfo;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.PlayException;
import com.bramosystems.oss.player.core.client.MediaStateListener;
import com.bramosystems.oss.player.core.client.MediaStateListenerAdapter;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.PlaylistSupport;
import com.bramosystems.oss.player.core.client.impl.VLCPlayerImpl;
import com.bramosystems.oss.player.core.client.skin.FlatCustomControl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import java.util.ArrayList;

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
    private String playerId,  playerDivId,  mediaUrl;
    private SimplePanel playerDiv;
    private Logger logger;
    private boolean isEmbedded,  autoplay,  isLoaded;
    private MediaStateListener _onInitLoopCountListener,  _onInitListListener,  _onInitSuffleListener;
    private ArrayList<String> _playlistCache;
    private FlatCustomControl control;

    VLCPlayer() throws PluginNotFoundException, PluginVersionException {
        PluginVersion v = PlayerUtil.getVLCPlayerPluginVersion();
        if (v.compareTo(0, 8, 6) < 0) {
            throw new PluginVersionException("0.8.6", v.toString());
        }

        if (impl == null) {
            impl = GWT.create(VLCPlayerImpl.class);
        }

        _playlistCache = new ArrayList<String>();
        playerId = DOM.createUniqueId().replace("-", "");
        isLoaded = false;
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

        impl.init(playerId, new MediaStateListener() {

            public void onPlayFinished() {
                firePlayFinished();
            }

            public void onLoadingComplete() {
                fireLoadingComplete();
            }

            public void onError(String description) {
                fireError(description);
            }

            public void onDebug(String message) {
                fireDebug(message);
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

        playerDivId = playerId + "_div";
        playerDiv = new SimplePanel();
        playerDiv.getElement().setId(playerDivId);

        VerticalPanel dp = new VerticalPanel();
        dp.add(playerDiv);

        isEmbedded = (height == null) || (width == null);
        if (!isEmbedded) {
            control = new FlatCustomControl(this);
            dp.add(control);

            logger = new Logger();
            logger.setVisible(false);
            dp.add(logger);
            addMediaStateListener(new MediaStateListenerAdapter() {

                @Override
                public void onError(String description) {
                    Window.alert(description);
                    logger.log(description, false);
                }

                @Override
                public void onDebug(String message) {
                    logger.log(message, false);
                }

                @Override
                public void onMediaInfoAvailable(MediaInfo info) {
                    logger.log(info.asHTMLString(), true);
                }
            });
        } else {
            height = "0px";
            width = "0px";
        }

        initWidget(dp);
        playerDiv.setHeight(height);
        setWidth(width);
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
        this(mediaURL, true, "1px", "100%");
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
        this(mediaURL, autoplay, "1px", "100%");
    }

    /**
     * Overridden to register player for plugin DOM events
     *
     */
    @Override
    protected final void onLoad() {
        Timer t = new Timer() {

            @Override
            public void run() {
                impl.injectScript(playerDivId, mediaUrl, playerId, autoplay,
                        playerDiv.getOffsetHeight(), playerDiv.getOffsetWidth());
                isLoaded = true;
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
        impl.close(playerId);
        playerDiv.getElement().setInnerText("");
    }

    public void loadMedia(String mediaURL) throws LoadException {
        checkAvailable();
        impl.loadSound(playerId, mediaURL);
    }

    public void playMedia() throws PlayException {
        checkAvailable();
        impl.playMedia(playerId);
    }

    public void stopMedia() {
        checkAvailable();
        impl.stop(playerId);
    }

    public void pauseMedia() {
        checkAvailable();
        impl.pause(playerId);
    }

    public void close() {
        impl.close(playerId);
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
        return impl.getLoopCount(playerId);
    }

    @Override
    public void setLoopCount(final int loop) {
        if (impl.isPlayerAvailable(playerId)) {
            impl.setLoopCount(playerId, loop);
        } else {
            if (containsMediaStateListener(_onInitLoopCountListener)) {
                // ensure only one instance is queued ...
                removeMediaStateListener(_onInitLoopCountListener);
            }
            _onInitLoopCountListener = new MediaStateListenerAdapter() {

                @Override
                public void onPlayerReady() {
                    impl.setLoopCount(playerId, loop);
                    removeMediaStateListener(_onInitLoopCountListener);
                }
            };
            addMediaStateListener(_onInitLoopCountListener);
        }
    }

    @Override
    public void addToPlaylist(final String mediaURL) {
        if (impl.isPlayerAvailable(playerId)) {
            impl.addToPlaylist(playerId, mediaURL);
        } else {
            if (!containsMediaStateListener(_onInitListListener)) {
                _onInitListListener = new MediaStateListenerAdapter() {

                    @Override
                    public void onPlayerReady() {
                        for (String url : _playlistCache) {
                            impl.addToPlaylist(playerId, url);
                        }
                        removeMediaStateListener(_onInitListListener);
                    }
                };
                addMediaStateListener(_onInitListListener);
            }
            _playlistCache.add(mediaURL);
        }
    }

    @Override
    public boolean isShuffleEnabled() {
        checkAvailable();
        return impl.isShuffleEnabled(playerId);
    }

    @Override
    public void removeFromPlaylist(int index) {
        checkAvailable();
        impl.removeFromPlaylist(playerId, index);
    }

    @Override
    public void setShuffleEnabled(final boolean enable) {
        if (impl.isPlayerAvailable(playerId)) {
            impl.setShuffleEnabled(playerId, enable);
        } else {
            if (containsMediaStateListener(_onInitSuffleListener)) {
                removeMediaStateListener(_onInitSuffleListener);
            }

            _onInitSuffleListener = new MediaStateListenerAdapter() {

                @Override
                public void onPlayerReady() {
                    impl.setShuffleEnabled(playerId, enable);
                    removeMediaStateListener(_onInitSuffleListener);
                }
            };
            addMediaStateListener(_onInitSuffleListener);
        }
    }

    public void clearPlaylist() {
    }

    public void playNext() {
    }

    public void playPrevious() {
    }
}
