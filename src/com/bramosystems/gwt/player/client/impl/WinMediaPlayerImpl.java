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

    public void init(String playerId) {
        if (jso == null) {
            jso = JavaScriptObject.createObject();
            initGlobalEventListeners(jso);
        }

        initPlayerImpl(jso, playerId);
    }

    public String getPlayerScript(String mediaURL, String playerId, boolean autoplay,
            String height, String width) {
        return "<object id='" + playerId + "' type='" + getPluginType() + "' data='' " +
                "width='" + width + "' height='" + height + "' autoplay='" + autoplay + "'>" +
                "<param name='autostart' value='" + autoplay + "' />" +
                "<param name='URL' value='" + mediaURL + "' />" +
                "</object>";
    }

    public void setMediaStateListener(String playerId, MediaStateListener listener) {
        createMediaStateListenerImpl(jso, playerId, listener);
        registerMediaStateListenerImpl(jso, playerId);
    }

    protected native void initGlobalEventListeners(JavaScriptObject jso) /*-{
    jso['gEvtListnrId'] = new Array();
    $wnd.OnDSPlayStateChangeEvt = function(NewState) {
        for(i = 0; i < jso['gEvtListnrId'].length; i++) {
            var pid = jso['gEvtListnrId'][i];
            jso[pid].playStateChange(NewState);
        }
     }
    $wnd.OnDSBufferingEvt = function(Start) {
        for(i = 0; i < jso['gEvtListnrId'].length; i++) {
            var pid = jso['gEvtListnrId'][i];
            jso[pid].buffering(Start);
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

    private native void createMediaStateListenerImpl(JavaScriptObject jso, String playerId,
            MediaStateListener listener) /*-{
    jso[playerId].error = function() {
        listener.@com.bramosystems.gwt.player.client.MediaStateListener::onIOError()();
    };
    jso[playerId].playStateChange = function(NewState) {
        switch(NewState) {
            case 3:    // playing..
                listener.@com.bramosystems.gwt.player.client.MediaStateListener::onPlayStarted()();
                break;
            case 8:    // media ended...
                listener.@com.bramosystems.gwt.player.client.MediaStateListener::onPlayFinished()();
                break;
        }
    };
    jso[playerId].loadingProgress = function() {
        plyr = $doc.getElementById(playerId);
        var prog = plyr.network.downloadProgress / 100;
        listener.@com.bramosystems.gwt.player.client.MediaStateListener::onLoadingProgress(D)(prog);
    };
    jso[playerId].buffering = function(Start) {
        if(Start == true) { // downloading started...
            jso[playerId].progTimerId = $wnd.setInterval(jso[playerId].loadingProgress(), 1000);
        } else {    // downloading stoped...
            $wnd.clearInterval(jso[playerId].progTimerId);
            listener.@com.bramosystems.gwt.player.client.MediaStateListener::onLoadingComplete()();
        }
    };
    }-*/;

    /**
     * Register event listeners with WMP
     * @param jso function wrapper
     * @param playerId player ID
     */
    protected native void registerMediaStateListenerImpl(JavaScriptObject jso, String playerId) /*-{
        jso['gEvtListnrId'].push(playerId);
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

    public native void close(String playerId) /*-{
    var player = $doc.getElementById(playerId);
    player.close();
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
