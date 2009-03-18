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
package com.bramosystems.gwt.player.client.ui;

import com.bramosystems.gwt.player.client.*;
import com.bramosystems.gwt.player.client.impl.WinMediaPlayerImpl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

/**
 * Widget to embed Windows Media Player.
 *
 * <h3>Usage Example</h3>
 *
 * <p>
 * <code><pre>
 * SimplePanel panel = new SimplePanel();   // create panel to hold the player
 * Widget player = null;
 * try {
 *      // create the player
 *      player = new WinMediaPlayer("www.example.com/mediafile.wma");
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
public final class WinMediaPlayer extends AbstractMediaPlayer {

    private static WinMediaPlayerImpl impl;
    private String playerId,  uiMode = "full",  mediaURL;
    private HTML playerDiv;
    private Logger logger;
    private boolean isEmbedded,  isLoaded,  autoplay;

    WinMediaPlayer() {
        if (impl == null) {
            impl = GWT.create(WinMediaPlayerImpl.class);
        }
    }

    /**
     * Constructs <code>WinMediaPlayer</code> with the specified {@code height} and
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
     * @throws com.bramosystems.gwt.player.client.LoadException if an error occurs while loading the media.
     * @throws com.bramosystems.gwt.player.client.PluginVersionException if the required
     * Windows Media Player plugin version is not installed on the client.
     * @throws com.bramosystems.gwt.player.client.PluginNotFoundException if the Windows Media
     * Player plugin is not installed on the client.
     */
    public WinMediaPlayer(String mediaURL, boolean autoplay, String height, String width)
            throws LoadException, PluginNotFoundException, PluginVersionException {
        this();

        PluginVersion v = PlayerUtil.getWindowsMediaPlayerPluginVersion();
        if (v.compareTo(1, 1, 1) < 0) {
            throw new PluginVersionException("1, 1, 1", v.toString());
        }

        this.autoplay = autoplay;
        this.mediaURL = mediaURL;
        isLoaded = false;
        playerId = DOM.createUniqueId().replace("-", "");

        impl.init(playerId, new MediaStateListener() {

            public void onPlayStarted() {
                firePlayStarted();
            }

            public void onPlayFinished() {
                firePlayFinished();
            }

            public void onLoadingComplete() {
                fireLoadingComplete();
            }

            public void onLoadingProgress(double progress) {
                fireLoadingProgress(progress);
            }

            public void onError(String description) {
                fireError(description);
            }

            public void onDebug(String message) {
                fireDebug(message);
            }

            public void onPlayerReady() {
                firePlayerReady();
            }

            public void onMediaInfoAvailable(MediaInfo info) {
                fireMediaInfoAvailable(info);
            }
        });

        playerDiv = new HTML();
        playerDiv.setStyleName("");
        playerDiv.setHorizontalAlignment(HTML.ALIGN_CENTER);

        isEmbedded = (height == null) || (width == null);
        if (!isEmbedded) {
            logger = new Logger();
            logger.setVisible(false);
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
            width = "0px";
            height = "0px";
        }

        DockPanel dp = new DockPanel();
        if (!isEmbedded) {
            dp.add(logger, DockPanel.SOUTH);
        }
        dp.add(playerDiv, DockPanel.CENTER);

        initWidget(dp);
        playerDiv.setHeight(height);
        setWidth(width);
    }

    /**
     * Constructs <code>WinMediaPlayer</code> to automatically playback media located at
     * {@code mediaURL} using the default height of 50px and width of 100%.
     *
     * <p> This is the same as calling {@code WinMediaPlayer(mediaURL, true, "50px", "100%")}
     *
     * @param mediaURL the URL of the media to playback
     *
     * @throws com.bramosystems.gwt.player.client.LoadException if an error occurs while loading the media.
     * @throws com.bramosystems.gwt.player.client.PluginVersionException if the required
     * Windows Media Player plugin version is not installed on the client.
     * @throws com.bramosystems.gwt.player.client.PluginNotFoundException if the Windows Media
     * Player plugin is not installed on the client.
     */
    public WinMediaPlayer(String mediaURL) throws LoadException,
            PluginNotFoundException, PluginVersionException {
        this(mediaURL, true, "50px", "100%");
    }

    /**
     * Constructs <code>WinMediaPlayer</code> to playback media located at {@code mediaURL} using
     * the default height of 50px and width of 100%. Media playback begins automatically if
     * {@code autoplay} is {@code true}.
     *
     * <p> This is the same as calling {@code WinMediaPlayer(mediaURL, autoplay, "50px", "100%")}
     *
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     *
     * @throws com.bramosystems.gwt.player.client.LoadException if an error occurs while loading the media.
     * @throws com.bramosystems.gwt.player.client.PluginVersionException if the required
     * Windows Media Player plugin version is not installed on the client.
     * @throws com.bramosystems.gwt.player.client.PluginNotFoundException if the Windows Media
     * Player plugin is not installed on the client.
     */
    public WinMediaPlayer(String mediaURL, boolean autoplay) throws LoadException,
            PluginNotFoundException, PluginVersionException {
        this(mediaURL, autoplay, "50px", "100%");
    }

    /**
     * Overridden to register player for plugin DOM events
     */
    @Override
    protected final void onLoad() {
        Timer t = new Timer() {

            @Override
            public void run() {
                isLoaded = true;
                playerDiv.setHTML(impl.getPlayerScript(mediaURL, playerId, autoplay,
                        uiMode, playerDiv.getOffsetHeight(), playerDiv.getOffsetWidth()));
                impl.registerMediaStateListener(playerId);
            }
        };
        t.schedule(200);            // IE workarround...
    }

    /**
     * Overridden to remove player from browsers' DOM.
     */
    @Override
    protected void onUnload() {
        impl.close(playerId);
        playerDiv.setText("");
    }

    public void loadMedia(String mediaURL) throws LoadException {
        checkAvailable();
        impl.loadSound(playerId, mediaURL);
    }

    public void playMedia() throws PlayException {
        checkAvailable();
        impl.play(playerId);
    }

    public void stopMedia() {
        checkAvailable();
        impl.stop(playerId);
    }

    public void pauseMedia() {
        checkAvailable();
        impl.pause(playerId);
    }

    public void ejectMedia() {
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
        return impl.getCurrentPosition(playerId);
    }

    public void setPlayPosition(double position) {
        checkAvailable();
        impl.setCurrentPosition(playerId, position);
    }

    public double getVolume() {
        checkAvailable();
        return impl.getVolume(playerId) / (double) 100;
    }

    public void setVolume(double volume) {
        checkAvailable();
        volume *= 100;
        impl.setVolume(playerId, (int) volume);
        fireDebug("Volume set to " + ((int) volume) + "%");
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

    /**
     * Displays or hides the player controls.
     */
    @Override
    public void setControllerVisible(boolean show) {
        if (show) {
            uiMode = isEmbedded ? UI_MODE_INVISIBLE : UI_MODE_FULL;
        } else {
            uiMode = isEmbedded ? UI_MODE_INVISIBLE : UI_MODE_NONE;
        }

        if (isLoaded) {
            impl.setUIMode(playerId, uiMode);
        }
    }

    /**
     * Checks whether the player controls are visible.
     */
    @Override
    public boolean isControllerVisible() {
        return uiMode.equals(UI_MODE_FULL);
    }

    /**
     * Returns the remaining number of times this player loops playback before stopping.
     */
    @Override
    public int getLoopCount() {
        return impl.getLoopCount(playerId);
    }

    /**
     * Sets the number of times the current media file should loop playback before stopping.
     */
    @Override
    public void setLoopCount(int loop) {
        impl.setLoopCount(playerId, loop);
    }


    private final String UI_MODE_FULL = "full";
    private final String UI_MODE_NONE = "none";
    private final String UI_MODE_INVISIBLE = "invisible";
}
