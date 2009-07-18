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
import com.bramosystems.oss.player.core.client.MediaInfo.MediaInfoKey;
import com.bramosystems.oss.player.core.client.impl.QuickTimePlayerImpl;
import com.bramosystems.oss.player.core.event.client.DebugEvent;
import com.bramosystems.oss.player.core.event.client.DebugHandler;
import com.bramosystems.oss.player.core.event.client.MediaInfoEvent;
import com.bramosystems.oss.player.core.event.client.MediaInfoHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
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
    private DockPanel panel;
    private boolean isEmbedded,  autoplay,  resizeToVideoSize;
    private String _height,  _width;

    private QuickTimePlayer() throws PluginNotFoundException, PluginVersionException {
        PluginVersion req = Plugin.QuickTimePlayer.getVersion();
        PluginVersion v = PlayerUtil.getQuickTimePluginVersion();
        if (v.compareTo(req) < 0) {
            throw new PluginVersionException(req.toString(), v.toString());
        }

        if (impl == null) {
            impl = GWT.create(QuickTimePlayerImpl.class);
        }

        playerId = DOM.createUniqueId().replace("-", "");
        resizeToVideoSize = false;
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

        panel = new DockPanel();
        panel.setWidth("100%");
        panel.setStyleName("");
        panel.setHorizontalAlignment(DockPanel.ALIGN_CENTER);

        initWidget(panel);

        _height = height;
        _width = width;
        isEmbedded = (height == null) || (width == null);
        if (!isEmbedded) {
            logger = new Logger();
            logger.setVisible(false);
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

        setWidth(_width);
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
     * Sets the transformation matrix of the underlying QuickTime Player.
     *
     * <p>If this player is not attached to a panel, this method call is added to
     * the command-queue for later execution.
     *
     * @param matrix the matrix
     * @since 1.0
     * @see TransformationMatrix
     */
    public void setTransformationMatrix(final TransformationMatrix matrix) {
        if (impl.isPlayerAvailable(playerId)) {
            impl.setMatrix(playerId, matrix.toQTMatrixString());
        } else {
            addToPlayerReadyCommandQueue("matrix", new Command() {

                public void execute() {
                    impl.setMatrix(playerId, matrix.toQTMatrixString());
                }
            });
        }
    }

    /**
     * Returns the current matrix transformation
     *
     * @return the current matrix transformation
     * @since 1.0
     * @see TransformationMatrix
     */
    public TransformationMatrix getTransformationMatrix() {
        checkAvailable();
        String[] elements = impl.getMatrix(playerId).split(", ");

        TransformationMatrix matrix = new TransformationMatrix();
        matrix.setA(Double.parseDouble(elements[0].trim()));
        matrix.setB(Double.parseDouble(elements[1].trim()));
        matrix.setC(Double.parseDouble(elements[2].substring(3).trim()));
        matrix.setD(Double.parseDouble(elements[3].trim()));
        matrix.setTx(Double.parseDouble(elements[4].substring(3).trim()));
        matrix.setTy(Double.parseDouble(elements[5].trim()));
        return matrix;
    }

    /**
     * Returns the location and dimensions of the rectangle bounds of the display
     * component within the embed area.
     *
     * <p>The value returned is in the form : <code>left,top,right,bottom</code>
     * <ul>
     * <li>left  : x coordinate of the upper-left corner</li>
     * <li>top   : y coordinate of the upper-left corner</li>
     * <li>right  : x coordinate of the lower-right corner</li>
     * <li>bottom : y coordinate of the lower-right corner</li>
     * </ul>
     * 
     * @return the bounds of the display component
     * @since 1.0
     */
    public String getRectangleBounds() {
        checkAvailable();
        return impl.getRectangle(playerId);
    }

    /**
     * Sets the location and dimensions of the rectangle bounds of the display component
     * within the embed area.
     *
     * @param bounds the new location and dimension of the display component
     * @see #getRectangleBounds()
     * @since 1.0
     */
    public void setRectangleBounds(String bounds) {
        checkAvailable();
        impl.setRectangle(playerId, bounds);
    }

    @Override
    public int getVideoHeight() {
        checkAvailable();
        String bounds[] = getRectangleBounds().split(",");
        int height = Integer.parseInt(bounds[3]) - Integer.parseInt(bounds[1]);
        return height > 0 ? height : 0;
    }

    @Override
    public int getVideoWidth() {
        checkAvailable();
        String bounds[] = getRectangleBounds().split(",");
        int width = Integer.parseInt(bounds[2]) - Integer.parseInt(bounds[0]);
        return width > 0 ? width : 0;
    }

    @Override
    public void setResizeToVideoSize(boolean resize) {
        resizeToVideoSize = resize;
        if (impl.isPlayerAvailable(playerId)) {
            // if player is on panel now update its size, otherwise
            // allow it to be handled by the MediaInfoHandler...
            checkVideoSize(getVideoHeight() + 16, getVideoWidth());
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
                _w = vidWidth + "px";
                _h = vidHeight + "px";
            }
        }

        setWidth(_w);
        panel.setCellHeight(playerDiv, _h);
        Element pe = DOM.getElementById(playerId);
        DOM.setStyleAttribute(pe, "width", _w);
        DOM.setStyleAttribute(pe, "height", _h);
    }

    /**
     * Defines the transformation matrix of the QuickTime&trade; Player plugin.  The transformation
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
     * is always 1.0.
     *
     * @since 1.0
     * @see <a href='http://developer.apple.com/documentation/QuickTime/RM/MovieBasics/MTEditing/A-Intro/1Introduction.htm1'>QuickTime Movie Basics</a>
     */
    public static class TransformationMatrix {

        private double a,  b,  c,  d,  tx,  ty;

        /**
         * Constructs a new identity TransformationMatrix object. 
         */
        public TransformationMatrix() {
            a = 1.0;
            d = 1.0;
        }

        /**
         * Constructs a new TransformationMatrix object.
         *
         * @param a matrix element a
         * @param b matrix element b
         * @param c matrix element c
         * @param d matrix element d
         * @param tx matrix element tx
         * @param ty matrix element ty
         */
        public TransformationMatrix(double a, double b, double c, double d, double tx, double ty) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.tx = tx;
            this.ty = ty;
        }


        /**
         * Returns the matrix elements as a String
         *
         * @return the matrix in the form <code>a, b, u, c, d, v, tx, ty, w</code>
         */
        @Override
        public String toString() {
            return a + ", " + b + ", 0.0, " + c + ", " + d + ", 0.0, " + tx + ", " + ty + ", 1.0";
        }

        /**
         * Returns the matrix elements as a string in the form understood by the
         * QuickTime&trade; player plugin.
         *
         * @return the matrix in the form <code>a, b, 0.0c, d, 0.0tx, ty, 1.0</code>
         */
        public String toQTMatrixString() {
            return a + ", " + b + ", 0.0" + c + ", " + d + ", 0.0" + tx + ", " + ty + ", 1.0";
        }

        /**
         * Returns matrix element a
         *
         * @return the a
         */
        public double getA() {
            return a;
        }

        /**
         * Returns matrix element b
         *
         * @return the b
         */
        public double getB() {
            return b;
        }

        /**
         * Returns matrix element c
         *
         * @return the c
         */
        public double getC() {
            return c;
        }

        /**
         * Returns matrix element d
         *
         * @return the d
         */
        public double getD() {
            return d;
        }

        /**
         * Returns matrix element tx
         *
         * @return the tx
         */
        public double getTx() {
            return tx;
        }

        /**
         * Returns matrix element ty
         *
         * @return the ty
         */
        public double getTy() {
            return ty;
        }

        /**
         * Sets the matrix element a
         *
         * @param a the a to set
         */
        public void setA(double a) {
            this.a = a;
        }

        /**
         * Sets the matrix element b
         *
         * @param b the b to set
         */
        public void setB(double b) {
            this.b = b;
        }

        /**
         * Sets the matrix element c
         *
         * @param c the c to set
         */
        public void setC(double c) {
            this.c = c;
        }

        /**
         * Sets the matrix element d
         *
         * @param d the d to set
         */
        public void setD(double d) {
            this.d = d;
        }

        /**
         * Sets the matrix element tx
         *
         * @param tx the tx to set
         */
        public void setTx(double tx) {
            this.tx = tx;
        }

        /**
         * Sets the matrix element ty
         *
         * @param ty the ty to set
         */
        public void setTy(double ty) {
            this.ty = ty;
        }       
    }
}
