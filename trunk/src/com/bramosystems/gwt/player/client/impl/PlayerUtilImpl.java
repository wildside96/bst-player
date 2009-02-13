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

import com.bramosystems.gwt.player.client.*;
import java.util.Arrays;

/**
 * Native implementation of the PlayerUtil class. It is not recommended to
 * interact with this class directly.
 *
 * @see com.bramosystems.gwt.player.client.PlayerUtil
 * @author Sikirulai Braheem
 */
public class PlayerUtilImpl {

    protected final static String[] qtPool = {"wav", "bwf", "midi", "au", "snd", "aiff",
        "aif", "aifc", "cdda", "ac3", "aac", "amr", "gsm", "3gp", "3gpp", "3g2",
        "3gp2", "mp4", "mov", "qt", "mqv", "mp3", "mpeg", "mpg", "mp4"};
    protected final static String[] wmpPool = {"asf", "asx", "wmv", "wvx", "wm",
        "wma", "wax", "wav", "mp3", "midi"};
    protected final static String[] swfPool = {"swf", "spl", "mp3"};

    public PlayerUtilImpl() {
        Arrays.sort(swfPool);
        Arrays.sort(qtPool);
        Arrays.sort(wmpPool);
    }

    public Plugin suggestPlayer(String ext) throws PluginNotFoundException {
        // suggest player with preference for SWF and QT...
        PluginVersion pv = new PluginVersion();
        Plugin pg = Plugin.Auto;

        if (Arrays.binarySearch(swfPool, ext.toLowerCase()) >= 0) {
            getFlashPluginVersion(pv);          // SWF plugin supported ext....
            if (pv.compareTo(9, 0, 0) >= 0) {   // req SWF plugin found...
                pg = Plugin.FlashMP3Player;
            }
        }

        if (pg.equals(Plugin.Auto)) {    // supported player not found yet, try QT...
            if (Arrays.binarySearch(qtPool, ext.toLowerCase()) >= 0) {
                // check if plugin is available...
                pv = new PluginVersion();
                getQuickTimePluginVersion(pv);
                if (pv.compareTo(7, 2, 1) >= 0) {   // req QT plugin found...
                    pg = Plugin.QuickTimePlayer;
                }
            }
        }

        if (pg.equals(Plugin.Auto)) {    // supported player not found yet, try WMP...
            if (Arrays.binarySearch(wmpPool, ext.toLowerCase()) >= 0) {
                // check if plugin is available...
                pv = new PluginVersion();
                getWindowsMediaPlayerVersion(pv);
                if (pv.compareTo(1, 1, 1) >= 0) {   // req WMP plugin found...
                    pg = Plugin.WinMediaPlayer;
                }
            }
        }

        if (pg.equals(Plugin.Auto)) {    // plugin not found
            throw new PluginNotFoundException();
        } else {
            return pg;
        }
    }

    public native String getPlugins() /*-{
    var pp = navigator.plugins.length;
    var plugs = "Length : " + pp + "<br/>";
    for (counter=0; counter < pp; counter++ ) {
    plugs += navigator.plugins[counter].name + ", " +
    navigator.plugins[counter].description;
    plugs += "<br/>";
    }
    return plugs;
    }-*/;

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

    version.@com.bramosystems.gwt.player.client.PluginVersion::setMajor(I)(parseInt(mmArray[0]));
    version.@com.bramosystems.gwt.player.client.PluginVersion::setMinor(I)(parseInt(mmArray[1]));
    version.@com.bramosystems.gwt.player.client.PluginVersion::setRevision(I)(parseInt(rev));
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
    version.@com.bramosystems.gwt.player.client.PluginVersion::setMajor(I)(parseInt(ver[0]));
    version.@com.bramosystems.gwt.player.client.PluginVersion::setMinor(I)(parseInt(ver[1]));
    version.@com.bramosystems.gwt.player.client.PluginVersion::setRevision(I)(parseInt(ver[2]));
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
    version.@com.bramosystems.gwt.player.client.PluginVersion::setMajor(I)(parseInt(1));
    version.@com.bramosystems.gwt.player.client.PluginVersion::setMinor(I)(parseInt(1));
    version.@com.bramosystems.gwt.player.client.PluginVersion::setRevision(I)(parseInt(1));
    }
    }-*/;
}
