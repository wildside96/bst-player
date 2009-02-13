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
 * Native implementation of the QuickTimePlayer class. It is not recommended to
 * interact with this class directly.

 * @author Sikirulai Braheem
 * @see com.bramosystems.gwt.player.client.ui.QuickTimePlayer
 */
public class QuickTimePlayerImpl {
    private JavaScriptObject jso;

    // Make package private....
    QuickTimePlayerImpl() {
    }

    public void init(String playerId, MediaStateListener listener) {
        if(jso == null) {
            jso = JavaScriptObject.createObject();
        }

        initImpl(jso, playerId);
        createMediaStateListenerImpl(jso, playerId, listener);
    }

    public void injectScript(String divId, String mediaSrc, String playerId, boolean autoplay,
            String height, String width) {
        injectScriptImpl(divId, getPlayerScript(mediaSrc, playerId, autoplay, height, width));
        registerMediaStateListenerImpl(jso, playerId);
    }

    protected String getPlayerScript(String mediaSrc, String playerId, boolean autoplay,
            String height, String width) {
        String h = height == null ? "0px" : height;
        String w = width == null ? "0px" : width;
        return "<embed id='" + playerId + "' name='" + playerId + "' " +
                "type='video/quicktime' postdomevents='true' kioskmode='true' " +
                "autoplay='" + autoplay + "' src='" + mediaSrc + "' " +
                "width='" + w + "' height='" + h + "' EnableJavaScript='true'></embed> ";
    }

    public final boolean isPlayerAvailable(String playerId) {
        return isPlayerAvailableImpl(jso, playerId);
    }

    protected native void initImpl(JavaScriptObject jso, String playerId) /*-{
        if(jso[playerId] != null) {
            return;
        } else {
            jso[playerId] = new Object();
            jso[playerId].qtInit = false;
            jso[playerId].initComplete = function() {
                if(jso[playerId].qtInit == false) {
                    jso[playerId].qtInit = true;
                }
            };
        }
    }-*/;

    /**
     * Creates the DOM event listeners that required to monitor media state events.
     * @param jso object wrapping the event functions.
     * @param playerId the ID of the QT player
     * @param listener fired when QT emits DOM event occurs
     *
     * @see "JavaScript Scripting Guide for QuickTime"
     */
    protected native void createMediaStateListenerImpl(JavaScriptObject jso, String playerId,
            MediaStateListener listener) /*-{
    jso[playerId].errorr = function() {
    listener.@com.bramosystems.gwt.player.client.MediaStateListener::onError(Ljava/lang/String;)("An" +
             " error occured while loading media");
    };
    jso[playerId].playStarted = function() {
    listener.@com.bramosystems.gwt.player.client.MediaStateListener::onPlayStarted()();
    };
    jso[playerId].playerReady = function() {
    listener.@com.bramosystems.gwt.player.client.MediaStateListener::onPlayerReady()();
    };
    jso[playerId].soundComplete = function() {
    listener.@com.bramosystems.gwt.player.client.MediaStateListener::onPlayFinished()();
    };
    jso[playerId].loadingComplete = function() {
    listener.@com.bramosystems.gwt.player.client.MediaStateListener::onLoadingComplete()();
    };
    jso[playerId].loadingProgress = function() {
    var plyr = $doc.getElementById(playerId);
    var prog = plyr.GetMaxTimeLoaded() / plyr.GetDuration();
    listener.@com.bramosystems.gwt.player.client.MediaStateListener::onLoadingProgress(D)(prog);
    };
   }-*/;

    /**
     * Registers the QT object (playerId) for DOM events
     *
     * @param jso object wrapping the event functions.
     * @param playerId the ID of the QT player
     *
     * @see "JavaScript Scripting Guide for QuickTime"
     */
    protected native void registerMediaStateListenerImpl(JavaScriptObject jso, String playerId) /*-{
    var playr = $doc.getElementById(playerId);
    playr.addEventListener('qt_begin', jso[playerId].initComplete, false);
    playr.addEventListener('qt_load', jso[playerId].loadingComplete, false);
    playr.addEventListener('qt_ended', jso[playerId].soundComplete, false);
    playr.addEventListener('qt_error', jso[playerId].errorr, false);
    playr.addEventListener('qt_progress', jso[playerId].loadingProgress, false);
    playr.addEventListener('qt_play', jso[playerId].playStarted, false);
    playr.addEventListener('qt_canplay', jso[playerId].playerReady, false);
    }-*/;

    private native boolean isPlayerAvailableImpl(JavaScriptObject jso, String playerId) /*-{
        return (jso[playerId] != undefined) || (jso[playerId] != null);
     }-*/;

    private native void injectScriptImpl(String divId, String script) /*-{
        var e = $doc.getElementById(divId);
        e.innerHTML = script;
    }-*/;

    public void playMedia(String playerId){
        playImpl(jso, playerId);
    }

    public void stop(String playerId){
        stopImpl(jso, playerId);
    }

    public void pause(String playerId) {
        pauseImpl(jso, playerId);
    }

    public void close(String playerId) {
        closeImpl(jso, playerId);
    }

    public void loadSound(String playerId, String mediaURL){
        loadImpl(jso, playerId, mediaURL);
    }

    public double getTime(String playerId) {
        return getTimeImpl(jso, playerId);
    }

    public void setTime(String playerId, double time){
        setTimeImpl(jso, playerId, time);
    }

    public double getDuration(String playerId) {
        return getDurationImpl(jso, playerId);
    }

    public int getMovieSize(String playerId) {
        return getMovieSizeImpl(jso, playerId);
    }

    public int getMaxBytesLoaded(String playerId) {
        return getMaxBytesLoadedImpl(jso, playerId);
    }

    public int getVolume(String playerId) {
        return getVolumeImpl(jso, playerId);
    }

    public void setVolume(String playerId, int volume) {
        setVolumeImpl(jso, playerId, volume);
    }

    private native void playImpl(JavaScriptObject jso, String playerId) /*-{
    if(jso[playerId].qtInit == true) {
        var plyr = $doc.getElementById(playerId);
        plyr.Play();
    }
    }-*/;

    private native void stopImpl(JavaScriptObject jso, String playerId) /*-{
    if(jso[playerId].qtInit == true) {
        var plyr = $doc.getElementById(playerId);
        plyr.Rewind();
        plyr.Stop();
    }
    }-*/;

    private native void pauseImpl(JavaScriptObject jso, String playerId) /*-{
    if(jso[playerId].qtInit == true) {
        var plyr = $doc.getElementById(playerId);
        plyr.Stop();
    }
    }-*/;

    private native void closeImpl(JavaScriptObject jso, String playerId) /*-{
        delete jso[playerId];
    }-*/;

    private native void loadImpl(JavaScriptObject jso, String playerId, String mediaURL) /*-{
    if(jso[playerId].qtInit == true) {
        var plyr = $doc.getElementById(playerId);
        plyr.SetURL(mediaURL);
    }
    }-*/;

    private native double getTimeImpl(JavaScriptObject jso, String playerId) /*-{
    if(jso[playerId].qtInit == true) {
        var plyr = $doc.getElementById(playerId);
        return parseFloat(plyr.GetTime() / plyr.GetTimeScale()) * 1000;
    }
    return 0;
    }-*/;

    private native void setTimeImpl(JavaScriptObject jso, String playerId, double time) /*-{
    if(jso[playerId].qtInit == true) {
        var plyr = $doc.getElementById(playerId);
        plyr.SetTime(parseInt(time / 1000 * plyr.GetTimeScale()));
    }
    }-*/;

    private native double getDurationImpl(JavaScriptObject jso, String playerId) /*-{
    if(jso[playerId].qtInit == true) {
        var plyr = $doc.getElementById(playerId);
        return parseFloat(plyr.GetDuration() / plyr.GetTimeScale() * 1000);
    }
    return 0;
    }-*/;

    private native int getMovieSizeImpl(JavaScriptObject jso, String playerId) /*-{
    if(jso[playerId].qtInit == true) {
        var plyr = $doc.getElementById(playerId);
        return $doc.getElementById(playerId).GetMovieSize();
    }
    return 0;
    }-*/;

    private native int getMaxBytesLoadedImpl(JavaScriptObject jso, String playerId) /*-{
    if(jso[playerId].qtInit == true) {
        var plyr = $doc.getElementById(playerId);
        return $doc.getElementById(playerId).GetMaxBytesLoaded();
    }
    return 0;
    }-*/;

    private native int getVolumeImpl(JavaScriptObject jso, String playerId) /*-{
    if(jso[playerId].qtInit == true) {
        var plyr = $doc.getElementById(playerId);
        return plyr.GetVolume();
    }
    return 0;
    }-*/;

    private native void setVolumeImpl(JavaScriptObject jso, String playerId, int volume) /*-{
    if(jso[playerId].qtInit == true) {
        var plyr = $doc.getElementById(playerId);
        plyr.SetVolume(volume);
    }
    }-*/;
}
