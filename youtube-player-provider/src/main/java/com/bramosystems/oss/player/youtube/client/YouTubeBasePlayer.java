/*
 * Copyright 2013 Sikirulai Braheem
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

import com.bramosystems.oss.player.core.client.*;
import com.bramosystems.oss.player.core.event.client.LoadingProgressEvent;
import com.bramosystems.oss.player.core.event.client.PlayStateEvent;
import com.bramosystems.oss.player.core.event.client.PlayerStateEvent;
import com.bramosystems.oss.player.core.event.client.PlayerStateHandler;
import com.bramosystems.oss.player.youtube.client.PlaybackQuality;
import com.bramosystems.oss.player.youtube.client.PlaybackQualityChangeEvent;
import com.bramosystems.oss.player.youtube.client.PlaybackQualityChangeHandler;
import com.bramosystems.oss.player.youtube.client.PlayerParameters;
import com.bramosystems.oss.player.youtube.client.YouTubeConfigParameter;
import com.bramosystems.oss.player.youtube.client.impl.YouTubePlayerImpl;
import com.bramosystems.oss.player.youtube.client.impl.YouTubePlayerProvider;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Base player widget to embed YouTube videos
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems dot com>
 * @since 2.0
 */
public abstract class YouTubeBasePlayer extends AbstractMediaPlayer {

    protected YouTubePlayerImpl impl;
    protected String playerId, _width, _height, _vid;
    protected HashMap<String, String> configParam = new HashMap<String, String>();
    protected PlayerParameters pParams = new PlayerParameters();
    private Timer bufferingTimer;
    private RepeatMode repeatMode = RepeatMode.REPEAT_OFF;
//    private YouTubePlaylistManager ypm;

    /**
     * Constructs <code>YouTubeBasePlayer</code> with the specified {@code height} and
     * {@code width} to playback video {@code videoID}
     *
     * <p> {@code height} and {@code width} are specified as CSS units.
     *
     * @param videoID the ID of the video
     * @param width the width of the player
     * @param height the height of the player
     *
     * @throws PluginNotFoundException if the required player plugin is not found
     * @throws PluginVersionException if player plugin version not found
     * @throws NullPointerException if either {@code videoID}, {@code height} or
     * {@code width} is null
     */
    protected YouTubeBasePlayer(String videoID, String width, String height, final boolean useIframe)  {

        if (height == null) {
            throw new NullPointerException("height cannot be null");
        }
        if (width == null) {
            throw new NullPointerException("width cannot be null");
        }
        if (videoID == null) {
            throw new NullPointerException("videoURL cannot be null");
        }

        _width = width;
        _height = height;
        _vid = videoID;
        /*
         ypm = new YouTubePlaylistManager(new YouTubePlaylistManager.CallbackHandler() {
        
         @Override
         public void onError(String message) {
         fireError(message);
         }
        
         @Override
         public YouTubePlayerImpl getPlayerImpl() {
         return impl;
         }
        
         @Override
         public void onInfo(String info) {
         fireDebug(info);
         }
         });
         */
        playerId = DOM.createUniqueId().replace("-", "");

        // setup loading event management ...
        bufferingTimer = new Timer() {
            @Override
            public void run() {
                LoadingProgressEvent.fire(YouTubeBasePlayer.this, impl.getVideoLoaded());
            }
        };
        addPlayerStateHandler(new PlayerStateHandler() {
            @Override
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
     * Converts the PlayerParameters object into YouTube&trade; video URL
     * parameters.
     *
     * @param playerParameters the player parameters
     * @return the parameters in YouTube&trade; video URL format
     */
    protected final String paramsToString(PlayerParameters playerParameters) {
        StringBuilder url = new StringBuilder(URLParameters.autoplay.name());
        url.append("=").append(playerParameters.getAutoplay());
        for (URLParameters _param : URLParameters.values()) {
            switch (_param) {
                case origin:
                    url.append("&").append(_param.name()).append("=");
                    url.append(playerParameters.getOrigin());
                    break;
                case disablekb:
                    url.append("&").append(_param.name()).append("=");
                    url.append(playerParameters.getDisableKeyboardControls());
                    break;
                case enablejsapi:
                    url.append("&").append(_param.name()).append("=");
                    url.append(playerParameters.getEnableJsApi());
                    break;
                case fs:
                    url.append("&").append(_param.name()).append("=");
                    url.append(playerParameters.getFullScreen());
                    break;
                case iv_load_policy:
                    url.append("&").append(_param.name()).append("=");
                    url.append(playerParameters.getIvLoadPolicy());
                    break;
                case loop:
                    url.append("&").append(_param.name()).append("=");
                    url.append(playerParameters.getLoop());
                    break;
                case playerapiid:
                    url.append("&").append(_param.name()).append("=");
                    url.append(playerParameters.getPlayerAPIId());
                    break;
                case rel:
                    url.append("&").append(_param.name()).append("=");
                    url.append(playerParameters.getLoadRelatedVideos());
                    break;
                case showinfo:
                    url.append("&").append(_param.name()).append("=");
                    url.append(playerParameters.getShowInfo());
                    break;
                case start:
                    url.append("&").append(_param.name()).append("=");
                    url.append(playerParameters.getStartTime());
                    break;
                case autohide:
                    url.append("&").append(_param.name()).append("=");
                    url.append(playerParameters.getAutoHide().ordinal());
                    break;
                case controls:
                    url.append("&").append(_param.name()).append("=");
                    url.append(playerParameters.getShowControls());
                    break;
                case modestbranding:
                    url.append("&").append(_param.name()).append("=");
                    url.append(playerParameters.getModestBranding());
                    break;
            }
        }
        return url.toString();
    }

    @Override
    public void loadMedia(String mediaURL) throws LoadException {
        if (impl != null) {
            impl.loadVideoByUrl(mediaURL, 0);
        }
    }

    @Override
    public void playMedia() throws PlayException {
        if (impl != null) {
            impl.play();
        }
    }

    @Override
    public void stopMedia() {
        if (impl != null) {
            impl.pause();
            impl.seekTo(0, true);
        }
    }

    @Override
    public void pauseMedia() {
        if (impl != null) {
            impl.pause();
        }
    }

    @Override
    public long getMediaDuration() {
        if (impl != null) {
            return (long) impl.getDuration();
        }
        return 0;
    }

    @Override
    public double getPlayPosition() {
        if (impl != null) {
            return impl.getCurrentTime();
        }
        return 0;
    }

    @Override
    public void setPlayPosition(double position) {
        if (impl != null) {
            impl.seekTo(position, true);
        }
    }

    @Override
    public double getVolume() {
        if (impl != null) {
            return impl.getVolume();
        }
        return 0;
    }

    @Override
    public void setVolume(double volume) {
        if (impl != null) {
            impl.setVolume(volume);
        }
    }

    @Override
    public int getLoopCount() {
        return 1;
    }

    @Override
    public void setLoopCount(int loop) {
        if ((impl != null) && (loop < 0)) {
            setRepeatMode(RepeatMode.REPEAT_ALL);
        }
    }

    @Override
    public RepeatMode getRepeatMode() {
        return repeatMode;
    }

    @Override
    public void setRepeatMode(RepeatMode mode) {
        if (impl != null) {
            switch (mode) {
                case REPEAT_ALL:
                    impl.setLoop(true);
                    repeatMode = mode;
                    break;
                case REPEAT_OFF:
                    impl.setLoop(false);
                    repeatMode = mode;
            }
        }
    }

    /**
     * Checks whether the player controls are visible. This implementation
     * <b>always</b> return true.
     */
    @Override
    public boolean isControllerVisible() {
        return true;
    }

    /**
     * Sets the suggested video quality for the current video. This method
     * causes the video to reload at its current position in the new quality.
     *
     * <p> <b>Note:</b> Calling this method does not guarantee that the playback
     * quality will actually change. If the playback quality does change, it
     * will only change for the video being played and the
     * {@linkplain PlaybackQualityChangeEvent} event will be fired.
     *
     * <p> If {@code suggestedQuality} is not available for the current video,
     * then the quality will be set to the next lowest level that is available.
     * That is, if {@code suggestedQuality} is
     * {@linkplain PlaybackQuality#hd720} and that is unavailable, then the
     * playback quality will be set to {@linkplain PlaybackQuality#large} if
     * that quality level is available.
     *
     * @param suggestedQuality the suggested video quality for the current video
     */
    public void setPlaybackQuality(PlaybackQuality suggestedQuality) {
        if (impl != null) {
            impl.setPlaybackQuality(suggestedQuality.name().toLowerCase());
        }
    }

    /**
     * Retrieves the playback quality of the current video.
     *
     * @return the playback quality of the current video
     *
     * @throws IllegalStateException if no video is loaded in the player
     */
    public PlaybackQuality getPlaybackQuality() throws IllegalStateException {
        if (impl != null) {
            String qua = impl.getPlaybackQuality();
            if (qua.equals("undefined")) {
                throw new IllegalStateException("Player not loaded!");
            }
            return PlaybackQuality.valueOf(qua);
        }
        throw new IllegalStateException("Player not available");
    }

    /**
     * Returns the list of quality formats in which the current video is
     * available.
     *
     * <p>An empty list is returned if no video is loaded.
     *
     * @return a list of quality formats available for the current video
     */
    public ArrayList<PlaybackQuality> getAvailableQualityLevels() {
        ArrayList<PlaybackQuality> pqs = new ArrayList<PlaybackQuality>();
        if (impl != null) {
            JsArrayString qua = impl.getAvailableQualityLevels();
            for (int i = 0; i < qua.length(); i++) {
                pqs.add(PlaybackQuality.valueOf(qua.get(i)));
            }
        }
        return pqs;
    }

    /**
     * Adds a {@link PlaybackQualityChangeHandler} handler to the player
     *
     * @param handler handler for the PlaybackQualityChangeEvent event
     * @return {@link HandlerRegistration} used to remove the handler
     */
    public HandlerRegistration addPlaybackQualityChangeHandler(PlaybackQualityChangeHandler handler) {
        return addHandler(handler, PlaybackQualityChangeEvent.TYPE);
    }

    @Override
    public <C extends ConfigParameter> void setConfigParameter(C param, Object value) {
        super.setConfigParameter(param, value);
        if (param.getName().equals(DefaultConfigParameter.TransparencyMode.getName())) {
            if (value != null) {
                configParam.put("wmode", ((TransparencyMode) value).name().toLowerCase());
            } else {
                configParam.put("wmode", null);
            }
        } else if (param.getName().equals(YouTubeConfigParameter.URLParameters.getName())) {
            if (value != null) {
                pParams = (PlayerParameters) value;
            } else {
                pParams = null;
            }
        }
    }

    /**
     * Convenience method to retrieve the PlayerProvider implementation class
     * 
     * @return the PlayerProvider implementation class for this player.
     */
    protected YouTubePlayerProvider getProvider() {
        return (YouTubePlayerProvider) getWidgetFactory(YouTubePlayerProvider.PROVIDER_NAME);
    }

    /**
     * Roll into v 2.0
     *
     * @Override public void addToPlaylist(String mediaURL) {
     * ypm.addToPlaylist(mediaURL); }
     *
     * @Override public void addToPlaylist(String... mediaURLs) {
     * ypm.addToPlaylist(mediaURLs); }
     *
     * @Override public void addToPlaylist(MRL mediaLocator) {
     * ypm.addToPlaylist(mediaLocator); }
     *
     * @Override public void addToPlaylist(List<MRL> mediaLocators) {
     * ypm.addToPlaylist(mediaLocators); }
     *
     * @Override public void clearPlaylist() { ypm.clearPlaylist(); }
     *
     * @Override public void removeFromPlaylist(int index) {
     * ypm.removeFromPlaylist(index); }
     *
     * @Override public int getPlaylistSize() { if (impl != null) { return
     * impl.getPlaylist().length(); } return 0; }
     *
     * @Override public boolean isShuffleEnabled() { return
     * ypm.isShuffleEnabled(); }
     *
     * @Override public void play(int index) throws IndexOutOfBoundsException {
     * if (impl != null) { impl.playVideoAt(index); } }
     *
     * @Override public void playNext() throws PlayException { if (impl != null)
     * { impl.nextVideo(); } }
     *
     * @Override public void playPrevious() throws PlayException { if (impl !=
     * null) { impl.previousVideo(); } }
     *
     * @Override public void setShuffleEnabled(boolean enable) { if (impl !=
     * null) { ypm.setShuffleEnabled(enable); impl.setShuffle(enable); } }
     */
    private enum URLParameters {

        // AS3/HTML5 player params
        autohide, autoplay, color, controls, enablejsapi, iv_load_policy,
        loop, modestbranding, origin, playlist, rel, showinfo, start,
        theme,
        // AS3 player params
        disablekb, end, fs, list, listType, playerapiid
//egm,border,color1,color2,hd,showsearch 
        //cc_load_policy
    }

    /**
     * Default EventHandler implementation for YouTube player events.
     * @since 2.0
     */
    protected abstract class DefaultEventHandler implements YouTubePlayerProvider.EventHandler {

        @Override
        public void onYTReady() {
            firePlayerStateEvent(PlayerStateEvent.State.Ready);
        }

        @Override
        public void onYTStateChanged(int state) {
            switch (state) {
                case -1: // unstarted
                    fireDebug("Waiting for video...");
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
                    fireDebug("Buffering...");
                    break;
                case 5: // video cued
                    fireDebug("Video ready for playback");
                    break;
            }
        }

        @Override
        public void onYTQualityChanged(String quality) {
            PlaybackQuality pq = PlaybackQuality.Default;
            for (PlaybackQuality _pq : PlaybackQuality.values()) {
                if (_pq.name().toLowerCase().equals(quality)) {
                    pq = _pq;
                }
            }
            PlaybackQualityChangeEvent.fire(YouTubeBasePlayer.this, pq);
            fireDebug("Playback quality changed : " + quality);
        }

        @Override
        public void onYTError(int errorCode) {
            switch (errorCode) {
                case 2: // invalid parameter ...
                    fireError("Invalid Parameter !!!");
                    break;
                case 5: // HTML5 error ...
                    fireError("An HTML5 Player error has occured!");
                    break;
                case 100: // video not found. Occurs when video is removed (for any reason), or marked private.
                    fireError("Video not found! It may have been removed or marked private");
                    break;
                case 101: // video does not allow playback in the embedded players.
                case 150: // is the same as 101, it's just 101 in disguise!
                    fireError("Video playback not allowed");
                    break;
                default: // workarround for Issue 66
                    fireError("An unknown error has occured - API Error Code(" + errorCode + ")");
            }
        }
    }
}
