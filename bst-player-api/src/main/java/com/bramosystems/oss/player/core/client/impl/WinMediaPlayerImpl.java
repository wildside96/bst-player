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
import com.bramosystems.oss.player.core.client.MediaStateListener;
import com.bramosystems.oss.player.core.client.ui.WinMediaPlayer;
import com.google.gwt.user.client.Timer;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Native implementation of the WinMediaPlayer class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see WinMediaPlayer
 */
public class WinMediaPlayerImpl {

    protected HashMap<String, StateHandler> cache;

    WinMediaPlayerImpl() {
        cache = new HashMap<String, StateHandler>();
        initGlobalEventListeners(this);
    }

    public void init(String playerId, MediaStateListener listener) {
        cache.put(playerId, new StateHandler(playerId, listener));
    }

    public String getPlayerScript(String mediaURL, String playerId, boolean autoplay,
            String uiMode, int height, int width) {
        return "<object id='" + playerId + "' type='" + getPluginType() + "' " +
                "width='" + width + "px' height='" + height + "px' >" +
                "<param name='autostart' value='" + autoplay + "' />" +
                "<param name='URL' value='" + mediaURL + "' />" +
                "<param name='uiMode' value='" + uiMode + "' /> " +
                "</object>";
    }

    public final boolean isPlayerAvailable(String playerId) {
        return cache.containsKey(playerId) && isPlayerOnPageImpl(playerId);
    }

    public void play(String playerId) {
        playImpl(playerId);
    }

    public void stop(String playerId) {
        stopImpl(playerId);
    }

    public void close(String playerId) {
        cache.remove(playerId);
    }

    private void firePlayStateChanged() {
        Iterator<String> keys = cache.keySet().iterator();
        while (keys.hasNext()) {
            cache.get(keys.next()).checkPlayState();
        }
    }

    private void fireError() {
        Iterator<String> keys = cache.keySet().iterator();
        while (keys.hasNext()) {
            String id = keys.next();
            cache.get(id).onError(getErrorDiscriptionImpl(id));
        }
    }

    private void fireBuffering(boolean buffering) {
        Iterator<String> keys = cache.keySet().iterator();
        while (keys.hasNext()) {
            cache.get(keys.next()).doBuffering(buffering);
        }
    }

    protected native void initGlobalEventListeners(WinMediaPlayerImpl impl) /*-{
    $wnd.OnDSPlayStateChangeEvt = function(NewState) {
    impl.@com.bramosystems.oss.player.core.client.impl.WinMediaPlayerImpl::firePlayStateChanged()();
    }
    $wnd.OnDSErrorEvt = function() {
    impl.@com.bramosystems.oss.player.core.client.impl.WinMediaPlayerImpl::fireError()();
    }
    $wnd.OnDSBufferingEvt = function(Start) {
    impl.@com.bramosystems.oss.player.core.client.impl.WinMediaPlayerImpl::fireBuffering(Z)(Start);
    }
    }-*/;

    public void registerMediaStateListener(String playerId) {
        // do nothing, provided for DOM event registration in IE.
    }

    /**
     * Gets WMP plugin type based on mime types available
     * @return
     */
    private native String getPluginType() /*-{
    if (navigator.mimeTypes && navigator.mimeTypes['application/x-ms-wmp']) {
    return "application/x-ms-wmp"; // wmp plugin for firefox
    } else {
    return "application/x-mplayer2"; // generic wmp
    }
    }-*/;

    private native boolean isPlayerOnPageImpl(String playerId) /*-{
    return ($doc.getElementById(playerId) != null);
    }-*/;

    public native void loadSound(String playerId, String mediaURL) /*-{
    var player = $doc.getElementById(playerId);
    player.URL = mediaURL;
    }-*/;

    public native void pause(String playerId) /*-{
    var player = $doc.getElementById(playerId);
    player.controls.pause();
    }-*/;

    public native double getDuration(String playerId) /*-{
    var player = $doc.getElementById(playerId);
    return player.currentMedia.duration * 1000;
    }-*/;

    public native double getCurrentPosition(String playerId) /*-{
    var player = $doc.getElementById(playerId);
    return player.controls.currentPosition * 1000;
    }-*/;

    public native void setCurrentPosition(String playerId, double position) /*-{
    var player = $doc.getElementById(playerId);
    player.controls.currentPosition = position / 1000;
    }-*/;

    public native int getVolume(String playerId) /*-{
    var player = $doc.getElementById(playerId);
    return player.settings.volume;
    }-*/;

    public native void setVolume(String playerId, int volume) /*-{
    var player = $doc.getElementById(playerId);
    player.settings.volume = volume;
    }-*/;

    public native void setUIMode(String playerId, String uiMode) /*-{
    var player = $doc.getElementById(playerId);
    player.uiMode = uiMode;
    }-*/;

    public native void setLoopCount(String playerId, int count) /*-{
    try {
    var playr = $doc.getElementById(playerId);
    playr.settings.playCount = count;
    } catch(e) {}
    }-*/;

    public native int getLoopCount(String playerId) /*-{
    try {
    var playr = $doc.getElementById(playerId);
    if(playr.settings.getMode("loop")){
    return -1;
    }else {
    return playr.settings.playCount;
    }
    } catch(e) {
    return 0;
    }
    }-*/;

    private native void playImpl(String playerId) /*-{
    try {
    var playr = $doc.getElementById(playerId);
    playr.controls.play();
    } catch(e) {}
    }-*/;

    private native void stopImpl(String playerId) /*-{
    try {
    var playr = $doc.getElementById(playerId);
    playr.controls.stop();
    } catch(e) {}
    }-*/;

    private native String getPlayerVersionImpl(String playerId) /*-{
    var playr = $doc.getElementById(playerId);
    return playr.versionInfo;
    }-*/;

    private native int getPlayStateImpl(String playerId) /*-{
    var playr = $doc.getElementById(playerId);
    var state = playr.playState;
    if(state == undefined) {
    return -10;
    }
    return playr.playState;
    }-*/;

    private native void fillMetadataImpl(String playerId, MediaInfo info, String errorMsg) /*-{
    try {
    var plyrMedia = $doc.getElementById(playerId).currentMedia;
    info.@com.bramosystems.oss.player.core.client.MediaInfo::title = plyrMedia.getItemInfo('Title');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::copyright = plyrMedia.getItemInfo('Copyright');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::duration = parseFloat(plyrMedia.getItemInfo('Duration')) * 1000;
    info.@com.bramosystems.oss.player.core.client.MediaInfo::publisher = plyrMedia.getItemInfo('WM/Publisher');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::comment = plyrMedia.getItemInfo('Description');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::year = plyrMedia.getItemInfo('WM/Year');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::albumTitle = plyrMedia.getItemInfo('WM/AlbumTitle');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::artists = plyrMedia.getItemInfo('WM/AlbumArtist');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::contentProviders = plyrMedia.getItemInfo('WM/Provider');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::genre = plyrMedia.getItemInfo('WM/Genre');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::internetStationOwner = plyrMedia.getItemInfo('WM/RadioStationOwner');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::internetStationName = plyrMedia.getItemInfo('WM/RadioStationName');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::hardwareSoftwareRequirements = plyrMedia.getItemInfo('WM/EncodingSettings');
    } catch(e) {
    errorMsg = e;
    }
    }-*/;

    protected native String getErrorDiscriptionImpl(String playerId) /*-{
    var playr = $doc.getElementById(playerId);
    var err = playr.error;
    if(err == undefined)
    return '';

    return err.item(0).errorDescription;
    }-*/;

    private native double getDownloadProgressImpl(String playerId) /*-{
    var playr = $doc.getElementById(playerId);
    if(playr.network) {
    return playr.network.downloadProgress / 100;
    } else {
    return -1;
    }
    }-*/;

    private native String getMediaURLImpl(String playerId) /*-{
    var player = $doc.getElementById(playerId);
    return player.URL;
    }-*/;

    protected class StateHandler {

        private MediaStateListener listener;
        private String id;
        private Timer downloadProgressTimer;

        public StateHandler(final String id, MediaStateListener listener) {
            this.id = id;
            this.listener = listener;
            downloadProgressTimer = new Timer() {

                @Override
                public void run() {
                    StateHandler.this.listener.onLoadingProgress(getDownloadProgressImpl(id));
                }
            };
        }

        public String getId() {
            return id;
        }

        public void checkPlayState() {
            int state = getPlayStateImpl(id);
            if (state < 0) {
                return;
            }

            switch (state) {
                case 1:    // stopped..
                    debug("Media playback stopped");
                    break;
                case 2:    // paused..
                    debug("Media playback paused");
                    break;
                case 3:    // playing..
                    listener.onPlayStarted();
                    debug("Playing media at " + getMediaURLImpl(id));

                    // do metadata ...
                    doMetadata();
                    break;
                case 8:    // media ended...
                    listener.onPlayFinished();
                    listener.onDebug("Media playback finished");
                    break;
                case 10:    // player ready...
                    listener.onPlayerReady();
                    listener.onDebug("Windows Media Player plugin");
                    listener.onDebug("Version : " + getPlayerVersionImpl(id));
                    break;
                case 6:    // buffering ...
                case 11:    // reconnecting to stream  ...
                    break;
            }
        }

        public void onError(String message) {
            listener.onError(message);
        }

        public void debug(String msg) {
            listener.onDebug(msg);
        }

        private void doMetadata() {
            MediaInfo info = new MediaInfo();
            String err = "";
            fillMetadataImpl(id, info, err);
            if (err.length() == 0) {
                listener.onMediaInfoAvailable(info);
            } else {
                onError(err);
            }
        }

        public void doBuffering(boolean buffering) {
            if (buffering) {
                downloadProgressTimer.scheduleRepeating(1000);
            } else {
                downloadProgressTimer.cancel();
                listener.onLoadingComplete();
                listener.onDebug("Media loading complete");
            }
        }
    }
}
