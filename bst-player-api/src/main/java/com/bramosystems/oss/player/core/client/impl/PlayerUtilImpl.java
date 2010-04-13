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

import com.bramosystems.oss.player.core.client.MimePool;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersion;
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.util.client.BrowserPlugin;
import com.bramosystems.oss.player.util.client.MimeType;
import com.bramosystems.oss.player.util.client.RegExp;
import com.bramosystems.oss.player.util.client.RegExp.RegexException;
import java.util.Set;

/**
 * Native implementation of the PlayerUtil class. It is not recommended to
 * interact with this class directly.
 *
 * @see PlayerUtil
 * @author Sikirulai Braheem
 *
 */
public class PlayerUtilImpl {

    public PlayerUtilImpl() {
    }

    public boolean canHandleMedia(Plugin plugin, String protocol, String ext) {
        PluginVersion pv = new PluginVersion();
        Set<String> types = MimePool.get().getRegisteredExtensions(plugin);
        Set<String> prots = MimePool.get().getRegisteredProtocols(plugin);

        if (protocol == null) {
            protocol = "-";
        }

        // check if plugin is available...
        switch (plugin) {
            case FlashPlayer:
                getFlashPluginVersion(pv);
                break;
            case QuickTimePlayer:
                getQuickTimePluginVersion(pv);
                break;
            case WinMediaPlayer:
                getWindowsMediaPlayerVersion(pv);
                break;
            case VLCPlayer:
                getVLCPluginVersion(pv);
                break;
            case DivXPlayer:
                getDivXPluginVersion(pv);
                break;
            case Native:
                if (isHTML5CompliantClient()) {
                    pv = PluginVersion.get(0, 0, 1);
                }
        }

        if (pv.compareTo(plugin.getVersion()) >= 0) {   // req plugin found...
            // check for streaming protocol & extension ...
            return ((prots != null) && prots.contains(protocol.toLowerCase()))
                    || ((types != null) && types.contains(ext.toLowerCase()));
        }
        return false;
    }

    /**
     * Native implementation of Flash plugin detection
     * @param version wraps the detected version numbers.
     */
    public void getFlashPluginVersion(PluginVersion version) {
        MimeType mt = MimeType.getMimeType("application/x-shockwave-flash");  // get SWF mime type...
        if (mt != null) {   // plugin present
            try {
                // plugin present
                if (mt.getEnabledPlugin().getName().contains("Shockwave Flash")) {
                    // the type is enabled for SWF
                    String desc = mt.getEnabledPlugin().getDescription();
                    RegExp.RegexResult res = RegExp.getRegExp("(\\d+).(\\d+)\\s*[r|d|b](\\d+)", "").exec(desc);
                    version.setMajor(Integer.parseInt(res.getMatch(1)));
                    version.setMinor(Integer.parseInt(res.getMatch(2)));
                    version.setRevision(Integer.parseInt(res.getMatch(3)));
                }
            } catch (RegexException ex) {
            } catch (PluginNotFoundException ex) {
            }
        }
    }

    /**
     * QuickTime plugin detection
     * @param version wraps the detected version numbers.
     */
    public void getQuickTimePluginVersion(PluginVersion version) {
        MimeType mt = MimeType.getMimeType("video/quicktime");  // get quicktime mime type...
        if (mt != null) {   // plugin present
            try {
                // plugin present
                String name = mt.getEnabledPlugin().getName().toLowerCase();
                if (name.contains("quicktime")) {    // the type is enabled for QuickTime (not VLC)...
                    RegExp.RegexResult res = RegExp.getRegExp("(\\d+).(\\d+).(\\d+)", "").exec(name);
                    version.setMajor(Integer.parseInt(res.getMatch(1)));
                    version.setMinor(Integer.parseInt(res.getMatch(2)));
                    version.setRevision(Integer.parseInt(res.getMatch(3)));
                }
            } catch (RegexException ex) {
            } catch (PluginNotFoundException ex) {
            }
        }
    }

    /**
     * Windows Media Player plugin detection. The method
     * simply checks if Windows Media Player plugin is available.
     *
     * @param version wraps the detected version numbers.
     */
    public void getWindowsMediaPlayerVersion(PluginVersion version) {
        // check for WMP firefox plugin mime type
        boolean found = false;
        MimeType mt = MimeType.getMimeType("application/x-ms-wmp");
        if (mt != null) {   // firefox plugin present...
            found = true;
        } else {   // firefox plugin not found check for generic..
            mt = MimeType.getMimeType("application/x-mplayer2");
            if (mt != null) {
                try {
                    BrowserPlugin plug = mt.getEnabledPlugin(); // who's got the mime ? (WMP / VLC)
                    if (plug.getName().contains("Windows Media Player")) {
                        found = true;
                    }
                } catch (PluginNotFoundException ex) {
                }
            }
        }

        if (found) {
            version.setMajor(1);
            version.setMinor(1);
            version.setRevision(1);
        }
    }

    /**
     * VLC plugin detection
     * @param version wraps the detected version numbers.
     */
    public void getVLCPluginVersion(PluginVersion version) {
        // check for VLC plugin mime type
        MimeType mt = MimeType.getMimeType("application/x-vlc-plugin");
        if (mt != null) {   // plugin present...
            try {
                String desc = mt.getEnabledPlugin().getDescription();
                if (mt.getEnabledPlugin().getName().toLowerCase().contains("vlc")) {
                    RegExp.RegexResult res = RegExp.getRegExp("(\\d+).(\\d+).(\\d+)", "").exec(desc);
                    version.setMajor(Integer.parseInt(res.getMatch(1)));
                    version.setMinor(Integer.parseInt(res.getMatch(2)));
                    version.setRevision(Integer.parseInt(res.getMatch(3)));
                }
            } catch (RegexException ex) {
            } catch (PluginNotFoundException ex) {
            }
        }
    }

    /**
     * DivX plugin detection
     * @param version wraps the detected version numbers.
     */
    public void getDivXPluginVersion(PluginVersion version) {
        // check for DivX plugin mime type
        MimeType mt = MimeType.getMimeType("video/divx");
        if (mt != null) {   // plugin present...
            try {
                String desc = mt.getEnabledPlugin().getDescription();
                if (mt.getEnabledPlugin().getName().toLowerCase().contains("divx")) { // who has it?
                    RegExp.RegexResult res = RegExp.getRegExp("(\\d+).(\\d+).(\\d+)", "").exec(desc);
                    version.setMajor(Integer.parseInt(res.getMatch(1)));
                    version.setMinor(Integer.parseInt(res.getMatch(2)));
                    version.setRevision(Integer.parseInt(res.getMatch(3)));
                }
            } catch (RegexException ex) {
            } catch (PluginNotFoundException ex) {
            }
        }
    }

    public native boolean isHTML5CompliantClient() /*-{
    try {
    var test = new Audio();
    test = null;
    return true;
    } catch(e){
    return false;
    }
    }-*/;
}
