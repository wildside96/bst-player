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
import com.bramosystems.oss.player.core.client.ui.FlashMediaPlayer;
import java.util.HashMap;

/**
 * Native implementation of the FlashMediaPlayer class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see FlashMediaPlayer
 */
public class FlashMediaPlayerImpl {

    private HashMap<String, EventHandler> cache;

    public FlashMediaPlayerImpl() {
        cache = new HashMap<String, EventHandler>();
        initGlobalCallbacks(this);
    }

    public final void init(String playerId, String mediaURL, boolean autoplay,
            MediaStateListener listener) {
        cache.put(playerId, new EventHandler(playerId, mediaURL, autoplay, listener));
    }

    public final boolean isPlayerAvailable(String playerId) {
        return cache.containsKey(playerId) && isPlayerOnPageImpl(playerId);
    }

    public final void closeMedia(String playerId) {
        closeMediaImpl(playerId);
        cache.remove(playerId);
    }

    public final double getPlayPosition(String playerId) {
        return Double.parseDouble(getPlayPositionImpl(playerId));
    }

    public final double getVolume(String playerId) {
        return Double.parseDouble(getVolumeImpl(playerId));
    }

    public final double getMediaDuration(String playerId) {
        return Double.parseDouble(getMediaDurationImpl(playerId));
    }

    private void onState(String playerId, int stateId, int listIndex) {
        cache.get(playerId).onStateChange(stateId, listIndex);
    }

    private void onInit(String playerId) {
        cache.get(playerId).initComplete();
    }

    private void onDebug(String playerId, String message) {
        cache.get(playerId).onDebug(message);
    }

    private void onError(String playerId, String description) {
        cache.get(playerId).onError(description);
    }

    private void onProgress(String playerId, double progress) {
        cache.get(playerId).onLoading(progress);
    }

    private void onMediaInfo(String playerId, String info) {
        MediaInfo mi = new MediaInfo();
        fillMediaInfoImpl(info, mi);
        cache.get(playerId).onMediaInfo(mi);
    }

    private native void initGlobalCallbacks(FlashMediaPlayerImpl impl) /*-{
    $wnd.bstSwfMdaMediaStateChanged = function(playerId, state, listIndex){
    impl.@com.bramosystems.oss.player.core.client.impl.FlashMediaPlayerImpl::onState(Ljava/lang/String;II)(playerId, state, listIndex);
    }
    $wnd.bstSwfMdaInit = function(playerId){
    impl.@com.bramosystems.oss.player.core.client.impl.FlashMediaPlayerImpl::onInit(Ljava/lang/String;)(playerId);
    }
    $wnd.bstSwfMdaLoadingProgress = function(playerId, progress){
    impl.@com.bramosystems.oss.player.core.client.impl.FlashMediaPlayerImpl::onProgress(Ljava/lang/String;D)(playerId, progress);
    }
    $wnd.bstSwfMdaError = function(playerId, error){
    impl.@com.bramosystems.oss.player.core.client.impl.FlashMediaPlayerImpl::onError(Ljava/lang/String;Ljava/lang/String;)(playerId, error);
    }
    $wnd.bstSwfMdaDebug = function(playerId, message){
    impl.@com.bramosystems.oss.player.core.client.impl.FlashMediaPlayerImpl::onDebug(Ljava/lang/String;Ljava/lang/String;)(playerId, message);
    }
    $wnd.bstSwfMdaMetadata = function(playerId, id3){
    impl.@com.bramosystems.oss.player.core.client.impl.FlashMediaPlayerImpl::onMediaInfo(Ljava/lang/String;Ljava/lang/String;)(playerId, id3);
    }
    }-*/;

    private native void fillMediaInfoImpl(String infoCSV, MediaInfo mData) /*-{
    // parse from CSV like values ...
    // year[$]albumTitle[$]artists[$]comment[$]genre[$]title[$]
    // contentProviders[$]copyright[$]duration[$]hardwareSoftwareRequirements[$]
    // publisher[$]internetStationOwner[$]internetStationName

    csv = infoCSV.split("[$]");
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::year = csv[0];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::albumTitle = csv[1];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::artists = csv[2];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::comment = csv[3];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::genre = csv[4];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::title = csv[5];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::contentProviders = csv[6];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::copyright = csv[7];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::duration = parseFloat(csv[8]);
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::hardwareSoftwareRequirements = csv[9];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::publisher = csv[10];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::internetStationOwner = csv[11];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::internetStationName = csv[12];
    }-*/;

    private native String getPluginVersion(String playerId) /*-{
    var player = $doc.getElementById(playerId);
    return player.getMdaPlayerVer();
    }-*/;

    protected native boolean isPlayerOnPageImpl(String playerId) /*-{
    return $doc.getElementById(playerId) != null;
    }-*/;

    public native void loadMedia(String playerId, String url) /*-{
    var player = $doc.getElementById(playerId);
    player.loadMda(url);
    }-*/;

    public native void playMedia(String playerId) /*-{
    var player = $doc.getElementById(playerId);
    player.playMda();
    }-*/;

    public native boolean playMedia(String playerId, int index) /*-{
    var player = $doc.getElementById(playerId);
    return player.playMdaIndex(index);
    }-*/;

    public native boolean playNext(String playerId) /*-{
    var player = $doc.getElementById(playerId);
    return player.playNextMda();
    }-*/;

    public native boolean playPrevious(String playerId) /*-{
    var player = $doc.getElementById(playerId);
    return player.playPrevMda();
    }-*/;

    public native void stopMedia(String playerId) /*-{
    var player = $doc.getElementById(playerId);
    player.stopMda(true);
    }-*/;

    public native void pauseMedia(String playerId) /*-{
    var player = $doc.getElementById(playerId);
    player.stopMda(false);
    }-*/;

    private native void closeMediaImpl(String playerId) /*-{
    try {
    var player = $doc.getElementById(playerId);
    player.closeMda();
    }catch(err){}
    }-*/;

    private native String getPlayPositionImpl(String playerId) /*-{
    var player = $doc.getElementById(playerId);
    return player.getMdaPlayPosition().toString();
    }-*/;

    public native void setPlayPosition(String playerId, double position) /*-{
    var player = $doc.getElementById(playerId);
    player.setMdaPlayPosition(position);
    }-*/;

    private native String getMediaDurationImpl(String playerId) /*-{
    var player = $doc.getElementById(playerId);
    return player.getMdaDuration().toString();
    }-*/;

    public native void addToPlaylist(String playerId, String mediaURL) /*-{
    var player = $doc.getElementById(playerId);
    player.addToMdaPlaylist(mediaURL);
    }-*/;

    public native void removeFromPlaylist(String playerId, int index) /*-{
    var player = $doc.getElementById(playerId);
    player.removeFromMdaPlaylist(index);
    }-*/;

    public native void clearPlaylist(String playerId) /*-{
    var player = $doc.getElementById(playerId);
    player.clearMdaPlaylist();
    }-*/;

    public native int getPlaylistCount(String playerId) /*-{
    var player = $doc.getElementById(playerId);
    return player.getMdaPlaylistSize();
    }-*/;

    private native String getVolumeImpl(String playerId) /*-{
    var player = $doc.getElementById(playerId);
    return player.getMdaVolume().toString();
    }-*/;

    public native void setVolume(String playerId, double volume) /*-{
    var player = $doc.getElementById(playerId);
    player.setMdaVolume(volume);
    }-*/;

    public native void setLoopCount(String playerId, int count) /*-{
    var player = $doc.getElementById(playerId);
    player.setMdaLoopCount(count);
    }-*/;

    public native int getLoopCount(String playerId) /*-{
    var player = $doc.getElementById(playerId);
    player.getMdaLoopCount();
    }-*/;

    public native boolean isShuffleEnabled(String playerId) /*-{
    var player = $doc.getElementById(playerId);
    return player.isMdaShuffleEnabled();
    }-*/;

    public native void setShuffleEnabled(String playerId, boolean enable) /*-{
    var player = $doc.getElementById(playerId);
    player.setMdaShuffleEnabled(enable);
    }-*/;

    private class EventHandler {

        private MediaStateListener listener;
        private String id,  mediaUrl;
        private boolean autoplay;

        public EventHandler(String id, String mediaURL, boolean autoplay, MediaStateListener listener) {
            this.listener = listener;
            this.id = id;
            this.mediaUrl = mediaURL;
            this.autoplay = autoplay;
        }

        public void onStateChange(int newState, int listIndex) {
            switch (newState) {
                case 1: // loading started...
//                    listener.onPlayerReady();
                    break;
                case 2: // play started...
                    listener.onPlayStarted(listIndex);
                    break;
                case 9: // play finished...
                    listener.onPlayFinished(listIndex);
                    break;
                case 10: // loading complete ...
                    listener.onLoadingComplete();
                    break;
            }
        }

        public void initComplete() {
            listener.onDebug("Flash Player plugin");
            listener.onDebug("Version : " + getPluginVersion(id));
            loadMedia(id, mediaUrl);
            listener.onPlayerReady();
            if (autoplay) {
                playMedia(id);
            }
        }

        public void onLoading(double progress) {
            listener.onLoadingProgress(progress);
        }

        public void onError(String description) {
            listener.onError(description);
        }

        public void onDebug(String message) {
            listener.onDebug(message);
        }

        public void onMediaInfo(MediaInfo info) {
            listener.onMediaInfoAvailable(info);
        }
    }
}