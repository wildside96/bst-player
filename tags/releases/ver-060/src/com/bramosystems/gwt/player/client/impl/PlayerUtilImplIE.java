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

import com.bramosystems.gwt.player.client.Plugin;
import com.bramosystems.gwt.player.client.PluginNotFoundException;
import com.bramosystems.gwt.player.client.PluginVersion;
import java.util.Arrays;

/**
 * IE specific native implementation of the PlayerUtil class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see com.bramosystems.gwt.player.client.PlayerUtil
 */
public class PlayerUtilImplIE extends PlayerUtilImpl {

    @Override
    public Plugin suggestPlayer(String protocol, String ext) throws PluginNotFoundException {
        // suggest player with preference for WMP, SWF, QT...
        PluginVersion pv = null;
        Plugin pg = Plugin.Auto;

        if (protocol != null) {  // check for special streaming media...
            if (Arrays.binarySearch(wmpProt, protocol.toLowerCase()) >= 0) {
                // check if plugin is available...
                pv = new PluginVersion();
                getWindowsMediaPlayerVersion(pv);
                if (pv.compareTo(1, 1, 1) >= 0) {   // req WMP plugin found...
                    pg = Plugin.WinMediaPlayer;
                }
            }

            if (pg.equals(Plugin.Auto)) {    // supported player not found yet, try WMP...
                if (Arrays.binarySearch(qtProt, protocol.toLowerCase()) >= 0) {
                    pv = new PluginVersion();
                    getQuickTimePluginVersion(pv);
                    if (pv.compareTo(7, 2, 1) >= 0) {   // req QT plugin found...
                        pg = Plugin.QuickTimePlayer;
                    }
                }
            }
        }

        if (pg.equals(Plugin.Auto)) {
            if (Arrays.binarySearch(wmpPool, ext) >= 0) { // supported player not found yet, try WMP...
                pv = new PluginVersion();
                getWindowsMediaPlayerVersion(pv);
                if (pv.compareTo(1, 1, 1) >= 0) {   // req WMP plugin found...
                    pg = Plugin.WinMediaPlayer;
                }
            }
        }

        if (pg.equals(Plugin.Auto)) {    // supported player not found yet, try flash sound ...
            if (Arrays.binarySearch(flsPool, ext.toLowerCase()) >= 0) {
                pv = new PluginVersion();
                getFlashPluginVersion(pv);          // SWF plugin supported ext....
                if (pv.compareTo(9, 0, 0) >= 0) {   // req SWF plugin found...
                    pg = Plugin.FlashMP3Player;
                }
            }
        }

        if (pg.equals(Plugin.Auto)) {    // supported player not found yet, try flash video ...
            if (Arrays.binarySearch(flvPool, ext.toLowerCase()) >= 0) {
                // check if plugin is available...
                pv = new PluginVersion();
                getFlashPluginVersion(pv);          // SWF plugin supported ext....
                if (pv.compareTo(9, 0, 0) >= 0) {   // req SWF plugin found...
                    pg = Plugin.FlashVideoPlayer;
                }
            }
        }

        /*
         * TODO: Fix QT control in IE and enable this...
        if (pg.equals(Plugin.Auto)) {    // supported player not found yet, try QT...
        if (Arrays.binarySearch(qtPool, ext) >= 0) {
        // check if plugin is available...
        pv = new PluginVersion();
        getQuickTimePluginVersion(pv);
        if (pv.compareTo(7, 2, 1) >= 0) {   // req QT plugin found...
        pg = Plugin.QuickTimePlayer;
        }
        }
        }
         */
        if (pg.equals(Plugin.Auto)) {    // plugin not found
            throw new PluginNotFoundException();
        } else {
            return pg;
        }
    }

    @Override
    public native void getFlashPluginVersion(PluginVersion version) /*-{
    try {
    ax = new ActiveXObject("ShockwaveFlash.ShockwaveFlash");
    ver = ax.GetVariable("$version");   // "WIN A,B,CCC,DD
    ver = ver.split(" ")[1].split(",");
    version.@com.bramosystems.gwt.player.client.PluginVersion::setMajor(I)(parseInt(ver[0]));
    version.@com.bramosystems.gwt.player.client.PluginVersion::setMinor(I)(parseInt(ver[1]));
    version.@com.bramosystems.gwt.player.client.PluginVersion::setRevision(I)(parseInt(ver[2]));
    ax.Quit();
    } catch (e) {
    }
    }-*/;

    @Override
    public native void getQuickTimePluginVersion(PluginVersion version) /*-{
    try {
    ax = new ActiveXObject('QuickTimeCheckObject.QuickTimeCheck');
    ver = ax.QuickTimeVersion.toString(16);
    version.@com.bramosystems.gwt.player.client.PluginVersion::setMajor(I)(parseInt(ver.charAt(0)));
    version.@com.bramosystems.gwt.player.client.PluginVersion::setMinor(I)(parseInt(ver.charAt(1)));
    version.@com.bramosystems.gwt.player.client.PluginVersion::setRevision(I)(parseInt(ver.charAt(2)));
    ax.Quit();
    } catch (e) {
    }
    }-*/;

    /**
     * Native implementation of Windows Media Player plugin detection
     * @param version wraps the detected version numbers.
     */
    @Override
    public native void getWindowsMediaPlayerVersion(PluginVersion version) /*-{
    try {
    ax = new ActiveXObject('WMPlayer.ocx');
    ver = ax.versionInfo;
    ver = ver.split(".");
    version.@com.bramosystems.gwt.player.client.PluginVersion::setMajor(I)(parseInt(ver[0]));
    version.@com.bramosystems.gwt.player.client.PluginVersion::setMinor(I)(parseInt(0));
    version.@com.bramosystems.gwt.player.client.PluginVersion::setRevision(I)(parseInt(0));
    ax.Quit();
    } catch (e) {
    }
    }-*/;
}
