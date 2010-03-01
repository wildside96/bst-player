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

import com.bramosystems.oss.player.util.client.MimeType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ObjectElement;
import com.google.gwt.dom.client.ParamElement;
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

    protected static final PlayerWidgetFactory get() {
        return factory;
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
        xo.getElement().setType(hasWMPFFPlugin() ? wmpFFMimeType : wmpAppMimeType);
        xo.addParam("autostart", hasWMPFFPlugin() ? Boolean.toString(autoplay) : (autoplay ? "1" : "0"));
        xo.addParam(hasWMPFFPlugin() ? "URL" : "SRC", mediaURL);

        Iterator<String> keys = params.keySet().iterator();
        while (keys.hasNext()) {
            String name = keys.next();
            xo.addParam(name, params.get(name));
        }
        return xo.getElement();
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

    protected Element getNativeElement(String playerId, String mediaURL, boolean autoplay) {
        Element videoElement = _doc.createElement("video");
        videoElement.setId(playerId);
        videoElement.setPropertyString("src", mediaURL);
        videoElement.setPropertyBoolean("autoplay", autoplay);
        videoElement.setPropertyBoolean("controls", true);
        return videoElement;
    }

    protected Element getNativeElement(String playerId, ArrayList<String> sources, boolean autoplay) {
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

    public static boolean hasWMPFFPlugin() {
        // check for firefox plugin mime type...
        MimeType mt = MimeType.getMimeType(wmpFFMimeType);
        if (mt != null) {
            return true;
        } else {
            return false;
        }
    }
    private static String wmpFFMimeType = "application/x-ms-wmp", wmpAppMimeType = "application/x-mplayer2";
}
