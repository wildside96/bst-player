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
package com.bramosystems.oss.player.core.client.impl;

import com.bramosystems.oss.player.core.client.MediaInfo;
import com.bramosystems.oss.player.core.client.MediaStateListener;
import com.bramosystems.oss.player.core.client.ui.QuickTimePlayer;
import com.google.gwt.i18n.client.NumberFormat;
import java.util.HashMap;

/**
 * Native implementation of the QuickTimePlayer class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see QuickTimePlayer
 */
public class QuickTimePlayerImpl {

    private HashMap<String, EventHandler> cache;

    public QuickTimePlayerImpl() {
        cache = new HashMap<String, EventHandler>();
    }

    public void init(String playerId, MediaStateListener listener) {
        cache.put(playerId, new EventHandler(playerId, listener));
    }

    public String getPlayerScript(String playerId, String mediaSrc, boolean autoplay,
            String height, String width) {
        return "<embed id='" + playerId + "' name='" + playerId + "' " +
                "autoplay='" + autoplay + "' src='" + mediaSrc + "' " +
                "width='" + width + "' height='" + height + "' " +
                "type='video/quicktime' postdomevents='true' kioskmode='true' " +
                "controller='true' EnableJavaScript='true' scale='Aspect' " + //ToFit' " +
                "bgcolor='#000000' showlogo='false' targetcache='false'></embed>";
    }

    public final boolean isPlayerAvailable(String playerId) {
        return cache.containsKey(playerId) && isPlayerOnPageImpl(playerId);
    }

    protected void onState(String playerId, int stateId) {
        cache.get(playerId).onStateChange(stateId);
    }

    public void close(String playerId) {
        cache.remove(playerId);
    }

    public void registerMediaStateListener(final String playerId, final String mediaUrl) {
        registerMediaStateListenerImpl(this, playerId);
    }

    public final int getLoopCount(String playerId) {
        if (isLoopingImpl(playerId)) {
            return -1;
        } else {
            return cache.get(playerId).getLoopCount();
        }
    }

    public final void setLoopCount(String playerId, int count) {
        setLoopingImpl(playerId, count < 0);
        cache.get(playerId).setLoopCount(count);
    }

    public native String getPluginVersionImpl(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    return plyr.GetPluginVersion();
    }-*/;

    public native void resetPropertiesOnReload(String playerId, boolean reset) /*-{
    var plyr = $doc.getElementById(playerId);
    plyr.SetResetPropertiesOnReload(reset);
    }-*/;

    private native void fillMediaInfoImpl(String playerId, MediaInfo id3) /*-{
    try {
    var plyr = $doc.getElementById(playerId);
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::year = plyr.GetUserData("&#xA9;day");
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::albumTitle = plyr.GetUserData("name");
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::artists = plyr.GetUserData('@prf');
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::comment = plyr.GetUserData('info');
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::title = plyr.GetUserData('name');
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::contentProviders = plyr.GetUserData('@src');
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::copyright = plyr.GetUserData('cprt');
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::hardwareSoftwareRequirements = plyr.GetUserData('@req');
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::publisher = plyr.GetUserData('@prd');
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::genre = '';
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::internetStationOwner = '';
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::internetStationName = '';
    } catch(e) {
    }
    }-*/;

    private native void registerMediaStateListenerImpl(QuickTimePlayerImpl impl, String playerId) /*-{
    var playr = $doc.getElementById(playerId);
    playr.addEventListener('qt_begin', function(){  // plugin init complete
    impl.@com.bramosystems.oss.player.core.client.impl.QuickTimePlayerImpl::onState(Ljava/lang/String;I)(playerId, 1);
    }, false);
    playr.addEventListener('qt_load', function(){   // loading complete
    impl.@com.bramosystems.oss.player.core.client.impl.QuickTimePlayerImpl::onState(Ljava/lang/String;I)(playerId, 2);
    }, false);
    playr.addEventListener('qt_play', function(){   // play started
    impl.@com.bramosystems.oss.player.core.client.impl.QuickTimePlayerImpl::onState(Ljava/lang/String;I)(playerId, 3);
    }, false);
    playr.addEventListener('qt_ended', function(){  // playback finished
    impl.@com.bramosystems.oss.player.core.client.impl.QuickTimePlayerImpl::onState(Ljava/lang/String;I)(playerId, 4);
    }, false);
    playr.addEventListener('qt_canplay', function(){    // player ready
    impl.@com.bramosystems.oss.player.core.client.impl.QuickTimePlayerImpl::onState(Ljava/lang/String;I)(playerId, 5);
    }, false);
    playr.addEventListener('qt_volumechange', function(){   // volume changed
    impl.@com.bramosystems.oss.player.core.client.impl.QuickTimePlayerImpl::onState(Ljava/lang/String;I)(playerId, 6);
    }, false);
    playr.addEventListener('qt_progress', function(){   // progress
    impl.@com.bramosystems.oss.player.core.client.impl.QuickTimePlayerImpl::onState(Ljava/lang/String;I)(playerId, 7);
    }, false);
    playr.addEventListener('qt_error', function(){  // error
    impl.@com.bramosystems.oss.player.core.client.impl.QuickTimePlayerImpl::onState(Ljava/lang/String;I)(playerId, 8);
    }, false);
    playr.addEventListener('qt_loadedmetadata', function(){
    impl.@com.bramosystems.oss.player.core.client.impl.QuickTimePlayerImpl::onState(Ljava/lang/String;I)(playerId, 9);
    }, false);
    playr.addEventListener('qt_pause', function(){   // play paused
    impl.@com.bramosystems.oss.player.core.client.impl.QuickTimePlayerImpl::onState(Ljava/lang/String;I)(playerId, 10);
    }, false);
    playr.addEventListener('qt_waiting', function(){   // buffering
    impl.@com.bramosystems.oss.player.core.client.impl.QuickTimePlayerImpl::onState(Ljava/lang/String;I)(playerId, 11);
    }, false);
    playr.addEventListener('qt_stalled', function(){   // playback stalled
    impl.@com.bramosystems.oss.player.core.client.impl.QuickTimePlayerImpl::onState(Ljava/lang/String;I)(playerId, 12);
    }, false);
    }-*/;

    private native boolean isPlayerOnPageImpl(String playerId) /*-{
    return $doc.getElementById(playerId) != null;
    }-*/;

    public native void play(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    plyr.Play();
    }-*/;

    public native void stop(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    plyr.Rewind();
    plyr.Stop();
    }-*/;

    public native void pause(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    plyr.Stop();
    }-*/;

    public native void load(String playerId, String mediaURL) /*-{
    var plyr = $doc.getElementById(playerId);
    plyr.SetURL(mediaURL);
    }-*/;

    public native String getMovieURL(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    return plyr.GetURL();
    }-*/;

    public native double getTime(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    return parseFloat(plyr.GetTime() / plyr.GetTimeScale()) * 1000;
    }-*/;

    public native void setTime(String playerId, double time) /*-{
    var plyr = $doc.getElementById(playerId);
    plyr.SetTime(parseInt(time / 1000 * plyr.GetTimeScale()));
    }-*/;

    public native double getDuration(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    return parseFloat(plyr.GetDuration() / plyr.GetTimeScale() * 1000);
    }-*/;

    public native int getMovieSize(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    return $doc.getElementById(playerId).GetMovieSize();
    }-*/;

    public native int getMaxBytesLoaded(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    return $doc.getElementById(playerId).GetMaxBytesLoaded();
    }-*/;

    public native double getVolume(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    return plyr.GetVolume() / 255.0;
    }-*/;

    public native void setVolume(String playerId, double volume) /*-{
    var plyr = $doc.getElementById(playerId);
    plyr.SetVolume(parseInt(volume * 255));
    }-*/;

    public native void setQTNextURL(String playerId, int index, String url) /*-{
    var plyr = $doc.getElementById(playerId);
    plyr.SetQTNEXTUrl(index, url);
    }-*/;

    public native String getQTNextURL(String playerId, int index) /*-{
    var plyr = $doc.getElementById(playerId);
    return plyr.GetQTNEXTUrl(index);
    }-*/;

    public native void setLoopingImpl(String playerId, boolean loop) /*-{
    var plyr = $doc.getElementById(playerId);
    plyr.SetIsLooping(loop);
    }-*/;

    public native boolean isLoopingImpl(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    return plyr.GetIsLooping(loop);
    }-*/;

    public native void setControllerVisible(String playerId, boolean visible) /*-{
    var plyr = $doc.getElementById(playerId);
    plyr.SetControllerVisible(visible);
    }-*/;

    public native boolean isControllerVisible(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    return plyr.GetControllerVisible();
    }-*/;

    public native void setMute(String playerId, boolean mute) /*-{
    var plyr = $doc.getElementById(playerId);
    plyr.SetMute(mute);
    }-*/;

    public native boolean isMute(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    return plyr.GetMute();
    }-*/;

    public native void setRate(String playerId, double rate) /*-{
    var plyr = $doc.getElementById(playerId);
    plyr.SetRate(rate);
    }-*/;

    public native double getRate(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    return plyr.GetRate();
    }-*/;

    public native String getMatrix(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    return plyr.GetMatrix();
    }-*/;

    public native void setMatrix(String playerId, String matrix) /*-{
    var plyr = $doc.getElementById(playerId);
    plyr.SetMatrix(matrix);
    }-*/;

    public native void setRectangle(String playerId, String rect) /*-{
    var plyr = $doc.getElementById(playerId);
    plyr.SetRectange(rect);
    }-*/;

    public native String getRectangle(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    return plyr.GetRectange();
    }-*/;

    public native void setKioskMode(String playerId, boolean kioskMode) /*-{
    var plyr = $doc.getElementById(playerId);
    plyr.SetKioskMode(kioskMode);
    }-*/;

    public native boolean isKioskMode(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    return plyr.GetKioskMode();
    }-*/;

    public native void setBackgroundColor(String playerId, String color) /*-{
    var plyr = $doc.getElementById(playerId);
    plyr.SetBgColor(color);
    }-*/;

    public native String getBackgroundColor(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    return plyr.GetBgColor();
    }-*/;

    public native String getStatus(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    return plyr.GetPluginStatus();
    }-*/;

    public class EventHandler {

        protected MediaStateListener listener;
        protected String id;
        private int count;
        protected int _count;
        private NumberFormat volFmt = NumberFormat.getPercentFormat();
        private boolean isBuffering;

        public EventHandler(String _id, MediaStateListener _listener) {
            listener = _listener;
            id = _id;
            isBuffering = false;
        }

        public void initComplete() {
            listener.onDebug("QuickTime Player plugin");
            listener.onDebug("Version : " + getPluginVersionImpl(id));
        }

        public void onStateChange(int newState) {
            switch (newState) {
                case 1: // plugin init complete ...
                    listener.onDebug("QuickTime Player plugin");
                    listener.onDebug("Version : " + getPluginVersionImpl(id));
                    break;
                case 2: // loading complete ...
                    onDebug("Media loading complete");
                    listener.onLoadingComplete();
                    break;
                case 3: // play started ...
                    if (isBuffering) {
                        isBuffering = false;
                        listener.onBuffering(false);
                        onDebug("Buffering ended ...");
                    }
                    listener.onPlayStarted(0);
                    onDebug("Playing media at " + getMovieURL(id));
                    break;
                case 4: // play finished ...
                    if (_count > 1) {
                        _count--;
                        play(id);
                    } else {
                        listener.onPlayFinished(0);
                        onDebug("Media playback complete");
                    }
                    break;
                case 5: // player ready ...
                    onDebug("Plugin ready for media playback");
                    listener.onPlayerReady();
                    //
                    break;
                case 6: // volume changed ...
                    onDebug("Volume changed to " + volFmt.format(getVolume(id)));
                    break;
                case 7: // progress changed ...
                    listener.onLoadingProgress(getMaxBytesLoaded(id) / (double) getMovieSize(id));
                    break;
                case 8: // error event ...
                    listener.onError(getStatus(id) + " occured while loading media!");
                    break;
                case 9: // metadata stuffs ...
                    MediaInfo info = new MediaInfo();
                    fillMediaInfoImpl(id, info);
                    listener.onMediaInfoAvailable(info);
                    break;
                case 10: // playback paused ...
                    listener.onDebug("Playback paused");
                    break;
                case 11: // buffering ...
                    isBuffering = true;
                    onDebug("Buffering started ...");
                    listener.onBuffering(true);
                    break;
                case 12: // stalled ...
                    onDebug("Player stalled !");
                    break;
            }
        }

        public void onError(String description) {
            listener.onError(description);
        }

        public void onDebug(String message) {
            listener.onDebug(message);
        }

        public void setLoopCount(int count) {
            this.count = count;
            _count = count;
        }

        public int getLoopCount() {
            return count;
        }
    }
}
