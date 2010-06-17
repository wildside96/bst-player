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
    try {
    this.URL = mediaURL;
    } catch(e) {}
    }-*/;

    public final native String getURL() /*-{
    try {
    return this.URL;
    } catch(e) {return null;}
    }-*/;

    public final native void close() /*-{
    try {
    this.close();
    } catch(e){} // suppress exp...
    }-*/;

    public final native double getDuration() /*-{
    try {
    return this.currentMedia.duration * 1000;
    } catch(e) {return -1;}
    }-*/;

    public final native void setUIMode(String mode) /*-{
    try {
    this.uiMode = mode;
    } catch(e) {}
    }-*/;

    public final native String getUIMode() /*-{
    try {
    return this.uiMode || '';
    } catch(e) {return null;}
    }-*/;

    public final native Controls getControls() /*-{
    return this.controls;
    }-*/;

    public final native Settings getSettings() /*-{
    return this.settings;
    }-*/;

    public final native CurrentMedia getCurrentMedia() /*-{
    return this.currentMedia;
    }-*/;

    public final native String getPlayerVersion() /*-{
    try {
    return this.versionInfo;
    } catch(e) {return null;}
    }-*/;

    public final native int getPlayState() /*-{
    try {
    return this.playState || -10;
    } catch(e) {return -10;}
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
    } catch(e) {errorMsg = e;}
    }-*/;

    public final native String getErrorDiscription() /*-{
    try {
    var err = this.error;
    if(err == undefined)
    return '';

    return err.item(0).errorDescription;
    } catch(e) {return null;}
    }-*/;

    public final native double getDownloadProgress() /*-{
    try {
    if(this.network) {
    return this.network.downloadProgress / 100;
    } else {return -1;}
    } catch(e) {return -1;}
    }-*/;

    public final native double getBufferingProgress() /*-{
    try {
    if(this.network) {
    return this.network.bufferingProgress / 100;
    } else {return -1;}
    } catch(e) {return -1;}
    }-*/;

    public final native void setStretchToFit(boolean stretch) /*-{
    try {
    this.stretchToFit = stretch;
    } catch(e) {}
    }-*/;

    public final native boolean isStretchToFit() /*-{
    try {
    return this.stretchToFit;
    } catch(e) {return false;}
    }-*/;

    public final native void setWindowlessVideo(boolean windowless) /*-{
    try {
    this.windowlessVideo = windowless;
    } catch(e) {}
    }-*/;

    public final native boolean isWindowlessVideo() /*-{
    try {
    return this.windowlessVideo;
    } catch(e) {return false;}
    }-*/;

    public final native String getAspectRatio() /*-{
    try {
    return this.getItemInfo('PixelAspectRatioX')+':'+this.getItemInfo('PixelAspectRatioY');
    } catch(e) {return 0;}
    }-*/;

    public final native String getPlayerId() /*-{
    return this.id;
    }-*/;

    public static class CurrentMedia extends JavaScriptObject {

        protected CurrentMedia() {
        }

        public final native int getVideoHeight() /*-{
        try {
        return this.imageSourceHeight;
        } catch(e) {return 0;}
        }-*/;

        public final native int getVideoWidth() /*-{
        try {
        return this.imageSourceWidth;
        } catch(e) {return 0;}
        }-*/;

        public final native String getSourceURL() /*-{
        try {
        return this.sourceURL;
        } catch(e) {return '';}
        }-*/;
    }

    public static class Controls extends JavaScriptObject {

        protected Controls() {
        }

        public final native void play() /*-{
        try {
        this.play();
        } catch(e) {}
        }-*/;

        public final native void stop() /*-{
        try {
        this.stop();
        } catch(e) {}
        }-*/;

        public final native void pause() /*-{
        try {
        this.pause();
        } catch(e) {}
        }-*/;

        public final native double getCurrentPosition() /*-{
        try {
        return this.currentPosition * 1000;
        } catch(e) {return -1;}
        }-*/;

        public final native void setCurrentPosition(double position) /*-{
        try {
        this.currentPosition = position / 1000;
        } catch(e) {}
        }-*/;
    }

    public static class Settings extends JavaScriptObject {

        protected Settings() {
        }

        public final native int getVolume() /*-{
        try {
        return this.volume;
        } catch(e) {return -1;}
        }-*/;

        public final native void setVolume(int volume) /*-{
        try {
        this.volume = volume;
        } catch(e) {}
        }-*/;

        public final native void setPlayCount(double _count) /*-{
        try {
        this.playCount = _count;
        } catch(e) {}
        }-*/;

        public final native int getPlayCount() /*-{
        try {
        return this.playCount || 1;
        } catch(e) {return 0;}
        }-*/;

        public final native boolean isModeEnabled(String mode) /*-{
        try {
        return this.getMode(mode) || false;
        } catch(e) {return false;}
        }-*/;

        /**
         * Not supported in non-IE browsers
         * @param _mode
         * @param _enable
         */
        public final native void enableMode(String _mode, boolean _enable) /*-{
        this.setMode(_mode, _enable);
        }-*/;

        public final native void setRate(double rate) /*-{
        try {
        this.rate = rate;
        } catch(e) {}
        }-*/;

        public final native double getRate() /*-{
        try {
        return this.rate;
        } catch(e) {
        return 0;
        }
        }-*/;

        public final native boolean requestMediaAccessRight(String accessLevel) /*-{
        try {
        return this.requestMediaAccessRights(accessLevel);
        } catch(e) {
        return false;
        }
        }-*/;

        public final native String getMediaAccessRight() /*-{
        try {
        return this.mediaAccessRights;
        } catch(e) {
        return null;
        }
        }-*/;
    }
}
