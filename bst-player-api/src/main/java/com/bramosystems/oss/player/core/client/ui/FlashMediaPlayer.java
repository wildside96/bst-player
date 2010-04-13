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
import com.bramosystems.oss.player.core.client.geom.TransformationMatrix;
import com.bramosystems.oss.player.core.client.geom.MatrixSupport;
import com.bramosystems.oss.player.core.client.impl.FMPStateManager;
import com.bramosystems.oss.player.core.client.impl.FlashMediaPlayerImpl;
import com.bramosystems.oss.player.core.client.impl.BeforeUnloadCallback;
import com.bramosystems.oss.player.core.client.impl.PlayerWidget;
import com.bramosystems.oss.player.core.client.skin.CustomPlayerControl;
import com.bramosystems.oss.player.core.event.client.DebugEvent;
import com.bramosystems.oss.player.core.event.client.DebugHandler;
import com.bramosystems.oss.player.core.event.client.MediaInfoEvent;
import com.bramosystems.oss.player.core.event.client.MediaInfoHandler;
import com.bramosystems.oss.player.core.event.client.PlayerStateEvent;
import com.bramosystems.oss.player.core.event.client.PlayerStateHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;
import java.util.ArrayList;

/**
 * Widget to embed Flash plugin for playback of flash-supported formats
 *
 * <h3>Usage Example</h3>
 *
 * <p>
 * <code><pre>
 * SimplePanel panel = new SimplePanel();   // create panel to hold the player
 * Widget player = null;
 * try {
 *      // create the player
 *      player = new FlashMediaPlayer("www.example.com/mediafile.flv", false, "200px", "250px");
 * } catch(LoadException e) {
 *      // catch loading exception and alert user
 *      Window.alert("An error occured while loading");
 * } catch(PluginVersionException e) {
 *      // catch plugin version exception and alert user to download plugin first.
 *      // An option is to use the utility method in PlayerUtil class.
 *      player = PlayerUtil.getMissingPluginNotice(Plugin.FlashMediaPlayer, "Missing Plugin",
 *              ".. some nice message telling the user to click and download plugin first ..",
 *              false);
 * } catch(PluginNotFoundException e) {
 *      // catch PluginNotFoundException and tell user to download plugin, possibly providing
 *      // a link to the plugin download page.
 *      player = new HTML(".. another kind of message telling the user to download plugin..");
 * }
 *
 * panel.setWidget(player); // add player to panel.
 * </pre></code>
 *
 * <h3>M3U Playlist Support</h3>
 * <p>
 * This player supports M3U formatted playlists.  However, each entry in the playlist MUST be
 * a flash-supported media file.
 * </p>
 *
 * @author Sikirulai Braheem
 * @since 1.0
 */
public class FlashMediaPlayer extends AbstractMediaPlayer implements PlaylistSupport, MatrixSupport {

    private static FMPStateManager manager = new FMPStateManager();
    private FlashMediaPlayerImpl impl;
    private HandlerRegistration initListHandler;
    private String playerId;
    private boolean isEmbedded, resizeToVideoSize;
    private Logger logger;
    private CustomPlayerControl control;
    private ArrayList<String> _playlistCache;
    private PlayerWidget swf;
    private String _height, _width;

    /**
     * Constructs <code>FlashMediaPlayer</code> with the specified {@code height} and
     * {@code width} to playback media located at {@code mediaURL}. Media playback
     * begins automatically if {@code autoplay} is {@code true}.
     *
     * <p> {@code height} and {@code width} are specified as CSS units. A value of {@code null}
     * for {@code height} or {@code width} puts the player in embedded mode.  When in embedded mode,
     * the player is made invisible on the page and media state events are propagated to registered
     * listeners only.  This is desired especially when used with custom sound controls.  For custom
     * video-playback control, specify valid CSS values for {@code height} and {@code width} but hide the
     * player controls with {@code setControllerVisible(false)}.
     *
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     * @param height the height of the player
     * @param width the width of the player.
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginVersionException if the required Flash plugin version is not installed on the client.
     * @throws PluginNotFoundException if the Flash plugin is not installed on the client.
     */
    public FlashMediaPlayer(final String mediaURL, final boolean autoplay, String height, String width)
            throws PluginNotFoundException, PluginVersionException, LoadException {
        PluginVersion req = Plugin.FlashPlayer.getVersion();
        PluginVersion v = PlayerUtil.getFlashPlayerVersion();
        if (v.compareTo(req) < 0) {
            throw new PluginVersionException(Plugin.FlashPlayer, req.toString(), v.toString());
        }

        _playlistCache = new ArrayList<String>();
        _height = height;
        _width = width;
        resizeToVideoSize = false;
        playerId = DOM.createUniqueId().replace("-", "");

        isEmbedded = (height == null) || (width == null);
        if (isEmbedded) {
            _height = "0px";
            _width = "0px";
        }

        // inject bst-flash-player version via maven resources filter...
        String playerAppFile = "bst-flash-player-${version}.swf";

        swf = new PlayerWidget(Plugin.FlashPlayer, playerId, GWT.getModuleBaseURL() + playerAppFile,
                autoplay, new BeforeUnloadCallback() {

            public void onBeforeUnload() {
                impl.closeMedia();
                manager.closeMedia(playerId);
            }
        });
        swf.addParam("flashVars", "playerId=" + playerId);
        swf.addParam("allowScriptAccess", "always");
        swf.addParam("bgcolor", "#000000");

        manager.init(playerId, this, new Command() {

            public void execute() {
                impl = FlashMediaPlayerImpl.getPlayer(playerId);
                fireDebug("Flash Player plugin");
                fireDebug("Version : " + impl.getPluginVersion());
                impl.loadMedia(mediaURL);
                firePlayerStateEvent(PlayerStateEvent.State.Ready);
                if (autoplay) {
                    impl.playMedia();
                }
            }
        });
        FlowPanel panel = new FlowPanel();
        panel.add(swf);

        if (!isEmbedded) {
            control = new CustomPlayerControl(this);
            panel.add(control);

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
                    MediaInfo info = event.getMediaInfo();
                    if (info.getAvailableItems().contains(MediaInfoKey.VideoHeight)
                            || info.getAvailableItems().contains(MediaInfoKey.VideoWidth)) {
                        checkVideoSize(Integer.parseInt(info.getItem(MediaInfoKey.VideoHeight)),
                                Integer.parseInt(info.getItem(MediaInfoKey.VideoWidth)));
                    }
                    logger.log(event.getMediaInfo().asHTMLString(), true);
                }
            });
        }

        initWidget(panel);
    }

    /**
     * Constructs <code>FlashMediaPlayer</code> to automatically playback media located at
     * {@code mediaURL}.
     *
     * <p> Note: This constructor hides the video display component, the player controls are
     * however visible.
     *
     * @param mediaURL the URL of the media to playback
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginVersionException if the required Flash plugin version is not installed on the client.
     * @throws PluginNotFoundException if the Flash plugin is not installed on the client.
     *
     */
    public FlashMediaPlayer(String mediaURL) throws PluginNotFoundException,
            PluginVersionException, LoadException {
        this(mediaURL, true, "0px", "100%");
    }

    /**
     * Constructs <code>FlashMediaPlayer</code> to playback media located at {@code mediaURL}.
     * Media playback begins automatically if {@code autoplay} is {@code true}.
     *
     * <p> Note: This constructor hides the video display component, the player controls are
     * however visible.
     *
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginVersionException if the required Flash plugin version is not installed on the client.
     * @throws PluginNotFoundException if the Flash plugin is not installed on the client.
     */
    public FlashMediaPlayer(String mediaURL, boolean autoplay) throws PluginNotFoundException,
            PluginVersionException, LoadException {
        this(mediaURL, autoplay, "0px", "100%");
    }

    private void checkVideoSize(int vidHeight, int vidWidth) {
        String _h = _height, _w = _width;
        if (vidHeight == 0) {
            _h = "0px"; // suppress SWF app height for audio files ...
        }

        if (resizeToVideoSize) {
            if ((vidHeight > 0) && (vidWidth > 0)) {
                // adjust to video size, if video width < control bar width,
                // use control bar width ...
//                int cntWidth = control.getOffsetWidth();
//                if (vidWidth <= cntWidth) {
//                    vidWidth = cntWidth;
//                }

                fireDebug("Resizing Player : " + vidWidth + " x " + vidHeight);
                _h = vidHeight + "px";
                _w = vidWidth + "px";
            }
        }

        swf.setSize(_w, _h);
        setWidth(_w);

        if (!_height.equals(_h) && !_width.equals(_w)) {
            firePlayerStateEvent(PlayerStateEvent.State.DimensionChangedOnVideo);
        }
    }

    private void checkAvailable() {
        if (!isPlayerOnPage(playerId)) {
            String message = "Player not available, create an instance";
            fireDebug(message);
            throw new IllegalStateException(message);
        }
    }

    /**
     * Overriden to release associated resources
     */
    @Override
    protected void onLoad() {
        swf.setSize(_width, _height);
        setWidth(_width);
    }

    /**
     * @deprecated As of version 1.1, remove player from panel instead
     */
    public void close() {
        impl.closeMedia();
        manager.closeMedia(playerId);
    }

    public long getMediaDuration() {
        checkAvailable();
        return (long) impl.getMediaDuration();
    }

    public double getPlayPosition() {
        checkAvailable();
        return impl.getPlayPosition();
    }

    public double getVolume() {
        checkAvailable();
        return impl.getVolume();
    }

    public void loadMedia(String mediaURL) throws LoadException {
        checkAvailable();
        impl.loadMedia(mediaURL);
    }

    public void pauseMedia() {
        checkAvailable();
        impl.pauseMedia();
    }

    public void playMedia() throws PlayException {
        checkAvailable();
        impl.playMedia();
    }

    public void setPlayPosition(double position) {
        checkAvailable();
        impl.setPlayPosition(position);
    }

    public void setVolume(double volume) {
        checkAvailable();
        impl.setVolume(volume);
    }

    public void stopMedia() {
        checkAvailable();
        impl.stopMedia();
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
        if (!isEmbedded) {
            control.setVisible(show);
        }
    }

    /**
     * Checks whether the player controls are visible.
     */
    @Override
    public boolean isControllerVisible() {
        return control.isVisible();
    }

    /**
     * Returns the number of times this player repeats playback before stopping.
     */
    @Override
    public int getLoopCount() {
        checkAvailable();
        return impl.getLoopCount();
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
            impl.setLoopCount(loop);
        } else {
            addToPlayerReadyCommandQueue("loopcount", new Command() {

                public void execute() {
                    impl.setLoopCount(loop);
                }
            });
        }
    }

    public void addToPlaylist(final String mediaURL) {
        if (isPlayerOnPage(playerId)) {
            impl.addToPlaylist(mediaURL);
        } else {
            if (initListHandler == null) {
                initListHandler = addPlayerStateHandler(new PlayerStateHandler() {

                    public void onPlayerStateChanged(PlayerStateEvent event) {
                        switch (event.getPlayerState()) {
                            case Ready:
                                for (String url : _playlistCache) {
                                    impl.addToPlaylist(url);
                                }
                                break;
                        }
                        initListHandler.removeHandler();
                    }
                });
            }
            _playlistCache.add(mediaURL);
        }
    }

    public boolean isShuffleEnabled() {
        checkAvailable();
        return impl.isShuffleEnabled();
    }

    public void removeFromPlaylist(int index) {
        checkAvailable();
        impl.removeFromPlaylist(index);
    }

    /**
     * Enables or disables players' shuffle mode.
     *
     * <p>As of version 1.0, if this player is not available on the panel, this method
     * call is added to the command-queue for later execution.
     */
    public void setShuffleEnabled(final boolean enable) {
        if (isPlayerOnPage(playerId)) {
            impl.setShuffleEnabled(enable);
        } else {
            addToPlayerReadyCommandQueue("shuffle", new Command() {

                public void execute() {
                    impl.setShuffleEnabled(enable);
                }
            });
        }
    }

    public void clearPlaylist() {
        checkAvailable();
        impl.clearPlaylist();
    }

    public int getPlaylistSize() {
        checkAvailable();
        return impl.getPlaylistCount();
    }

    public void play(int index) throws IndexOutOfBoundsException {
        checkAvailable();
        if (!impl.playMedia(index)) {
            throw new IndexOutOfBoundsException();
        }
    }

    public void playNext() throws PlayException {
        checkAvailable();
        if (!impl.playNext()) {
            throw new PlayException("No more entries in playlist");
        }
    }

    public void playPrevious() throws PlayException {
        checkAvailable();
        if (!impl.playPrevious()) {
            throw new PlayException("Beginning of playlist reached");
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
        if (isPlayerOnPage(playerId)) {
            // if player is on panel now update its size, otherwise
            // allow it to be handled by the MediaInfoHandler...
            checkVideoSize(impl.getVideoHeight(), impl.getVideoWidth());
        }
    }

    @Override
    public boolean isResizeToVideoSize() {
        return resizeToVideoSize;
    }

    /**
     * Sets the transformation matrix of the underlying Flash player.
     *
     * <p>If this player is not attached to a panel, this method call is added to
     * the command-queue for later execution.
     */
    public void setMatrix(final TransformationMatrix matrix) {
        if (isPlayerOnPage(playerId)) {
            impl.setMatrix(matrix.getMatrix().getVx().getX(),
                    matrix.getMatrix().getVy().getX(), matrix.getMatrix().getVx().getY(),
                    matrix.getMatrix().getVy().getY(), matrix.getMatrix().getVx().getZ(),
                    matrix.getMatrix().getVy().getZ());

            if (resizeToVideoSize) {
                checkVideoSize(getVideoHeight() + 16, getVideoWidth());
            }
        } else {
            addToPlayerReadyCommandQueue("matrix", new Command() {

                public void execute() {
                    setMatrix(matrix);
                }
            });
        }
    }

    public TransformationMatrix getMatrix() {
        checkAvailable();
        String[] elements = impl.getMatrix().split(",");

        TransformationMatrix matrix = new TransformationMatrix();
        matrix.getMatrix().getVx().setX(Double.parseDouble(elements[0]));
        matrix.getMatrix().getVy().setX(Double.parseDouble(elements[1]));
        matrix.getMatrix().getVx().setY(Double.parseDouble(elements[2]));
        matrix.getMatrix().getVy().setY(Double.parseDouble(elements[3]));
        matrix.getMatrix().getVx().setZ(Double.parseDouble(elements[4]));
        matrix.getMatrix().getVy().setZ(Double.parseDouble(elements[5]));
        return matrix;
    }

    @Override
    public <T extends ConfigValue> void setConfigParameter(ConfigParameter param, T value) {
        super.setConfigParameter(param, value);
        switch (param) {
            case TransparencyMode:
                if (value != null) {
                    swf.addParam("wmode", ((TransparencyMode) value).name().toLowerCase());
                } else {
                    swf.addParam("wmode", null);
                }
        }
    }
/*
 * Not available yet !
 * 
    @Override
    public void setRate(final double rate) {
        if (isPlayerOnPage(playerId)) {
//            impl.setRate(rate);
        } else {
            addToPlayerReadyCommandQueue("rate", new Command() {

                public void execute() {
//                    impl.setRate(rate);
                }
            });
        }
    }

    @Override
    public double getRate() {
        checkAvailable();
        return 0;//impl.getRate();
    }
*/
}

/**
 * TODO:
 * About sandboxes
The sandbox type indicates the type of security zone in which the SWF file is operating. In Flash Player, all SWF
files (and HTML files, for the purposes of SWF-to-HTML scripting) are placed into one of four types of sandbox:
remote All files from non-local URLs are placed in a remote sandbox. There are many such sandboxes, one for
each Internet (or intranet) domain from which files are loaded.
local-with-filesystem The default sandbox for local files. SWF files in this sandbox may not contact the Internet
(or any servers) in any way—they may not access network endpoints with addresses such as HTTP URLs.
local-with-networking SWF file in this sandbox may communicate over the network but may not read from local
file systems.
local-trusted This sandbox is not restricted. Any local file can be placed in this sandbox if given authorization by
the end user. This authorization can come in two forms: interactively through the Settings Manager or noninteractively
through an executable installer that creates Flash Player configuration files on the user’s computer.
You can determine the current sandbox type by using the sandboxType property of the Security class, as the
following example shows:
<?xml version="1.0" encoding="utf-8"?>
<!-- security/DetectCurrentSandbox.mxml -->
<mx:Application xmlns:mx="http://www.adobe.com/2006/mxml" creationComplete="initApp()">
<mx:Script><![CDATA[
[Bindable]
private var l_sandboxType:String;
private function initApp():void {
l_sandboxType = String(Security.sandboxType);
}
]]></mx:Script>
<mx:Form>
<mx:FormItem id="fi1" label="Security.sandboxType">
<mx:Label id="l1" text="{l_sandboxType}"/>
</mx:FormItem>
</mx:Form>
</mx:Application>
When you compile a Flex application, you have some control over which sandbox the application is in. This determination
is a combination of the value of the use-network compiler option (the default is true) and whether the
SWF file was loaded by the client over a network connection or as a local file.
34 CHAPTER 3
The following table shows how the sandbox type is determined:
Browser security
Flash Player clients can be one of the following four types:
•Embedded Flash Player
•Debugger version of embedded Flash Player
•Stand-alone Flash Player
•Debugger version of stand-alone Flash Player
The stand-alone Flash Player runs on the desktop. It is typically used by people who are running applications that
are installed and maintained by an IT department that has access to the desktop on which the application runs.
The embedded Flash Player is run within a browser. Anyone with Internet access can run applications from
anywhere with this player. For Internet Explorer, the embedded player is loaded as an ActiveX control inside the
browser. For Netscape-based browsers (including Firefox), it is loaded as a plug-in inside the browser. Using an
embedded player lets the developer use browser-based technologies such as FORM and BASIC authentication as
well as SSL.
Browser APIs
Applications hosting the Flash Player ActiveX control or Flash Player plug-in can use the EnforceLocalSecurity
and DisableLocalSecurity API calls to control security settings. If DisableLocalSecurity is opened, the application
does not benefit from the local-with-networking and local-with-file-system sandboxes. All files loaded from the
local file system are placed into the local-trusted sandbox. The default behavior for an ActiveX control hosted in
a client application is DisableLocalSecurity.
If EnforceLocalSecurity is opened, the application can use all three local sandboxes. The default behavior for the
browser plug-in is EnforceLocalSecurity.
Cross-scripting
Cross-scripting is when a SWF file communicates directly with another SWF file. This communication includes
calling methods and setting properties of the other SWF file.
use-network
Loaded
Sandbox type
false
locally
local-with-filesystem
true
locally
local-with-network
true
network
remote
false
network
n/a (causes an error)
 */