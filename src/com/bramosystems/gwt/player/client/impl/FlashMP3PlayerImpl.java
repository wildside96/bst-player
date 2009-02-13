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

import com.bramosystems.gwt.player.client.MediaStateListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Native implementation of the FlashMP3Player class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see com.bramosystems.gwt.player.client.ui.FlashMP3Player
 */
public class FlashMP3PlayerImpl {
    protected final static String SWF_URL = GWT.getModuleBaseURL() + "MP3Player.swf";
    private JavaScriptObject jso;

    public final void init(String playerId, MediaStateListener listener) {
        // check if player object exists...
        if (jso == null) {
            jso = JavaScriptObject.createObject();
            initGlobalCallbacks(jso);
        }
        initImpl(jso, playerId, listener);
    }

    public void injectScript(String divId, String playerId, String mediaURL,
            boolean autoplay, boolean visible) {
        cacheParamsImpl(jso, playerId, mediaURL, autoplay);
        injectScriptImpl(divId, getPlayerScript(playerId, mediaURL, autoplay, visible));
    }

    protected String getPlayerScript(String playerId, String mediaURL, boolean autoplay, boolean visible) {
        return "<embed src='" + SWF_URL + "' id='" + playerId + "' name='" + playerId + "' " +
                "flashVars='bridgeName=" + playerId + "&playerId=" + playerId + "&mediaSrc=" + mediaURL +
                "&autoplay=" + autoplay + "' allowScriptAccess='sameDomain' " +
                "type='application/x-shockwave-flash' " +
                (visible ? "" : "width='1px' height='1px'") + " ></embed>";
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
            player.loadSnd(jso[playerId]._mediaURL);
            if(jso[playerId]._autoplay == true) {
                player.playSnd(jso[playerId].playPosition);
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
     }-*/;

    protected native void initImpl(JavaScriptObject jso, String playerId, MediaStateListener listener) /*-{
    jso[playerId] = new Object();
    jso[playerId].playPosition = 0;

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
    jso[playerId].playPosition = 0;
    listener.@com.bramosystems.gwt.player.client.MediaStateListener::onPlayFinished()();
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
    }-*/;

    private native boolean isPlayerAvailableImpl(JavaScriptObject jso, String playerId) /*-{
        return (jso[playerId] != undefined) || (jso[playerId] != null);
     }-*/;

    private native void cacheParamsImpl(JavaScriptObject jso, String playerId, String mediaURL,
            boolean autoplay) /*-{
        jso[playerId]._mediaURL = mediaURL;
        jso[playerId]._autoplay = autoplay;
     }-*/;

    private native void injectScriptImpl(String divId, String script) /*-{
        var e = $doc.getElementById(divId);
        e.innerHTML = script;
    }-*/;

    private native void loadMediaImpl(JavaScriptObject jso, String playerId, String url) /*-{
        var player = $doc.getElementById(playerId);
        player.loadSnd(url);
    }-*/;

    private native void playMediaImpl(JavaScriptObject jso, String playerId) /*-{
        var player = $doc.getElementById(playerId);
        player.playSnd(jso[playerId].playPosition);
     }-*/;

    private native void stopMediaImpl(JavaScriptObject jso, String playerId, boolean rewind) /*-{
    try {
        var player = $doc.getElementById(playerId);
        jso[playerId].playPosition = rewind ? 0 : player.getPlayPosition();
        player.stopSnd();
    } catch(err) {
        jso[playerId].doDebug(err.message);
    }
    }-*/;

    private native void ejectMediaImpl(JavaScriptObject jso, String playerId) /*-{
//        jso[playerId].player.eject();
    }-*/;

    private native void closeMediaImpl(JavaScriptObject jso, String playerId) /*-{
        var player = $doc.getElementById(playerId);
        player.closeSnd();
        delete jso[playerId];
    }-*/;


    private native String getPlayPositionImpl(JavaScriptObject jso, String playerId) /*-{
        var player = $doc.getElementById(playerId);
        return player.getSndPlayPosition().toString();
    }-*/;

    private native void setPlayPositionImpl(JavaScriptObject jso, String playerId, double position) /*-{
        var player = $doc.getElementById(playerId);
        player.playSnd(position);
        jso[playerId].playPosition = position;
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
}