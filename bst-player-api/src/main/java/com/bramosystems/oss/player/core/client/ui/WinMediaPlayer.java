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

import com.bramosystems.oss.player.core.client.*;
import com.bramosystems.oss.player.core.client.impl.WinMediaPlayerImpl;
import com.bramosystems.oss.player.core.event.client.DebugEvent;
import com.bramosystems.oss.player.core.event.client.DebugHandler;
import com.bramosystems.oss.player.core.event.client.MediaInfoEvent;
import com.bramosystems.oss.player.core.event.client.MediaInfoHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
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
    private boolean isEmbedded, autoplay;

    private WinMediaPlayer() throws PluginNotFoundException, PluginVersionException {
        PluginVersion v = PlayerUtil.getWindowsMediaPlayerPluginVersion();
        if (v.compareTo(1, 1, 1) < 0) {
            throw new PluginVersionException("1.1.1", v.toString());
        }

        if (impl == null) {
            impl = GWT.create(WinMediaPlayerImpl.class);
        }

        playerId = DOM.createUniqueId().replace("-", "");
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
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginVersionException if the required Windows Media Player plugin version is not installed on the client.
     * @throws PluginNotFoundException if the Windows Media Player plugin is not installed on the client.
     */
    public WinMediaPlayer(String mediaURL, boolean autoplay, String height, String width)
            throws LoadException, PluginNotFoundException, PluginVersionException {
        this();

        this.autoplay = autoplay;
        this.mediaURL = mediaURL;

        impl.init(playerId, new MediaStateListenerAdapter(), this);

        DockPanel dp = new DockPanel();

        isEmbedded = (height == null) || (width == null);
        if (!isEmbedded) {
            logger = new Logger();
            logger.setVisible(false);
            dp.add(logger, DockPanel.SOUTH);

            addDebugHandler(new DebugHandler() {

                public void onDebug(DebugEvent event) {
                    switch(event.getMessageType()) {
                        case Error:
                            Window.alert(event.getMessage());
                        case Info:
                            logger.log(event.getMessage(), false);
                    }
                }
            });
            addMediaInfoHandler(new MediaInfoHandler() {

                public void onMediaInfoAvailable(MediaInfoEvent event) {
                    logger.log(event.getMediaInfo().asHTMLString(), true);
                }
            });
        } else {
            width = "0px";
            height = "0px";
        }

        playerDiv = new HTML();
        playerDiv.setStyleName("");
        playerDiv.setHorizontalAlignment(HTML.ALIGN_CENTER);
        playerDiv.setHeight(height);
        dp.add(playerDiv, DockPanel.CENTER);

        initWidget(dp);
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
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginVersionException if the required Windows Media Player plugin version is not installed on the client.
     * @throws PluginNotFoundException if the Windows Media Player plugin is not installed on the client.
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
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginVersionException if the required Windows Media Player plugin version is not installed on the client.
     * @throws PluginNotFoundException if the Windows Media Player plugin is not installed on the client.
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

        if (impl.isPlayerAvailable(playerId)) {
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
     * Returns the number of times this player repeats playback before stopping.
     */
    @Override
    public int getLoopCount() {
        checkAvailable();
        return impl.getLoopCount(playerId);
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
            impl.setLoopCount(playerId, loop);
        } else {
            addToPlayerReadyCommandQueue("loopcount", new Command() {

                public void execute() {
                    impl.setLoopCount(playerId, loop);
                }
            });
        }
    }

    /**
     * Retrieves the current media library access rights of the player
     *
     * @return current media access rights
     * @since 1.0
     */
    public MediaAccessRights getMediaAccessRights() {
        checkAvailable();
        return MediaAccessRights.valueOf(impl.getMediaAccessRight(playerId).toLowerCase());
    }

    /**
     * Sets the media library access rights level of the player.
     *
     * <p>If the player is not attached to a player, this call is scheduled for execution
     * when the underlying plugin is initialized.
     *
     * <p><em>Note:</em> The request succeeds IF AND ONLY IF the user grants this permission.
     * <p><em>Note:</em> This operation ALWAYS FAIL on browsers using the Windows Media Player
     * plugin for Firefox.
     *
     * @param accessRights access level
     * @since 1.0
     */
    public void setMediaAccessRights(MediaAccessRights accessRights) {
        if(impl.isPlayerAvailable(playerId)) {
            impl.requestMediaAccessRight(playerId, accessRights.name().toLowerCase());
        } else {
            impl.requestAccessRightsOnInit(playerId, accessRights.name().toLowerCase());
        }
    }

    /**
     * Specifies the Library Access levels of the Windows Media Player plugin.
     *
     * @since 1.0
     * @author Sikirulai Braheem
     */
    public static enum MediaAccessRights {
        /**
         * No access
         */
        NONE,

        /**
         * Read only access
         */
        READ,

        /**
         * Read/write access
         */
        FULL
    }

    private final String UI_MODE_FULL = "full";
    private final String UI_MODE_NONE = "none";
    private final String UI_MODE_INVISIBLE = "invisible";
}
