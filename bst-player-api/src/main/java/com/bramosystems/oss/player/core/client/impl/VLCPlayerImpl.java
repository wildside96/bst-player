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
package com.bramosystems.oss.player.core.client.impl;

import com.bramosystems.oss.player.core.client.MediaInfo;
import com.bramosystems.oss.player.core.event.client.PlayerStateEvent;
import com.bramosystems.oss.player.core.event.client.HasMediaStateHandlers;
import com.bramosystems.oss.player.core.event.client.PlayStateEvent;
import com.bramosystems.oss.player.core.event.client.LoadingProgressEvent;
import com.bramosystems.oss.player.core.event.client.DebugEvent;
import com.bramosystems.oss.player.core.client.MediaStateListener;
import com.bramosystems.oss.player.core.client.ui.VLCPlayer;
import com.bramosystems.oss.player.core.event.*;
import com.bramosystems.oss.player.core.event.client.MediaInfoEvent;
import com.google.gwt.user.client.Timer;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Native implementation of the VLCPlayer class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see VLCPlayer
 */
public class VLCPlayerImpl {

    private HashMap<String, StateHandler> cache;
    private ArrayList<Integer> playlistIndexCache;

    VLCPlayerImpl() {
        cache = new HashMap<String, StateHandler>();
        playlistIndexCache = new ArrayList<Integer>();
    }

    public void init(String playerId, MediaStateListener listener, HasMediaStateHandlers handler) {
        cache.put(playerId, new StateHandler(playerId, listener, handler));
    }

    public String getPlayerScript(String playerId, int height, int width) {
        return "<embed id='" + playerId + "' name='" + playerId + "' loop='false' " +
                "target='' autoplay='false' type='application/x-vlc-plugin' " +
                "version='VideoLAN.VLCPlugin.2' width='" + width + "px' " +
                "height='" + height + "px' toolbar='false'></embed>";
    }

    public void initPlayer(String playerId, String mediaUrl, boolean autoplay, int height, int width) {
        cache.get(playerId).initPlayer(mediaUrl, autoplay, height, width);
    }

    protected void fixIEStyleBug(String playerId, int height, int width) {
    }

    public final boolean isPlayerAvailable(String playerId) {
        return cache.containsKey(playerId) && isPlayerOnPage(playerId);
    }

    public void play(String playerId) {
        playImpl(playerId);
    }

    public boolean playPrevious(String playerId) {
        playPreviousImpl(playerId);
        return true;
    }

    public boolean playNext(String playerId) {
        playNextImpl(playerId);
        return true;
    }

    public void playMedia(String playerId, int index) {
        playMediaImpl(playerId, playlistIndexCache.get(index));
    }

    public void stop(String playerId) {
        cache.get(playerId).stoppedByUser = true;
        stopImpl(playerId);
    }

    public void pause(String playerId) {
        pauseImpl(playerId);
    }

    public void close(String playerId) {
        cache.get(playerId).close();
        cache.remove(playerId);
    }

    public void load(String playerId, String mediaURL) {
        clearPlaylist(playerId);
        addToPlaylist(playerId, mediaURL, "");
    }

    public final int getLoopCount(String playerId) {
        return cache.get(playerId).getLoopCount();
    }

    public final void setLoopCount(String playerId, int count) {
        cache.get(playerId).setLoopCount(count);
    }

    public void addToPlaylist(String playerId, String mediaURL, String options) {
        int index = 0;
        if (options != null) {
            index = addToPlaylistImpl(playerId, mediaURL, options);
        } else {
            index = addToPlaylistImpl(playerId, mediaURL);
        }
        cache.get(playerId).debug("Added '" + mediaURL + "' to playlist @ #" + index + " with options [" + options + "]");
        playlistIndexCache.add(index);
    }

    public void removeFromPlaylist(String playerId, int index) {
        removeFromPlaylistImpl(playerId, playlistIndexCache.get(index));
        playlistIndexCache.remove(index);
    }

    public void clearPlaylist(String playerId) {
        clearPlaylistImpl(playerId);
        playlistIndexCache.clear();
    }

    private native boolean isPlayerOnPage(String playerId) /*-{
    return ($doc.getElementById(playerId) != null);
    }-*/;

    private native void playImpl(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    plyr.playlist.play();
    }-*/;

    private native void playMediaImpl(String playerId, int index) /*-{
    var player = $doc.getElementById(playerId);
    player.playlist.playItem(index);
    }-*/;

    private native void playNextImpl(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    plyr.playlist.next();
    }-*/;

    private native void playPreviousImpl(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    plyr.playlist.prev();
    }-*/;

    private native void stopImpl(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    plyr.playlist.stop();
    }-*/;

    private native void pauseImpl(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    plyr.playlist.togglePause();
    }-*/;

    public native double getTime(String playerId) /*-{
    try {
    var plyr = $doc.getElementById(playerId);
    return plyr.input.time;
    } catch(e) {
    return 0;
    }
    }-*/;

    public native void setTime(String playerId, double time) /*-{
    try {
    var plyr = $doc.getElementById(playerId);
    plyr.input.time = time;
    } catch(e) {}
    }-*/;

    public native double getDuration(String playerId) /*-{
    try {
    var plyr = $doc.getElementById(playerId);
    return parseFloat(plyr.input.length);
    } catch(e) {
    return 0;
    }
    }-*/;

    public native double getVolume(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    return plyr.audio.volume / 100.0;
    }-*/;

    public native void setVolume(String playerId, double volume) /*-{
    var plyr = $doc.getElementById(playerId);
    plyr.audio.volume = parseInt(volume * 100);
    }-*/;

    public native boolean isMute(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    return plyr.audio.mute;
    }-*/;

    public native void setMute(String playerId, boolean mute) /*-{
    var plyr = $doc.getElementById(playerId);
    plyr.audio.mute = mute;
    }-*/;

    public native double getRate(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    return plyr.input.rate;
    }-*/;

    public native void setRate(String playerId, double rate) /*-{
    var plyr = $doc.getElementById(playerId);
    plyr.input.rate = rate;
    }-*/;

    public native String getAspectRatio(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    return plyr.video.aspectRatio;
    }-*/;

    public native void setAspectRatio(String playerId, String aspect) /*-{
    var plyr = $doc.getElementById(playerId);
    plyr.video.aspectRatio = aspect;
    }-*/;

    public native String getVideoWidth(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    if(plyr.input.hasVout)
        return plyr.video.width;
     return 0;
    }-*/;

    public native String getVideoHeight(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    if(plyr.input.hasVout)
        return plyr.video.height;
     return 0;
    }-*/;

    public native void toggleFullScreen(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    plyr.video.toggleFullscreen();
    }-*/;

    public native boolean hasVideo(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    return plyr.input.hasVout;
    }-*/;

    public native int getCurrentAudioTrack(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    return plyr.audio.track;
    }-*/;

    public native int getAudioChannelMode(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    return plyr.audio.channel;
    }-*/;

    public native void setAudioChannelMode(String playerId, int mode) /*-{
    var plyr = $doc.getElementById(playerId);
    plyr.audio.channel = mode;
    }-*/;

    private native int addToPlaylistImpl(String playerId, String mediaURL) /*-{
    var plyr = $doc.getElementById(playerId);
    return plyr.playlist.add(mediaURL);
    }-*/;

    private native int addToPlaylistImpl(String playerId, String mediaURL, String options) /*-{
    var plyr = $doc.getElementById(playerId);
    var opts = new Array();
    opts.push(options);
    return plyr.playlist.add(mediaURL, 'silence', opts);
    }-*/;

    private native void removeFromPlaylistImpl(String playerId, int index) /*-{
    var player = $doc.getElementById(playerId);
    player.playlist.items.remove(index);
    }-*/;

    private native void clearPlaylistImpl(String playerId) /*-{
    var player = $doc.getElementById(playerId);
    player.playlist.items.clear();
    }-*/;

    public native int getPlaylistCount(String playerId) /*-{
    var player = $doc.getElementById(playerId);
    return player.playlist.items.count - 1; // [vlc://quit is also part of playlist]
    }-*/;

    private native int getPlayerStateImpl(String playerId) /*-{
    try{
    var player = $doc.getElementById(playerId);
    return player.input.state;
    } catch(e){
    return -1;
    }
    }-*/;

    private native String getPluginVersionImpl(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    return plyr.VersionInfo;
    }-*/;

    public native boolean isShuffleEnabled(String playerId) /*-{
    var player = $doc.getElementById(playerId);
    return player.isMediaShuffleOn();
    }-*/;

//    public native void setShuffleEnabled(String playerId, boolean enable) /*-{
//    var plyr = $doc.getElementById(playerId);
//    var index = plyr.playlist.add('', '', enable ? '--random' : '--no-random');
//     $wnd.alert('shuffle enabled');
//    }-*/;
    private native int setPlayerOptionImpl(String playerId, String options) /*-{
    var plyr = $doc.getElementById(playerId);
    return plyr.playlist.add('', '', options);
    }-*/;

    private native void fillMediaInfoImpl(String playerId, MediaInfo id3) /*-{
    try {
    var plyr = $doc.getElementById(playerId);
//    id3.@com.bramosystems.oss.player.core.client.MediaInfo::year = ;
//    id3.@com.bramosystems.oss.player.core.client.MediaInfo::albumTitle = ;
//    id3.@com.bramosystems.oss.player.core.client.MediaInfo::artists = ;
//    id3.@com.bramosystems.oss.player.core.client.MediaInfo::comment = ;
//    id3.@com.bramosystems.oss.player.core.client.MediaInfo::title = ;
//    id3.@com.bramosystems.oss.player.core.client.MediaInfo::contentProviders = ;
//    id3.@com.bramosystems.oss.player.core.client.MediaInfo::copyright = ;
//    id3.@com.bramosystems.oss.player.core.client.MediaInfo::hardwareSoftwareRequirements = ;
//    id3.@com.bramosystems.oss.player.core.client.MediaInfo::publisher =;
//    id3.@com.bramosystems.oss.player.core.client.MediaInfo::genre = ;
//    id3.@com.bramosystems.oss.player.core.client.MediaInfo::internetStationOwner = '';
//    id3.@com.bramosystems.oss.player.core.client.MediaInfo::internetStationName = '';
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::duration = parseFloat(plyr.input.length);

    if(plyr.input.hasVout) {
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::videoWidth = plyr.video.width;
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::videoHeight = plyr.video.height;
     }
    } catch(e) {
    }
    }-*/;


    private class StateHandler {

        // TODO: handle metadata firing for VLC
        private MediaStateListener listener;
        private String id;
        private Timer statePooler;
        private final int poolerPeriod = 200;
        private int loopCount,  _loopCount,  previousState;
        private HasMediaStateHandlers handlers;
        private boolean isBuffering, wasPlaying, stoppedByUser, canDoMetadata;

        public StateHandler(String _id, MediaStateListener listener, HasMediaStateHandlers handlers) {
            this.id = _id;
            this.listener = listener;
            this.handlers = handlers;

            loopCount = 1;
            _loopCount = 1;
            previousState = -10;
            wasPlaying = false;
            stoppedByUser = false;
            canDoMetadata = true;

            statePooler = new Timer() {

                @Override
                public void run() {
                    checkPlayState();
                }
            };
        }

        public void initPlayer(final String mediaUrl, final boolean autoplay,
                final int height, final int width) {
            Timer t = new Timer() {

                @Override
                public void run() {
                    if (isPlayerOnPage(id)) {
                        cancel();
                        fixIEStyleBug(id, height, width);

                        // init complete ...
                        debug("VLC Media Player plugin");
                        debug("Version : " + getPluginVersionImpl(id));
                        statePooler.scheduleRepeating(poolerPeriod);

                        // load player ...
                        addToPlaylist(id, mediaUrl, ":no-loop");

                        // fire player ready ...
                        debug("Plugin ready for media playback");
          //            listener.onPlayerReady();
                        PlayerStateEvent.fire(handlers, PlayerStateEvent.State.Ready);

                        // and play if required ...
                        if (autoplay) {
                            play(id);
                        }
                    } else {
                        schedule(700);
                    }
                }
            };
            t.schedule(100);
        }

        private int checkPlayState() {
            int state = getPlayerStateImpl(id);

            if (state == previousState) {
                return state;
            }

            switch (state) {
                case -1:   // no input yet...
                    break;
                case 0:    // idle/close
                    if(wasPlaying && stoppedByUser) {
                        wasPlaying = false;
                        debug("Media playback stopped");
                        PlayStateEvent.fire(handlers, PlayStateEvent.State.Stopped, 0);
                    } else if(wasPlaying && !stoppedByUser) {
                        // just in case we miss state 6 ...
                        wasPlaying = false;
                        debug("Media playback complete");
                        PlayStateEvent.fire(handlers, PlayStateEvent.State.Finished, 0);
                    }
                    break;
                case 6:    // finished
                    if (_loopCount > 1) {
                        _loopCount--;
                        playImpl(id);
                    } else if (_loopCount < 0) {
                        playImpl(id);
                    } else {
//                        listener.onPlayFinished(0);
                        PlayStateEvent.fire(handlers, PlayStateEvent.State.Finished, 0);
                        debug("Media playback complete");
                    }
                    break;
                case 1:    // opening media
                    debug("Opening media ...");
                    canDoMetadata = true;
                    break;
                case 2:    // buffering
                    debug("Buffering started");
//                        listener.onBuffering(true);
                    PlayerStateEvent.fire(handlers, PlayerStateEvent.State.BufferingStarted);
                    isBuffering = true;
                    break;
                case 3:    // playing
                    if (isBuffering) {
                        debug("Buffering stopped");
//                        listener.onBuffering(false);
                        PlayerStateEvent.fire(handlers, PlayerStateEvent.State.BufferingFinished);
                        isBuffering = false;
                    }

                    if(canDoMetadata) {
                        canDoMetadata = false;
                        MediaInfo info = new MediaInfo();
                        fillMediaInfoImpl(id, info);
                        MediaInfoEvent.fire(handlers, info);
                    }

                    debug("Current Track : " + getCurrentAudioTrack(id));
                    debug("Media playback started");
                    wasPlaying = true;
                    stoppedByUser = false;
                    PlayStateEvent.fire(handlers, PlayStateEvent.State.Started, 0);
//                        listener.onPlayStarted(0);

//                    loadingComplete();
                    break;
                case 4:    // paused
                    debug("Media playback paused");
                    PlayStateEvent.fire(handlers, PlayStateEvent.State.Paused, 0);
                    break;
                case 5:    // stopping
                    PlayStateEvent.fire(handlers, PlayStateEvent.State.Stopped, 0);
                    break;
                case 7:    // error
                    //                if(jso[playerId].canFirePlayerReady) {
                    //                   jso[playerId].errorr('Media playback stopped');
                    //                }
                    break;
                case 8:    // playback complete
                    //    if(jso[playerId].canFirePlayFinished) {
                    //        jso[playerId].canFirePlayFinished = false;
                    //        jso[playerId].playFinished();
                    //    }
                    break;
            }
            previousState = state;
            return state;
        }

        private void loadingComplete() {
//            listener.onLoadingComplete();
            LoadingProgressEvent.fire(handlers, 1.0);
        }

        private void onError() {
//            listener.onError("An error occured while loading media");
            DebugEvent.fire(handlers, DebugEvent.MessageType.Error,
                    "An error occured while loading media");
        }

        private void debug(String msg) {
//            listener.onDebug(msg);
            DebugEvent.fire(handlers, DebugEvent.MessageType.Info, msg);
        }

        public int getLoopCount() {
            return loopCount;
        }

        public void setLoopCount(int loopCount) {
            this.loopCount = loopCount;
            _loopCount = loopCount;
            debug("Loop Count set : " + loopCount);
        }

        public void close() {
            statePooler.cancel();
        }
    }
}
