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
package com.bramosystems.gwt.player.client.impl;

import com.bramosystems.gwt.player.client.MediaInfo;
import com.bramosystems.gwt.player.client.MediaStateListener;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Native implementation of the FlashMP3Player class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see com.bramosystems.gwt.player.client.ui.FlashMP3Player
 */
public class FlashMP3PlayerImpl {
    private JavaScriptObject jso;

    public FlashMP3PlayerImpl() {
        jso = JavaScriptObject.createObject();
        initGlobalCallbacks(jso);
    }

    public final void init(String playerId, String mediaURL, boolean autoplay,
            MediaStateListener listener) {
        initImpl(jso, playerId, mediaURL, autoplay, listener, new MediaInfo());
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
        pauseMediaImpl(jso, playerId);
    }

    public final void stopMedia(String playerId) {
        stopMediaImpl(jso, playerId);
    }

    public final void ejectMedia(String playerId) {
        ejectMediaImpl(jso, playerId);
    }

    public final void closeMedia(String playerId) {
        closeMediaImpl(jso, playerId);
    }

    public final double getPlayPosition(String playerId) {
        return Double.parseDouble(getPlayPositionImpl(jso, playerId));
    }

    public final void setPlayPosition(String playerId, double position) {
        setPlayPositionImpl(jso, playerId, position);
    }

    public final double getVolume(String playerId) {
        return Double.parseDouble(getVolumeImpl(jso, playerId));
    }

    public final void setVolume(String playerId, double volume) {
        setVolumeImpl(jso, playerId, volume);
    }

    public final int getLoopCount(String playerId) {
        return getLoopCountImpl(jso, playerId);
    }

    public final void setLoopCount(String playerId, int count) {
        setLoopCountImpl(jso, playerId, count);
    }

    public final double getMediaDuration(String playerId) {
        return Double.parseDouble(getMediaDurationImpl(jso, playerId));
    }

    protected native void initGlobalCallbacks(JavaScriptObject jso) /*-{
        $wnd.bstSwfSndMediaStateChanged = function(playerId, state){
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
        $wnd.bstSwfSndInit = function(playerId){
            var player = $doc.getElementById(playerId);
            jso[playerId].doDebug("FlashMP3Player instance");
            jso[playerId].doDebug("Flash Player plugin");
            jso[playerId].doDebug("Version : " + player.getSndPlayerVer());
            player.loadSnd(jso[playerId]._mediaURL);
            if(jso[playerId]._autoplay == true) {
                player.playSnd();
            }
        }
        $wnd.bstSwfSndLoadingProgress = function(playerId, progress){
            jso[playerId].progress(progress);
        }
        $wnd.bstSwfSndError = function(playerId, error){
            jso[playerId].errorr(error);
        }
        $wnd.bstSwfSndDebug = function(playerId, message){
            jso[playerId].doDebug(message);
        }
        $wnd.bstSwfSndID3 = function(playerId){
            jso[playerId].doID3();
        }
     }-*/;

    private native void initImpl(JavaScriptObject jso, String playerId, String mediaURL,
            boolean autoplay, MediaStateListener listener, MediaInfo id3) /*-{
    jso[playerId] = new Object();
    jso[playerId]._mediaURL = mediaURL;
    jso[playerId]._autoplay = autoplay;
    jso[playerId].loopCount = 0;

    // add callbacks...
    jso[playerId].errorr = function(message) {
    listener.@com.bramosystems.gwt.player.client.MediaStateListener::onError(Ljava/lang/String;)(message);
    };
    jso[playerId].playStarted = function() {
    listener.@com.bramosystems.gwt.player.client.MediaStateListener::onPlayStarted()();
    };
    jso[playerId].playerReady = function() {
    listener.@com.bramosystems.gwt.player.client.MediaStateListener::onPlayerReady()();
    };
    jso[playerId].playFinished = function() {
        if (jso[playerId].loopCount < 0) {
            jso[playerId].statPlay();
        } else {
            if (jso[playerId].loopCount > 1) {
                jso[playerId].statPlay();
                jso[playerId].loopCount--;
            } else {
                listener.@com.bramosystems.gwt.player.client.MediaStateListener::onPlayFinished()();
            }
        }
    };
    jso[playerId].statPlay = function() {
        var player = $doc.getElementById(playerId);
        player.playSnd();
    };
    jso[playerId].loaded = function() {
        listener.@com.bramosystems.gwt.player.client.MediaStateListener::onLoadingComplete()();
    };
    jso[playerId].progress = function(progress) {
    listener.@com.bramosystems.gwt.player.client.MediaStateListener::onLoadingProgress(D)(progress);
    };
    jso[playerId].doDebug = function(report) {
    listener.@com.bramosystems.gwt.player.client.MediaStateListener::onDebug(Ljava/lang/String;)(report);
    };
    jso[playerId].parseID3 = function(value) {
        return (value == undefined) ? '' : value;
    };
    jso[playerId].doID3 = function() {
        try {
            var playr = $doc.getElementById(playerId);
            var pInfo = playr.getSndID3();
            id3.@com.bramosystems.gwt.player.client.MediaInfo::year = jso[playerId].parseID3(pInfo.year);
            id3.@com.bramosystems.gwt.player.client.MediaInfo::albumTitle = jso[playerId].parseID3(pInfo.album);
            id3.@com.bramosystems.gwt.player.client.MediaInfo::artists = jso[playerId].parseID3(pInfo.artist);
            id3.@com.bramosystems.gwt.player.client.MediaInfo::comment = jso[playerId].parseID3(pInfo.comment);
            id3.@com.bramosystems.gwt.player.client.MediaInfo::genre = jso[playerId].parseID3(pInfo.genre);
            id3.@com.bramosystems.gwt.player.client.MediaInfo::title = jso[playerId].parseID3(pInfo.songName);
            id3.@com.bramosystems.gwt.player.client.MediaInfo::contentProviders = jso[playerId].parseID3(pInfo.TOLY);
            id3.@com.bramosystems.gwt.player.client.MediaInfo::copyright = jso[playerId].parseID3(pInfo.TOWN);
            id3.@com.bramosystems.gwt.player.client.MediaInfo::hardwareSoftwareRequirements = jso[playerId].parseID3(pInfo.TSSE);
            id3.@com.bramosystems.gwt.player.client.MediaInfo::publisher = jso[playerId].parseID3(pInfo.TPUB);
            id3.@com.bramosystems.gwt.player.client.MediaInfo::internetStationOwner = jso[playerId].parseID3(pInfo.TRSO);
            id3.@com.bramosystems.gwt.player.client.MediaInfo::internetStationName = jso[playerId].parseID3(pInfo.TRSN);
            listener.@com.bramosystems.gwt.player.client.MediaStateListener::onMediaInfoAvailable(Lcom/bramosystems/gwt/player/client/MediaInfo;)(id3);
        } catch(e) {
            jso[playerId].doDebug(e);
        }
    };
    }-*/;

    private native boolean isPlayerAvailableImpl(JavaScriptObject jso, String playerId) /*-{
        return (jso[playerId] != undefined) || (jso[playerId] != null);
     }-*/;

    private native void loadMediaImpl(JavaScriptObject jso, String playerId, String url) /*-{
        var player = $doc.getElementById(playerId);
        player.loadSnd(url);
    }-*/;

    private native void playMediaImpl(JavaScriptObject jso, String playerId) /*-{
        jso[playerId].statPlay();
     }-*/;

    private native void stopMediaImpl(JavaScriptObject jso, String playerId) /*-{
        var player = $doc.getElementById(playerId);
        player.stopSnd();
    }-*/;

    private native void pauseMediaImpl(JavaScriptObject jso, String playerId) /*-{
        var player = $doc.getElementById(playerId);
        player.pauseSnd();
    }-*/;

    private native void ejectMediaImpl(JavaScriptObject jso, String playerId) /*-{
//        jso[playerId].player.eject();
    }-*/;

    private native void closeMediaImpl(JavaScriptObject jso, String playerId) /*-{
        try {
            var player = $doc.getElementById(playerId);
            player.closeSnd();
        }catch(err) {
        }
        delete jso[playerId];
    }-*/;

    private native String getPlayPositionImpl(JavaScriptObject jso, String playerId) /*-{
        var player = $doc.getElementById(playerId);
        return player.getSndPlayPosition().toString();
    }-*/;

    private native void setPlayPositionImpl(JavaScriptObject jso, String playerId, double position) /*-{
        var player = $doc.getElementById(playerId);
        player.setSndPlayPosition(position);
     }-*/;

    private native String getMediaDurationImpl(JavaScriptObject jso, String playerId) /*-{
        var player = $doc.getElementById(playerId);
        return player.getSndDuration().toString();
    }-*/;

    private native String getVolumeImpl(JavaScriptObject jso, String playerId) /*-{
        var player = $doc.getElementById(playerId);
        return player.getSndVolume().toString();
    }-*/;

    private native void setVolumeImpl(JavaScriptObject jso, String playerId, double volume) /*-{
        var player = $doc.getElementById(playerId);
        player.setSndVolume(volume);
    }-*/;

    private native void setLoopCountImpl(JavaScriptObject jso, String playerId, int count) /*-{
        jso[playerId].loopCount = count;
    }-*/;

    private native int getLoopCountImpl(JavaScriptObject jso, String playerId) /*-{
        return jso[playerId].loopCount;
    }-*/;
}