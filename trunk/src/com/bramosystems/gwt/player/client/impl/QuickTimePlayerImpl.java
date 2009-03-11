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
 * Native implementation of the QuickTimePlayer class. It is not recommended to
 * interact with this class directly.
 *
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
        createMediaStateListenerImpl(jso, playerId, listener, new MediaInfo());
    }

    public void injectScript(String divId, String mediaSrc, String playerId, boolean autoplay,
            boolean showControls, int height, int width) {
        injectScriptImpl(divId, getPlayerScript(mediaSrc, playerId, autoplay, 
                showControls, height + "px", width + "px"));
        registerMediaStateListenerImpl(jso, playerId);
    }

    protected String getPlayerScript(String mediaSrc, String playerId, boolean autoplay,
            boolean showControls, String height, String width) {
        return "<embed id='" + playerId + "' name='" + playerId + "' " +
                "type='video/quicktime' postdomevents='true' kioskmode='true' " +
                "autoplay='" + autoplay + "' src='" + mediaSrc + "' " +
                "controller='" + showControls + "' EnableJavaScript='true' " +
                "width='" + width + "' height='" + height + "'></embed>";
    }

    public final boolean isPlayerAvailable(String playerId) {
        return isPlayerAvailableImpl(jso, playerId);
    }

    protected native void initImpl(JavaScriptObject jso, String playerId) /*-{
        if(jso[playerId] != null) {
            return;
        } else {
            jso[playerId] = new Object();
            jso[playerId].loopCount = 0;
            jso[playerId].qtInit = false;
            jso[playerId].initComplete = function() {
                if(jso[playerId].qtInit == false) {
                    jso[playerId].qtInit = true;
                    jso[playerId].debug('QuickTime Player plugin');
                    var plyr = $doc.getElementById(playerId);
                    jso[playerId].debug('Version : ' + plyr.GetPluginVersion());
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
    private native void createMediaStateListenerImpl(JavaScriptObject jso, String playerId,
            MediaStateListener listener, MediaInfo id3) /*-{
    jso[playerId].errorr = function() {
        listener.@com.bramosystems.gwt.player.client.MediaStateListener::onError(Ljava/lang/String;)("An" +
             " error occured while loading media");
    };
    jso[playerId].debug = function(message) {
        listener.@com.bramosystems.gwt.player.client.MediaStateListener::onDebug(Ljava/lang/String;)(message);
    };
    jso[playerId].playStarted = function() {
        listener.@com.bramosystems.gwt.player.client.MediaStateListener::onPlayStarted()();
        var plyr = $doc.getElementById(playerId);
        jso[playerId].debug('Playing media at ' + plyr.GetURL());
        jso[playerId].doMetadata();
    };
    jso[playerId].playerReady = function() {
        listener.@com.bramosystems.gwt.player.client.MediaStateListener::onPlayerReady()();
        jso[playerId].debug('Plugin ready for media playback');
    };
    jso[playerId].statPlay = function() {
        var plyr = $doc.getElementById(playerId);
        plyr.Play();
    };
    jso[playerId].soundComplete = function() {
        if (jso[playerId].loopCount < 0) {
            jso[playerId].statPlay();
        } else {
            if (jso[playerId].loopCount > 0) {
                jso[playerId].statPlay();
                jso[playerId].loopCount--;
            } else {
                listener.@com.bramosystems.gwt.player.client.MediaStateListener::onPlayFinished()();
                jso[playerId].debug('Media playback complete');
            }
        }
    };
    jso[playerId].loadingComplete = function() {
        listener.@com.bramosystems.gwt.player.client.MediaStateListener::onLoadingComplete()();
        jso[playerId].debug('Media loading complete');
    };
    jso[playerId].loadingProgress = function() {
        var plyr = $doc.getElementById(playerId);
        var prog = plyr.GetMaxTimeLoaded() / plyr.GetDuration();
        listener.@com.bramosystems.gwt.player.client.MediaStateListener::onLoadingProgress(D)(prog);
    };
    jso[playerId].volumeChange = function() {
        var plyr = $doc.getElementById(playerId);
        var vol = (plyr.GetVolume() / 255 * 100).toFixed(0);
        jso[playerId].debug('Volume changed to ' + vol + '%');
    };
    jso[playerId].doMetadata = function() {
        try {
            var plyr = $doc.getElementById(playerId);
            id3.@com.bramosystems.gwt.player.client.MediaInfo::year = plyr.GetUserData("&#xA9;day");
            id3.@com.bramosystems.gwt.player.client.MediaInfo::albumTitle = plyr.GetUserData("name");
            id3.@com.bramosystems.gwt.player.client.MediaInfo::artists = plyr.GetUserData('©prf');
            id3.@com.bramosystems.gwt.player.client.MediaInfo::comment = plyr.GetUserData('info');
            id3.@com.bramosystems.gwt.player.client.MediaInfo::title = plyr.GetUserData('name');
            id3.@com.bramosystems.gwt.player.client.MediaInfo::contentProviders = plyr.GetUserData('©src');
            id3.@com.bramosystems.gwt.player.client.MediaInfo::copyright = plyr.GetUserData('cprt');
            id3.@com.bramosystems.gwt.player.client.MediaInfo::hardwareSoftwareRequirements = plyr.GetUserData('©req');
            id3.@com.bramosystems.gwt.player.client.MediaInfo::publisher = plyr.GetUserData('©prd');
            id3.@com.bramosystems.gwt.player.client.MediaInfo::genre = '';
            id3.@com.bramosystems.gwt.player.client.MediaInfo::internetStationOwner = '';
            id3.@com.bramosystems.gwt.player.client.MediaInfo::internetStationName = '';
            listener.@com.bramosystems.gwt.player.client.MediaStateListener::onMediaInfoAvailable(Lcom/bramosystems/gwt/player/client/MediaInfo;)(id3);
        } catch(e) {
            jso[playerId].debug(e);
        }
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
    playr.addEventListener('qt_volumechange', jso[playerId].volumeChange, false);
//    playr.addEventListener('qt_loadedmetadata', jso[playerId].doMetadata, false);
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

    public void showController(String playerId, boolean show) {
        showControllerImpl(jso, playerId, show);
    }

    public final int getLoopCount(String playerId) {
        return getLoopCountImpl(jso, playerId);
    }

    public final void setLoopCount(String playerId, int count) {
        setLoopCountImpl(jso, playerId, count);
    }

    private native void playImpl(JavaScriptObject jso, String playerId) /*-{
    if(jso[playerId].qtInit == true) {
        jso[playerId].statPlay();
    }
    }-*/;

    private native void stopImpl(JavaScriptObject jso, String playerId) /*-{
    if(jso[playerId].qtInit == true) {
        var plyr = $doc.getElementById(playerId);
        plyr.Rewind();
        plyr.Stop();
        jso[playerId].debug('Media playback stoped');
    }
    }-*/;

    private native void pauseImpl(JavaScriptObject jso, String playerId) /*-{
    if(jso[playerId].qtInit == true) {
        var plyr = $doc.getElementById(playerId);
        plyr.Stop();
        jso[playerId].debug('Media playback paused');
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

    private native void setLoopCountImpl(JavaScriptObject jso, String playerId, int count) /*-{
        jso[playerId].loopCount = count;
    }-*/;

    private native int getLoopCountImpl(JavaScriptObject jso, String playerId) /*-{
        return jso[playerId].loopCount;
    }-*/;

    private native void showControllerImpl(JavaScriptObject jso, String playerId, boolean show) /*-{
    if(jso[playerId].qtInit == true) {
        var plyr = $doc.getElementById(playerId);
        plyr.SetControllerVisible(show);
    }
    }-*/;
}
