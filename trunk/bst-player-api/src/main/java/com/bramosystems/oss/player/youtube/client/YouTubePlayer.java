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
package com.bramosystems.oss.player.youtube.client;

import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayException;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersion;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.ui.Logger;
import com.bramosystems.oss.player.core.client.ui.SWFWidget;
import com.bramosystems.oss.player.core.event.client.DebugEvent;
import com.bramosystems.oss.player.core.event.client.DebugHandler;
import com.bramosystems.oss.player.core.event.client.LoadingProgressEvent;
import com.bramosystems.oss.player.core.event.client.PlayStateEvent;
import com.bramosystems.oss.player.core.event.client.PlayerStateEvent;
import com.bramosystems.oss.player.core.event.client.PlayerStateHandler;
import com.bramosystems.oss.player.youtube.client.impl.EventManager;
import com.bramosystems.oss.player.youtube.client.impl.YouTubePlayerImpl;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.DockPanel;

/**
 * Widget to embed YouTube video
 *
 * <h3>Usage Example</h3>
 *
 * <p>
 * <code><pre>
 * SimplePanel panel = new SimplePanel();   // create panel to hold the player
 * Widget player = null;
 * try {
 *      // create the player
 *      player = new YouTubePlayer("http://www.youtube.com/v/VIDEO_ID&fs=1", "100%", "350px");
 * } catch(PluginVersionException e) {
 *      // catch plugin version exception and alert user to download plugin first.
 *      // An option is to use the utility method in PlayerUtil class.
 *      player = PlayerUtil.getMissingPluginNotice(e.getPlugin());
 * } catch(PluginNotFoundException e) {
 *      // catch PluginNotFoundException and tell user to download plugin, possibly providing
 *      // a link to the plugin download page.
 *      player = new HTML(".. another kind of message telling the user to download plugin..");
 * }
 *
 * panel.setWidget(player); // add player to panel.
 * </pre></code>
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems dot com>
 * @since 1.1
 */
public class YouTubePlayer extends AbstractMediaPlayer {

    private static EventManager eventMgr = new EventManager();
    protected YouTubePlayerImpl impl;
    protected String playerId,  apiId;
    private Timer bufferingTimer;
    private Logger logger;

    /**
     * Constructs <code>YouTubePlayer</code> with the specified {@code height} and
     * {@code width} to playback video located at {@code videoURL}
     *
     * <p> {@code height} and {@code width} are specified as CSS units.
     *
     * @param videoURL the URL of the video
     * @param width the width of the player
     * @param height the height of the player
     *
     * @throws PluginNotFoundException if the required Flash player plugin is not found
     * @throws PluginVersionException if Flash player version 8 and above is not found
     * @throws NullPointerException if either {@code videoURL}, {@code height} or {@code width} is null
     */
    public YouTubePlayer(String videoURL, String width, String height)
            throws PluginNotFoundException, PluginVersionException {
        this(videoURL, new PlayerParameters(), width, height);
    }

    /**
     * Constructs <code>YouTubePlayer</code> with the specified {@code height} and
     * {@code width} to playback video located at {@code videoURL}.  Playback starts
     * automatically if {@code autoplay} is {@code true}
     *
     * <p> {@code height} and {@code width} are specified as CSS units.
     *
     * @param videoURL the URL of the video
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     * @param width the width of the player
     * @param height the height of the player
     *
     * @throws PluginNotFoundException if the required Flash player plugin is not found
     * @throws PluginVersionException if Flash player version 8 and above is not found
     * @throws NullPointerException if either {@code videoURL}, {@code height} or {@code width} is null
     */
    public YouTubePlayer(String videoURL, boolean autoplay, String width, String height)
            throws PluginNotFoundException, PluginVersionException {
        this(videoURL, new PlayerParameters(), width, height);
    }

    /**
     * Constructs <code>YouTubePlayer</code> with the specified {@code height} and
     * {@code width} to playback video located at {@code videoURL} using the specified
     * {@code playerParameters}
     *
     * <p> {@code height} and {@code width} are specified as CSS units.
     *
     * @param videoURL the URL of the video
     * @param playerParameters the parameters of the player
     * @param width the width of the player
     * @param height the height of the player
     *
     * @throws PluginNotFoundException if the required Flash player plugin is not found
     * @throws PluginVersionException if Flash player version 8 and above is not found
     * @throws NullPointerException if either {@code videoURL}, {@code height} or {@code width} is null
     */
    public YouTubePlayer(String videoURL, PlayerParameters playerParameters, String width, String height)
            throws PluginNotFoundException, PluginVersionException {
        if (height == null) {
            throw new NullPointerException("height cannot be null");
        }
        if (width == null) {
            throw new NullPointerException("width cannot be null");
        }
        if (videoURL == null) {
            throw new NullPointerException("videoURL cannot be null");
        }
        apiId = "apiid_" + DOM.createUniqueId().replace("-", "");

        SWFWidget swf = new SWFWidget(getNormalizedVideoAppURL(videoURL, playerParameters),
                width, height, PluginVersion.get(8, 0, 0));
//                "100%", "100%", PluginVersion.get(8, 0, 0));
        swf.addProperty("allowScriptAccess", "always");
        swf.addProperty("bgcolor", "#000000");
        if (playerParameters.isFullScreenEnabled()) {
            swf.addProperty("allowFullScreen", "true");
        }
        playerId = swf.getId();

        logger = new Logger();
        addDebugHandler(new DebugHandler() {

            public void onDebug(DebugEvent event) {
                logger.log(event.getMessage(), false);
            }
        });

        DockPanel panel = new DockPanel();
        panel.setStyleName("");
        panel.add(logger, DockPanel.SOUTH);
        panel.add(swf, DockPanel.CENTER);
//        panel.setCellHeight(swf, height);
        panel.setCellWidth(swf, width);
        initWidget(panel);
        setWidth(width);
//        initWidget(swf);

        // register for DOM events ...
        eventMgr.init(apiId, new Command() {

            public void execute() {
                impl = YouTubePlayerImpl.getPlayerImpl(playerId);
                impl.registerHandlers(YouTubePlayer.this);
                fireDebug("YouTube Player");
                playerInit();
            }
        });

        // setup loading event management ...
        bufferingTimer = new Timer() {

            @Override
            public void run() {
                LoadingProgressEvent.fire(YouTubePlayer.this,
                        impl.getBytesLoaded() / impl.getBytesTotal());
            }
        };
        addPlayerStateHandler(new PlayerStateHandler() {

            public void onPlayerStateChanged(PlayerStateEvent event) {
                switch (event.getPlayerState()) {
                    case BufferingStarted:
                        bufferingTimer.scheduleRepeating(1000);
                        break;
                    case BufferingFinished:
                        bufferingTimer.cancel();
                }
            }
        });
    }

    /**
     * Returns the normalized URL of the video.
     *
     * <p>This method is called by the player Constructors.  It adjusts the parameters that may
     * be present in the <code>videoURL</code> and <code>playerParameters</code> (possibly overriding some)
     * to match the requirements of this players' internals.
     *
     * @param videoURL the URL of the YouTube&trade; video
     * @param playerParameters the parameters of the video
     * @return the normalized URL of the video
     */
    protected String getNormalizedVideoAppURL(String videoURL, PlayerParameters playerParameters) {
        parseURLParams(videoURL, playerParameters);
        playerParameters.setJSApiEnabled(true);
        playerParameters.setPlayerAPIId(apiId);
        return videoURL + paramsToString(playerParameters);
    }

    /**
     * Called when player initialization is completed.
     */
    protected void playerInit() {
    }

    /**
     * Puts all URL parameters that may be present in the <code>videoURL</code> into the
     * <code>playerParameters</code> object.
     *
     * <p>Note: when this method returns, all parameters present in <code>videoURL</code> would have been
     * removed. For example:
     *
     * <p>If <code>videoURL</code> before method call is <em>http://www.youtube.com/v/VIDEO_ID&paramName=value</em>,
     * then <code>videoURL</code> after method call is <em>http://www.youtube.com/v/VIDEO_ID</em>.
     *
     * @param videoURL the URL of the YouTube&trade; video
     * @param playerParameters the parameters of the video
     */
    protected final void parseURLParams(String videoURL, PlayerParameters playerParameters) {
        String _params[] = videoURL.split("&");
        videoURL = _params[0];
        if (_params.length > 1) {
            for (int i = 1; i < _params.length; i++) {
                try {
                    String value[] = _params[i].split("=");
                    switch (URLParameters.valueOf(value[0])) {
                        case autoplay:
                            playerParameters.setAutoplay(value[1].equals("1"));
                            break;
                        case border:
                            playerParameters.showBorder(value[1].equals("1"));
                            break;
//                        case cc_load_policy:
//                            playerParameters.showClosedCaptions(value[1].equals("1"));
//                            break;
                        case color1:
                            playerParameters.setPrimaryBorderColor(value[1]);
                            break;
                        case color2:
                            playerParameters.setSecondaryBorderColor(value[1]);
                            break;
                        case disablekb:
                            playerParameters.setKeyboardControlsEnabled(value[1].equals("0"));
                            break;
                        case egm:
                            playerParameters.setEnhancedGenieMenuEnabled(value[1].equals("1"));
                            break;
                        case enablejsapi:
                            playerParameters.setJSApiEnabled(value[1].equals("1"));
                            break;
                        case fs:
                            playerParameters.setFullScreenEnabled(value[1].equals("1"));
                            break;
                        case hd:
                            playerParameters.setHDEnabled(value[1].equals("1"));
                            break;
                        case iv_load_policy:
                            playerParameters.showVideoAnnotations(value[1].equals("1"));
                            break;
                        case loop:
                            playerParameters.setLoopEnabled(value[1].equals("1"));
                            break;
                        case playerapiid:
                            playerParameters.setPlayerAPIId(value[1]);
                            break;
                        case rel:
                            playerParameters.setLoadRelatedVideos(value[1].equals("1"));
                            break;
                        case showinfo:
                            playerParameters.showVideoInformation(value[1].equals("1"));
                            break;
                        case showsearch:
                            playerParameters.showSearchBox(value[1].equals("1"));
                            break;
                        case start:
                            playerParameters.setStartTime(Integer.parseInt(value[1]));
                            break;
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * Converts the PlayerParameters object into YouTube&trade; video URL parameters.
     *
     * @param playerParameters the player parameters
     * @return the parameters in YouTube&trade; video URL format
     */
    protected final String paramsToString(PlayerParameters playerParameters) {
        String url = "";
        for (URLParameters _param : URLParameters.values()) {
            url += "&" + _param.name() + "=";
            switch (_param) {
                case autoplay:
                    url += playerParameters.isAutoplay() ? "1" : "0";
                    break;
                case border:
                    url += playerParameters.isShowBorder() ? "1" : "0";
                    break;
//                case cc_load_policy:
//                    url += playerParameters.() ? "1" : "0";
//                    break;
                case color1:
                    url += playerParameters.getPrimaryBorderColor();
                    break;
                case color2:
                    url += playerParameters.getSecondaryBorderColor();
                    break;
                case disablekb:
                    url += playerParameters.isKeyboardControlsEnabled() ? "0" : "1";
                    break;
                case egm:
                    url += playerParameters.isEnhancedGenieMenuEnabled() ? "1" : "0";
                    break;
                case enablejsapi:
                    url += playerParameters.isJSApiEnabled() ? "1" : "0";
                    break;
                case fs:
                    url += playerParameters.isFullScreenEnabled() ? "1" : "0";
                    break;
                case hd:
                    url += playerParameters.isHDEnabled() ? "1" : "0";
                    break;
                case iv_load_policy:
                    url += playerParameters.isShowVideoAnnotations() ? "1" : "3";
                    break;
                case loop:
                    url += playerParameters.isLoopEnabled() ? "1" : "0";
                    break;
                case playerapiid:
                    url += playerParameters.getPlayerAPIId();
                    break;
                case rel:
                    url += playerParameters.isLoadRelatedVideos() ? "1" : "0";
                    break;
                case showinfo:
                    url += playerParameters.isShowVideoInformation() ? "1" : "0";
                    break;
                case showsearch:
                    url += playerParameters.isShowSearchBox() ? "1" : "0";
                    break;
                case start:
                    url += playerParameters.getStartTime();
                    break;
            }
        }
        return url;
    }

    @Override
    public void loadMedia(String mediaURL) throws LoadException {
        impl.loadVideoByUrl(mediaURL, 0);
    }

    @Override
    public void playMedia() throws PlayException {
        impl.play();
    }

    @Override
    public void stopMedia() {
        impl.pause();
        impl.seekTo(0, true);
    }

    @Override
    public void pauseMedia() {
        impl.pause();
    }

    @Override
    public void close() {
        impl.stop();
        impl.clear();
    }

    @Override
    public long getMediaDuration() {
        return (long) impl.getDuration();
    }

    @Override
    public double getPlayPosition() {
        return impl.getCurrentTime();
    }

    @Override
    public void setPlayPosition(double position) {
        impl.seekTo(position, true);
    }

    @Override
    public double getVolume() {
        return impl.getVolume();
    }

    @Override
    public void setVolume(double volume) {
        impl.setVolume(volume);
    }

    @Override
    public int getLoopCount() {
        return 1;
    }

    @Override
    public void setLoopCount(int loop) {
    }

    /**
     * Checks whether the player controls are visible.  This implementation <b>always</b> return true.
     */
    @Override
    public boolean isControllerVisible() {
        return true;
    }

    @Override
    public void showLogger(boolean show) {
        logger.setVisible(show);
    }

    private void onYTStateChanged(int state) {
        switch (state) {
            case -1: // unstarted
                fireDebug("YouTube application loaded!");
                break;
            case 0: // ended
                firePlayStateEvent(PlayStateEvent.State.Finished, 0);
                fireDebug("Playback finished");
                break;
            case 1: // playing
                firePlayerStateEvent(PlayerStateEvent.State.BufferingFinished);
                firePlayStateEvent(PlayStateEvent.State.Started, 0);
                fireDebug("Playback started");
                break;
            case 2: // paused
                firePlayStateEvent(PlayStateEvent.State.Paused, 0);
                fireDebug("Playback paused");
                break;
            case 3: // buffering
                firePlayerStateEvent(PlayerStateEvent.State.BufferingStarted);
                fireDebug("Buffering");
                break;
            case 5: // video cued
                firePlayerStateEvent(PlayerStateEvent.State.Ready);
                fireDebug("Video ready for playback");
                break;
        }
    }

    private void onYTError(int errorCode) {
        switch (errorCode) {
            case 100: // video not found. Occurs when video is removed (for any reason), or marked private.
                fireError("Video not found! It may have been removed or marked private");
                break;
            case 101: // video does not allow playback in the embedded players.
            case 150: // is the same as 101, it's just 101 in disguise!
                fireError("Video playback not allowed");
                break;
        }
    }

    private enum URLParameters {

        rel, autoplay, loop, enablejsapi, playerapiid, disablekb, egm, border,
        color1, color2, start, fs, hd, showsearch, showinfo, iv_load_policy
        //cc_load_policy
    }
}
