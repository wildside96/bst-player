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

        initPlayerImpl(jso, playerId, new MediaInfo(), listener);
        createMediaStateListenerImpl(jso, playerId, listener);
    }

    public String getPlayerScript(String mediaURL, String playerId, boolean autoplay,
            String uiMode, int height, int width) {
        return "<object id='" + playerId + "' type='" + getPluginType() + "' " +
                "width='" + width + "px' height='" + height + "px' >" +
                "<param name='autostart' value='" + autoplay + "' />" +
                "<param name='URL' value='" + mediaURL + "' />" +
                "<param name='uiMode' value='" + uiMode + "' /> " +
                "</object>";
    }

   public void registerMediaStateListener(String playerId){
       registerMediaStateListenerImpl(jso, playerId);
   }

    public final boolean isPlayerAvailable(String playerId) {
        return isPlayerAvailableImpl(jso, playerId);
    }

    public void play(String playerId) {
        playImpl(jso, playerId);
    }

    public void stop(String playerId) {
        stopImpl(jso, playerId);
    }

    public void close(String playerId) {
        closeImpl(jso, playerId);
    }

    public final int getLoopCount(String playerId) {
        return getLoopCountImpl(jso, playerId);
    }

    public final void setLoopCount(String playerId, int count) {
        setLoopCountImpl(jso, playerId, count);
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

    private native void initPlayerImpl(JavaScriptObject jso, String playerId, 
            MediaInfo mData, MediaStateListener listener) /*-{
    if(jso[playerId] != null) {
        return;
    } else {
        jso[playerId] = new Object();
        jso[playerId].initd = false;
        jso[playerId].doLoop = false;
        jso[playerId].loopCount = 0;
        jso[playerId].playing = false;
        jso[playerId].doWMPMetadata = function() {
            try {
                var plyrMedia = $doc.getElementById(playerId).currentMedia;
                mData.@com.bramosystems.gwt.player.client.MediaInfo::title = plyrMedia.getItemInfo('Title');
                mData.@com.bramosystems.gwt.player.client.MediaInfo::copyright = plyrMedia.getItemInfo('Copyright');
                mData.@com.bramosystems.gwt.player.client.MediaInfo::duration = parseFloat(plyrMedia.getItemInfo('Duration')) * 1000;
                mData.@com.bramosystems.gwt.player.client.MediaInfo::publisher = plyrMedia.getItemInfo('WM/Publisher');
                mData.@com.bramosystems.gwt.player.client.MediaInfo::comment = plyrMedia.getItemInfo('Description');
                mData.@com.bramosystems.gwt.player.client.MediaInfo::year = plyrMedia.getItemInfo('WM/Year');
                mData.@com.bramosystems.gwt.player.client.MediaInfo::albumTitle = plyrMedia.getItemInfo('WM/AlbumTitle');
                mData.@com.bramosystems.gwt.player.client.MediaInfo::artists = plyrMedia.getItemInfo('WM/AlbumArtist');
                mData.@com.bramosystems.gwt.player.client.MediaInfo::contentProviders = plyrMedia.getItemInfo('WM/Provider');
                mData.@com.bramosystems.gwt.player.client.MediaInfo::genre = plyrMedia.getItemInfo('WM/Genre');
                mData.@com.bramosystems.gwt.player.client.MediaInfo::internetStationOwner = plyrMedia.getItemInfo('WM/RadioStationOwner');
                mData.@com.bramosystems.gwt.player.client.MediaInfo::internetStationName = plyrMedia.getItemInfo('WM/RadioStationName');
                mData.@com.bramosystems.gwt.player.client.MediaInfo::hardwareSoftwareRequirements = plyrMedia.getItemInfo('WM/EncodingSettings');
                listener.@com.bramosystems.gwt.player.client.MediaStateListener::onMediaInfoAvailable(Lcom/bramosystems/gwt/player/client/MediaInfo;)(mData);
            } catch(e) {
                jso[playerId].debug(e);
            }
        };
        jso[playerId].debug = function(message) {
            listener.@com.bramosystems.gwt.player.client.MediaStateListener::onDebug(Ljava/lang/String;)(message);
        };
        jso[playerId].statPlay = function() {
             try {
                var playr = $doc.getElementById(playerId);
                playr.controls.play();
             } catch(e) {
                jso[playerId].errorr()
             }
        };
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
            case 1:    // stopped..
                jso[playerId].playing = false;
                jso[playerId].debug('Media playback stopped');
                if(jso[playerId].doLoop == true) {
                    jso[playerId].statPlay();
                }
                break;
            case 2:    // paused..
                jso[playerId].playing = false;
                jso[playerId].debug('Media playback paused');
                break;
            case 3:    // playing..
                if(jso[playerId].playing == false) {
                    jso[playerId].playing = true;
                    listener.@com.bramosystems.gwt.player.client.MediaStateListener::onPlayStarted()();
                    jso[playerId].debug('Playing media at ' + playr.URL);
                    jso[playerId].doWMPMetadata();
                }
                break;
            case 11:    // reconnecting to stream  ...
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
                                jso[playerId].debug('Media loading complete');
                            }
                        } else {
                            $wnd.clearInterval(jso[playerId].progTimerId);
                            delete jso[playerId].progTimerId;
                        }
                     }, 1000);
                }
                break;
            case 8:    // media ended...
                if (jso[playerId].loopCount < 0) {
                    jso[playerId].doLoop = true;
                } else {
                    if (jso[playerId].loopCount > 1) {
                        jso[playerId].doLoop = true;
                        jso[playerId].loopCount--;
                    } else {
                        jso[playerId].doLoop = false;
                        listener.@com.bramosystems.gwt.player.client.MediaStateListener::onPlayFinished()();
                        jso[playerId].debug('Media playback finished');
                    }
                }
                break;
            case 10:    // player ready...
                if(jso[playerId].initd == false) {
                    jso[playerId].initd = true;
                    listener.@com.bramosystems.gwt.player.client.MediaStateListener::onPlayerReady()();
                    var versn = playr.versionInfo;
                    jso[playerId].debug('Windows Media Player plugin');
                    jso[playerId].debug('Version : ' + versn);
             }
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

    public native void loadSound(String playerId, String mediaURL) /*-{
    var player = $doc.getElementById(playerId);
    player.URL = mediaURL;
    }-*/;

    public native void pause(String playerId) /*-{
    var player = $doc.getElementById(playerId);
    player.controls.pause();
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

    public native int getVolume(String playerId) /*-{
    var player = $doc.getElementById(playerId);
    return player.settings.volume;
    }-*/;

    public native void setVolume(String playerId, int volume) /*-{
    var player = $doc.getElementById(playerId);
    player.settings.volume = volume;
    }-*/;

    public native void setUIMode(String playerId, String uiMode) /*-{
    var player = $doc.getElementById(playerId);
    player.uiMode = uiMode;
    }-*/;

    protected native void closeImpl(JavaScriptObject jso, String playerId) /*-{
    delete jso[playerId];
    var index = jso['geId'].indexOf(playerId);
    delete jso['geId'][index];
    jso['geId'].splice(index, 1);
    }-*/;

    private native void setLoopCountImpl(JavaScriptObject jso, String playerId, int count) /*-{
        jso[playerId].loopCount = count;
    }-*/;

    private native int getLoopCountImpl(JavaScriptObject jso, String playerId) /*-{
        return jso[playerId].loopCount;
    }-*/;

    private native void playImpl(JavaScriptObject jso, String playerId) /*-{
    try {
     var playr = $doc.getElementById(playerId);
        playr.controls.play();
    } catch(e) {
     jso[playerId].errorr()
    }
    }-*/;

    private native void stopImpl(JavaScriptObject jso, String playerId) /*-{
    jso[playerId].doLoop = false;
    var player = $doc.getElementById(playerId);
    player.controls.stop();
    }-*/;
}
