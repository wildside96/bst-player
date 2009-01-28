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
    protected final static String SWF_URL = GWT.getModuleBaseURL() + "MP3Player-fab.swf";
    private JavaScriptObject jso;

    public final void init(String playerId, MediaStateListener listener, boolean debug) {
        // check if player object exists...
        if (jso == null) {
            jso = JavaScriptObject.createObject();
        }
        initImpl(jso, playerId, listener, debug);
    }

    public String getPlayerScript(String playerId, boolean visible) {
        return "<embed src='" + SWF_URL + "' id='" + playerId + "' " +
                "name='" + playerId + "' flashVars='bridgeName=" + playerId + "' " +
                "allowScriptAccess='sameDomain' type='application/x-shockwave-flash' " +
                (visible ? "" : "width='0' height='0'") + " ></embed>";
    }

    private String getDownloadFlashNotice() {
        return "This content requires the Adobe Flash Player. " +
                "<a href=http://www.adobe.com/go/getflash/>Get Flash</a>";
    }

    public final void loadMedia(String playerId, String url, boolean autoplay) {
        loadMediaImpl(jso, playerId, url);
        if (autoplay) {
            playMedia(playerId);
        }
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

    public final double getMediaDuration(String playerId) {
        return Double.parseDouble(getMediaDurationImpl(jso, playerId));
    }

    public final boolean isPlayerInit(String playerId) {
        return isPlayerInit(jso, playerId);
    }

    private native boolean isPlayerInit(JavaScriptObject jso, String playerId) /*-{
    return jso[playerId].init;
    }-*/;

    protected native void initImpl(JavaScriptObject jso, String playerId,
            MediaStateListener listener, boolean debug) /*-{
    jso[playerId] = new Object();
    jso[playerId].init = false;

    // add callbacks...
    jso[playerId].error = function(event) {
    listener.@com.bramosystems.gwt.player.client.MediaStateListener::onIOError()();
//    listener.@com.bramosystems.gwt.player.client.MediaStateListener::onDebug(Ljava/lang/String;)(event.text);
    };
    jso[playerId].playStarted = function() {
    listener.@com.bramosystems.gwt.player.client.MediaStateListener::onPlayStarted()();
    };
    jso[playerId].playFinished = function(event) {
    listener.@com.bramosystems.gwt.player.client.MediaStateListener::onPlayFinished()();
    };
    jso[playerId].loaded = function(event) {
        listener.@com.bramosystems.gwt.player.client.MediaStateListener::onLoadingComplete()();
    };
    jso[playerId].progress = function(event) {
    var prog = event.getBytesLoaded() / event.getBytesTotal();
    listener.@com.bramosystems.gwt.player.client.MediaStateListener::onLoadingProgress(D)(prog);
    };
    jso[playerId].doDebug = function(report) {
    listener.@com.bramosystems.gwt.player.client.MediaStateListener::onDebug(Ljava/lang/String;)(report);
    };
    jso[playerId].initCallback = function() {
        jso[playerId].init = true;
        jso[playerId].player = $wnd.FABridge[playerId].root().initPlayer(debug);
    };
    $wnd.FABridge.addInitializationCallback(playerId, jso[playerId].initCallback);
    }-*/;

    private native void loadMediaImpl(JavaScriptObject jso, String playerId, String url) /*-{
        jso[playerId].player.loadSound(url, jso[playerId].progress,
             jso[playerId].loaded, jso[playerId].error);
    }-*/;

    private native void playMediaImpl(JavaScriptObject jso, String playerId) /*-{
        jso[playerId].player.play(jso[playerId].playFinished);
        jso[playerId].playStarted();
     }-*/;

    private native void pauseMediaImpl(JavaScriptObject jso, String playerId) /*-{
    try {
        jso[playerId].player.pause();
    } catch(err) {
        jso[playerId].doDebug(err.message);
    }
    }-*/;

    private native void stopMediaImpl(JavaScriptObject jso, String playerId) /*-{
    try {
        jso[playerId].player.stop();
    } catch(err) {
        jso[playerId].doDebug(err.message);
    }
    }-*/;

    private native void ejectMediaImpl(JavaScriptObject jso, String playerId) /*-{
        jso[playerId].player.eject();
    }-*/;

    private native void closeMediaImpl(JavaScriptObject jso, String playerId) /*-{
        jso[playerId] = null;
        $wnd.FABridge[playerId].root().closePlayer();
    }-*/;

    private native String getPlayPositionImpl(JavaScriptObject jso, String playerId) /*-{
        return jso[playerId].player.getPlayPosition().toString();
    }-*/;

    private native void setPlayPositionImpl(JavaScriptObject jso, String playerId, double position) /*-{
        jso[playerId].player.setPlayPosition(position);
    }-*/;

    private native String getMediaDurationImpl(JavaScriptObject jso, String playerId) /*-{
     var duration = jso[playerId].player.getTotalDuration();
     return duration.toString();
    }-*/;

    private native String getVolumeImpl(JavaScriptObject jso, String playerId) /*-{
        var vol = jso[playerId].player.getVolume();
        return vol.toString();
    }-*/;

    private native void setVolumeImpl(JavaScriptObject jso, String playerId, double volume) /*-{
        jso[playerId].player.setVolume(volume);
    }-*/;

}
