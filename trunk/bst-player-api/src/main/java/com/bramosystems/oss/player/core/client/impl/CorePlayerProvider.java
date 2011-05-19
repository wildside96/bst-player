/*
 * Copyright 2011 Sikirulai Braheem <sbraheem at bramosystems.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bramosystems.oss.player.core.client.impl;

import com.bramosystems.oss.player.core.client.PluginInfo;
import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersion;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.impl.plugin.PluginManager;
import com.bramosystems.oss.player.core.client.spi.PlayerProvider;
import com.bramosystems.oss.player.core.client.ui.DivXPlayer;
import com.bramosystems.oss.player.core.client.ui.FlashMediaPlayer;
import com.bramosystems.oss.player.core.client.ui.NativePlayer;
import com.bramosystems.oss.player.core.client.ui.QuickTimePlayer;
import com.bramosystems.oss.player.core.client.ui.VLCPlayer;
import com.bramosystems.oss.player.core.client.ui.WinMediaPlayer;
import com.bramosystems.oss.player.core.client.spi.PlayerProviderFactory;
import com.bramosystems.oss.player.util.client.MimeType;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ObjectElement;
import com.google.gwt.dom.client.ParamElement;
import com.google.gwt.user.client.DOM;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 */
@PlayerProvider("core")
public class CorePlayerProvider implements PlayerProviderFactory {

    private Document _doc = Document.get();
    private String wmpFFMimeType = "application/x-ms-wmp", wmpAppMimeType = "application/x-mplayer2";

    @Override
    public AbstractMediaPlayer getPlayer(String playerName, String mediaURL, boolean autoplay, String height, String width)
            throws LoadException, PluginNotFoundException, PluginVersionException {
        AbstractMediaPlayer player;
        switch (Plugin.valueOf(playerName)) {
            case VLCPlayer:
                player = new VLCPlayer(mediaURL, autoplay, height, width);
                break;
            case FlashPlayer:
                player = new FlashMediaPlayer(mediaURL, autoplay, height, width);
                break;
            case QuickTimePlayer:
                player = new QuickTimePlayer(mediaURL, autoplay, height, width);
                break;
            case WinMediaPlayer:
                player = new WinMediaPlayer(mediaURL, autoplay, height, width);
                break;
            case Native:
                player = new NativePlayer(mediaURL, autoplay, height, width);
                break;
            case DivXPlayer:
                player = new DivXPlayer(mediaURL, autoplay, height, width);
                break;
            default:
                throw new PluginNotFoundException();
        }
        return player;
    }

    @Override
    public AbstractMediaPlayer getPlayer(String playerName, String mediaURL, boolean autoplay) throws LoadException, PluginNotFoundException, PluginVersionException {
        AbstractMediaPlayer player;
        switch (Plugin.valueOf(playerName)) {
            case VLCPlayer:
                player = new VLCPlayer(mediaURL, autoplay);
                break;
            case FlashPlayer:
                player = new FlashMediaPlayer(mediaURL, autoplay);
                break;
            case QuickTimePlayer:
                player = new QuickTimePlayer(mediaURL, autoplay);
                break;
            case WinMediaPlayer:
                player = new WinMediaPlayer(mediaURL, autoplay);
                break;
            case Native:
                player = new NativePlayer(mediaURL, autoplay);
                break;
            case DivXPlayer:
                player = new DivXPlayer(mediaURL, autoplay);
                break;
            default:
                throw new PluginNotFoundException();
        }
        return player;
    }

    protected class XObject {

        private ObjectElement e = _doc.createObjectElement();

        public XObject(String id) {
            e.setId(id);
        }

        public final void addParam(String name, String value) {
            ParamElement param = _doc.createParamElement();
            param.setName(name);
            param.setValue(value);
            e.appendChild(param);
        }

        public final ObjectElement getElement() {
            return e;
        }
    }

    protected class XEmbed {

        private Element e = _doc.createElement("embed");

        public XEmbed(String id) {
            e.setId(id);
        }

        public final void addParam(String name, String value) {
            e.setAttribute(name, value);
        }

        public final Element getElement() {
            return e;
        }
    }

    @Override
    public Element getWidgetElement(String playerName, String playerId, String mediaURL, boolean autoplay, HashMap<String, String> params) {
        Element e = DOM.createDiv();
        switch (Plugin.valueOf(playerName)) {
            case Native:
                e = getNativeElement(playerId, mediaURL, autoplay);
                break;
            case FlashPlayer:
                e = getSWFElement(playerId, mediaURL, params);
                break;
            case QuickTimePlayer:
                e = getQTElement(playerId, mediaURL, autoplay, params);
                break;
            case VLCPlayer:
                e = getVLCElement(playerId, "", false);
                break;
            case WinMediaPlayer:
                e = getWMPElement(playerId, mediaURL, autoplay, params);
                /*               e.setAttribute("height", _height);
                e.getStyle().setProperty("height", _height);
                e.setAttribute("width", _width);
                e.getStyle().setProperty("width", _width);
                 */ break;
            case DivXPlayer:
                e = getDivXElement(playerId, mediaURL, autoplay, params);
                break;
        }
        return e;
    }

    protected Element getVLCElement(String playerId, String mediaURL, boolean autoplay) {
        XEmbed e = new XEmbed(playerId);
        e.addParam("loop", "" + false);
        e.addParam("target", "");
        e.addParam("autoplay", "" + autoplay);
        e.addParam("type", "application/x-vlc-plugin");
        e.addParam("events", "true");
        e.addParam("version", "VideoLAN.VLCPlugin.2");
        return e.getElement();
    }

    protected Element getWMPElement(String playerId, String mediaURL, boolean autoplay,
            HashMap<String, String> params) {
        XEmbed xo = new XEmbed(playerId);
        xo.addParam("type", hasWMPFFPlugin() ? wmpFFMimeType : wmpAppMimeType);
//        XObject xo = new XObject(playerId);
        //      xo.getElement().setType(hasWMPFFPlugin() ? wmpFFMimeType : wmpAppMimeType);
        xo.addParam("autostart", hasWMPFFPlugin() ? Boolean.toString(autoplay) : (autoplay ? "1" : "0"));
        xo.addParam(hasWMPFFPlugin() ? "URL" : "SRC", mediaURL);

        Iterator<String> keys = params.keySet().iterator();
        while (keys.hasNext()) {
            String name = keys.next();
            xo.addParam(name, params.get(name));
        }
        return xo.getElement();
    }

    protected Element getQTElement(String playerId, String mediaURL, boolean autoplay,
            HashMap<String, String> params) {
        XEmbed xo = new XEmbed(playerId);
        xo.addParam("type", "video/quicktime");
        xo.addParam("autoplay", Boolean.toString(autoplay));
//        xo.addParam("src", mediaURL);

        Iterator<String> keys = params.keySet().iterator();
        while (keys.hasNext()) {
            String name = keys.next();
            xo.addParam(name, params.get(name));
        }
        return xo.getElement();
    }

    protected Element getSWFElement(String playerId, String swfURL, HashMap<String, String> params) {
        XEmbed e = new XEmbed(playerId);
        e.addParam("type", "application/x-shockwave-flash");
        e.addParam("src", swfURL);
        e.addParam("name", playerId);

        Iterator<String> keys = params.keySet().iterator();
        while (keys.hasNext()) {
            String name = keys.next();
            e.addParam(name, params.get(name));
        }
        return e.getElement();
    }

    protected Element getNativeElement(String playerId, String mediaURL, boolean autoplay) {
        Element videoElement = _doc.createElement("video");
        videoElement.setId(playerId);
        videoElement.setPropertyString("src", mediaURL);
        if (autoplay) {
            videoElement.setPropertyBoolean("autoplay", autoplay);
        }
        videoElement.setPropertyBoolean("controls", true);
        return videoElement;
    }

    private boolean hasWMPFFPlugin() {
        // check for firefox plugin mime type...
        MimeType mt = MimeType.getMimeType(wmpFFMimeType);
        if (mt != null) {
            return true;
        } else {
            return false;
        }
    }

    protected Element getDivXElement(String playerId, String mediaURL,
            boolean autoplay, HashMap<String, String> params) {
        XEmbed xo = new XEmbed(playerId);
        xo.addParam("type", "video/divx");
        xo.addParam("autoPlay", Boolean.toString(autoplay));
//        xo.addParam("src", mediaURL);

        Iterator<String> keys = params.keySet().iterator();
        while (keys.hasNext()) {
            String name = keys.next();
            xo.addParam(name, params.get(name));
        }
        return xo.getElement();
    }

    public boolean isWMPProgrammableEmbedModeSupported() {
        try {
            PluginInfo.PlayerPluginWrapperType w = PluginManager.getPluginInfo(Plugin.WinMediaPlayer).getWrapperType();
            return w.equals(PluginInfo.PlayerPluginWrapperType.WMPForFirefox) || w.equals(PluginInfo.PlayerPluginWrapperType.Totem);
        } catch (PluginNotFoundException ex) {
            return false;
        }
    }

    /********************************************* Plugin detection ******************************************************/
    @Override
    public PluginVersion getDetectedPluginVersion(String playerName) throws PluginNotFoundException {
        PluginVersion pv = new PluginVersion();
        Plugin plugin = Plugin.valueOf(playerName);

        switch (plugin) {
            case DivXPlayer:
                pv = PlayerUtil.getDivXPlayerPluginVersion();
                break;
            case FlashPlayer:
                pv = PlayerUtil.getFlashPlayerVersion();
                break;
            case QuickTimePlayer:
                pv = PlayerUtil.getQuickTimePluginVersion();
                break;
            case VLCPlayer:
                pv = PlayerUtil.getVLCPlayerPluginVersion();
                break;
            case WinMediaPlayer:
                pv = PlayerUtil.getWindowsMediaPlayerPluginVersion();
                break;
            case Native:
                if (PluginManager.isHTML5CompliantClient()) {
                    pv = PluginVersion.get(5, 0, 0);
                }
                break;

        }
        return pv;
    }
}
