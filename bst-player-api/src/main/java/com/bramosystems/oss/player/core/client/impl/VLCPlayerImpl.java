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
import java.util.HashMap;

/**
 * Native implementation of the VLCPlayer class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see VLCPlayer
 */
public class VLCPlayerImpl {

    private Timer statePooler;
    private final int poolerPeriod = 500;
    private HashMap<String, StateHandler> cache;
    private boolean canCancelPooling;

    // Make package private....
    VLCPlayerImpl() {
        cache = new HashMap<String, StateHandler>();
        canCancelPooling = false;
    }

    public void init(String playerId, MediaStateListener listener) {
        cache.put(playerId, new StateHandler(playerId, listener));
    }

    public void injectScript(String divId, String mediaSrc, final String playerId,
            boolean autoplay, int height, int width) {
        injectScriptImpl(divId, getPlayerScript(mediaSrc, playerId, autoplay,
                height + "px", width + "px"));

        // pool for plugin ready ...
        statePooler = new Timer() {

            @Override
            public void run() {
                if ((cache.get(playerId).checkPlayState() == 0) && canCancelPooling) {
                    cancel();
                } else {
                    schedule(poolerPeriod);
                }
//                cache.get(playerId).debug("Play Length : " + getDuration(playerId));
            }
        };

        Timer t = new Timer() {

            @Override
            public void run() {
                if (isPlayerOnPage(playerId)) {
                    cancel();
                    cache.get(playerId).initComplete();
                    statePooler.schedule(poolerPeriod);
                } else {
                    schedule(800);
                }
            }
        };
        t.schedule(800);
    }

    protected String getPlayerScript(String mediaSrc, String playerId, boolean autoplay,
            String height, String width) {
        return "<embed id='" + playerId + "' name='" + playerId + "' loop='false' " +
                "target='" + mediaSrc + "' autoplay='" + autoplay + "' " +
                "type='application/x-vlc-plugin' version='VideoLAN.VLCPlugin.2' " +
                "width='" + width + "' height='" + height + "'></embed>";
    }

    public final boolean isPlayerAvailable(String playerId) {
        return cache.containsKey(playerId) && isPlayerOnPage(playerId);
    }

    private native boolean isPlayerOnPage(String playerId) /*-{
    return $doc.getElementById(playerId) != null;
    }-*/;

    private native void injectScriptImpl(String divId, String script) /*-{
    var e = $doc.getElementById(divId);
    e.innerHTML = script;
    }-*/;

    public void playMedia(String playerId) {
        cache.get(playerId).canFireStarted = true;
        playImpl(playerId);
        statePooler.run();
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

    public void loadSound(String playerId, String mediaURL) {
        loadImpl(playerId, mediaURL);
    }

    public int getMovieSize(String playerId) {
        return 0; //getMovieSizeImpl(jso, playerId);
    }

    public int getMaxBytesLoaded(String playerId) {
        return 0; //getMaxBytesLoadedImpl(jso, playerId);
    }

    public final int getLoopCount(String playerId) {
        return 1; //getLoopCountImpl(jso, playerId);
    }

    public final void setLoopCount(String playerId, int count) {
//        setLoopCountImpl(jso, playerId, count);
    }

    private native void playImpl(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    plyr.playlist.play();
    }-*/;

    private native void stopImpl(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    plyr.playlist.stop();
    }-*/;

    private native void pauseImpl(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    plyr.playlist.togglePause();
    }-*/;

    private native void loadImpl(String playerId, String mediaURL) /*-{
    var plyr = $doc.getElementById(playerId);
    plyr.playlist.add(mediaURL);
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
    return plyr.audio.volume / 200;
    }-*/;

    public native void setVolume(String playerId, double volume) /*-{
    var plyr = $doc.getElementById(playerId);
    plyr.audio.volume = parseInt(volume * 200);
    }-*/;

    public native void addToPlaylist(String playerId, String mediaURL) /*-{
    var plyr = $doc.getElementById(playerId);
    plyr.playlist.add(mediaURL);
    }-*/;

    public native void removeFromPlaylist(String playerId, int index) /*-{
    var player = $doc.getElementById(playerId);
    //    player.removeMediaFromPlaylist(index);
    }-*/;

    public native boolean isShuffleEnabled(String playerId) /*-{
    var player = $doc.getElementById(playerId);
    return player.isMediaShuffleOn();
    }-*/;

    public native void setShuffleEnabled(String playerId, boolean enable) /*-{
    var player = $doc.getElementById(playerId);
    player.setMediaShuffleOn(enable);
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
     $wnd.alert("Visible : " + plyr.visible);
     plyr.visible = false;
     $wnd.alert("Visible : " + plyr.visible);
    return plyr.VersionInfo;
    }-*/;

    private class StateHandler {

        public boolean canFireReady,  canFireStarted,  canFirePaused,  canFireStopped,  canFireFinished;
        private MediaStateListener listener;
        private String id;

        public StateHandler(String id, MediaStateListener listener) {
            canFireFinished = false;
            canFirePaused = false;
            canFireReady = false;
            canFireStarted = false;
            canFireStopped = false;

            this.id = id;
            this.listener = listener;
        }

        public int checkPlayState() {
            int state = getPlayerStateImpl(id);
//            listener.onDebug("Play State :: " + state);

            switch (state) {
                case -1:   // no input yet...
                    break;
                case 0:    // idle/close
                    if (canFireReady) {
                        playerReady();
                        canFireReady = false;
                    }
                case 6:    // finished
                    if (canFireFinished) {
                        canFireFinished = false;
                        playFinished();
                    }
                    break;
                case 1:    // opening
                    if (canFireReady) {
                    }
                    break;
                case 2:    // buffering
                    if (canFireReady) {
                    }
                    break;
                case 3:    // playing
                    if (canFireStarted) {
                        playStarted();
                        listener.onDebug("Media playback started");
                        canFireStarted = false;
                        canFireFinished = true;
                        canCancelPooling = true;
                        loadingComplete();
                    }
                    break;
                case 4:    // paused
                    if (canFirePaused) {
                        canFirePaused = false;
                        listener.onDebug("Media playback paused");
                    }
                    break;
                case 5:    // stopping
                    if (canFireStopped) {
                        canFireStopped = false;
                        listener.onDebug("Media playback stoped");
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

        public void initComplete() {
            listener.onDebug("VLC Media Player plugin");
            listener.onDebug("Version : " + getPluginVersionImpl(id));
            canFireReady = true;
        }

        public void playerReady() {
            listener.onPlayerReady();
            listener.onDebug("Plugin ready for media playback");
        }

        public void playStarted() {
            listener.onPlayStarted();
        }

        public void playFinished() {
            listener.onPlayFinished();
            listener.onDebug("Media playback complete");
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
    }
}
