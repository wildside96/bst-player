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
package com.bramosystems.gwt.player.core.client.impl;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * IE specific native implementation of the QuickTimePlayer class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see com.bramosystems.gwt.player.client.ui.QuickTimePlayer
 */
public class QuickTimePlayerImplIE extends QuickTimePlayerImpl {

    // Make package private
    QuickTimePlayerImplIE() {
    }

    @Override
    public String getPlayerScript(String mediaSrc, String playerId, boolean autoplay,
            boolean showControls, String height, String width) {
        String evtSrc = playerId + "_evtSrc";
        return  "<object id='" + evtSrc + "' classid='clsid:CB927D12-4FF7-4A9E-A169-56E4B8A75598'></object>" +
                "<object id='" + playerId + "' classid='clsid:02BF25D5-8C17-4B23-BC80-D3488ABDDC6B' " +
                "width='" + width + "' height='" + height + "' style='behavior:url(#" + evtSrc + ");' > " +
                "<param name='src' value='" + mediaSrc + "' />" +
                "<param name='autoplay' value='" + autoplay + "' />" +
                "<param name='controller' value='" + showControls + "' />" +
                "<param name='bgcolor' value='#000000' />" +
                "<param name='showlogo' value='false' />" +
                "<param name='KioskMode' value='true' >" +
                "<param name='postdomevents' value='true' >" +
                "</object>";
    }

    @Override
    protected native void registerMediaStateListenerImpl(JavaScriptObject jso, String playerId) /*-{
        var qtbegin = function(evt) {
            $wnd.alert('qt begin');
            jso[playerId].initComplete();
        };
        var qtload = function(evt) {
            $wnd.alert('qt load');
            jso[playerId].loadingComplete();
         };

        var playr = $doc.getElementById(playerId);
        playr.attachEvent("onqt_begin", qtbegin);
        playr.attachEvent("onqt_load", qtload);

        playr.attachEvent('onqt_ended', function(evt) {
            jso[playerId].soundComplete();
        });
        playr.attachEvent('onqt_error', function(evt) {
            jso[playerId].errorr();
        });
        playr.attachEvent('onqt_progress', function(evt) {
            jso[playerId].loadingProgress();
        });
        playr.attachEvent('onqt_canplay', function(evt) {
            jso[playerId].playerReady();
        });
        playr.attachEvent('onqt_play', function(evt) {
            $wnd.alert('qt play');
            jso[playerId].playStarted();
        });
    }-*/;
}
