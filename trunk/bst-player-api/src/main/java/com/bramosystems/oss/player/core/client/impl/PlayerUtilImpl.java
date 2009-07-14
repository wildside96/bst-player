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

import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.PluginVersion;
import com.bramosystems.oss.player.core.client.Plugin;
import java.util.Arrays;

/**
 * Native implementation of the PlayerUtil class. It is not recommended to
 * interact with this class directly.
 *
 * @see PlayerUtil
 * @author Sikirulai Braheem
 *
 */
public class PlayerUtilImpl {

    protected final static String[] qtPool = {"wav", "bwf", "mid", "midi", "smf", "au", "snd", "aiff",
        "aif", "aifc", "cdda", "ac3", "caf", "aac", "adts", "amr", "amc", "gsm", "3gp", "3gpp", "3g2",
        "3gp2", "mp2", "mp3", "mp4", "mov", "qt", "mqv", "mpeg", "mpg", "m3u",
        "sdv", "m1s", "m1a", "m1v", "mpm", "mpv", "mpa", "m2a", "m4a", "m4p", "m4b"};
    protected final static String[] wmpPool = {"asf", "asx", "wmv", "wvx", "wm",
        "wma", "wax", "wav", "mp3", "mid", "midi", "smf", "m3u"};
    protected final static String[] flvPool = {"flv", "mp4", "f4v", "m4a", // "mov",
        "mp4v", "mp3", "m3u"}; // "3gp", "3g2"
    protected final static String[] vlcPool = {"3gp2", "mp2", "mp3", "mp4", "mov", "qt", "mpeg",
        "mpg", "mpga", "mpega", "mpe", "vob", "mpg4", "avi", "ogg", "vlc", "asf", "asx", "wmv",
        "wav", "3gp", "3gpp", "3g2", "3gpp2", "divx", "flv", "mkv", "mka", "xspf", "m4a", "m3u", "wma"};
    protected final static String[] qtProt = {"rtsp", "rts"};
    protected final static String[] vlcProt = {"rtp", "rtsp", "mms", "udp"};
    protected final static String[] wmpProt = {"mms"};

    public PlayerUtilImpl() {
        Arrays.sort(flvPool);
        Arrays.sort(qtPool);
        Arrays.sort(qtProt);
        Arrays.sort(wmpPool);
        Arrays.sort(vlcPool);
        Arrays.sort(vlcProt);
    }

    public boolean canHandleMedia(Plugin plugin, String protocol, String ext) {
        PluginVersion pv = null;
        boolean canHandle = false;

        switch (plugin) {
            case FlashPlayer:
                // check if plugin is available...
                pv = new PluginVersion();
                getFlashPluginVersion(pv);          // SWF plugin supported ext....
                if (pv.compareTo(9, 0, 0) < 0) {   // req SWF plugin not found...
                    break;
                }
                canHandle = Arrays.binarySearch(flvPool, ext.toLowerCase()) >= 0;
                break;
            case QuickTimePlayer:
                // check if plugin is available...
                pv = new PluginVersion();
                getQuickTimePluginVersion(pv);
                if (pv.compareTo(7, 2, 1) < 0) {   // req QT plugin not found...
                    break;
                }

                // check for streaming protocol ...
                canHandle = (protocol != null) && (Arrays.binarySearch(qtProt, protocol.toLowerCase()) >= 0);

                // check for extension ...
                if (!canHandle && Arrays.binarySearch(qtPool, ext.toLowerCase()) >= 0) {
                    canHandle = true;
                }
                break;
            case WinMediaPlayer:
                // check if plugin is available...
                pv = new PluginVersion();
                getWindowsMediaPlayerVersion(pv);
                if (pv.compareTo(1, 1, 1) < 0) {   // req WMP plugin not found...
                    break;
                }

                canHandle = (protocol != null) && (Arrays.binarySearch(wmpProt, protocol.toLowerCase()) >= 0);

                // check for extension ...
                if (!canHandle && Arrays.binarySearch(wmpPool, ext.toLowerCase()) >= 0) {
                    canHandle = true;
                }
                break;
            case VLCPlayer:
                // check if plugin is available...
                pv = new PluginVersion();
                getVLCPluginVersion(pv);
                if (pv.compareTo(0, 8, 6) < 0) {   // req VLC plugin not found...
                    break;
                }

                canHandle = (protocol != null) && (Arrays.binarySearch(vlcProt, protocol.toLowerCase()) >= 0);

                // check for extension ...
                if (!canHandle && Arrays.binarySearch(vlcPool, ext.toLowerCase()) >= 0) {
                    canHandle = true;
                }
                break;
        }
        return canHandle;
    }

    /**
     * Native implementation of Flash plugin detection
     * @param version wraps the detected version numbers.
     */
    public native void getFlashPluginVersion(PluginVersion version) /*-{
    if (navigator.plugins != null && navigator.plugins.length > 0 && navigator.plugins["Shockwave Flash"]) {
    var desc = navigator.plugins["Shockwave Flash"].description;
    var descArray = desc.split(" ");
    var mmArray = descArray[2].split(".");
    var rev = descArray[3];
    if (rev == "") {
    rev = descArray[4];
    }

    if ((rev[0] == "d") || (rev[0] == "b")) {
    rev = rev.substring(1);
    } else if (rev[0] == "r") {
    rev = rev.substring(1);
    if (rev.indexOf("d") > 0) {
    rev = rev.substring(0, rev.indexOf("d"));
    }
    }

    version.@com.bramosystems.oss.player.core.client.PluginVersion::setMajor(I)(parseInt(mmArray[0]));
    version.@com.bramosystems.oss.player.core.client.PluginVersion::setMinor(I)(parseInt(mmArray[1]));
    version.@com.bramosystems.oss.player.core.client.PluginVersion::setRevision(I)(parseInt(rev));
    }
    }-*/;

    /**
     * Native implementation of QuickTime plugin detection
     * @param version wraps the detected version numbers.
     */
    public native void getQuickTimePluginVersion(PluginVersion version) /*-{
    if (navigator.plugins && navigator.plugins.length > 0) {
    for(i = 0; i < navigator.plugins.length; i++) {
    if(navigator.plugins[i].name.indexOf("QuickTime") > -1) {
    ver = navigator.plugins[i].name.toLowerCase().replace("quicktime plug-in", "");
    ver = ver.split(".");
    version.@com.bramosystems.oss.player.core.client.PluginVersion::setMajor(I)(parseInt(ver[0]));
    version.@com.bramosystems.oss.player.core.client.PluginVersion::setMinor(I)(parseInt(ver[1]));
    version.@com.bramosystems.oss.player.core.client.PluginVersion::setRevision(I)(parseInt(ver[2]));
    break;
    }
    }
    }
    }-*/;

    /**
     * Native implementation of Windows Media Player plugin detection. The method
     * simply checks if Windows Media Player plugin is available.
     *
     * @param version wraps the detected version numbers.
     */
    public native void getWindowsMediaPlayerVersion(PluginVersion version) /*-{
    var wmp = false;
    if (navigator.mimeTypes && navigator.mimeTypes['application/x-ms-wmp']) {
    wmp = true; // wmp for firefox
    } else if (navigator.mimeTypes && navigator.mimeTypes['application/x-mplayer2']) {
    wmp = true; // generic wmp
    }

    if(wmp){
    version.@com.bramosystems.oss.player.core.client.PluginVersion::setMajor(I)(parseInt(1));
    version.@com.bramosystems.oss.player.core.client.PluginVersion::setMinor(I)(parseInt(1));
    version.@com.bramosystems.oss.player.core.client.PluginVersion::setRevision(I)(parseInt(1));
    }
    }-*/;

    /**
     * Native implementation of VLC plugin detection
     * @param version wraps the detected version numbers.
     */
    public native void getVLCPluginVersion(PluginVersion version) /*-{
    if (navigator.plugins != null && navigator.plugins.length > 0 &&
    navigator.plugins["VLC Multimedia Plug-in"]) {
    var desc = navigator.plugins["VLC Multimedia Plug-in"].description;
    var descRegex = new RegExp("\\d+.\\d+.\\d+", "");
    var verArray = (descRegex.exec(desc))[0].split(".");

    version.@com.bramosystems.oss.player.core.client.PluginVersion::setMajor(I)(parseInt(verArray[0]));
    version.@com.bramosystems.oss.player.core.client.PluginVersion::setMinor(I)(parseInt(verArray[1]));
    version.@com.bramosystems.oss.player.core.client.PluginVersion::setRevision(I)(parseInt(verArray[2]));
    }
    }-*/;
}
