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

import com.bramosystems.oss.player.core.client.ui.QuickTimePlayer;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * IE specific native implementation of the QuickTimePlayer class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see QuickTimePlayer
 */
public class QuickTimePlayerImplIE extends QuickTimePlayerImpl {

    private String behaviourObjId = "qt_event_source";

    public QuickTimePlayerImplIE() {
        // inject event source object...
        if (DOM.getElementById(behaviourObjId) == null) {
            Element oe = DOM.createElement("object");
            oe.setId(behaviourObjId);
            oe.setAttribute("classid", "clsid:CB927D12-4FF7-4a9e-A169-56E4B8A75598");
            RootPanel.getBodyElement().appendChild(oe);
        }
    }

    @Override
    public String getPlayerScript(String playerId, String mediaSrc, boolean autoplay,
            String height, String width) {
        return "<object id='" + playerId + "' classid='clsid:02BF25D5-8C17-4B23-BC80-D3488ABDDC6B' " +
                "width='" + width + "' height='" + height + "' " +
                "style='behavior:url(#" + behaviourObjId + ")'>" +
                "<param name='src' value='' />" +
                "<param name='autoplay' value='" + autoplay + "' />" +
                "<param name='controller' value='true' />" +
                "<param name='bgcolor' value='#000000' />" +
                "<param name='showlogo' value='false' />" +
                "<param name='Scale' value='1' />" +
                "<param name='KioskMode' value='true' />" +
                "<param name='postdomevents' value='true' />" +
                "<param name='targetcache' value='false' />" +
                "</object>";
    }

    @Override
    public void registerMediaStateListener(final String playerId, final String mediaUrl) {
        Timer tt = new Timer() {

            @Override
            public void run() {
                if (isPlayerAvailable(playerId)) {
                    cancel();
                    resetPropertiesOnReload(playerId, false);
                    registerMediaStateListenerImpl(QuickTimePlayerImplIE.this, playerId);
                    load(playerId, mediaUrl);
                }
            }
        };
        tt.scheduleRepeating(200);
    }

    private native void registerMediaStateListenerImpl(QuickTimePlayerImpl impl, String playerId) /*-{
    var playr = $doc.getElementById(playerId);
    playr.attachEvent("onqt_begin", function(evt){
    impl.@com.bramosystems.oss.player.core.client.impl.QuickTimePlayerImpl::onState(Ljava/lang/String;I)(playerId, 1);
    });
    playr.attachEvent("onqt_load", function(evt){
    impl.@com.bramosystems.oss.player.core.client.impl.QuickTimePlayerImpl::onState(Ljava/lang/String;I)(playerId, 2);
    });
    playr.attachEvent('onqt_play', function(evt) {
    impl.@com.bramosystems.oss.player.core.client.impl.QuickTimePlayerImpl::onState(Ljava/lang/String;I)(playerId, 3);
    });
    playr.attachEvent('onqt_ended', function(evt) {
    impl.@com.bramosystems.oss.player.core.client.impl.QuickTimePlayerImpl::onState(Ljava/lang/String;I)(playerId, 4);
    });
    playr.attachEvent('onqt_canplay', function(evt) {
    impl.@com.bramosystems.oss.player.core.client.impl.QuickTimePlayerImpl::onState(Ljava/lang/String;I)(playerId, 5);
    });
    playr.attachEvent('onqt_volumechange', function(evt) {
    impl.@com.bramosystems.oss.player.core.client.impl.QuickTimePlayerImpl::onState(Ljava/lang/String;I)(playerId, 6);
    });
    playr.attachEvent('onqt_progress', function(evt) {
    impl.@com.bramosystems.oss.player.core.client.impl.QuickTimePlayerImpl::onState(Ljava/lang/String;I)(playerId, 7);
    });
    playr.attachEvent('onqt_error', function(evt) {
    impl.@com.bramosystems.oss.player.core.client.impl.QuickTimePlayerImpl::onState(Ljava/lang/String;I)(playerId, 8);
    });
    playr.attachEvent('onqt_loadedmetadata', function(evt) {
    impl.@com.bramosystems.oss.player.core.client.impl.QuickTimePlayerImpl::onState(Ljava/lang/String;I)(playerId, 9);
    });
    playr.attachEvent('onqt_pause', function(evt) {
    impl.@com.bramosystems.oss.player.core.client.impl.QuickTimePlayerImpl::onState(Ljava/lang/String;I)(playerId, 10);
    });
    playr.attachEvent('onqt_waiting', function(evt) {
    impl.@com.bramosystems.oss.player.core.client.impl.QuickTimePlayerImpl::onState(Ljava/lang/String;I)(playerId, 11);
    });
    playr.attachEvent('onqt_stalled', function(evt) {
    impl.@com.bramosystems.oss.player.core.client.impl.QuickTimePlayerImpl::onState(Ljava/lang/String;I)(playerId, 12);
    });
    }-*/;
}
