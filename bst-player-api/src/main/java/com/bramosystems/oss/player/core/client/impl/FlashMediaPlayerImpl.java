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
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Native implementation of the FlashMediaPlayer class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see FlashMediaPlayer
 */
public class FlashMediaPlayerImpl {
    protected JavaScriptObject jso;

    public FlashMediaPlayerImpl() {
        jso = JavaScriptObject.createObject();
        initGlobalCallbacks(jso);
    }

    public final void init(String playerId, String mediaURL, MediaStateListener listener) {
        initImpl(jso, playerId, listener, mediaURL, new MediaInfo());
    }

    public final boolean isPlayerAvailable(String playerId) {
        return isPlayerAvailableImpl(jso, playerId);
    }

    public final void closeMedia(String playerId) {
        closeMediaImpl(jso, playerId);
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

    protected native void initGlobalCallbacks(JavaScriptObject jso) /*-{
        $wnd.bstSwfMdaMediaStateChanged = function(playerId, state){
            switch(state) {
                case 1: // loading started...
                     jso[playerId].playerReady();
                     break;
                case 2: // play started...
                    jso[playerId].playStarted();
                     break;
                case 9: // play finished...
                    jso[playerId].playFinished();
                     break;
                case 10: // loading complete ...
                    jso[playerId].loaded();
                    break;
            }
        }
        $wnd.bstSwfMdaInit = function(playerId){
            jso[playerId].initComplete();
        }
        $wnd.bstSwfMdaLoadingProgress = function(playerId, progress){
            jso[playerId].progress(progress);
        }
        $wnd.bstSwfMdaError = function(playerId, error){
            jso[playerId].errorr(error);
        }
        $wnd.bstSwfMdaDebug = function(playerId, message){
            jso[playerId].doDebug(message);
        }
        $wnd.bstSwfMdaMetadata = function(playerId, duration, hdwr){
            jso[playerId].doMetadata(duration, hdwr);
        }
        $wnd.bstSwfMdaID3 = function(playerId, id3){
            jso[playerId].doID3(id3);
        }
     }-*/;

    protected native void initImpl(JavaScriptObject jso, String playerId,
            MediaStateListener listener, String mediaURL, MediaInfo mData) /*-{
    jso[playerId] = new Object();

    jso[playerId].initComplete = function() {
        var player = $doc.getElementById(playerId);
        jso[playerId].doDebug("Flash Player plugin");
        jso[playerId].doDebug("Version : " + player.getMdaPlayerVer());
        player.loadMda(mediaURL);
    };

    // add callbacks...
    jso[playerId].errorr = function(message) {
    listener.@com.bramosystems.oss.player.core.client.MediaStateListener::onError(Ljava/lang/String;)(message);
    };
    jso[playerId].playStarted = function() {
    listener.@com.bramosystems.oss.player.core.client.MediaStateListener::onPlayStarted()();
    };
    jso[playerId].playerReady = function() {
    listener.@com.bramosystems.oss.player.core.client.MediaStateListener::onPlayerReady()();
    };
    jso[playerId].playFinished = function() {
        listener.@com.bramosystems.oss.player.core.client.MediaStateListener::onPlayFinished()();
    };
    jso[playerId].loaded = function() {
        listener.@com.bramosystems.oss.player.core.client.MediaStateListener::onLoadingComplete()();
    };
    jso[playerId].progress = function(progress) {
    listener.@com.bramosystems.oss.player.core.client.MediaStateListener::onLoadingProgress(D)(progress);
    };
    jso[playerId].doDebug = function(report) {
    listener.@com.bramosystems.oss.player.core.client.MediaStateListener::onDebug(Ljava/lang/String;)(report);
    };
    jso[playerId].parseID3 = function(value) {
        return (value == undefined) ? '' : value;
    };
    jso[playerId].doMetadata = function(duration, hdwr) {
        try {
            mData.@com.bramosystems.oss.player.core.client.MediaInfo::year = '';
            mData.@com.bramosystems.oss.player.core.client.MediaInfo::albumTitle = '';
            mData.@com.bramosystems.oss.player.core.client.MediaInfo::duration = parseFloat(duration) * 1000;
            mData.@com.bramosystems.oss.player.core.client.MediaInfo::artists = '';
            mData.@com.bramosystems.oss.player.core.client.MediaInfo::comment = '';
            mData.@com.bramosystems.oss.player.core.client.MediaInfo::genre = '';
            mData.@com.bramosystems.oss.player.core.client.MediaInfo::title = '';
            mData.@com.bramosystems.oss.player.core.client.MediaInfo::contentProviders = '';
            mData.@com.bramosystems.oss.player.core.client.MediaInfo::copyright = '';
            mData.@com.bramosystems.oss.player.core.client.MediaInfo::hardwareSoftwareRequirements = hdwr;
            mData.@com.bramosystems.oss.player.core.client.MediaInfo::publisher = '';
            mData.@com.bramosystems.oss.player.core.client.MediaInfo::internetStationOwner = '';
            mData.@com.bramosystems.oss.player.core.client.MediaInfo::internetStationName = '';
            listener.@com.bramosystems.oss.player.core.client.MediaStateListener::onMediaInfoAvailable(Lcom/bramosystems/oss/player/core/client/MediaInfo;)(mData);
        } catch(e) {
            jso[playerId].doDebug(e);
        }
    };
    jso[playerId].doID3 = function(pInfo) {
        try {
            mData.@com.bramosystems.oss.player.core.client.MediaInfo::year = jso[playerId].parseID3(pInfo.year);
            mData.@com.bramosystems.oss.player.core.client.MediaInfo::albumTitle = jso[playerId].parseID3(pInfo.album);
            mData.@com.bramosystems.oss.player.core.client.MediaInfo::artists = jso[playerId].parseID3(pInfo.artist);
            mData.@com.bramosystems.oss.player.core.client.MediaInfo::comment = jso[playerId].parseID3(pInfo.comment);
            mData.@com.bramosystems.oss.player.core.client.MediaInfo::genre = jso[playerId].parseID3(pInfo.genre);
            mData.@com.bramosystems.oss.player.core.client.MediaInfo::title = jso[playerId].parseID3(pInfo.songName);
            mData.@com.bramosystems.oss.player.core.client.MediaInfo::contentProviders = jso[playerId].parseID3(pInfo.TOLY);
            mData.@com.bramosystems.oss.player.core.client.MediaInfo::copyright = jso[playerId].parseID3(pInfo.TOWN);
            mData.@com.bramosystems.oss.player.core.client.MediaInfo::duration = parseFloat(jso[playerId].parseID3(pInfo.TLEN));
            mData.@com.bramosystems.oss.player.core.client.MediaInfo::hardwareSoftwareRequirements = jso[playerId].parseID3(pInfo.TSSE);
            mData.@com.bramosystems.oss.player.core.client.MediaInfo::publisher = jso[playerId].parseID3(pInfo.TPUB);
            mData.@com.bramosystems.oss.player.core.client.MediaInfo::internetStationOwner = jso[playerId].parseID3(pInfo.TRSO);
            mData.@com.bramosystems.oss.player.core.client.MediaInfo::internetStationName = jso[playerId].parseID3(pInfo.TRSN);
            listener.@com.bramosystems.oss.player.core.client.MediaStateListener::onMediaInfoAvailable(Lcom/bramosystems/oss/player/core/client/MediaInfo;)(mData);
        } catch(e) {
            jso[playerId].doDebug(e);
        }
    }
    }-*/;

    private native String getPluginVersion(String playerId) /*-{
        var player = $doc.getElementById(playerId);
        return player.getMdaPlayerVer();
    }-*/;

    private class EventHandler implements MediaStateListener {
        private MediaStateListener listener;
        private String id;

        public EventHandler(MediaStateListener listener, String id) {
            this.listener = listener;
            this.id = id;
        }

        public void initComplete() {
            listener.onDebug("Flash Player plugin");
            listener.onDebug("Version : " + getPluginVersion(id));
        }

        public void onPlayerReady() {
            listener.onPlayerReady();
        }

        public void onPlayStarted() {
            listener.onPlayStarted();
        }

        public void onPlayFinished() {
            listener.onPlayFinished();
        }

        public void onLoadingComplete() {
            listener.onLoadingComplete();
        }

        public void onLoadingProgress(double progress) {
            listener.onLoadingProgress(progress);
        }

        public void onError(String description) {
            listener.onError(description);
        }

        public void onDebug(String message) {
            listener.onDebug(message);
        }

        public void onMediaInfoAvailable(MediaInfo info) {
            listener.onMediaInfoAvailable(info);
        }

    }

    protected native boolean isPlayerAvailableImpl(JavaScriptObject jso, String playerId) /*-{
        return ((jso[playerId] != undefined) || (jso[playerId] != null)) &&
                ($doc.getElementById(playerId) != null);
     }-*/;

    public native void loadMedia(String playerId, String url) /*-{
        var player = $doc.getElementById(playerId);
        player.loadMda(url);
    }-*/;

    public native void playMedia(String playerId) /*-{
        var player = $doc.getElementById(playerId);
        player.playMda();
     }-*/;

    public native void stopMedia(String playerId) /*-{
        var player = $doc.getElementById(playerId);
        player.stopMda();
    }-*/;

    public native void pauseMedia(String playerId) /*-{
        var player = $doc.getElementById(playerId);
        player.pauseMda();
    }-*/;

    private native void closeMediaImpl(JavaScriptObject jso, String playerId) /*-{
        try {
            var player = $doc.getElementById(playerId);
            player.closeMda();
        }catch(err) {
        }
        delete jso[playerId];
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
        player.setMdaPlayCount(count);
    }-*/;

    public native int getLoopCount(String playerId) /*-{
        var player = $doc.getElementById(playerId);
        player.getMdaPlayCount();
    }-*/;

    public native void addToPlaylist(String playerId, String mediaURL) /*-{
        var player = $doc.getElementById(playerId);
        player.addMdaToPlaylist(mediaURL);
    }-*/;

    public native void removeFromPlaylist(String playerId, int index) /*-{
        var player = $doc.getElementById(playerId);
        player.removeMediaFromPlaylist(index);
    }-*/;

    public native boolean isShuffleEnabled(String playerId) /*-{
        var player = $doc.getElementById(playerId);
        return player.isMediaShuffleOn();
    }-*/;

    public native void setShuffleEnabled(String playerId, boolean enable) /*-{
        var player = $doc.getElementById(playerId);
        player.setMediaShuffleOn(enable);
    }-*/;
}