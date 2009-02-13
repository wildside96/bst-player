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
 * IE specific native implementation of the WinMediaPlayer class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see com.bramosystems.gwt.player.client.ui.WinMediaPlayer
 */
public class WinMediaPlayerImplIE extends WinMediaPlayerImpl {

    WinMediaPlayerImplIE() {
    }

    @Override
    public String getPlayerScript(String mediaURL, String playerId, boolean autoplay,
            String height, String width) {
        String uiMode = (height == null || width == null) ? "invisible" : "full";
        String h = height == null ? "0px" : height;
        String w = width == null ? "0px" : width;
        return "<object id='" + playerId + "' classid='CLSID:6BF52A52-394A-11d3-B153-00C04F79FAA6' " +
                "width='" + w + "' height='" + h + "' >" +
                "<param name='autoStart' value='" + autoplay + "' />" +
                "<param name='uiMode' value='" + uiMode + "' /> " +
                "<param name='URL' value='" + mediaURL + "' />" +
                "</object>";
    }

    @Override
    protected void initGlobalEventListeners(JavaScriptObject jso) {
        // override this and do nothing to avoid IE errors. Method is just a
        // workaround for non IE browsers.
    }

    /**
     * Create native MediaStateListener functions
     * @param jso function wrapper
     * @param playerId player ID
     * @param listener callback
     */
    @Override
    protected native void createMediaStateListenerImpl(JavaScriptObject jso, String playerId,
            MediaStateListener listener) /*-{
    jso[playerId].errorr = function() {
        var playr = $doc.getElementById(playerId);
        var desc = playr.error.item(0).errorDescription;
        listener.@com.bramosystems.gwt.player.client.MediaStateListener::onError(Ljava/lang/String;)(desc);
    };
    jso[playerId].buffering = function(Start) {
        var playr = $doc.getElementById(playerId);
        if(Start == true) {
            jso[playerId].progTimerId = $wnd.setInterval(function() {
                  var prog = playr.network.downloadProgress / 100;
                  listener.@com.bramosystems.gwt.player.client.MediaStateListener::onLoadingProgress(D)(prog);
            }, 1000);
        } else {
           $wnd.clearInterval(jso[playerId].progTimerId);
           listener.@com.bramosystems.gwt.player.client.MediaStateListener::onLoadingComplete()();
        }
    };
    jso[playerId].playStateChange = function(NewState) {
        switch(NewState) {
            case 3:    // playing..
                listener.@com.bramosystems.gwt.player.client.MediaStateListener::onPlayStarted()();
                break;
            case 8:    // media ended...
                listener.@com.bramosystems.gwt.player.client.MediaStateListener::onPlayFinished()();
                break;
            case 10:    // player ready...
              listener.@com.bramosystems.gwt.player.client.MediaStateListener::onPlayerReady()();
              break;
        }
    };
    }-*/;

    @Override
    protected native void registerMediaStateListenerImpl(JavaScriptObject jso, String playerId) /*-{
        var playr = $doc.getElementById(playerId);
        playr.attachEvent('playStateChange', jso[playerId].playStateChange);
        playr.attachEvent('buffering', jso[playerId].buffering);
        playr.attachEvent('error', jso[playerId].errorr);
        if(playr.playState) {
             jso[playerId].playStateChange(playr.playState);
        }
     }-*/;

//    @Override
//    public native void loadSound(String playerId, String mediaURL) /*-{
//    var player = $doc.getElementById(playerId);
//    player.URL = mediaURL;
//    }-*/;

    @Override
    protected native void closeImpl(JavaScriptObject jso, String playerId) /*-{
    var player = $doc.getElementById(playerId);
//    player.close();
    delete jso[playerId];
    }-*/;
}
