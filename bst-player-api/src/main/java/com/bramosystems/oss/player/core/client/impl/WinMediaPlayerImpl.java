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
import com.bramosystems.oss.player.core.client.ui.WinMediaPlayer;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Native implementation of the WinMediaPlayer class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see WinMediaPlayer
 */
public class WinMediaPlayerImpl extends JavaScriptObject {

    protected WinMediaPlayerImpl() {
    }

    public static native WinMediaPlayerImpl getPlayer(String playerId) /*-{
    return $doc.getElementById(playerId);
    }-*/;

    public final native void setURL(String mediaURL) /*-{
    this.URL = mediaURL;
    }-*/;

    public final native String getURL() /*-{
    return this.URL;
    }-*/;

    public final native void play() /*-{
    try {
    this.controls.play();
    } catch(e) {}
    }-*/;

    public final native void stop() /*-{
    try {
    this.controls.stop();
    } catch(e) {}
    }-*/;

    public final native void pause() /*-{
    this.controls.pause();
    }-*/;

    public final native void close() /*-{
    this.close();
    }-*/;

    public final native double getDuration() /*-{
    return this.currentMedia.duration * 1000;
    }-*/;

    public final native double getCurrentPosition() /*-{
    return this.controls.currentPosition * 1000;
    }-*/;

    public final native void setCurrentPosition(double position) /*-{
    this.controls.currentPosition = position / 1000;
    }-*/;

    public final native int getVolume() /*-{
    return this.settings.volume;
    }-*/;

    public final native void setVolume(int volume) /*-{
    this.settings.volume = volume;
    }-*/;

    public final native void setUIMode(String mode) /*-{
     this.uiMode = mode;
    }-*/;

    public final native String getUIMode() /*-{
    return this.uiMode || '';
    }-*/;

    public final native void setLoopCount(int count) /*-{
    try {
    this.settings.playCount = count;
    } catch(e) {}
    }-*/;

    public final native int getLoopCount() /*-{
    try {
    if(this.settings.getMode("loop")){
    return -1;
    }else {
    return this.settings.playCount;
    }
    } catch(e) {
    return 0;
    }
    }-*/;

    public final native String getPlayerVersion() /*-{
    return this.versionInfo;
    }-*/;

    public final native int getPlayState() /*-{
    return this.playState || -10;
    }-*/;

    public final native void fillMetadata(MediaInfo info, String errorMsg) /*-{
    try {
    var plyrMedia = this.currentMedia;
    info.@com.bramosystems.oss.player.core.client.MediaInfo::title = plyrMedia.getItemInfo('Title');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::copyright = plyrMedia.getItemInfo('Copyright');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::duration = parseFloat(plyrMedia.getItemInfo('Duration')) * 1000;
    info.@com.bramosystems.oss.player.core.client.MediaInfo::publisher = plyrMedia.getItemInfo('WM/Publisher');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::comment = plyrMedia.getItemInfo('Description');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::year = plyrMedia.getItemInfo('WM/Year');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::albumTitle = plyrMedia.getItemInfo('WM/AlbumTitle');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::artists = plyrMedia.getItemInfo('WM/AlbumArtist');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::contentProviders = plyrMedia.getItemInfo('WM/Provider');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::genre = plyrMedia.getItemInfo('WM/Genre');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::internetStationOwner = plyrMedia.getItemInfo('WM/RadioStationOwner');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::internetStationName = plyrMedia.getItemInfo('WM/RadioStationName');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::hardwareSoftwareRequirements = plyrMedia.getItemInfo('WM/EncodingSettings');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::videoWidth = String(plyrMedia.imageSourceWidth);
    info.@com.bramosystems.oss.player.core.client.MediaInfo::videoHeight = String(plyrMedia.imageSourceHeight);
    } catch(e) {
    errorMsg = e;
    }
    }-*/;

    public final native String getErrorDiscription() /*-{
    var err = this.error;
    if(err == undefined)
    return '';

    return err.item(0).errorDescription;
    }-*/;

    public final native double getDownloadProgress() /*-{
    if(this.network) {
    return this.network.downloadProgress / 100;
    } else {
    return -1;
    }
    }-*/;

    public final native double getBufferingProgress() /*-{
    if(this.network) {
    return this.network.bufferingProgress / 100;
    } else {
    return -1;
    }
    }-*/;

    public final native int getVideoHeight() /*-{
    try {
    return this.currentMedia.imageSourceHeight;
    } catch(e) {
    return 0;
    }
    }-*/;

    public final native int getVideoWidth() /*-{
    try {
    return this.currentMedia.imageSourceWidth;
    } catch(e) {
    return 0;
    }
    }-*/;

    public final native void setStretchToFit(boolean stretch) /*-{
    this.stretchToFit = stretch;
    }-*/;

    public final native boolean isStretchToFit() /*-{
    return this.stretchToFit;
    }-*/;

    public final native void setWindowlessVideo(boolean windowless) /*-{
     this.windowlessVideo = windowless;
    }-*/;

    public final native boolean isWindowlessVideo() /*-{
    return this.windowlessVideo;
    }-*/;

    public final native String getAspectRatio() /*-{
    try {
    return this.getItemInfo('PixelAspectRatioX')+':'+this.getItemInfo('PixelAspectRatioY');
    } catch(e) {
    return 0;
    }
    }-*/;

    public final native boolean requestMediaAccessRight(String accessLevel) /*-{
    return this.settings.requestMediaAccessRights(accessLevel);
    }-*/;

    public final native String getMediaAccessRight() /*-{
    return this.settings.mediaAccessRights;
    }-*/;

    public final native String getPlayerId() /*-{
    return this.id;
    }-*/;
}
