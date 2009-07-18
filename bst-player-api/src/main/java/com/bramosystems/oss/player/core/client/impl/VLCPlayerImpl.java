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
import com.bramosystems.oss.player.core.client.ui.VLCPlayer;
import com.google.gwt.core.client.JavaScriptObject;
import java.util.ArrayList;

/**
 * Native implementation of the VLCPlayer class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see VLCPlayer
 */
public class VLCPlayerImpl {

    VLCPlayerImpl() {
    }

    public String getPlayerScript(String playerId, int height, int width) {
        return "<embed id='" + playerId + "' name='" + playerId + "' loop='false' " +
                "target='' autoplay='false' type='application/x-vlc-plugin' " +
                "version='VideoLAN.VLCPlugin.2' width='" + width + "px' " +
                "height='" + height + "px' toolbar='true'></embed>";
    }

    public final native boolean isPlayerAvailable(String playerId) /*-{
    return ($doc.getElementById(playerId) != null);
    }-*/;

    public native void play(String playerId) /*-{
    try{
    var plyr = $doc.getElementById(playerId);
    plyr.playlist.play();
    } catch(e){}
    }-*/;

    public native void playMediaAt(String playerId, int index) /*-{
    try{
    var player = $doc.getElementById(playerId);
    player.playlist.playItem(index);
    } catch(e){}
    }-*/;

    public native void playNext(String playerId) /*-{
    try{
    var plyr = $doc.getElementById(playerId);
    plyr.playlist.next();
    } catch(e){}
    }-*/;

    public native void playPrevious(String playerId) /*-{
    try{
    var plyr = $doc.getElementById(playerId);
    plyr.playlist.prev();
    } catch(e){}
    }-*/;

    public native void stop(String playerId) /*-{
    try{
    var plyr = $doc.getElementById(playerId);
    plyr.playlist.stop();
    } catch(e){}
    }-*/;

    public native void togglePause(String playerId) /*-{
    try{
    var plyr = $doc.getElementById(playerId);
    plyr.playlist.togglePause();
    } catch(e){}
    }-*/;

    public native double getTime(String playerId) /*-{
    try {
    var plyr = $doc.getElementById(playerId);
    return plyr.input.time;
    } catch(e) {
    return 0;
    }
    }-*/;

    public native void setTime(String playerId, double time) /*-{
    try {
    var plyr = $doc.getElementById(playerId);
    plyr.input.time = time;
    } catch(e) {}
    }-*/;

    public native double getDuration(String playerId) /*-{
    try {
    var plyr = $doc.getElementById(playerId);
    return parseFloat(plyr.input.length);
    } catch(e) {
    return 0;
    }
    }-*/;

    public native double getVolume(String playerId) /*-{
    try{
    var plyr = $doc.getElementById(playerId);
    return plyr.audio.volume / 100.0;
    } catch(e){
    return 0;
    }
    }-*/;

    public native void setVolume(String playerId, double volume) /*-{
    try{
    var plyr = $doc.getElementById(playerId);
    plyr.audio.volume = parseInt(volume * 100);
    } catch(e){}
    }-*/;

    public native boolean isMute(String playerId) /*-{
    try{
    var plyr = $doc.getElementById(playerId);
    return plyr.audio.mute;
    } catch(e){
    return false;
    }
    }-*/;

    public native void setMute(String playerId, boolean mute) /*-{
    try{
    var plyr = $doc.getElementById(playerId);
    plyr.audio.mute = mute;
    } catch(e){}
    }-*/;

    public native double getRate(String playerId) /*-{
    try{
    var plyr = $doc.getElementById(playerId);
    return plyr.input.rate;
    } catch(e){
    return -1;
    }
    }-*/;

    public native void setRate(String playerId, double rate) /*-{
    try{
    var plyr = $doc.getElementById(playerId);
    plyr.input.rate = rate;
    } catch(e){}
    }-*/;

    public native String getAspectRatio(String playerId) /*-{
    try{
    var plyr = $doc.getElementById(playerId);
    return plyr.video.aspectRatio;
    } catch(e){
    return '';
    }
    }-*/;

    public native void setAspectRatio(String playerId, String aspect) /*-{
    try{
    var plyr = $doc.getElementById(playerId);
    plyr.video.aspectRatio = aspect;
    } catch(e){}
    }-*/;

    public native String getVideoWidth(String playerId) /*-{
    try{
    var plyr = $doc.getElementById(playerId);
    if(plyr.input.hasVout)
        return plyr.video.width;
     return 0;
    } catch(e){
    return 0;
    }
    }-*/;

    public native String getVideoHeight(String playerId) /*-{
    try{
    var plyr = $doc.getElementById(playerId);
    if(plyr.input.hasVout)
        return plyr.video.height;
     return 0;
    } catch(e){
    return 0;
    }
    }-*/;

    public native void toggleFullScreen(String playerId) /*-{
    try{
    var plyr = $doc.getElementById(playerId);
    plyr.video.toggleFullscreen();
    } catch(e){}
    }-*/;

    public native boolean hasVideo(String playerId) /*-{
    try{
    var plyr = $doc.getElementById(playerId);
    return plyr.input.hasVout;
    } catch(e){
    return false;
    }
    }-*/;

    public native int getCurrentAudioTrack(String playerId) /*-{
    try{
    var plyr = $doc.getElementById(playerId);
    return plyr.audio.track;
    } catch(e){
    return -1;
    }
    }-*/;

    public native int getAudioChannelMode(String playerId) /*-{
    try{
    var plyr = $doc.getElementById(playerId);
    return plyr.audio.channel;
    } catch(e){
    return -1;
    }
    }-*/;

    public native void setAudioChannelMode(String playerId, int mode) /*-{
    try{
    var plyr = $doc.getElementById(playerId);
    plyr.audio.channel = mode;
    } catch(e){}
    }-*/;

    public native int addToPlaylist(String playerId, String mediaURL) /*-{
    try{
    var plyr = $doc.getElementById(playerId);
    return plyr.playlist.add(mediaURL);
    } catch(e){
    return -1;
    }
    }-*/;

    public native int addToPlaylist(String playerId, String mediaURL, String options) /*-{
    try{
    var plyr = $doc.getElementById(playerId);
    var opts = new Array();
    opts.push(options);
    return plyr.playlist.add(mediaURL, 'silence', opts);
    } catch(e){
    return -1;
    }
    }-*/;

    public native void removeFromPlaylist(String playerId, int index) /*-{
    try{
    var player = $doc.getElementById(playerId);
    player.playlist.items.remove(index);
    } catch(e){}
    }-*/;

    public native void clearPlaylist(String playerId) /*-{
    try{
    var player = $doc.getElementById(playerId);
    player.playlist.items.clear();
    } catch(e){}
    }-*/;

    public native int getPlaylistCount(String playerId) /*-{
    try{
    var player = $doc.getElementById(playerId);
    return player.playlist.items.count - 1; // [vlc://quit is also part of playlist]
    } catch(e){
    return 1;
    }
    }-*/;

    public native int getPlayerState(String playerId) /*-{
    try{
    var player = $doc.getElementById(playerId);
    return player.input.state;
    } catch(e){
    return -1;
    }
    }-*/;

    public native String getPluginVersion(String playerId) /*-{
    var plyr = $doc.getElementById(playerId);
    return plyr.VersionInfo;
    }-*/;

    public native void fillMediaInfo(String playerId, MediaInfo id3) /*-{
    try {
    var plyr = $doc.getElementById(playerId);
//    id3.@com.bramosystems.oss.player.core.client.MediaInfo::year = ;
//    id3.@com.bramosystems.oss.player.core.client.MediaInfo::albumTitle = ;
//    id3.@com.bramosystems.oss.player.core.client.MediaInfo::artists = ;
//    id3.@com.bramosystems.oss.player.core.client.MediaInfo::comment = ;
//    id3.@com.bramosystems.oss.player.core.client.MediaInfo::title = ;
//    id3.@com.bramosystems.oss.player.core.client.MediaInfo::contentProviders = ;
//    id3.@com.bramosystems.oss.player.core.client.MediaInfo::copyright = ;
//    id3.@com.bramosystems.oss.player.core.client.MediaInfo::hardwareSoftwareRequirements = ;
//    id3.@com.bramosystems.oss.player.core.client.MediaInfo::publisher =;
//    id3.@com.bramosystems.oss.player.core.client.MediaInfo::genre = ;
//    id3.@com.bramosystems.oss.player.core.client.MediaInfo::internetStationOwner = '';
//    id3.@com.bramosystems.oss.player.core.client.MediaInfo::internetStationName = '';
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::duration = parseFloat(plyr.input.length);

    if(plyr.input.hasVout) {
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::videoWidth = plyr.video.width;
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::videoHeight = plyr.video.height;
     }
    } catch(e) {
    }
    }-*/;

    public ArrayList<VLCPlayer.Message> getMessages(String playerId) {
        ArrayList<VLCPlayer.Message> logs = new ArrayList<VLCPlayer.Message>();
        if(getLogMessageCount(playerId) > 0) {
            JavaScriptObject jso = getMessageIterator("");
            while(hasNextMessage(jso)) {
                VLCPlayer.Message msg = new VLCPlayer.Message();
                fillNextMessage(jso, msg);
                logs.add(msg);
            }
            jso = null;
            clearLogMessages(playerId);
        }
        return logs;
    }

    public native int setLogLevel(String playerId, int level) /*-{
    var player = $doc.getElementById(playerId);
    if(player.log) {
        player.log.verbosity = level;
     }
    }-*/;

    private native int getLogMessageCount(String playerId) /*-{
    var player = $doc.getElementById(playerId);
    if(player.log) {
     return player.log.messages.count;
     }
     return 0;
    }-*/;

    private native void clearLogMessages(String playerId) /*-{
    var player = $doc.getElementById(playerId);
    player.log.messages.clear();
    }-*/;

    private native JavaScriptObject getMessageIterator(String playerId) /*-{
    var player = $doc.getElementById(playerId);
    return player.log.messages.iterator();
    }-*/;

    private native boolean hasNextMessage(JavaScriptObject iterator) /*-{
    return iterator.hasNext;
    }-*/;

    private native void fillNextMessage(JavaScriptObject iterator, VLCPlayer.Message msg) /*-{
    var _msg = iterator.next();
    msg.@com.bramosystems.oss.player.core.client.ui.VLCPlayer.Message::moduleName = _msg.name;
    msg.@com.bramosystems.oss.player.core.client.ui.VLCPlayer.Message::moduleType = _msg.type;
    msg.@com.bramosystems.oss.player.core.client.ui.VLCPlayer.Message::message = _msg.message;
    msg.@com.bramosystems.oss.player.core.client.ui.VLCPlayer.Message::severity = _msg.severity;
    }-*/;
}
