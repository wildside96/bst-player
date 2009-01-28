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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * Native implementation of the QuickTimePlayer class. It is not recommended to
 * interact with this class directly.

 * @author Sikirulai Braheem
 * @see com.bramosystems.gwt.player.client.ui.QuickTimePlayer
 */
public class QuickTimePlayerImpl {
    private JavaScriptObject jso;
    protected final String EVENT_SOURCE_ID = "bst_qt_event";

    // Make package private....
    QuickTimePlayerImpl() {
    }

    public void init(String playerId) {
        if(jso == null) {
            jso = JavaScriptObject.createObject();

            // inject DOM event source object...
            Element e = DOM.createDiv();
            e.setInnerHTML(getEventSourceScript());
 //           RootPanel.getBodyElement().appendChild(e);
        }

        initImpl(jso, playerId);
    }

    public String getEventSourceScript() {
        // This is required only for IE...
        return "";
    }

    public void addMediaStateListener(String playerId, MediaStateListener listener) {
        createMediaStateListenerImpl(jso, playerId, listener);
        registerMediaStateListenerImpl(jso, playerId);
    }
    
    public String getPlayerScript(String qtSrc, String playerId, boolean autoplay,
            String height, String width) {
        boolean useQtSrc = (qtSrc != null) && (qtSrc.length() != 0);
        return "<embed id='" + playerId + "' name='" + playerId + "' " +
                "type='video/quicktime' postdomevents='true' kioskmode='true' " +
                "autoplay='" + autoplay + "' src='" + GWT.getModuleBaseURL() + "qtmov.mov' " +
                "width='" + width + "' height='" + height + "' EnableJavaScript='true' " +
                (useQtSrc ? "QTSrc='" + qtSrc + "' " : "") +
                "/> ";
    }

    private native void initImpl(JavaScriptObject jso, String playerId) /*-{
    if(jso[playerId] != null) {
    return;
    } else {
    jso[playerId] = new Object();
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
            MediaStateListener listener) /*-{
    jso[playerId].ioError = function() {
    listener.@com.bramosystems.gwt.player.client.MediaStateListener::onIOError()();
    };
    jso[playerId].playStarted = function() {
    listener.@com.bramosystems.gwt.player.client.MediaStateListener::onPlayStarted()();
    };
    jso[playerId].soundComplete = function() {
    listener.@com.bramosystems.gwt.player.client.MediaStateListener::onPlayFinished()();
    };
    jso[playerId].loadingComplete = function() {
    listener.@com.bramosystems.gwt.player.client.MediaStateListener::onLoadingComplete()();
    };
    jso[playerId].loadingProgress = function() {
    var plyr = $doc.getElementById(playerId);
//    $wnd.alert("loaded : " + plyr.GetMaxTimeLoaded() + ", duratioo : " + plyr.GetDuration());
    var prog = plyr.GetMaxTimeLoaded() / parseDouble(plyr.GetDuration());
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
    playr.addEventListener('qt_load', jso[playerId].loadingComplete, false);
    playr.addEventListener('qt_ended', jso[playerId].soundComplete, false);
    playr.addEventListener('qt_error', jso[playerId].ioError, false);
    playr.addEventListener('qt_progress', jso[playerId].loadingProgress, false);
    playr.addEventListener('qt_play', jso[playerId].playStarted, false);
    }-*/;

    public native void play(String playerId) /*-{
    $doc.getElementById(playerId).Play();
    }-*/;

    public native void stop(String playerId) /*-{
    $doc.getElementById(playerId).Rewind();
    $doc.getElementById(playerId).Stop();
    }-*/;

    public native void pause(String playerId) /*-{
    $doc.getElementById(playerId).Stop();
    }-*/;

    public native void loadSound(String playerId, String mediaURL) /*-{
    $doc.getElementById(playerId).SetURL(mediaURL);
    }-*/;

    public native int getTime(String playerId) /*-{
    return $doc.getElementById(playerId).GetTime();
    }-*/;

    public native void setTime(String playerId, int time) /*-{
    $doc.getElementById(playerId).SetTime(time);
    }-*/;

    public native int getDuration(String playerId) /*-{
    return $doc.getElementById(playerId).GetDuration();
    }-*/;

    public native int getMovieSize(String playerId) /*-{
    return $doc.getElementById(playerId).GetMovieSize();
    }-*/;

    public native int getMaxBytesLoaded(String playerId) /*-{
    return $doc.getElementById(playerId).GetMaxBytesLoaded();
    }-*/;

    public native int getVolume(String playerId) /*-{
    return $doc.getElementById(playerId).GetVolume();
    }-*/;

    public native void setVolume(String playerId, int volume) /*-{
    $doc.getElementById(playerId).SetVolume(volume);
    }-*/;

}
