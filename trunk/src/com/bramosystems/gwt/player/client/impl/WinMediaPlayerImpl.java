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
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Native implementation of the WinMediaPlayer class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see com.bramosystems.gwt.player.client.ui.WinMediaPlayer
 */
public class WinMediaPlayerImpl {

    private JavaScriptObject jso;

    WinMediaPlayerImpl() {
    }

    public void init(String playerId, MediaStateListener listener) {
        if (jso == null) {
            jso = JavaScriptObject.createObject();
            initGlobalEventListeners(jso);
        }

        initPlayerImpl(jso, playerId);
        createMediaStateListenerImpl(jso, playerId, listener);
    }

    public String getPlayerScript(String mediaURL, String playerId, boolean autoplay,
            String height, String width) {
        String h = height == null ? "1px" : height;
        String w = width == null ? "1px" : width;
        return "<object id='" + playerId + "' type='" + getPluginType() + "' " +
                "width='" + w + "' height='" + h + "' >" +
                "<param name='autostart' value='" + autoplay + "' />" +
                "<param name='URL' value='" + mediaURL + "' />" +
                "</object>";
    }

   public void registerMediaStateListener(String playerId){
       registerMediaStateListenerImpl(jso, playerId);
   }

    public final boolean isPlayerAvailable(String playerId) {
        return isPlayerAvailableImpl(jso, playerId);
    }

    protected native void initGlobalEventListeners(JavaScriptObject jso) /*-{
    jso['geId'] = new Array();
    $wnd.OnDSPlayStateChangeEvt = function(NewState) {
     for(i = 0; i < jso['geId'].length; i++) {
            var pid = jso['geId'][i];
            jso[pid].playStateChange(NewState);
        }
     }
    $wnd.OnDSErrorEvt = function() {
        for(i = 0; i < jso['geId'].length; i++) {
            var pid = jso['geId'][i];
            jso[pid].errorr();
        }
     }
    }-*/;

    private native void initPlayerImpl(JavaScriptObject jso, String playerId) /*-{
    if(jso[playerId] != null) {
        return;
    } else {
        jso[playerId] = new Object();
    }
    }-*/;

    /**
     * Create native MediaStateListener functions
     * @param jso function wrapper
     * @param playerId player ID
     * @param listener callback
     */
    protected native void createMediaStateListenerImpl(JavaScriptObject jso, String playerId,
            MediaStateListener listener) /*-{
    jso[playerId].errorr = function() {
        var playr = $doc.getElementById(playerId);
        var err = playr.error;
        if(err == undefined)
           return;

        var desc = err.item(0).errorDescription;
        listener.@com.bramosystems.gwt.player.client.MediaStateListener::onError(Ljava/lang/String;)(desc);
    };
    jso[playerId].playStateChange = function(NewState) {
        var playr = $doc.getElementById(playerId);
        var state = playr.playState;
        if(state == undefined)
           return;

        switch(state) {
            case 3:    // playing..
                listener.@com.bramosystems.gwt.player.client.MediaStateListener::onPlayStarted()();
                break;
            case 6:    // buffering ...
                if(playr.network.downloadProgress >=  0) {
                    jso[playerId].progTimerId = $wnd.setInterval(function() {
                        if(playr.network) {
                            var prog = playr.network.downloadProgress / 100;
                            if(prog < 1) {
                                listener.@com.bramosystems.gwt.player.client.MediaStateListener::onLoadingProgress(D)(prog);
                            } else {
                                listener.@com.bramosystems.gwt.player.client.MediaStateListener::onLoadingComplete()();
                                $wnd.clearInterval(jso[playerId].progTimerId);
                                delete jso[playerId].progTimerId;
                            }
                        } else {
                            $wnd.clearInterval(jso[playerId].progTimerId);
                            delete jso[playerId].progTimerId;
                        }
                     }, 1000);
                }
                break;
            case 8:    // media ended...
                listener.@com.bramosystems.gwt.player.client.MediaStateListener::onPlayFinished()();
                break;
            case 10:    // player ready...
             listener.@com.bramosystems.gwt.player.client.MediaStateListener::onPlayerReady()();
             break;
        }
    };
    jso['geId'].push(playerId);
    }-*/;

    /**
     * Register event listeners with WMP
     * @param jso function wrapper
     * @param playerId player ID
     */
    protected native void registerMediaStateListenerImpl(JavaScriptObject jso, String playerId) /*-{
    }-*/;

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

    private native boolean isPlayerAvailableImpl(JavaScriptObject jso, String playerId) /*-{
        return (jso[playerId] != undefined) || (jso[playerId] != null);
     }-*/;

    public void close(String playerId) {
        closeImpl(jso, playerId);
    }

    public native void loadSound(String playerId, String mediaURL) /*-{
    var player = $doc.getElementById(playerId);
    player.URL = mediaURL;
    }-*/;

    public native void play(String playerId) /*-{
    var player = $doc.getElementById(playerId);
    player.controls.play();
    }-*/;

    public native void pause(String playerId) /*-{
    var player = $doc.getElementById(playerId);
    player.controls.pause();
    }-*/;

    public native void stop(String playerId) /*-{
    var player = $doc.getElementById(playerId);
    player.controls.stop();
    }-*/;

    public native double getDuration(String playerId) /*-{
    var player = $doc.getElementById(playerId);
    // WMP duration is secs, convert to millisecs
     return player.currentMedia.duration * 1000;
    }-*/;

    public native double getCurrentPosition(String playerId) /*-{
    var player = $doc.getElementById(playerId);
    // WMP position is secs, convert to millisecs
    return player.controls.currentPosition * 1000;
    }-*/;

    public native void setCurrentPosition(String playerId, double position) /*-{
    var player = $doc.getElementById(playerId);
    player.controls.currentPosition = position / 1000;
    }-*/;

    protected native void closeImpl(JavaScriptObject jso, String playerId) /*-{
     delete jso[playerId];
    }-*/;

    public native int getVolume(String playerId) /*-{
    var player = $doc.getElementById(playerId);
    return player.settings.volume;
    }-*/;

    public native void setVolume(String playerId, int volume) /*-{
    var player = $doc.getElementById(playerId);
    player.settings.volume = volume;
    }-*/;
}
