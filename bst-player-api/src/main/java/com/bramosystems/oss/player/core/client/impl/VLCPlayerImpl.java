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

import com.bramosystems.oss.player.core.client.MediaStateListener;
import com.bramosystems.oss.player.core.client.ui.VLCPlayer;
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
    private ArrayList<Integer> listIndexCache;

    VLCPlayerImpl() {
        cache = new HashMap<String, StateHandler>();
        listIndexCache = new ArrayList<Integer>();
    }

    public void init(String playerId, MediaStateListener listener) {
        cache.put(playerId, new StateHandler(playerId, listener));
    }

    public String getPlayerScript(String playerId, int height, int width) {
        return "<embed id='" + playerId + "' name='" + playerId + "' loop='false' " +
                "target='' autoplay='false' type='application/x-vlc-plugin' " +
                "version='VideoLAN.VLCPlugin.2' width='" + width + "px' " +
                "height='" + height + "px'></embed>";
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
        cache.get(playerId).canFireStarted = true;
        playImpl(playerId);
    }

    public boolean playPrevious(String playerId) {
        cache.get(playerId).canFireStarted = true;
        playPreviousImpl(playerId);
        return true;
    }

    public boolean playNext(String playerId) {
        cache.get(playerId).canFireStarted = true;
        playNextImpl(playerId);
        return true;
    }

    public void playMedia(String playerId, int index) {
        cache.get(playerId).canFireStarted = true;
        playMediaImpl(playerId, listIndexCache.get(index));
    }

    public void stop(String playerId) {
        cache.get(playerId).canFireStopped = true;
        stopImpl(playerId);
    }

    public void pause(String playerId) {
        cache.get(playerId).canFirePaused = true;
        pauseImpl(playerId);
    }

    public void close(String playerId) {
        cache.remove(playerId);
    }

    public void load(String playerId, String mediaURL) {
        clearPlaylist(playerId);
        addToPlaylist(playerId, mediaURL);
    }

    public final int getLoopCount(String playerId) {
        return cache.get(playerId).getLoopCount();
    }

    public final void setLoopCount(String playerId, int count) {
        cache.get(playerId).setLoopCount(count);
    }

    public void setShuffleEnabled(String playerId, boolean enable) {
        int index = setPlayerOptionImpl(playerId, enable ? "--random" : "--no-random");
        cache.get(playerId).debug("Added '' to playlist @ #" + index);
        listIndexCache.add(index);
    }

    public void addToPlaylist(String playerId, String mediaURL) {
        int index = addToPlaylistImpl(playerId, mediaURL);
        cache.get(playerId).debug("Added '" + mediaURL + "' to playlist @ #" + index);
        listIndexCache.add(index);
    }

    public void removeFromPlaylist(String playerId, int index) {
        removeFromPlaylistImpl(playerId, listIndexCache.get(index));
    }

    public void clearPlaylist(String playerId) {
        clearPlaylistImpl(playerId);
        listIndexCache.clear();
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
    return plyr.audio.volume / 200.0;
    }-*/;

    public native void setVolume(String playerId, double volume) /*-{
    var plyr = $doc.getElementById(playerId);
    plyr.audio.volume = parseInt(volume * 200);
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
    return player.playlist.items.count;
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

    public native int setPlayerOptionImpl(String playerId, String options) /*-{
    var plyr = $doc.getElementById(playerId);
    return plyr.playlist.add('', '', options);
    }-*/;

    private class StateHandler {

        private boolean canFireReady,  canFireStarted,  canFirePaused;
        private boolean canFireStopped,  canFireFinished,  canFireBuffering;
        private boolean isBuffering;
        private MediaStateListener listener;
        private String id;
        private Timer statePooler;
        private final int poolerPeriod = 800;
        private int loopCount, _loopCount;

        public StateHandler(String _id, MediaStateListener listener) {
            this.id = _id;
            this.listener = listener;

            canFireFinished = false;
            canFirePaused = false;
            canFireReady = false;
            canFireStarted = false;
            canFireStopped = false;
            canFireBuffering = true;
            isBuffering = false;
            loopCount = 1;
            _loopCount = 1;

            statePooler = new Timer() {

                @Override
                public void run() {
                    checkPlayState();
                    schedule(poolerPeriod);
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
                        statePooler.run();
                        initComplete();
                        addToPlaylist(id, mediaUrl);
                        playerReady();
                        canFireReady = false;
                        canFireStarted = true;
                        if (autoplay) {
                            play(id);
                        }
                    } else {
                        schedule(500);
                    }
                }
            };
            t.run();
        }

        public int checkPlayState() {
            int state = getPlayerStateImpl(id);

            switch (state) {
                case -1:   // no input yet...
                    break;
                case 0:    // idle/close
                    if (canFireReady) {
                        playerReady();
                        canFireReady = false;
                        canFireStarted = true;
                    }
                case 6:    // finished
                    if(_loopCount > 1) {
                        _loopCount--;
//                        canFireStarted = true;
                        playImpl(id);
                    } else if(_loopCount < 0) {
                        playImpl(id);
                    }else if (canFireFinished) {
                        listener.onPlayFinished(0);
                        listener.onDebug("Media playback complete");
                        canFireFinished = false;
                    }
                    break;
                case 1:    // opening
                    if (canFireReady) {
                    }
                    break;
                case 2:    // buffering
                    if (canFireBuffering) {
                        debug("Buffering started");
                        canFireBuffering = false;
                        listener.onBuffering(true);
                        isBuffering = true;
                    }
                    break;
                case 3:    // playing
                    if (isBuffering) {
                        debug("Buffering stopped");
                        listener.onBuffering(false);
                        isBuffering = false;
                    }

                    if (canFireStarted) {
                        debug("Current Track : " + getCurrentAudioTrack(id));
                        listener.onDebug("Media playback started");
                        listener.onPlayStarted(0);

                        loadingComplete();
                        canFireStarted = false;
                        canFireFinished = true;
                        canFirePaused = true;
                        canFireStopped = true;
                        canFireBuffering = true;
                    }
                    break;
                case 4:    // paused
                    if (canFirePaused) {
                        canFirePaused = false;
                        canFireStarted = true;
                        canFireStopped = true;
                        canFireBuffering = true;
                        listener.onDebug("Media playback paused");
                    }
                    break;
                case 5:    // stopping
                    if (canFireStopped) {
                        canFireStopped = false;
                        canFireStarted = true;
                        canFireBuffering = true;
                        listener.onDebug("Media playback stopped");
                    }
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
            return state;
        }

        private void initComplete() {
            listener.onDebug("VLC Media Player plugin");
            listener.onDebug("Version : " + getPluginVersionImpl(id));
            canFireReady = true;
        }

        private void playerReady() {
            listener.onDebug("Plugin ready for media playback");
            listener.onPlayerReady();
        }

        public void loadingComplete() {
            listener.onLoadingComplete();
        }

        public void onError() {
            listener.onError("An error occured while loading media");
        }

        public void debug(String msg) {
            listener.onDebug(msg);
        }

        public int getLoopCount() {
            return loopCount;
        }

        public void setLoopCount(int loopCount) {
            this.loopCount = loopCount;
            _loopCount = loopCount;
            debug("Loop Count set : " + loopCount);
        }

    }
}
