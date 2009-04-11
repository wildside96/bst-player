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
package com.bramosystems.gwt.player.core.client.impl;

import com.bramosystems.gwt.player.core.client.MediaInfo;
import com.bramosystems.gwt.player.core.client.MediaStateListener;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Native implementation of the FLVPlayer class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see com.bramosystems.gwt.player.client.ui.FLVPlayer
 */
public class FlashVideoPlayerImpl {
    private JavaScriptObject jso;

    public FlashVideoPlayerImpl() {
        jso = JavaScriptObject.createObject();
        initGlobalCallbacks(jso);
    }

    public final void init(String playerId, String mediaURL, boolean autoplay,
            MediaStateListener listener) {
        initImpl(jso, playerId, listener, mediaURL, autoplay, new MediaInfo());
    }

    public final boolean isPlayerAvailable(String playerId) {
        return isPlayerAvailableImpl(jso, playerId);
    }

    public final void loadMedia(String playerId, String url) {
        loadMediaImpl(jso, playerId, url);
    }

    public final void playMedia(String playerId) {
        playMediaImpl(jso, playerId);
    }

    public final void pauseMedia(String playerId) {
        stopMediaImpl(jso, playerId, false);
    }

    public final void stopMedia(String playerId) {
        stopMediaImpl(jso, playerId, true);
    }

    public final void ejectMedia(String playerId) {
        ejectMediaImpl(jso, playerId);
    }

    public final void closeMedia(String playerId) {
        closeMediaImpl(jso, playerId);
    }

    public final double getPlayPosition(String playerId) {
        return Double.parseDouble(getPlayPositionImpl(jso, playerId)) * 1000;
    }

    public final void setPlayPosition(String playerId, double position) {
        setPlayPositionImpl(jso, playerId, position / 1000.0);
    }

    public final double getVolume(String playerId) {
        return Double.parseDouble(getVolumeImpl(jso, playerId));
    }

    public final void setVolume(String playerId, double volume) {
        setVolumeImpl(jso, playerId, volume);
    }

    public final double getMediaDuration(String playerId) {
        return Double.parseDouble(getMediaDurationImpl(jso, playerId)) * 1000;
    }

    public final int getLoopCount(String playerId) {
        return getLoopCountImpl(jso, playerId);
    }

    public final void setLoopCount(String playerId, int count) {
        setLoopCountImpl(jso, playerId, count);
    }

    protected native void initGlobalCallbacks(JavaScriptObject jso) /*-{
        $wnd.bstSwfVidMediaStateChanged = function(playerId, state){
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
        $wnd.bstSwfVidInit = function(playerId){
            jso[playerId].initComplete();
        }
        $wnd.bstSwfVidLoadingProgress = function(playerId, progress){
            jso[playerId].progress(progress);
        }
        $wnd.bstSwfVidError = function(playerId, error){
            jso[playerId].errorr(error);
        }
        $wnd.bstSwfVidDebug = function(playerId, message){
            jso[playerId].doDebug(message);
        }
        $wnd.bstSwfVidMetadata = function(playerId, duration, hdwr){
            jso[playerId].doMetadata(duration, hdwr);
        }
     }-*/;

    protected native void initImpl(JavaScriptObject jso, String playerId,
            MediaStateListener listener, String mediaURL, boolean autoplay, MediaInfo mData) /*-{
    jso[playerId] = new Object();
    jso[playerId].loopCount = 0;

    jso[playerId].initComplete = function() {
        var player = $doc.getElementById(playerId);
        jso[playerId].doDebug("FlashVideoPlayer instance");
        jso[playerId].doDebug("Flash Player plugin");
        jso[playerId].doDebug("Version : " + player.getVidPlayerVer());
        player.loadVid(mediaURL);
        if(autoplay) {
           player.playVid();
        }
    };

    // add callbacks...
    jso[playerId].errorr = function(message) {
    listener.@com.bramosystems.gwt.player.core.client.MediaStateListener::onError(Ljava/lang/String;)(message);
    };
    jso[playerId].playStarted = function() {
    listener.@com.bramosystems.gwt.player.core.client.MediaStateListener::onPlayStarted()();
    };
    jso[playerId].playerReady = function() {
    listener.@com.bramosystems.gwt.player.core.client.MediaStateListener::onPlayerReady()();
    };
    jso[playerId].playFinished = function() {
        if (jso[playerId].loopCount < 0) {
            jso[playerId].statPlay();
        } else {
            if (jso[playerId].loopCount > 1) {
                jso[playerId].statPlay();
                jso[playerId].loopCount--;
            } else {
                listener.@com.bramosystems.gwt.player.core.client.MediaStateListener::onPlayFinished()();
            }
        }
    };
    jso[playerId].statPlay = function() {
        var player = $doc.getElementById(playerId);
        player.playVid();
    };
    jso[playerId].loaded = function() {
        listener.@com.bramosystems.gwt.player.core.client.MediaStateListener::onLoadingComplete()();
    };
    jso[playerId].progress = function(progress) {
    listener.@com.bramosystems.gwt.player.core.client.MediaStateListener::onLoadingProgress(D)(progress);
    };
    jso[playerId].doDebug = function(report) {
    listener.@com.bramosystems.gwt.player.core.client.MediaStateListener::onDebug(Ljava/lang/String;)(report);
    };
    jso[playerId].parseID3 = function(value) {
        return (value == undefined) ? '' : value;
    };
    jso[playerId].doMetadata = function(duration, hdwr) {
        try {
            mData.@com.bramosystems.gwt.player.core.client.MediaInfo::year = '';
            mData.@com.bramosystems.gwt.player.core.client.MediaInfo::albumTitle = '';
            mData.@com.bramosystems.gwt.player.core.client.MediaInfo::duration = parseFloat(duration) * 1000;
            mData.@com.bramosystems.gwt.player.core.client.MediaInfo::artists = '';
            mData.@com.bramosystems.gwt.player.core.client.MediaInfo::comment = '';
            mData.@com.bramosystems.gwt.player.core.client.MediaInfo::genre = '';
            mData.@com.bramosystems.gwt.player.core.client.MediaInfo::title = '';
            mData.@com.bramosystems.gwt.player.core.client.MediaInfo::contentProviders = '';
            mData.@com.bramosystems.gwt.player.core.client.MediaInfo::copyright = '';
            mData.@com.bramosystems.gwt.player.core.client.MediaInfo::hardwareSoftwareRequirements = hdwr;
            mData.@com.bramosystems.gwt.player.core.client.MediaInfo::publisher = '';
            mData.@com.bramosystems.gwt.player.core.client.MediaInfo::internetStationOwner = '';
            mData.@com.bramosystems.gwt.player.core.client.MediaInfo::internetStationName = '';
            listener.@com.bramosystems.gwt.player.core.client.MediaStateListener::onMediaInfoAvailable(Lcom/bramosystems/gwt/player/core/client/MediaInfo;)(mData);
        } catch(e) {
            jso[playerId].doDebug(e);
        }
    };
    }-*/;

    protected native boolean isPlayerAvailableImpl(JavaScriptObject jso, String playerId) /*-{
        return (jso[playerId] != undefined) || (jso[playerId] != null);
     }-*/;

    protected native void loadMediaImpl(JavaScriptObject jso, String playerId, String url) /*-{
        var player = $doc.getElementById(playerId);
        player.loadVid(url);
    }-*/;

    protected native void playMediaImpl(JavaScriptObject jso, String playerId) /*-{
        jso[playerId].statPlay();
     }-*/;

    protected native void stopMediaImpl(JavaScriptObject jso, String playerId, boolean rewind) /*-{
        var player = $doc.getElementById(playerId);
        player.stopVid(rewind);
    }-*/;

    protected native void ejectMediaImpl(JavaScriptObject jso, String playerId) /*-{
    }-*/;

    protected native void closeMediaImpl(JavaScriptObject jso, String playerId) /*-{
//        var player = $doc.getElementById(playerId);
//        player.closeVid();
        delete jso[playerId];
    }-*/;


    protected native String getPlayPositionImpl(JavaScriptObject jso, String playerId) /*-{
        var player = $doc.getElementById(playerId);
        return player.getVidPlayPosition().toString();
    }-*/;

    private native void setPlayPositionImpl(JavaScriptObject jso, String playerId, double position) /*-{
        var player = $doc.getElementById(playerId);
        player.setVidPlayPosition(position);
     }-*/;

    protected native String getMediaDurationImpl(JavaScriptObject jso, String playerId) /*-{
        var player = $doc.getElementById(playerId);
        return player.getVidDuration().toString();
    }-*/;

    protected native String getVolumeImpl(JavaScriptObject jso, String playerId) /*-{
        var player = $doc.getElementById(playerId);
        return player.getVidVolume().toString();
    }-*/;

    protected native void setVolumeImpl(JavaScriptObject jso, String playerId, double volume) /*-{
        var player = $doc.getElementById(playerId);
        player.setVidVolume(volume);
    }-*/;

    private native void setLoopCountImpl(JavaScriptObject jso, String playerId, int count) /*-{
        jso[playerId].loopCount = count;
    }-*/;

    private native int getLoopCountImpl(JavaScriptObject jso, String playerId) /*-{
        return jso[playerId].loopCount;
    }-*/;
}