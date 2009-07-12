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
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.PlayException;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.impl.QuickTimePlayerImpl;
import com.bramosystems.oss.player.core.event.client.DebugEvent;
import com.bramosystems.oss.player.core.event.client.DebugHandler;
import com.bramosystems.oss.player.core.event.client.MediaInfoEvent;
import com.bramosystems.oss.player.core.event.client.MediaInfoHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;

/**
 * Widget to embed QuickTime plugin.
 *
 * <h3>Usage Example</h3>
 *
 * <p>
 * <code><pre>
 * SimplePanel panel = new SimplePanel();   // create panel to hold the player
 * Widget player = null;
 * try {
 *      // create the player
 *      player = new QuickTimePlayer("www.example.com/mediafile.mov");
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
//TODO: fix sturborn pixel for custom players
public final class QuickTimePlayer extends AbstractMediaPlayer {

    private static QuickTimePlayerImpl impl;
    private String playerId,  mediaUrl;
    private HTML playerDiv;
    private Logger logger;
    private boolean isEmbedded,  autoplay;

    private QuickTimePlayer() throws PluginNotFoundException, PluginVersionException {
        PluginVersion v = PlayerUtil.getQuickTimePluginVersion();
        if (v.compareTo(7, 2, 1) < 0) {
            throw new PluginVersionException("7.2.1", v.toString());
        }

        if (impl == null) {
            impl = GWT.create(QuickTimePlayerImpl.class);
        }

        playerId = DOM.createUniqueId().replace("-", "");
    }

    /**
     * Constructs <code>QuickTimePlayer</code> with the specified {@code height} and
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
     * @throws PluginVersionException if the required QuickTime plugin version is not installed on the client.
     * @throws PluginNotFoundException if the QuickTime plugin is not installed on the client.
     */
    public QuickTimePlayer(String mediaURL, boolean autoplay, String height, String width)
            throws LoadException, PluginVersionException, PluginNotFoundException {
        this();

        mediaUrl = mediaURL;
        this.autoplay = autoplay;

        impl.init(playerId, this);
/*
        impl.init(playerId, new MediaStateListener() {

            public void onPlayFinished() {
                firePlayFinished(0);
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
                firePlayStarted(0);
            }

            public void onPlayerReady() {
                firePlayerReady();
            }

            public void onMediaInfoAvailable(MediaInfo info) {
                fireMediaInfoAvailable(info);
            }

            public void onPlayStarted(int index) {
                firePlayStarted(index);
            }

            public void onPlayFinished(int index) {
                firePlayFinished(index);
            }

            public void onBuffering(boolean buffering) {
               fireBuffering(buffering);
            }
        });
*/
        DockPanel dp = new DockPanel();
        initWidget(dp);

        isEmbedded = (height == null) || (width == null);
        if (!isEmbedded) {
            logger = new Logger();
            logger.setVisible(false);
            dp.add(logger, DockPanel.SOUTH);
            /*
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
            */
            addDebugHandler(new DebugHandler() {

                public void onDebug(DebugEvent event) {
                    logger.log(event.getMessage(), false);
                }
            });
            addMediaInfoHandler(new MediaInfoHandler() {

                public void onMediaInfoAvailable(MediaInfoEvent event) {
                    logger.log(event.getMediaInfo().asHTMLString(), true);
                }
            });
        } else {
            height = "0px";
            width = "0px";
        }

        playerDiv = new HTML();
        playerDiv.setStyleName("");
        playerDiv.setHorizontalAlignment(HTML.ALIGN_CENTER);
        playerDiv.setHeight(height);
        dp.add(playerDiv, DockPanel.CENTER);

        setWidth(width);
    }

    /**
     * Constructs <code>QuickTimePlayer</code> to automatically playback media located at
     * {@code mediaURL} using the default height of 16px and width of 100%.
     *
     * <p> This is the same as calling {@code QuickTimePlayer(mediaURL, true, "16px", "100%")}
     *
     * @param mediaURL the URL of the media to playback
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginVersionException if the required QuickTime plugin version is not installed on the client.
     * @throws PluginNotFoundException if the QuickTime plugin is not installed on the client.
     *
     */
    public QuickTimePlayer(String mediaURL) throws LoadException, PluginVersionException,
            PluginNotFoundException {
        this(mediaURL, true, "16px", "100%");
    }

    /**
     * Constructs <code>QuickTimePlayer</code> to playback media located at {@code mediaURL}
     * using the default height of 16px and width of 100%. Media playback begins
     * automatically if {@code autoplay} is {@code true}.
     *
     * <p> This is the same as calling {@code QuickTimePlayer(mediaURL, autoplay, "16px", "100%")}
     *
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginVersionException if the required QuickTime plugin version is not installed on the client.
     * @throws PluginNotFoundException if the QuickTime plugin is not installed on the client.
     */
    public QuickTimePlayer(String mediaURL, boolean autoplay) throws LoadException,
            PluginVersionException, PluginNotFoundException {
        this(mediaURL, autoplay, "16px", "100%");
    }

    /**
     * Overridden to register player for plugin DOM events
     */
    @Override
    protected final void onLoad() {
        Timer t = new Timer() {

            @Override
            public void run() {
                playerDiv.setHTML(impl.getPlayerScript(playerId, mediaUrl,
                        autoplay, playerDiv.getOffsetHeight() + "px",
                        playerDiv.getOffsetWidth() + "px"));
                impl.registerMediaStateListener(playerId, mediaUrl);
            }
        };
        t.schedule(200);            // IE workarround...
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
        impl.load(playerId, mediaURL);
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
        checkAvailable();
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

    /**
     * Displays or hides the player controls.
     *
     * <p>As of version 1.0, if this player is not available on the panel, this method
     * call is added to the command-queue for later execution.
     */
    @Override
    public void setControllerVisible(final boolean show) {
        if (impl.isPlayerAvailable(playerId)) {
            impl.setControllerVisible(playerId, show);
        } else {
            addToPlayerReadyCommandQueue("controller", new Command() {

                public void execute() {
                    impl.setControllerVisible(playerId, show);
                }
            });
        }
    }

    /**
     * Checks whether the player controls are visible.
     */
    @Override
    public boolean isControllerVisible() {
        checkAvailable();
        return impl.isControllerVisible(playerId);
    }

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
     * Sets the transformation matrix of the underlying QuickTime Player.  The transformation
     * matrix can be used to perform several standard graphical operations like translation,
     * rotation and scaling.
     *
     * <p>The QuickTime&trade; player uses the 3 x 3 matrix shown below to accomplish 2-dimensional
     * transformations:
     *
     * <div style="text-align:center"><pre>
     *      --         --
     *      | a   b   u |
     *      | c   d   v |
     *      | tx  ty  w |
     *      --         --
     * </pre></div>
     * <p>However, elements <code>u</code> and <code>v</code> are always 0.0, while <code>w</code>
     * is always 1.0. Consequently, this method accepts the remaining elements only.
     *
     * <p>If this player is not attached to a panel, this method call is added to
     * the command-queue for later execution.
     *
     * @param a matrix element a
     * @param b matrix element b
     * @param c matrix element c
     * @param d matrix element d
     * @param tx matrix element tx
     * @param ty matrix element ty
     *
     * @since 1.0
     * @see <a href='http://developer.apple.com/documentation/QuickTime/RM/MovieBasics/MTEditing/A-Intro/1Introduction.htm1'>QuickTime Movie Basics</a>
     */
    public void setTransformationMatrix(double a, double b, double c, double d, double tx, double ty) {
        final String matrix = a + "," + b + ",0.0" + c + "," + d + ",0.0" + tx + "," + ty + ",1.0";
        if (impl.isPlayerAvailable(playerId)) {
            impl.setMatrix(playerId, matrix);
        } else {
            addToPlayerReadyCommandQueue("matrix", new Command() {

                public void execute() {
                    impl.setMatrix(playerId, matrix);
                }
            });
        }
    }

    /**
     * Returns the current matrix transformation
     *
     * @return the current matrix transformation in the form <code>a,b,0.0c,d,0.0tx,ty,1.0</code>
     * @since 1.0
     * @see #setTransformationMatrix(double, double, double, double, double, double)
     */
    public String getTransformationMatrix() {
        checkAvailable();
        return impl.getMatrix(playerId);
    }
}
