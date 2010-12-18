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

    public boolean canHandleMedia(Plugin plugin, String protocol, String ext) throws PluginNotFoundException {
        PluginVersion pv = getPluginInfo(plugin).getVersion();
        Set<String> types = MimePool.instance.getRegisteredExtensions(plugin);
        Set<String> prots = MimePool.instance.getRegisteredProtocols(plugin);

        if (protocol == null) {
            protocol = "-";
        }
        if (pv.compareTo(plugin.getVersion()) >= 0) {   // req plugin found...
            // check for streaming protocol & extension ...
            return ((prots != null) && prots.contains(protocol.toLowerCase()))
                    || ((types != null) && types.contains(ext.toLowerCase()));
        }
        return false;
    }

    public PluginInfo getPluginInfo(Plugin plugin) throws PluginNotFoundException {
        PluginInfo pi = new PluginInfo();

        if (plugin.equals(Plugin.Native) || plugin.equals(Plugin.WinMediaPlayer)) {
            switch (plugin) {
                case WinMediaPlayer:
                    boolean found = false;
                    MimeType mt = MimeType.getMimeType("application/x-ms-wmp");
                    BrowserPlugin plug = mt.getEnabledPlugin();
                    if (mt != null) {   // firefox plugin present...
                        found = true;
                        pi.setWrapperType(PluginInfo.PlayerPluginWrapperType.WMPForFirefox);
                    } else {   // firefox plugin not found check for generic..
                        mt = MimeType.getMimeType("application/x-mplayer2");
                        if (mt != null) {
                            try {
                                plug = mt.getEnabledPlugin(); // who's got the mime ? (WMP / VLC)
                                if (plug.getName().contains("Windows Media Player")) {
                                    found = true;
                                }
                            } catch (PluginNotFoundException ex) {
                            }
                        }
                    }

                    if (found) {
                        pi.setVersion(PluginVersion.get(1, 1, 1));
                        if (plug.getFileName().toLowerCase().contains("totem")
                                || plug.getDescription().toLowerCase().contains("totem")) {
                            pi.setWrapperType(PluginInfo.PlayerPluginWrapperType.Totem);
                        }
                    }
                    break;
                case Native:
                    if (isHTML5CompliantClient()) {
                        pi.setVersion(PluginVersion.get(5, 0, 0));
                    }
            }
            return pi;
        }

        PluginMimeTypes pt = PluginMimeTypes.none;
        switch (plugin) {
            case DivXPlayer:
                pt = PluginMimeTypes.divx;
                break;
            case FlashPlayer:
                pt = PluginMimeTypes.flash;
                break;
            case QuickTimePlayer:
                pt = PluginMimeTypes.quicktime;
                break;
            case VLCPlayer:
                pt = PluginMimeTypes.vlc;
                break;
        }

        MimeType mt = MimeType.getMimeType(pt.mime);
        if (mt != null) {   // plugin present...
            try {
                String desc = mt.getEnabledPlugin().getDescription();
                String name = mt.getEnabledPlugin().getName();
                if (name.toLowerCase().contains(pt.whois)) { // who has it?
                    RegExp.RegexResult res = RegExp.getRegExp(pt.regex, "").exec(pt.versionInName ? name : desc);
                    pi.getVersion().setMajor(Integer.parseInt(res.getMatch(1)));
                    pi.getVersion().setMinor(Integer.parseInt(res.getMatch(2)));
                    pi.getVersion().setRevision(Integer.parseInt(res.getMatch(3)));
                    if (mt.getEnabledPlugin().getFileName().toLowerCase().contains("totem")
                            || desc.toLowerCase().contains("totem")) {
                        pi.setWrapperType(PluginInfo.PlayerPluginWrapperType.Totem);
                    }
                }
            } catch (RegexException ex) {
            }
        }
        return pi;
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

    private enum PluginMimeTypes {

        none("", "", "", false),
        divx("video/divx", "divx", "(\\d+).(\\d+).(\\d+)", false),
        flash("application/x-shockwave-flash", "shockwave flash", "(\\d+).(\\d+)\\s*[r|d|b](\\d+)", false),
        vlc("application/x-vlc-plugin", "vlc", "(\\d+).(\\d+).(\\d+)", false),
        quicktime("video/quicktime", "quicktime", "(\\d+).(\\d+).(\\d+)", true);

        private PluginMimeTypes(String mime, String whois, String regex, boolean versionInName) {
            this.mime = mime;
            this.whois = whois;
            this.regex = regex;
            this.versionInName = versionInName;
        }
        String mime, whois, regex;
        boolean versionInName;
    }
}
