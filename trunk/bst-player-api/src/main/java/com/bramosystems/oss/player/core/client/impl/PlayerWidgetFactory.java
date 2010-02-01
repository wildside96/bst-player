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

import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.util.client.MimeType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ObjectElement;
import com.google.gwt.dom.client.ParamElement;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class PlayerWidgetFactory {

    private static PlayerWidgetFactory factory = GWT.create(PlayerWidgetFactory.class);
    private Document _doc = Document.get();

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

    PlayerWidgetFactory() {
    }

    public static final PlayerWidgetFactory get() {
        return factory;
    }

    public Widget getSWFWidget(String playerId, String swfURL, HashMap<String, String> params) {
        return new PlayerWidget2(getSWFElement(playerId, swfURL, params), null);
    }

    public Widget getPlayerWidget(Plugin plugin, String playerId, String mediaURL,
            boolean autoplay, HashMap<String, String> params, BeforeUnloadCallback callback) {
        Element e = null;
        switch (plugin) {
            case FlashPlayer:
                e = getSWFElement(playerId, mediaURL, params);
                break;
            case QuickTimePlayer:
                e = getQTElement(playerId, mediaURL, autoplay);
                break;
            case VLCPlayer:
                e = getVLCElement(playerId, "", false);
                break;
            case WinMediaPlayer:
                e = getWMPElement(playerId, mediaURL, autoplay, params);
                break;
        }
        PlayerWidget2 pw = new PlayerWidget2(e, callback);
        return pw;
    }

    protected Element getVLCElement(String playerId, String mediaURL, boolean autoplay) {
        XEmbed e = new XEmbed(playerId);
        e.addParam("loop", "" + false);
        e.addParam("target", "");
        e.addParam("autoplay", "" + autoplay);
        e.addParam("type", "application/x-vlc-plugin");
        e.addParam("version", "VideoLAN.VLCPlugin.2");
        return e.getElement();
    }

    protected Element getWMPElement(String playerId, String mediaURL, boolean autoplay,
            HashMap<String, String> params) {
        XObject xo = new XObject(playerId);
        xo.getElement().setType(getWMPPluginType());
        xo.addParam("autostart", Boolean.toString(autoplay));
        xo.addParam("URL", mediaURL);

        Iterator<String> keys = params.keySet().iterator();
        while (keys.hasNext()) {
            String name = keys.next();
            xo.addParam(name, params.get(name));
        }
        return xo.getElement();
    }

    private String getWMPPluginType() {
        // check for firefox plugin mime type...
        String ffMime = "application/x-ms-wmp";
        String genericMime = "application/x-mplayer2";
        MimeType mt = MimeType.getMimeType(ffMime);
        if (mt != null) {
            return ffMime;
        } else {
            return genericMime;
        }
    }

    protected Element getQTElement(String playerId, String mediaURL, boolean autoplay) {
        XEmbed xo = new XEmbed(playerId);
        xo.addParam("type", "video/quicktime");
        xo.addParam("autoplay", Boolean.toString(autoplay));
        xo.addParam("src", mediaURL);
        xo.addParam("bgcolor", "#000000");
        xo.addParam("showlogo", Boolean.toString(false));
        xo.addParam("kioskmode", Boolean.toString(true));
        xo.addParam("EnableJavaScript", Boolean.toString(true));
        xo.addParam("postdomevents", Boolean.toString(true));
//        xo.addParam("targetcache", Boolean.toString(false));
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

    public Element getNativeElement(String playerId, String mediaURL, boolean autoplay) {
        Element videoElement = _doc.createElement("video");
        videoElement.setId(playerId);
        videoElement.setPropertyString("src", mediaURL);
        videoElement.setPropertyBoolean("autoplay", autoplay);
        videoElement.setPropertyBoolean("controls", true);
        return videoElement;
    }

    public Element getNativeElement(String playerId, ArrayList<String> sources, boolean autoplay) {
        Element videoElement = _doc.createElement("video");
        videoElement.setId(playerId);
        videoElement.setPropertyBoolean("autoplay", autoplay);
        videoElement.setPropertyBoolean("controls", true);

        for (String item : sources) {
            Element s = _doc.createElement("source");
            s.setAttribute("src", item);
            videoElement.appendChild(s);
        }
        return videoElement;
    }

//    public class PlayerWidget2 extends Widget {
    private class PlayerWidget2 extends HTML {

        private Element e;
        private BeforeUnloadCallback callback;

        public PlayerWidget2(Element playerElement, BeforeUnloadCallback callback) {
//            setElement(playerElement);
            setHTML(playerElement.getString());
//            e = playerElement;
            this.callback = callback;

            setHeight("100%");
            setWidth("100%");
            setStyleName("");
        }
/*
        @Override
        protected void onLoad() {
            Window.alert("Player Widget onload ...");
            Timer t = new Timer() {

                @Override
                public void run() {
                    if (callback != null) {
                        callback.onInjected();
                    }
                }
            };
//            t.schedule(500);
        }

        @Override
        protected void onUnload() {
            if (callback != null) {
                callback.onBeforeRemove();
            }
        }
*/
    }
}
