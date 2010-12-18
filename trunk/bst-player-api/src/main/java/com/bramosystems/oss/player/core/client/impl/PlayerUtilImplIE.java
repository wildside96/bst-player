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
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginVersion;

/**
 * IE specific native implementation of the PlayerUtil class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see PlayerUtil
 */
public class PlayerUtilImplIE extends PlayerUtilImpl {

    private native void getFlashPluginVersion(PluginVersion version) /*-{
    try {
    verRegex = new RegExp("\\d+,\\d+,\\d+,\\d+", "");   // "WIN A,B,CCC,DD
    ax = new ActiveXObject("ShockwaveFlash.ShockwaveFlash");
    ver = ax.GetVariable("$version");   // "WIN A,B,CCC,DD
    ver = (verRegex.exec(ver))[0].split(",");
    version.@com.bramosystems.oss.player.core.client.PluginVersion::setMajor(I)(parseInt(ver[0]));
    version.@com.bramosystems.oss.player.core.client.PluginVersion::setMinor(I)(parseInt(ver[1]));
    version.@com.bramosystems.oss.player.core.client.PluginVersion::setRevision(I)(parseInt(ver[2]));
    ax.Quit();
    } catch (e) {}
    }-*/;

    private native void getQuickTimePluginVersion(PluginVersion version) /*-{
    try {
    ax = new ActiveXObject('QuickTimeCheckObject.QuickTimeCheck');
    ver = ax.QuickTimeVersion.toString(16);
    version.@com.bramosystems.oss.player.core.client.PluginVersion::setMajor(I)(parseInt(ver.charAt(0)));
    version.@com.bramosystems.oss.player.core.client.PluginVersion::setMinor(I)(parseInt(ver.charAt(1)));
    version.@com.bramosystems.oss.player.core.client.PluginVersion::setRevision(I)(parseInt(ver.charAt(2)));
    ax.Quit();
    } catch (e) {}
    }-*/;

    /**
     * Native implementation of Windows Media Player plugin detection
     * @param version wraps the detected version numbers.
     */
    private native void getWindowsMediaPlayerVersion(PluginVersion version) /*-{
    try {
    ax = new ActiveXObject('WMPlayer.ocx');
    ver = ax.versionInfo;
    ver = ver.split(".");
    version.@com.bramosystems.oss.player.core.client.PluginVersion::setMajor(I)(parseInt(ver[0]));
    version.@com.bramosystems.oss.player.core.client.PluginVersion::setMinor(I)(parseInt(0));
    version.@com.bramosystems.oss.player.core.client.PluginVersion::setRevision(I)(parseInt(0));
    ax.Quit();
    } catch (e) {}
    }-*/;

    private native void getVLCPluginVersion(PluginVersion version) /*-{
    try {
    descRegex = new RegExp("\\d+.\\d+.\\d+", "");
    ax = new ActiveXObject('VideoLAN.VLCPlugin');
    ver = ax.VersionInfo;
    verArray = (descRegex.exec(ver))[0].split(".");
    version.@com.bramosystems.oss.player.core.client.PluginVersion::setMajor(I)(parseInt(verArray[0]));
    version.@com.bramosystems.oss.player.core.client.PluginVersion::setMinor(I)(parseInt(verArray[1]));
    version.@com.bramosystems.oss.player.core.client.PluginVersion::setRevision(I)(parseInt(verArray[2]));
    ax.Quit();
    } catch (e) {}
    }-*/;

    private native void getDivXPluginVersion(PluginVersion version) /*-{
    try {
    descRegex = new RegExp("\\d+.\\d+.\\d+", "");
    ax = new ActiveXObject('npdivx.DivXBrowserPlugin');
    ver = ax.GetVersion();
    verArray = (descRegex.exec(ver))[0].split(".");
    version.@com.bramosystems.oss.player.core.client.PluginVersion::setMajor(I)(parseInt(verArray[0]));
    version.@com.bramosystems.oss.player.core.client.PluginVersion::setMinor(I)(parseInt(verArray[1]));
    version.@com.bramosystems.oss.player.core.client.PluginVersion::setRevision(I)(parseInt(verArray[2]));
    ax.Quit();
    } catch (e) {}
    }-*/;

    @Override
    public PluginInfo getPluginInfo(Plugin plugin) {
        PluginVersion pv = new PluginVersion();
        switch (plugin) {
            case DivXPlayer:
                getDivXPluginVersion(pv);
                break;
            case FlashPlayer:
                getFlashPluginVersion(pv);
                break;
            case QuickTimePlayer:
                getQuickTimePluginVersion(pv);
                break;
            case VLCPlayer:
                getVLCPluginVersion(pv);
                break;
            case WinMediaPlayer:
                getWindowsMediaPlayerVersion(pv);
        }
        return new PluginInfo(pv, PluginInfo.PlayerPluginWrapperType.Native);
    }
}
